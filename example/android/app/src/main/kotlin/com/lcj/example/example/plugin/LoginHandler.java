package com.lcj.example.example.plugin;


import com.lcj.example.example.logic.ILoginLogic;
import com.lcj.example.example.logic.LoginLogicImpl;
import com.lcj.example.example.logic.UserInfo;
import com.lcj.flutter_channel.annotation.annotation.AsyncMethodCall;
import com.lcj.flutter_channel.annotation.annotation.ChannelMethodCall;
import com.lcj.flutter_channel.annotation.annotation.MethodChannelHandler;
import com.lcj.flutter_channel.annotation.annotation.Parameter;

import java.util.HashMap;
import java.util.Map;

import io.flutter.Log;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

@MethodChannelHandler(
        pluginName = "com.lcj.example/loginPlugin",
        channelName = "com.lcj.example/login"
)
public class LoginHandler {

    private static final String TAG = "EXAMPLE";

    private ILoginLogic loginLogic = new LoginLogicImpl();

    @AsyncMethodCall(
            callback = "loginCallback",
            returnType = "com.lcj.example.example.logic.UserInfo"
    )
    //在其他线程被调用，使用asyncTask
    public UserInfo doLogin(@Parameter("userName") String userName,@Parameter("password") String pwd) {
        Log.d(TAG,"handle thread:" + Thread.currentThread().getName());
        return loginLogic.userLogin(userName,pwd);
    }

    //在主线程被调用
    public  Map<String,String> loginCallback(UserInfo userInfo) {
        Log.d(TAG,"callback thread:" + Thread.currentThread().getName());
        Map<String,String> map = new HashMap<>();
        if (userInfo == null) {
            map.put("msg","login failed");
        } else {
            map.put("id",userInfo.getId() + "");
            map.put("username",userInfo.getName());
        }
        return map;
    }

    /*
    @ChannelMethodCall
    //在主线程中，需要自己创建后台处理线程
    public void doLogin(MethodCall call, MethodChannel.Result result) {
        //doSomething
    }
    */


}
