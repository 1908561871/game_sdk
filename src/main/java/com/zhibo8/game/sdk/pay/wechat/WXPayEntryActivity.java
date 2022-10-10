package com.zhibo8.game.sdk.pay.wechat;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "MicroMsg.WXPayEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        WXPayManager.OnWXPayCallBackListener onWXPayCallBackListener = WXPayManager.getInstance().getOnWXPayCallBackListener();
        if (onWXPayCallBackListener == null) {
            return;
        }
        if (resp.errCode == 0) {
            onWXPayCallBackListener.onSuccess();
        } else if (resp.errCode == -2) {
            onWXPayCallBackListener.onCancel();
        } else {
            onWXPayCallBackListener.onFailure();
        }

        ZB8LogUtils.d("微信支付回调： " + " code = " + resp.errCode + "msg = " + resp.errStr);
        finish();
    }

}