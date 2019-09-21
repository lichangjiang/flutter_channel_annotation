package com.lcj.flutter_channel_annotation_ioc.model;

public class MethodInfo {

    private String classQualifiedName;
    private String methodName;


    public MethodInfo(String classQualifiedName, String methodName) {
        this.classQualifiedName = classQualifiedName;
        this.methodName = methodName;
    }

    public String getClassQualifiedName() {
        return classQualifiedName;
    }

    public String getMethodName() {
        return methodName;
    }
}
