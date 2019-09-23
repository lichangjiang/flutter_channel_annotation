package com.lcj.flutter_channel_annotation_ioc.builder.impl;

import com.lcj.flutter_channel_annotation_ioc.builder.ClassBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.FieldBuilder;

public class DefaultFieldBuilder implements FieldBuilder {

    private String mAssignment;
    private ClassBuilder classBuilder;
    private String mName;
    private String mType;
    private boolean isFinal;
    private boolean isStatic;
    private ClassBuilder.Modifier mModifier;

    protected DefaultFieldBuilder(ClassBuilder classBuilder) {
        this.classBuilder = classBuilder;
    }

    @Override
    public FieldBuilder name(String name) {
        if (mName == null || mName.isEmpty()) mName = name;
        return this;
    }

    @Override
    public FieldBuilder assign(String value) {
        if (mAssignment == null || mAssignment.isEmpty()) {
            mAssignment = value;
        }
        return this;
    }

    @Override
    public FieldBuilder type(String type) {
        if (mType == null || mType.isEmpty()) mType = type;
        return this;
    }

    @Override
    public FieldBuilder isStatic() {
        isStatic = true;
        return this;
    }

    @Override
    public FieldBuilder isFinal() {
        isFinal = true;
        return this;
    }

    @Override
    public FieldBuilder modifier(ClassBuilder.Modifier modifier) {
        mModifier = modifier;
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
            case PROTECTED:
                sb.append("protected ");
                break;
            case PRIVATE:
                sb.append("private ");
                break;
            case DEFAULT:
            default:
                sb.append("");
                break;
        }

        sb.append(mType +" ");
        sb.append(mName);

        if (mAssignment != null && !mAssignment.isEmpty()) sb.append(" = " + mAssignment + ";\n");
        else sb.append(";\n");
        return sb.toString();
    }
}
