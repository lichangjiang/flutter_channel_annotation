package com.lcj.flutter_channel_annotation_ioc.builder;

public interface MethodBuilder extends Builder{

    MethodBuilder name(String name);
    MethodBuilder modifier(ClassBuilder.Modifier modifier);
    MethodBuilder isFinal();
    MethodBuilder isStatic();
    MethodBuilder returnType(String type);
    MethodBuilder addStatement(String statement);
    ArgumentBuilder addArgument(String name);
    ClassBuilder end();
}
