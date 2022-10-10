package com.zhibo8.game.sdk.login;

import android.text.TextUtils;

import com.zhibo8.game.sdk.utils.ZB8SPUtils;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8LoginManager {

    private static ZB8LoginManager instance = new ZB8LoginManager();

    public static ZB8LoginManager getInstance() {
        return instance;
    }

    private ZB8LoginManager() {

    }

    public void login(String token,String refresh_token) {
        ZB8SPUtils.putString("access_token", token);
        ZB8SPUtils.putString("refresh_token", refresh_token);
    }

    public void logout() {
        ZB8SPUtils.putString("access_token", "");
        ZB8SPUtils.putString("refresh_token", "");
    }

    public boolean hasLogin(){
       return !TextUtils.isEmpty(getToken());
    }


    public String getToken() {
        return ZB8SPUtils.getString("access_token");
    }


    public String getRefreshToken() {
        return ZB8SPUtils.getString("refresh_token");
    }
}
