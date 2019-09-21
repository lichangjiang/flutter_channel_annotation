package com.lcj.flutter_channel_annotation_ioc;

import com.google.auto.service.AutoService;
import com.lcj.flutter_channel.annotation.annotation.ChannelMethodCall;
import com.lcj.flutter_channel.annotation.annotation.MethodChannelConstruct;
import com.lcj.flutter_channel.annotation.annotation.MethodChannelHandler;
import com.lcj.flutter_channel_annotation_ioc.model.MethodInfo;
import com.lcj.flutter_channel_annotation_ioc.util.MethodChannelHandlerUtil;

import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class IocProcessor extends AbstractProcessor {

    private Filer mFileUtils;
    private Elements mElementUtils;
    private Messager mMessager;
    private Set<String> generatedSet;

    public static final String HANDLER_SUFFIX = "$$MethodChannelHandler";
    public static final String PROXY_CLASS_PATH = "com.lcj.flutter.methodchannel.proxy";


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFileUtils = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        generatedSet = new HashSet<>();
        System.out.println("process init");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(MethodChannelHandler.class.getCanonicalName());
        annotationTypes.add(ChannelMethodCall.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> methodChannelHandlerElements = roundEnv.getElementsAnnotatedWith(MethodChannelHandler.class);

        if (roundEnv.processingOver()) {
            createRegisterClass();
            return true;
        }

        if (methodChannelHandlerElements.size() == 0) return false;
        for (Element e : methodChannelHandlerElements) {
            if (e.getKind() != ElementKind.CLASS) {
                System.out.println("====================error==========================");
                System.out.println("Only classed can be annotated with " + MethodChannelHandler.class.getSimpleName());
                System.out.println("===================================================");
                return true;
            }
            System.out.println("begin to handleMethodChannelHandler " + e.getSimpleName());
            String proxyClass = handleMethodChannelHandler((TypeElement) e);
            if (proxyClass != null) {
                generatedSet.add(proxyClass);
            }
        }
        return true;
    }

    private void createRegisterClass() {
        String classPath = PROXY_CLASS_PATH;
        String className = "HandlerRegister";
        String clazz = MethodChannelHandlerUtil.createRegisterClass(classPath,className,generatedSet);
        writeSource(clazz,classPath,className);
    }

    private String handleMethodChannelHandler(TypeElement typeElement) {
        String className = typeElement.getQualifiedName().toString();
        String proxyClassPath = PROXY_CLASS_PATH;
        String proxyClassName = typeElement.getSimpleName().toString() + HANDLER_SUFFIX;
        String channelMethodName = typeElement.getAnnotation(MethodChannelHandler.class).channelName();
        String plugindName = typeElement.getAnnotation(MethodChannelHandler.class).pluginName();
        Set<MethodInfo> methodInfos = new HashSet<>();

        boolean hasChannelConstruct = false;
        for (Element e : typeElement.getEnclosedElements()) {
            if (e.getKind() == ElementKind.METHOD) {
                ExecutableElement methodElement = (ExecutableElement) e;
                ChannelMethodCall call = methodElement.getAnnotation(ChannelMethodCall.class);
                if (call != null) {
                    methodInfos.add(new MethodInfo(className, methodElement.getSimpleName().toString()));
                }
            } else if (e.getKind() == ElementKind.CONSTRUCTOR) {
                hasChannelConstruct = e.getAnnotation(MethodChannelConstruct.class) != null ? true : false;
            }
        }
        String fileContent = MethodChannelHandlerUtil.createProxyClass(proxyClassPath,
                proxyClassName,
                plugindName,
                channelMethodName,
                className,
                methodInfos,
                hasChannelConstruct);

        if (writeSource(fileContent,proxyClassPath,proxyClassName)) {
            return proxyClassPath + "." + proxyClassName;
        } else {
            return null;
        }
    }

    private boolean writeSource(String fileContent,String proxyClassPath,String proxyClassName) {
        System.out.println("==============================================");
        System.out.println(fileContent);
        System.out.println("==============================================");
        try {
            JavaFileObject source = mFileUtils.createSourceFile(proxyClassPath + "." + proxyClassName);
            Writer writer = source.openWriter();
            writer.write(fileContent);
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            System.out.println("======================error=========================");
            System.out.println("write class content to file " +  proxyClassPath + "." + proxyClassName + "error:" + e.getLocalizedMessage());
            System.out.println("====================================================");
            return false;
        }
    }



}
