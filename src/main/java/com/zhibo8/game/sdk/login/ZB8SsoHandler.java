package com.zhibo8.game.sdk.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.zhibo8.game.sdk.ZB8Constant;
import com.zhibo8.game.sdk.ZB8Game;
import com.zhibo8.game.sdk.utils.CommonUtils;

import org.json.JSONObject;


/**
 * author : Jeeson
 * date : 2019年12月09日 17:27
 * email : jisheng@zhibo8.cc
 * description : 直播吧授权登录
 */
public class ZB8SsoHandler {

    public final static int ERROR_CODE_INIT_ERROR = 0x0001; // 初始化失败
    public final static int ERROR_CODE_NOT_INSTALL = 0x0002; // 没安装
    private static final String mUri = "zhibo8://client.auth";
    private static final String PARAM_URL_PACKAGE_NAME = "package_name";
    private static final String PARAM_URL_APP_ID = "appid";
    private static final String INTENT_STRING_ACCESS_TOKEN = "intent_string_access_token";
    private final static int REQUEST_CODE = 0x1001;
    private final static int RESULT_CODE = 0x2001;
    private final Handler handler;

    private final Object activity;

    private ZB8AuthListener authListener;

    // 是否不能打开授权
    private boolean couldNotStartWbSsoActivity;


    public ZB8SsoHandler(Object activity) {
        this.activity = activity;
        this.handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 开始吊起登录
     *
     * @param listener
     */
    public void authorize(ZB8AuthListener listener) {

        this.authListener = listener;
        couldNotStartWbSsoActivity = false;
        if (activity == null) {
            if (authListener != null) {
                authListener.onFailure(ERROR_CODE_INIT_ERROR, "传入的Activity为空");
            }
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse(buildUrl()));//参数拼接在URI后面 package_name使用者包名,后续参数可自行添加
            intent.putExtra("appid", ZB8Game.getConfig().getAppId());
            if (activity instanceof Activity){
                ((Activity)activity).startActivityForResult(intent, REQUEST_CODE);
            }else if (activity instanceof Fragment){
                ((Fragment)activity).startActivityForResult(intent, REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ZB8Game.getApplication(),"请先安装最新版直播吧app",Toast.LENGTH_SHORT).show();
            if (authListener != null) {
                authListener.onFailure(ERROR_CODE_NOT_INSTALL, "请先安装最新版直播吧app");
            }
            CommonUtils.goToMarket(ZB8Game.getApplication(), ZB8Constant.PACKAGE_NAME);
            couldNotStartWbSsoActivity = true;
        }
    }

    private String buildUrl() {
        return mUri;
    }

    /**
     * 登录回调，必须在onActivityResult调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            if (data != null) {
                try {
                    String tokenStr = data.getStringExtra(INTENT_STRING_ACCESS_TOKEN);
                    JSONObject jsonObject = new JSONObject(tokenStr);
                    if (!couldNotStartWbSsoActivity) {
                        if (authListener != null) {
                            authListener.onSuccess(jsonObject);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            if (!couldNotStartWbSsoActivity) {
                if (authListener != null) {
                    authListener.cancel();
                }
            }
        }
    }

}
