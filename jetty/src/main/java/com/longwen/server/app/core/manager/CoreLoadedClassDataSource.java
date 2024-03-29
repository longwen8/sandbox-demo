package com.longwen.server.app.core.manager;

import com.longwen.server.app.api.resource.LoadedClassDataSource;
import com.longwen.server.app.core.util.matcer.Matcher;

import java.util.List;

public interface CoreLoadedClassDataSource extends LoadedClassDataSource {

    /**
     * 使用{@link Matcher}来完成类的检索
     * <p>
     * 本次检索将会用于Class型变，所以会主动过滤掉不支持的类和行为
     * </p>
     *
     * @param matcher 类匹配
     * @return 匹配的类
     */
    List<Class<?>> findForReTransform(Matcher matcher);
}
