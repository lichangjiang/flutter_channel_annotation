package com.lcj.flutter_channel_annotation_ioc.util;


import com.lcj.flutter_channel_annotation_ioc.builder.ClassBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.MethodBuilder;
import com.lcj.flutter_channel_annotation_ioc.builder.impl.DefaultClassBuilder;
import com.lcj.flutter_channel_annotation_ioc.model.AsyncMethodInfo;
import com.lcj.flutter_channel_annotation_ioc.model.MethodInfo;
import com.lcj.flutter_channel_annotation_ioc.model.ParameterInfo;

import java.util.Set;

import static com.lcj.flutter_channel_annotation_ioc.builder.ClassBuilder.STRING;

public class MethodChannelHandlerUtil {

    public final static String IMPORT_METHOD_CHANNEL = "io.flutter.plugin.common.MethodChannel";
    public final static String IMPORT_METHOD_CALL = "io.flutter.plugin.common.MethodCall";
    public final static String IMPORT_PLUGIN_RRGISTRY = "io.flutter.plugin.common.PluginRegistry";

    private MethodChannelHandlerUtil() {
    }


    public static String createRegisterClass(String classPath, String className, Set<String> generatedSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + classPath + ";\n");
        sb.append("import io.flutter.plugin.common.PluginRegistry;\n");
        sb.append("public class " + className + "{\n");
        sb.append("\tpublic static void registerAllHandler(PluginRegistry registry) {\n");
        for (String clazz : generatedSet) {
            sb.append("\t\t" + clazz + ".registerWith(registry);\n");
        }
        sb.append("\t}\n");
        sb.append("}");
        return sb.toString();
    }


    public static String createProxyClass(String proxyClassPath,
                                          String proxyClassName,
                                          String pluginName,
                                          String methodChannelName,
                                          String classQulifiedName,
                                          Set<MethodInfo> methodInfos,
                                          Set<AsyncMethodInfo> asyncMethodInfos,
                                          boolean needChannel) {

        ClassBuilder clazz = new DefaultClassBuilder()
                .name(proxyClassName)
                .addPackage(proxyClassPath)
                .addImport("android.os.AsyncTask")
                .addImport("java.lang.String")
                .addImport("java.util.Map")
                .addImport(IMPORT_METHOD_CALL)
                .addImport(IMPORT_METHOD_CHANNEL)
                .addImport(IMPORT_PLUGIN_RRGISTRY)
                .modifier(ClassBuilder.Modifier.PUBLIC)
                .implement("MethodChannel.MethodCallHandler")
                .addField("PLUGIN_NAME")
                .isFinal().isStatic().modifier(ClassBuilder.Modifier.PRIVATE).type(STRING).assign("\"" + pluginName + "\"")
                .end()
                .addField("METHOD_CHANNEL")
                .isFinal().isStatic().modifier(ClassBuilder.Modifier.PRIVATE).type(STRING).assign("\"" + methodChannelName + "\"")
                .end()
                .addField("channel")
                .modifier(ClassBuilder.Modifier.PRIVATE).type("MethodChannel")
                .end()
                .addField("delegate")
                .type(classQulifiedName).modifier(ClassBuilder.Modifier.PRIVATE)
                .end()
                .addMethod(proxyClassName).modifier(ClassBuilder.Modifier.PUBLIC)
                .addArgument("channel").type("MethodChannel").end()
                .addStatement("this.channel = channel;")
                .addStatement("this.delegate = new " + classQulifiedName + "(" + (needChannel ? "channel" : "") + ");")
                .end();

        createRegisterMethod(clazz, proxyClassName);
        createHandlerMethod(clazz, methodInfos, asyncMethodInfos);

        return clazz.create();
    }


    public static String createProxyClass(String proxyClassPath,
                                          String proxyClassName,
                                          String pluginName,
                                          String methodChannelName,
                                          String classQulifiedName,
                                          Set<MethodInfo> methodInfos,
                                          boolean needChannel) {
        return createProxyClass(proxyClassPath,
                proxyClassName,
                pluginName,
                methodChannelName,
                classQulifiedName,
                methodInfos,
                null,
                needChannel);
    }

    private static void createRegisterMethod(ClassBuilder clazz, String proxyClassName) {
        clazz.addMethod("registerWith")
                .isStatic().modifier(ClassBuilder.Modifier.PUBLIC).returnType("void")
                .addArgument("registry").type("PluginRegistry").end()
                .addStatement("if (!registry.hasPlugin(PLUGIN_NAME)) {")
                .addStatement("\tfinal MethodChannel methodChannel = new MethodChannel(registry.registrarFor(PLUGIN_NAME).messenger(),METHOD_CHANNEL);")
                .addStatement("\tmethodChannel.setMethodCallHandler(new " + proxyClassName + "(methodChannel));")
                .addStatement("}")
                .end();
    }

    private static void createHandlerMethod(ClassBuilder clazz, Set<MethodInfo> methodInfos, Set<AsyncMethodInfo> asyncMethodInfos) {
        MethodBuilder method = clazz.addMethod("onMethodCall").modifier(ClassBuilder.Modifier.PUBLIC).returnType("void")
                .addArgument("call").type("MethodCall").end()
                .addArgument("result").type("MethodChannel.Result").end()
                .addStatement("switch (call.method) {");

        if (methodInfos != null) {
            for (MethodInfo info : methodInfos) {
                method.addStatement("\tcase \"" + info.getMethodName() + "\":")
                        .addStatement("\t\tdelegate." + info.getMethodName() + "(call,result);")
                        .addStatement("\t\tbreak;");
            }
        }

        if (asyncMethodInfos != null) {
            for (AsyncMethodInfo info : asyncMethodInfos) {
                method.addStatement("\tcase \"" + info.getMethodName() + "\":")
                        .addStatement(createAsyncTaskTemple(info))
                        .addStatement("\t\tbreak;");
            }
        }

        method.addStatement("\tdefault:")
                .addStatement("\t\tresult.notImplemented();")
                .addStatement("\t\tbreak;")
                .addStatement("}");
    }


    private static String createAsyncTaskTemple(AsyncMethodInfo info) {
        String asyncTask = "\t\tAsyncTask task = new AsyncTask<Object,Void," + (info.getReturnType().isEmpty() ? "Void" : info.getReturnType()) + ">() {\n"
                + "\t\t\t\t\t\t\t@Override\n"
                + "\t\t\t\t\t\t\tprotected " + info.getReturnType() + " doInBackground(Object... voids) {\n"
                + "\t\t\t\t\t\t\t\tMap<String,Object> map = (Map<String,Object>) call.arguments;\n";

        int index = 0;
        for (ParameterInfo p : info.getParameters()) {
            asyncTask = asyncTask + "\t\t\t\t\t\t\t\t" + p.getType() + " p" + index + " = (" + p.getType() + ") map.get(\"" + p.getKey() + "\");\n";
            index++;
        }

        asyncTask = asyncTask + "\t\t\t\t\t\t\t\treturn delegate." + info.getMethodName() + "(";
        for (int i = 0; i < index; i++) {
            asyncTask = asyncTask + "p" + i;
            if (i < index - 1) asyncTask = asyncTask + ",";
        }
        asyncTask = asyncTask + ");\n";
        asyncTask = asyncTask +"\t\t\t\t\t\t\t}\n";
        asyncTask = asyncTask + "\t\t\t\t\t\t\t@Override\n";
        asyncTask = asyncTask + "\t\t\t\t\t\t\tprotected void onPostExecute(" + info.getReturnType() +" o) {\n";
        asyncTask = asyncTask + "\t\t\t\t\t\t\t\tif (result != null) result.success(delegate." + info.getCallback() + "(o));\n";
        asyncTask = asyncTask + "\t\t\t\t\t\t\t}\n";
        asyncTask = asyncTask + "\t\t\t\t\t\t};\n";
        asyncTask = asyncTask + "\t\t\t\t\t\ttask.execute();";
        return asyncTask;
    }
}
