package com.lcj.flutter_channel_annotation_ioc.builder;

public interface MethodBuilder extends Builder{

    MethodBuilder isFinal();
    MethodBuilder returnType(String type);
    ArgumentBuilder addArgument(String name);
    MethodBuilder addStatement(String stateMent);
    ClassBuilder end();
}
