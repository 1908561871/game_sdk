package com.zhibo8.game.sdk;

import org.json.JSONObject;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : 回调接口
 */
public interface ZB8RequestCallBack {

    void onFailure(int code,String info);

    void onSuccess(JSONObject jsonObject);

    void onCancel();
}
