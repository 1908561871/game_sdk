package com.zhibo8.game.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhibo8.game.sdk.bean.ZBOrderInfo;
import com.zhibo8.game.sdk.login.ZB8AuthLoginFragment;
import com.zhibo8.game.sdk.login.ZB8LoginManager;
import com.zhibo8.game.sdk.login.ZB8SsoHandler;
import com.zhibo8.game.sdk.pay.ZB8PayDetailFragment;
import com.zhibo8.game.sdk.utils.AppUtils;
import com.zhibo8.game.sdk.utils.CommonUtils;
import com.zhibo8.game.sdk.verify.ZB8UserVerifyFragment;

import org.json.JSONObject;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8ContainerActivity extends AppCompatActivity {

    private int mType;
    private ZBOrderInfo mOrderInfo;

    private ZB8RequestCallBack callBack;

    private ZB8SsoHandler zhibo8SsoHandler;


    public static void open(Context context, int type) {
        Intent intent = new Intent(context, ZB8ContainerActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void open(Context context, int type, ZBOrderInfo orderInfo) {
        Intent intent = new Intent(context, ZB8ContainerActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("type", type);
        intent.putExtra("order", orderInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getIntExtra("type", -1);
        mOrderInfo = getIntent().getParcelableExtra("order");
        callBack = ZB8Game.getRequestCallBack();
        ZB8Game.setRequestCallBack(null);
        if (mType == ZB8Constant.TYPE_PAY) {
            toPay();
        } else if (mType == ZB8Constant.TYPE_AUTHOR) {
            toAuthorize();
        }
    }


    private void toAuthorize() {
        zhibo8SsoHandler = new ZB8SsoHandler(this);
        if (AppUtils.isInstall(this, ZB8Constant.PACKAGE_NAME)) {
            ZB8AuthLoginFragment zb8AuthLoginFragment = ZB8AuthLoginFragment.getInstance();
            zb8AuthLoginFragment.setZhibo8SsoHandler(zhibo8SsoHandler);
            zb8AuthLoginFragment.setCallBack(new ZB8RequestCallBack() {
                @Override
                public void onSuccess(JSONObject json) {
                    zb8AuthLoginFragment.dismiss();
                    if (ZB8LoginManager.getInstance().hasLogin()){
                        callBack.onSuccess(json);
                        finish();
                    }else {
                        toVerify(json);
                    }
                }

                @Override
                public void onFailure(int code, String info) {
                    Toast.makeText(ZB8Game.getApplication(), "授权失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    callBack.onFailure(code, info);
                }

                @Override
                public void onCancel() {
                    callBack.onCancel();
                    Toast.makeText(ZB8Game.getApplication(), "用户取消授权", Toast.LENGTH_SHORT).show();
                }
            });
            zb8AuthLoginFragment.show(getSupportFragmentManager(),"authorize");
        } else {
            Toast.makeText(ZB8Game.getApplication(), "请安装最新版本直播吧app", Toast.LENGTH_SHORT).show();
            CommonUtils.goToMarket(this, ZB8Constant.PACKAGE_NAME);
            callBack.onFailure(ZB8CodeInfo.CODE_NOT_INSTALL, ZB8CodeInfo.MSG_NOT_INSTALL);
            finish();
        }
    }


    private void toPay() {
        ZB8PayDetailFragment zb8PayDetailFragment =ZB8PayDetailFragment.getInstance(mOrderInfo);
        zb8PayDetailFragment.setCallBack(new ZB8RequestCallBack() {
            @Override
            public void onFailure(int code, String info) {
                Toast.makeText(ZB8Game.getApplication(), "支付失败", Toast.LENGTH_SHORT).show();
                callBack.onFailure(code, info);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                callBack.onSuccess(jsonObject);
                finish();
            }

            @Override
            public void onCancel() {
                callBack.onCancel();
                Toast.makeText(ZB8Game.getApplication(), "支付失败", Toast.LENGTH_SHORT).show();
            }
        });
        zb8PayDetailFragment.show(getSupportFragmentManager(),"pay");
    }

    private void toVerify(JSONObject jsonObject) {
        ZB8UserVerifyFragment zb8UserVerifyDialog = ZB8UserVerifyFragment.getInstance(jsonObject);
        zb8UserVerifyDialog.setCallBack(new ZB8RequestCallBack() {
            @Override
            public void onFailure(int code, String info) {
                Toast.makeText(ZB8Game.getApplication(), "认证失败，请稍后再试", Toast.LENGTH_SHORT).show();
                callBack.onFailure(code, info);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                callBack.onSuccess(jsonObject);
                finish();
            }

            @Override
            public void onCancel() {
                callBack.onCancel();
            }
        });
        zb8UserVerifyDialog.show(getSupportFragmentManager(),"verify");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (zhibo8SsoHandler != null) {
            zhibo8SsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
