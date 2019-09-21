package com.lcj.test;

import com.lcj.flutter_channel_annotation_ioc.IocProcessor;
import com.lcj.flutter_channel_annotation_ioc.model.MethodInfo;
import com.lcj.flutter_channel_annotation_ioc.util.MethodChannelHandlerUtil;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class CreateClassTest {

    @Test
    public void createRegisterClass() {
        Set<String> set = new HashSet<>();
        set.add("com.lcj.flutter.methodchannel.proxy.JuejinHandler$$MethodChannelHandler");
        set.add("com.lcj.flutter.methodchannel.proxy.UserLoginHandler$$MethodChannelHandler");
        String className = "HandlerRegister";

        String clazz = MethodChannelHandlerUtil.createRegisterClass(IocProcessor.PROXY_CLASS_PATH,className,set);

        System.out.println(clazz);
        Assert.assertTrue(clazz.startsWith("package com.lcj.flutter.methodchannel.proxy;"));
    }


    @Test
    public void createClassDefine() {
        Set<MethodInfo> methodInfos = new HashSet<>();
        methodInfos.add(new MethodInfo("com.lcj.plugin.Example","doLogin"));
        methodInfos.add(new MethodInfo("com.lcj.plugin.Example","queryUser"));
        methodInfos.add(new MethodInfo("com.lcj.plugin.Example","updateInfo"));


        String result = MethodChannelHandlerUtil.createProxyClass(
                IocProcessor.PROXY_CLASS_PATH,
                "Example" + IocProcessor.HANDLER_SUFFIX,
                "com.lcj.channel.example_plugin",
                "com.lcj.channel/example",
                "com.lcj.plugin.Example",
                methodInfos,
                true);
        System.out.println(result);
        assertTrue(!result.isEmpty());
        assertTrue(result.startsWith("package " + IocProcessor.PROXY_CLASS_PATH));
        assertTrue(result.contains("public class Example" + IocProcessor.HANDLER_SUFFIX));
    }

}
