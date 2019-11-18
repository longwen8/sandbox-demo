package com.longwen.server.app.api.filter;

public interface ExtFilter extends Filter {

    boolean isIncludeSubClasses();

    boolean isIncludeBootstrap();

    class ExtFilterFactory {

        public static ExtFilter make(final Filter filter,
                                     final boolean isIncludeSubClasses,
                                     final boolean isincludeBootstrap){
            return new ExtFilter() {
                @Override
                public boolean isIncludeSubClasses() {
                    return isIncludeSubClasses;
                }

                @Override
                public boolean isIncludeBootstrap() {
                    return isincludeBootstrap;
                }

                @Override
                public boolean doClassFilter(int access, String javaClassName, String superClassTypeJavaClassName, String[] interfaceTypeJavaClassNameArray, String[] annotationTypeJavaClassNameArray) {
                    return filter.doClassFilter(
                            access,
                            javaClassName,
                            superClassTypeJavaClassName,
                            interfaceTypeJavaClassNameArray,
                            annotationTypeJavaClassNameArray
                    );
                }

                @Override
                public boolean doMethodFilter(int access, String javaMethodName, String[] parameterTypeJavaClassNameArray, String[] throwsTypeJavaClassNameArray, String[] annotationTypeJavaClassNameArray) {
                    return filter.doMethodFilter(
                            access,
                            javaMethodName,
                            parameterTypeJavaClassNameArray,
                            throwsTypeJavaClassNameArray,
                            annotationTypeJavaClassNameArray
                    );
                }
            };
        }
    }
}
