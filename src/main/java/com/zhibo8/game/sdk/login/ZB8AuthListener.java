package com.zhibo8.game.sdk.login;

import org.json.JSONObject;

/**
 * author : Jeeson
 * date : 2019年12月09日 17:41
 * email : jisheng@zhibo8.cc
 * description : 登录回调
 */
public interface ZB8AuthListener {

    void onSuccess(JSONObject var1);

    void cancel();

    void onFailure(int errorCode, String errorMsg);
}
