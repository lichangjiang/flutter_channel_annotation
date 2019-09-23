package com.lcj.test;

import com.lcj.flutter_channel_annotation_ioc.builder.ArgumentBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.ClassBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.FieldBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.MethodBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.impl.DefaultClassBuilder;

import org.junit.Assert;
import org.junit.Test;

import static com.lcj.flutter_channel_annotation_ioc.builder.ClassBuilder.VOID;

public class ClassBuilderTest {

    @Test
    public void FieldBuilderTest() {
        FieldBuilder fieldBuilder = new DefaultClassBuilder().addField("name");
        String fieldStr = fieldBuilder
                .isFinal()
                .isStatic()
                .modifier(ClassBuilder.Modifier.PUBLIC)
                .type(VOID)
                .create();

        Assert.assertEquals(fieldStr, "\t\tfinal static public void name;\n");
    }

    @Test
    public void argumentTest() {
        ArgumentBuilder argumentBuilder = new DefaultClassBuilder().addMethod("main").addArgument("name");
        String argument = argumentBuilder.isFinal().type("String").create();
        Assert.assertEquals(argument, "final String name");
    }

    @Test
    public void MethodTest() {
        String method = "\t\tstatic public void main(String[] args) {\n"
                + "\t\t\t\tString s = \"hello World\";\n"
                + "\t\t\t\tSystem.out.println(s);\n"
                + "\t\t}\n";

        MethodBuilder methodBuilder = new DefaultClassBuilder().addMethod("main");
        String methodCreated = methodBuilder
                .returnType("void")
                .isStatic()
                .modifier(ClassBuilder.Modifier.PUBLIC)
                .addArgument("args").type("String[]").end()
                .addStatement("String s = \"hello World\";")
                .addStatement("System.out.println(s);")
                .create();

        Assert.assertEquals(method, methodCreated);
    }

    @Test
    public void classTest() {
        String clazz = "package com.lcj.proxy;\n"
                + "import java.lang.String;\n"
                + "public class Test {\n"
                + "\t\tprivate String s = \"hello world\";\n"
                + "\t\tstatic public void main(String[] args) {\n"
                + "\t\t\t\tString s = \"hello World\";\n"
                + "\t\t\t\tSystem.out.println(s);\n"
                + "\t\t}\n"
                + "}\n";

        ClassBuilder classBuilder = new DefaultClassBuilder();
        classBuilder.addPackage("com.lcj.proxy")
                .addImport("java.lang.String")
                .addField("s").type("String").modifier(ClassBuilder.Modifier.PRIVATE).assign("\"hello world\"").end()
                .modifier(ClassBuilder.Modifier.PUBLIC)
                .name("Test");

        MethodBuilder methodBuilder = classBuilder.addMethod("main")
                .returnType("void")
                .isStatic()
                .modifier(ClassBuilder.Modifier.PUBLIC)
                .addArgument("args").type("String[]").end()
                .addStatement("String s = \"hello World\";")
                .addStatement("System.out.println(s);");

        Assert.assertEquals(clazz,classBuilder.create());
    }

}
