package com.lcj.flutter_channel_annotation_ioc.model;

import java.util.ArrayList;
import java.util.List;

public class AsyncMethodInfo extends MethodInfo {

    private List<ParameterInfo> parameters;
    private String callback;
    private String mReturnType;

    public AsyncMethodInfo(String classQualifiedName, String methodName,String returnType,String callback) {
        super(classQualifiedName, methodName);
        this.callback = callback;
        parameters = new ArrayList<>();
        this.mReturnType = returnType;
    }

    public String getCallback() {
        return callback;
    }

    public void addParameterInfo(ParameterInfo parameterInfo) {
        parameters.add(parameterInfo);
    }

    public String getReturnType() {
        return mReturnType;
    }

    public List<ParameterInfo> getParameters() {
        return parameters;
    }
}
