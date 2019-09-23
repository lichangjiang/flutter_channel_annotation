package com.lcj.example.example.logic;


import java.util.concurrent.TimeUnit;

import io.flutter.Log;

public class LoginLogicImpl implements ILoginLogic {

    private static final String TAG = "EXAMPLE";

    @Override
    public UserInfo userLogin(String userName, String pwd) {
        Log.d(TAG,"dologin with username:" + userName + " pwd:" + pwd);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"login success");
        return new UserInfo(1,userName);
    }
}
