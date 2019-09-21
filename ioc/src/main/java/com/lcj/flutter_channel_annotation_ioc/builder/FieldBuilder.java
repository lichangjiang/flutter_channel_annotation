package com.lcj.flutter_channel_annotation_ioc.builder;

import java.io.FileWriter;

public interface FieldBuilder extends Builder{
    FieldBuilder type(String type);
    FieldBuilder isStatic();
    FileWriter isFinal();
    FieldBuilder modifier(ClassBuilder.Modifier modifier);
    ClassBuilder end();
}
