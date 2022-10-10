package com.zhibo8.game.sdk;

import com.zhibo8.game.sdk.login.ZB8LoginManager;

import org.json.JSONObject;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8LoginProxyRequestCallBack implements ZB8RequestCallBack {
    private ZB8RequestCallBack callBack;

    public ZB8LoginProxyRequestCallBack(ZB8RequestCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onFailure(int code, String info) {
        callBack.onFailure(code, info);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {

            String access_token = jsonObject.optString("access_token");
            String refresh_token = jsonObject.optString("refresh_token");
            ZB8LoginManager.getInstance().login(access_token, refresh_token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        callBack.onSuccess(jsonObject);
    }

    @Override
    public void onCancel() {
        callBack.onCancel();
    }
}
