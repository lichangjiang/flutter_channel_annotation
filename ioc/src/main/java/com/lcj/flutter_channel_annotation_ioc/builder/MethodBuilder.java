package com.lcj.flutter_channel_annotation_ioc.builder;

public interface MethodBuilder extends Builder{

    MethodBuilder name(String name);
    MethodBuilder modifier(ClassBuilder.Modifier modifier);
    MethodBuilder isFinal();
    MethodBuilder isStatic();
    MethodBuilder returnType(String type);
    ArgumentBuilder addArgument(String name);
    MethodBuilder addStatement(String stateMent);
    MethodBuilder addArgument(ArgumentBuilder argument);
    ClassBuilder end();
}
