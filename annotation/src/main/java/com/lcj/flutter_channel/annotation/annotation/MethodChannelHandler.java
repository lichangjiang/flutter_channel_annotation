package com.lcj.flutter_channel.annotation.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface MethodChannelHandler {
    String pluginName() default "";
    String channelName() default "";
}
