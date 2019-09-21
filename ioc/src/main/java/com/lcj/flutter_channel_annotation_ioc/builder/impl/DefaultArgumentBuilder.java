package com.lcj.flutter_channel_annotation_ioc.builder.impl;

import com.lcj.flutter_channel_annotation_ioc.builder.ArgumentBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.MethodBuilder;

public class DefaultArgumentBuilder implements ArgumentBuilder {

    private MethodBuilder methodBuilder;
    private String mName;
    private String mType;
    private boolean isFinal;

    public DefaultArgumentBuilder(MethodBuilder methodBuilder) {
        this.methodBuilder = methodBuilder;
    }

    @Override
    public ArgumentBuilder name(String name) {
        if (mName == null || mName.isEmpty()) mName = name;
        return this;
    }

    @Override
    public ArgumentBuilder type(String type) {
        if (mType == null || mType.isEmpty()) mType = type;
        return this;
    }

    @Override
    public ArgumentBuilder isFinal() {
        isFinal = true;
        return this;
    }

    @Override
    public MethodBuilder end() {
        return methodBuilder;
    }

    @Override
    public String create() {
        StringBuilder sb = new StringBuilder();
        if (isFinal) sb.append("final ");
        sb.append(mType + " ");
        sb.append(mName);
        return sb.toString();
    }
}
