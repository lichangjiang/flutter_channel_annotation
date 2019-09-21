package com.lcj.flutter_channel_annotation_ioc.builder;

public interface FieldBuilder extends Builder {
    FieldBuilder name(String name);
    FieldBuilder assign(String value);
    FieldBuilder type(String type);
    FieldBuilder isStatic();
    FieldBuilder isFinal();
    FieldBuilder modifier(ClassBuilder.Modifier modifier);
    ClassBuilder end();
}
