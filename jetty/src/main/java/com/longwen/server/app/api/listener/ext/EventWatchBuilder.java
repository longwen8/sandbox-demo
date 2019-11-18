package com.longwen.server.app.api.listener.ext;

import com.longwen.server.app.api.event.Event;
import com.longwen.server.app.api.event.EventListener;
import com.longwen.server.app.api.filter.ExtFilter;
import com.longwen.server.app.api.filter.Filter;
import com.longwen.server.app.api.resource.ModuleEventWatcher;
import com.longwen.server.app.api.util.GaArrayUtils;
import com.longwen.server.util.GaStringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.longwen.server.app.api.listener.ext.EventWatchBuilder.PatternType.WILDCARD;
import static com.longwen.server.app.api.util.GaCollectionUtils.add;
import static com.longwen.server.app.api.event.Event.Type.*;

public class EventWatchBuilder {

    /**
     * 构建类匹配器
     */
    public interface IBuildingForClass {

        /**
         * 构建行为匹配器，匹配符合模版匹配名称的行为
         *
         * @param pattern 行为名称
         * @return IBuildingForBehavior
         */
        IBuildingForBehavior onBehavior(String pattern);

    }

    /**
     * 构建方法匹配器
     */
    public interface IBuildingForBehavior {
        EventWatcher onWatch(AdviceListener adviceListener, Event.Type... eventTypeArray);
    }



    /**
     * 构建观察构建器
     */
    public interface IBuildingForWatching {

    }

    /**
     * 构建删除观察构建器
     */
    public interface IBuildingForUnWatching{

        IBuildingForUnWatching withProgress(ModuleEventWatcher.Progress progress);

        void onUnWatched();
    }


    // ---------------  实现代码

    public enum PatternType {
        /**
         * 通配符表达式
         */
        WILDCARD,
        /**
         * 正则表达式
         */
        REGEX
    }

    private final ModuleEventWatcher moduleEventWatcher;
    private final PatternType patternType;
    private List<BuildingForClass> bfClasses = new ArrayList();

    public EventWatchBuilder(final ModuleEventWatcher moduleEventWatcher) {
        this(moduleEventWatcher, WILDCARD);
    }

    public EventWatchBuilder(final ModuleEventWatcher moduleEventWatcher,
                             final PatternType patternType) {
        this.moduleEventWatcher = moduleEventWatcher;
        this.patternType = patternType;
    }


    public EventWatchBuilder.IBuildingForClass onClass(final String pattern){
        return add(bfClasses,new BuildingForClass(pattern));
    }


    /**
     * 模式匹配
     *
     * @param string      目标字符串
     * @param pattern     模式字符串
     * @param patternType 匹配模式
     * @return TRUE:匹配成功 / FALSE:匹配失败
     */
    private static boolean patternMatching(final String string,
                                           final String pattern,
                                           final PatternType patternType) {
        switch (patternType) {
            case WILDCARD:
                return GaStringUtils.matching(string, pattern);
            case REGEX:
                return string.matches(pattern);
            default:
                return false;
        }
    }


    private class BuildingForClass implements IBuildingForClass {


        private final String pattern;
        private int withAccess = 0;
        private boolean isIncludeSubClasses = false;
        private boolean isIncludeBootstrap = false;
        private final PatternGroupList hasInterfaceTypes = new PatternGroupList();
        private final PatternGroupList hasAnnotationTypes = new PatternGroupList();
        private final List<BuildingForBehavior> bfBehaviors = new ArrayList<BuildingForBehavior>();

        /**
         * 构造类构建器
         *
         * @param pattern 类名匹配模版
         */
        BuildingForClass(final String pattern) {
            this.pattern = pattern;
        }

        @Override
        public IBuildingForBehavior onBehavior(final String pattern) {
            return add(bfBehaviors, new BuildingForBehavior(this, pattern));
        }


    }
    private class BuildingForBehavior implements IBuildingForBehavior {

        private final BuildingForClass bfClass;
        private final String pattern;
        private int withAccess = 0;
        private final PatternGroupList withParameterTypes = new PatternGroupList();
        private final PatternGroupList hasExceptionTypes = new PatternGroupList();
        private final PatternGroupList hasAnnotationTypes = new PatternGroupList();

        BuildingForBehavior(final BuildingForClass bfClass,
                            final String pattern) {
            this.bfClass = bfClass;
            this.pattern = pattern;
        }


        @Override
        public EventWatcher onWatch(final AdviceListener adviceListener, Event.Type... eventTypeArray) {
            if (eventTypeArray == null
                    || eventTypeArray.length == 0) {
                return build(new AdviceAdapterListener(adviceListener), null, BEFORE, RETURN, THROWS, IMMEDIATELY_RETURN, IMMEDIATELY_THROWS);
            }
            return build(new AdviceAdapterListener(adviceListener), null, eventTypeArray);
        }



    }


    private EventWatcher build(final EventListener listener,
                               final ModuleEventWatcher.Progress progress,
                               final Event.Type... eventTypes){
        final int watchId = moduleEventWatcher.watch(
                toEventWatchCondition(),
                listener,
                progress,
                eventTypes
        );

        return new EventWatcher() {
            final List<ModuleEventWatcher.Progress> progresses = new ArrayList<ModuleEventWatcher.Progress>();

            @Override
            public int getWatchId() {
                return watchId;
            }

            @Override
            public IBuildingForUnWatching withProgress(ModuleEventWatcher.Progress progress) {
                if(null != progress){
                    progresses.add(progress);
                }
                return this;
            }

            @Override
            public void onUnWatched() {
                //moduleEventWatcher
            }
        };
    }

    private EventWatchCondition toEventWatchCondition(){
        final List<Filter> filters = new ArrayList<>();

        for(final BuildingForClass bfClass : bfClasses){
            System.out.println("bfClasses:"+bfClass.toString());
            final Filter filter = new Filter() {
                @Override
                public boolean doClassFilter(int access, String javaClassName, String superClassTypeJavaClassName, String[] interfaceTypeJavaClassNameArray, String[] annotationTypeJavaClassNameArray) {

                    return (access & bfClass.withAccess) == bfClass.withAccess
                            && patternMatching(javaClassName, bfClass.pattern, patternType)
                            && bfClass.hasInterfaceTypes.patternHas(interfaceTypeJavaClassNameArray)
                            && bfClass.hasAnnotationTypes.patternHas(annotationTypeJavaClassNameArray);
                }

                @Override
                public boolean doMethodFilter(int access, String javaMethodName, String[] parameterTypeJavaClassNameArray, String[] throwsTypeJavaClassNameArray, String[] annotationTypeJavaClassNameArray) {
                    // nothing to matching
                    if (bfClass.bfBehaviors.isEmpty()) {
                        return false;
                    }

                    // matching any behavior
                    for (final BuildingForBehavior bfBehavior : bfClass.bfBehaviors) {
                        if ((access & bfBehavior.withAccess) == bfBehavior.withAccess
                                && patternMatching(javaMethodName, bfBehavior.pattern, patternType)
                                && bfBehavior.withParameterTypes.patternWith(parameterTypeJavaClassNameArray)
                                && bfBehavior.hasExceptionTypes.patternHas(throwsTypeJavaClassNameArray)
                                && bfBehavior.hasAnnotationTypes.patternHas(annotationTypeJavaClassNameArray)) {
                            return true;
                        }//if
                    }//for
                    return false;
                }
            };
            filters.add(makeExtFilter(filter,bfClass));
        }

        return new EventWatchCondition() {
            @Override
            public Filter[] getOrFilterArray() {
                return filters.toArray(new Filter[0]);
            }
        };
    }


    private Filter makeExtFilter(final Filter filter,
                                 final BuildingForClass bfClass) {
        return ExtFilter.ExtFilterFactory.make(
                filter,
                bfClass.isIncludeSubClasses,
                bfClass.isIncludeBootstrap
        );
    }
    private class BuildingForWatching implements IBuildingForWatching{

    }



    /**
     * 模式匹配组列表
     */
    private class PatternGroupList {

        final List<Group> groups = new ArrayList<Group>();

        /*
         * 添加模式匹配组
         */
        void add(String... patternArray) {
            groups.add(new Group(patternArray));
        }

        /*
         * 模式匹配With
         */
        boolean patternWith(final String[] stringArray) {

            // 如果模式匹配组为空，说明不参与本次匹配
            if (groups.isEmpty()) {
                return true;
            }

            for (final Group group : groups) {
                if (group.matchingWith(stringArray)) {
                    return true;
                }
            }
            return false;
        }

        /*
         * 模式匹配Has
         */
        boolean patternHas(final String[] stringArray) {

            // 如果模式匹配组为空，说明不参与本次匹配
            if (groups.isEmpty()) {
                return true;
            }

            for (final Group group : groups) {
                if (group.matchingHas(stringArray)) {
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * 模式匹配组
     */
    private class Group {

        final String[] patternArray;

        Group(String[] patternArray) {
            this.patternArray = GaArrayUtils.isEmpty(patternArray)
                    ? new String[0]
                    : patternArray;
        }

        /*
         * stringArray中任意字符串能匹配上匹配模式
         */
        boolean anyMatching(final String[] stringArray,
                            final String pattern) {
            if (GaArrayUtils.isEmpty(stringArray)) {
                return false;
            }
            for (final String string : stringArray) {
                if (patternMatching(string, pattern, patternType)) {
                    return true;
                }
            }
            return false;
        }

        /*
         * 匹配模式组中所有匹配模式都在目标中存在匹配通过的元素
         * 要求匹配组中每一个匹配项都在stringArray中存在匹配的字符串
         */
        boolean matchingHas(final String[] stringArray) {

            for (final String pattern : patternArray) {
                if (anyMatching(stringArray, pattern)) {
                    continue;
                }
                return false;
            }
            return true;
        }

        /*
         * 匹配模式组中所有匹配模式都在目标中对应数组位置存在匹配通过元素
         * 要求字符串数组每一个位对应模式匹配组的每一个模式匹配表达式
         * stringArray[0] matching wildcardArray[0]
         * stringArray[1] matching wildcardArray[1]
         * stringArray[2] matching wildcardArray[2]
         *     ...
         * stringArray[n] matching wildcardArray[n]
         */
        boolean matchingWith(final String[] stringArray) {

            // 长度不一样就不用不配了
            int length;
            if ((length = GaArrayUtils.getLength(stringArray)) != GaArrayUtils.getLength(patternArray)) {
                return false;
            }
            // 长度相同则逐个位置比较，只要有一个位置不符，则判定不通过
            for (int index = 0; index < length; index++) {
                if (!patternMatching(stringArray[index], patternArray[index], patternType)) {
                    return false;
                }
            }
            // 所有位置匹配通过，判定匹配成功
            return true;
        }

    }

}
