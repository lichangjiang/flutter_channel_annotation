package com.lcj.flutter_channel_annotation_ioc_api;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class FlutterChannelFactory {

    private FlutterChannelFactory(){}

    public static final String PROXY_CLASS_PATH = "com.lcj.flutter.methodchannel.proxy";

    public static void register(Object register) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String proxyClazzName = PROXY_CLASS_PATH + ".HandlerRegister";
        Class<?> proxyClazz = Class.forName(proxyClazzName);
        Class<?> pluginRegistryClazz = Class.forName("io.flutter.plugin.common.PluginRegistry");
        if (!pluginRegistryClazz.isAssignableFrom(register.getClass())) {
            throw new ClassCastException(register.getClass().getCanonicalName() + " can not be cast to io.flutter.plugin.common.PluginRegistry");
        }
        Method method = proxyClazz.getMethod("registerAllHandler",pluginRegistryClazz);
        method.invoke(null,register);
    }
}
