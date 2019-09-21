package com.lcj.flutter_channel_annotation_ioc.util;


import com.lcj.flutter_channel_annotation_ioc.model.MethodInfo;

import java.util.Set;
import java.util.regex.Matcher;

public class MethodChannelHandlerUtil {

    public final static String IMPORT_METHOD_CHANNEL = "import io.flutter.plugin.common.MethodChannel;\n";
    public final static String IMPORT_METHOD_CALL = "import io.flutter.plugin.common.MethodCall;\n";
    public final static String IMPORT_PLUGIN_RRGISTRY = "import io.flutter.plugin.common.PluginRegistry;\n";

    public final static String CLASS_NAME = "CLASS_NAME";
    public final static String METHOD_NAME = "METHOD_NAME";
    public final static String CASE_BLOCK = "CASE_BLOCK";
    public final static String METHOD_CHANNEL_NAME = "METHOD_CHANNEL_NAME";
    public final static String CASE_NAME = "CASE_NAME";
    public final static String PLIGIN_NAME = "PLUGIN_NAME_VALUE";
    public final static String TARGET_CLASS = "TARGET_CLASS";
    public final static String INSERT_CHANNEL = "INSERT_CHANNEL";


    public final static String HANDLER_TEMPLATE = "public class CLASS_NAME implements MethodChannel.MethodCallHandler {\n"
            + "\tprivate final static String PLUGIN_NAME = \"PLUGIN_NAME_VALUE\";\n"
            + "\tprivate final static String METHOD_CHANNEL = \"METHOD_CHANNEL_NAME\";\n"
            + "\tprivate MethodChannel channel;\n"
            + "\tprivate TARGET_CLASS delegate;\n"
            + "\tpublic CLASS_NAME(MethodChannel channel) {\n"
            + "\t\tthis.channel = channel;\n"
            + "\t\tthis.delegate = new TARGET_CLASS(INSERT_CHANNEL);\n"
            + "\t}\n"
            + "\t@Override\n"
            + "\tpublic void onMethodCall(MethodCall call,MethodChannel.Result result) {\n"
            + "\t\tswitch (call.method) {\n"
            + "CASE_BLOCK\n"
            + "\t\t}\n"
            + "\t}\n"
            + "\tpublic static void registerWith(PluginRegistry registry) {\n"
            + "\t\tif (!registry.hasPlugin(PLUGIN_NAME)) {\n"
            + "\t\t\tfinal MethodChannel methodChannel = new MethodChannel(registry.registrarFor(PLUGIN_NAME).messenger(),METHOD_CHANNEL);\n"
            + "\t\t\tmethodChannel.setMethodCallHandler(new CLASS_NAME(methodChannel));\n"
            + "\t\t}\n"
            + "\t}\n"
            + "}";


    public final static String ACASE = "\t\t\tcase \"CASE_NAME\":\n"
            + "\t\t\t\tdelegate.METHOD_NAME(call,result);\n"
            + "\t\t\t\tbreak;\n";

    public final static String DEFAULT_CASE = "\t\t\tdefault:\n"
            + "\t\t\t\tresult.notImplemented();\n"
            + "\t\t\t\tbreak;\n";

    private MethodChannelHandlerUtil() {
    }


    public static String createRegisterClass(String classPath,String className,Set<String> generatedSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + classPath +";\n");
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
                                          boolean needChannel) {
        StringBuilder sb = new StringBuilder();

        for (MethodInfo methodInfo : methodInfos) {
            String acase = ACASE.replace(CASE_NAME, methodInfo.getMethodName())
                    .replace(METHOD_NAME, methodInfo.getMethodName());
            sb.append(acase);
        }
        sb.append(DEFAULT_CASE);
        String classDefine = HANDLER_TEMPLATE.replaceAll(CLASS_NAME, Matcher.quoteReplacement(proxyClassName))
                .replaceAll(TARGET_CLASS, classQulifiedName)
                .replace(INSERT_CHANNEL,needChannel ? "channel" : "")
                .replace(PLIGIN_NAME,pluginName)
                .replace(METHOD_CHANNEL_NAME, methodChannelName)
                .replace(CASE_BLOCK, sb.toString());

        sb.delete(0, sb.length());
        sb.append("package ");
        sb.append(proxyClassPath + ";\n");
        sb.append(IMPORT_METHOD_CALL);
        sb.append(IMPORT_METHOD_CHANNEL);
        sb.append(IMPORT_PLUGIN_RRGISTRY);
        sb.append(classDefine);

        return sb.toString();
    }
}
