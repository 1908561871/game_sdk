package com.zhibo8.game.sdk;

import android.content.Context;

import com.zhibo8.game.sdk.bean.ZBOrderInfo;
import com.zhibo8.game.sdk.login.ZB8LoginManager;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;
import com.zhibo8.game.sdk.utils.ZB8SPUtils;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8Game {

    private static ZB8RequestCallBack requestCallBack;

    private static Config config;

    private static boolean isInit;

    private static Context application;


    private static void init(Context context, Config config) {
        if (!isInit) {
            ZB8Game.isInit = true;
            ZB8Game.config = config;
            ZB8Game.application = context.getApplicationContext();
            ZB8SPUtils.init(context);
            if (config != null) {
                ZB8LogUtils.isDebug(config.debug);
            }
        }
    }

    public static Config builder() {
        return new Config();
    }


    public static void login(Context context, ZB8RequestCallBack callBack) {
        if (!isInit) {
            callBack.onFailure(ZB8CodeInfo.CODE_UNINITIALIZED, ZB8CodeInfo.MSG_UNINITIALIZED);
            return;
        }
        requestCallBack = new ZB8LoginProxyRequestCallBack(callBack);
        ZB8ContainerActivity.open(context, ZB8Constant.TYPE_AUTHOR);
    }

    public static void logout() {
        ZB8LoginManager.getInstance().logout();
    }


    public static void pay(Context context, String order, String price, ZB8RequestCallBack callBack) {
        if (!isInit) {
            callBack.onFailure(ZB8CodeInfo.CODE_UNINITIALIZED, ZB8CodeInfo.MSG_UNINITIALIZED);
            return;
        }
        requestCallBack = callBack;
        ZBOrderInfo orderInfo = new ZBOrderInfo();
        orderInfo.setOrder(order);
        orderInfo.setPrice(price);
        ZB8ContainerActivity.open(context, ZB8Constant.TYPE_PAY, orderInfo);
    }

    public static Config getConfig() {
        return config;
    }

    public static boolean isIsInit() {
        return isInit;
    }

    public static Context getApplication() {
        return application;
    }

    public static ZB8RequestCallBack getRequestCallBack() {
        return requestCallBack;
    }

    public static void setRequestCallBack(ZB8RequestCallBack requestCallBack) {
        ZB8Game.requestCallBack = requestCallBack;
    }

    public static class Config {
        String appId;
        boolean debug;

        public Config setAppId(String appId) {
            this.appId = appId;
            return this;
        }


        public Config setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public String getAppId() {
            return appId;
        }


        public boolean isDebug() {
            return debug;
        }

        public void init(Context context) {
            ZB8Game.init(context, this);
        }

    }

}
