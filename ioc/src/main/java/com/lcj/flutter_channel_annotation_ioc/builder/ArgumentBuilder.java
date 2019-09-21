package com.lcj.flutter_channel_annotation_ioc.builder;

public interface ArgumentBuilder extends Builder{
    ArgumentBuilder type(String type);
    ArgumentBuilder isFinal();
    MethodBuilder end();
}
