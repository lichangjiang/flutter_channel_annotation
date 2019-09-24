# 使用annotation简化Flutter和Android Native之间通信
在一些项目中可能希望利用flutter来开发界面，而后端的网络通信和数据库存取等还是利用原生的Android Native代码。
这时候就需要使用flutter的Platform channel实现与Android Native的通信。

## flutter与Android Native通信常用方法：

**dart端调用代码**

```dart
//创建channel
static const channel = const MethodChannel("com.lcj.example/login");


void _doLogin() async {
    . . . . . . 

    final result = await channel
        .invokeMethod("doLogin", {"userName":_username, "password":_pwd});
    . . . . . .
}
```

**java端调用代码**

```java
public class LoginHandler implements MethodChannel.MethodCallHandler {
		
		final static private java.lang.String METHOD_CHANNEL = "com.lcj.example/login";
		final static private java.lang.String PLUGIN_NAME = "com.lcj.example/loginPlugin";
		public void onMethodCall(MethodCall call,MethodChannel.Result result) {
				switch (call.method) {
					case "doLogin":
						AsyncTask task = new AsyncTask<Object,Void,com.lcj.example.example.logic.UserInfo>() {
							@Override
							protected com.lcj.example.example.logic.UserInfo doInBackground(Object... voids) {
								Map<String,Object> map = (Map<String,Object>) call.arguments;
								java.lang.String p0 = (java.lang.String) map.get("userName");
								java.lang.String p1 = (java.lang.String) map.get("password");
								return doLogin(p0,p1);
							}
							@Override
							protected void onPostExecute(com.lcj.example.example.logic.UserInfo o) {
								if (result != null) result.success(delegate.loginCallback(o));
							}
						};
						task.execute();
						break;
                    case "anotherMethod":
                        //doSomething
                        break;
					default:
						result.notImplemented();
						break;
				}
		}
		
		public static void registerWith(PluginRegistry registry) {
				if (!registry.hasPlugin(PLUGIN_NAME)) {
					final MethodChannel methodChannel = new MethodChannel(registry.registrarFor(PLUGIN_NAME).messenger(),METHOD_CHANNEL);
					methodChannel.setMethodCallHandler(new LoginHandler());
				}
		}
}

//定义完handler还需要在Activity中注册channel,没实现一个handler都有注册一次
LoginHandler.registerWith(this);
```
上面的java端代码很多都是模板式的代码，注册channel，switch块，解析参数，创建异步AsyncTask等。业务代码只用doInBackground中的一小块。
为了实现一小块业务逻辑而需要写这么多模板代码也是很恼人的。<br>
在Android中很多框架都会使用注解来解决这种问题，通过APT机制在编译前生成这些模板化的代码。所以尝试实现一套简单的注解来简化flutter和android native
通信实现代码。

## 使用annotation实现通信处理：

**java端代码可简化为:**
![注解handler代码](https://github.com/lichangjiang/flutter_channel_annotation/blob/master/image/%E6%B3%A8%E8%A7%A3handler.png?raw=true)
通过MethodChannelHandler注解设置,channel的注册信息。用AsyncMethodCall设置dart端调用函数的处理函数与它的结果处理回调函数，
两个函数分别在后台线程和主线程执行，对应于AsyncTask的doInBackgroup和onPostExecute。所有通过注解的handler都只需要在
Activity中执行一条代码完成全部注册:`FlutterChannelFactory.register(this);`。<br>
使用annotation实现的handler有一些好处:
- 无需编写flutter channel的模板代码。
- 无需依赖flutter　API和Android API易于本地Junit测试。
- 当处理方法过多时避免的过多switch...case...代码块，更易读。

## 使用方法:
参考项目中example子项目。
