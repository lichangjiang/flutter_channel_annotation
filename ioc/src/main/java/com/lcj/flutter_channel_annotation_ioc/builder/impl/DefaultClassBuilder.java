package com.lcj.flutter_channel_annotation_ioc.builder.impl;

import com.lcj.flutter_channel_annotation_ioc.builder.ClassBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.FieldBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.MethodBuilder;

import java.util.HashSet;
import java.util.Set;

public class DefaultClassBuilder implements ClassBuilder {
    private String mPackage;
    private String mName;
    private boolean isFinal;
    private boolean isStatic;
    private Modifier mModifier;
    private Set<String> mImportSet = new HashSet<>();
    private Set<FieldBuilder> mFieldSet = new HashSet<>();
    private Set<MethodBuilder> mMethodSet = new HashSet<>();
    private Set<String> mInterfaces = new HashSet<>();
    private String mSuperClass;

    @Override
    public ClassBuilder extend(String superclass) {
        if (mSuperClass == null || mSuperClass.isEmpty()) mSuperClass = superclass;
        return this;
    }

    @Override
    public ClassBuilder implement(String interfaces) {
        mInterfaces.add(interfaces);
        return this;
    }

    @Override
    public ClassBuilder addPackage(String pkg) {
        if (mPackage == null || mPackage.isEmpty()) mPackage = pkg;
        return this;
    }

    @Override
    public ClassBuilder addImport(String imp) {
        if (imp != null) mImportSet.add(imp);
        return this;
    }

    @Override
    public ClassBuilder name(String name) {
        if (mName == null || mName.isEmpty()) mName = name;
        return this;
    }

    @Override
    public ClassBuilder isFinal() {
        isFinal = true;
        return this;
    }

    @Override
    public ClassBuilder isStatic() {
        isStatic = true;
        return this;
    }

    @Override
    public ClassBuilder modifier(Modifier modifier) {
        mModifier = modifier;
        return this;
    }


    @Override
    public FieldBuilder addField(String name) {
        FieldBuilder field = new DefaultFieldBuilder(this);
        field.name(name);
        mFieldSet.add(field);
        return field;
    }

    @Override
    public MethodBuilder addMethod(String name) {
        MethodBuilder method = new DefaultMethodBuilder(this);
        method.name(name);
        mMethodSet.add(method);
        return method;
    }

    @Override
    public String create() {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + mPackage + ";\n");
        for (String imp : mImportSet) {
            sb.append("import " + imp + ";\n");
        }

        if (isFinal) sb.append("final ");
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
                sb.append("");
        }

        if (isStatic) sb.append("static ");
        sb.append("class ");
        sb.append(mName + " ");

        if (mSuperClass != null && !mSuperClass.isEmpty())
            sb.append("extends " + mSuperClass + " ");

        if (mInterfaces.size() > 0)
            sb.append("implements ");

        int index = 0;
        for (String i : mInterfaces) {
            sb.append(i);
            if (index < mInterfaces.size() - 1) sb.append(",");
        }

        sb.append(" {\n");

        for (FieldBuilder field : mFieldSet) {
            sb.append(field.create());
        }

        for (MethodBuilder method : mMethodSet) {
            sb.append(method.create());
        }

        sb.append("}\n");
        return sb.toString();
    }
}
