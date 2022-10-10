package com.zhibo8.game.sdk.pay.wechat;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/29
 * email : 1908561871@qq.com
 * description : TODO
 */
public class WXPayManager {

    public static WXPayManager instance = new WXPayManager();

    private  OnWXPayCallBackListener onWXPayCallBackListener;

    public static WXPayManager getInstance() {
        return instance;
    }

    private WXPayManager() {
    }

    public void setOnWXPayCallBackListener(OnWXPayCallBackListener onWXPayCallBackListener) {
        this.onWXPayCallBackListener = onWXPayCallBackListener;
    }

    public OnWXPayCallBackListener getOnWXPayCallBackListener() {
        return onWXPayCallBackListener;
    }

    public interface OnWXPayCallBackListener {
        void onSuccess();

        void onFailure();

        void onCancel();
    }
}
