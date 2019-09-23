package com.lcj.flutter_channel_annotation_ioc.builder;

public interface ClassBuilder extends Builder{

    String VOID = "void";
    String STRING = "java.lang.String";
    String OBJECT = "object";

    public enum Modifier {
        PUBLIC,PROTECTED,PRIVATE,DEFAULT
    }

    ClassBuilder addPackage(String pkg);
    ClassBuilder addImport(String imp);
    ClassBuilder name(String name);
    ClassBuilder isFinal();
    ClassBuilder isStatic();
    ClassBuilder modifier(Modifier modifier);
    FieldBuilder addField(String name);
    MethodBuilder addMethod(String name);
}
