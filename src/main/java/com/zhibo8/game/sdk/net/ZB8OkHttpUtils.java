package com.zhibo8.game.sdk.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.zhibo8.game.sdk.utils.MD5Utils;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/27
 * email : 1908561871@qq.com
 * description : okhttp请求
 */
public class ZB8OkHttpUtils {


    private final Handler mHandler;
    private final OkHttpClient mClient;
    private static volatile ZB8OkHttpUtils sOkHttpUtils;

    private ZB8OkHttpUtils() {
        mHandler = new Handler(Looper.getMainLooper());
        mClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(ZB8SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
    }

    public static ZB8OkHttpUtils getInstance() {
        if (sOkHttpUtils == null) {
            synchronized (ZB8OkHttpUtils.class) {
                if (sOkHttpUtils == null) {
                    sOkHttpUtils = new ZB8OkHttpUtils();
                }
            }
        }
        return sOkHttpUtils;
    }

    public interface OkHttpCallBackListener {
        void failure(Exception e);

        void success(String json) throws Exception;
    }

    public void doPost(String path, Map<String, String> map, final OkHttpCallBackListener callBackListener) {
        if (map == null){
            map = new HashMap<>();
        }
        map.put("time",String.valueOf(System.currentTimeMillis() / 1000));
        Map<String, String> wrapper = new HashMap<>(map);
        String sign = encryptParams(map);
        wrapper.put("sign",sign);
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : wrapper.keySet()) {
            String s = wrapper.get(key);
            if (!TextUtils.isEmpty(s)) {
                builder.add(key, s);
            }
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(path)
                .build();

        Call call = mClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailure(e, callBackListener);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                    sendSuccess(json, callBackListener);
                }else {
                    onFailure(call,new IOException("其他错误"));
                }
            }
        });
    }


    private void sendSuccess(String data, OkHttpCallBackListener callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callBack.success(data);
                } catch (Exception e) {
                    callBack.failure(e);
                    e.printStackTrace();
                }
            }
        });
    }


    private void sendFailure(Exception e, OkHttpCallBackListener callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.failure(e);
            }
        });
    }


    public  static  String encryptParams(Map<String, String> params){
        // 3、将参数和公共参数合并到一个Map
        Map<String, String> signParams = new HashMap<>(params);
        signParams.put("pass", "test123123");

        // 4、根据key值排序参数
        List<String> keyList = new ArrayList<>(signParams.keySet());
        Collections.sort(keyList);

        // 5、拼接成key1value1&key2value2...(value值为空或者空字符串时不参与拼接)
        StringBuilder aSign = new StringBuilder();
        for (String key : keyList) {
            Object value = signParams.get(key);
            if(value != null) {
                String valueStr = String.valueOf(value);
                if (!TextUtils.isEmpty(valueStr)) {
                    aSign.append("&").append(key).append("=").append(valueStr);
                }
            }
        }
        String paramsStr = aSign.toString().replaceFirst("&", "");
        ZB8LogUtils.d("paramsStr:"+paramsStr);
        return MD5Utils.getMD5(paramsStr);
    }
}
