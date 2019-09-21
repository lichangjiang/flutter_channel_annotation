package com.lcj.flutter_channel_annotation_ioc.builder.impl;

import com.lcj.flutter_channel_annotation_ioc.builder.ArgumentBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.ClassBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.MethodBuilder;

import java.util.ArrayList;
import java.util.List;

public class DefaultMethodBuilder implements MethodBuilder {

    private ClassBuilder classBuilder;
    private boolean isFinal;
    private boolean isStatic;
    private ClassBuilder.Modifier mModifier;
    private String mReturnType;
    private String mName;
    private List<ArgumentBuilder> mArgumenSet = new ArrayList<>();
    private List<String> mStatementList = new ArrayList<>();

    public DefaultMethodBuilder(ClassBuilder classBuilder) {
        this.classBuilder = classBuilder;
    }

    @Override
    public MethodBuilder name(String name) {
        if (mName == null || mName.isEmpty()) mName = name;
        return this;
    }

    @Override
    public MethodBuilder modifier(ClassBuilder.Modifier modifier) {
        mModifier = modifier;
        return this;
    }

    @Override
    public MethodBuilder isFinal() {
        isFinal = true;
        return this;
    }

    @Override
    public MethodBuilder isStatic() {
        isStatic = true;
        return this;
    }

    @Override
    public MethodBuilder returnType(String type) {
        if (mReturnType == null || mReturnType.isEmpty()) mReturnType = type;
        return this;
    }

    @Override
    public ArgumentBuilder addArgument(String name) {
        ArgumentBuilder argumentBuilder = new DefaultArgumentBuilder(this);
        argumentBuilder.name(name);
        mArgumenSet.add(argumentBuilder);
        return argumentBuilder;
    }

    @Override
    public MethodBuilder addStatement(String stateMent) {
        if (stateMent != null) mStatementList.add(stateMent);
        return this;
    }

    @Override
    public MethodBuilder addArgument(ArgumentBuilder argument) {
        if (argument != null) mArgumenSet.add(argument);
        return this;
    }

    @Override
    public ClassBuilder end() {
        return classBuilder;
    }

    @Override
    public String create() {
        StringBuilder sb = new StringBuilder("\t\t");
        if (isFinal) sb.append("final ");
        if (isStatic) sb.append("static ");
        switch (mModifier) {
            case PUBLIC:
                sb.append("public ");
                break;
            case PRIVATE:
                sb.append("private ");
                break;
            case PROTECTED:
                sb.append("protected ");
                break;
            case DEFAULT:
            default:
                break;
        }
        sb.append(mReturnType + " ");
        sb.append(mName + "(");
        for (int i = 0;i < mArgumenSet.size();i++) {
            sb.append(mArgumenSet.get(i).create());
            if (i < mArgumenSet.size() - 1) sb.append(",");
        }
        sb.append(") {\n");
        for (String statement : mStatementList) {
            sb.append("\t\t\t\t" + statement + "\n");
        }
        sb.append("\t\t}\n");
        return sb.toString();
    }
}
