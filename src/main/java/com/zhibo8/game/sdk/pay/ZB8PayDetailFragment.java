package com.zhibo8.game.sdk.pay;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhibo8.game.sdk.R;
import com.zhibo8.game.sdk.ZB8CodeInfo;
import com.zhibo8.game.sdk.ZB8Constant;
import com.zhibo8.game.sdk.ZB8Game;
import com.zhibo8.game.sdk.ZB8RequestCallBack;
import com.zhibo8.game.sdk.base.BaseDialogFragment;
import com.zhibo8.game.sdk.base.ZB8LoadingLayout;
import com.zhibo8.game.sdk.base.ZB8LoadingView;
import com.zhibo8.game.sdk.bean.ZBOrderInfo;
import com.zhibo8.game.sdk.login.ZB8LoginManager;
import com.zhibo8.game.sdk.net.ZB8OkHttpUtils;
import com.zhibo8.game.sdk.pay.wechat.WXPayManager;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/30
 * email : 1908561871@qq.com
 * description : 支付界面
 */
public class ZB8PayDetailFragment extends BaseDialogFragment implements ZB8LoadingView.OnRetryClickListener {

    private RecyclerView mRvGoodsInfo;
    private TextView mTvPay;
    private ZBOrderInfo orderInfo;
    private ZB8PayModeAdapter mPayModeAdapter;
    private RecyclerView mRvPayMode;
    private ImageView mIvClose;

    private ZB8RequestCallBack callBack;
    private ZB8LoadingLayout mLoadingView;
    private JSONObject mPay;
    private TextView mTvTitle;


    public static ZB8PayDetailFragment getInstance(ZBOrderInfo orderInfo){
        ZB8PayDetailFragment fragment = new ZB8PayDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("order",orderInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData(Bundle arguments) {
        orderInfo = getArguments().getParcelable("order");
        WXPayManager.getInstance().setOnWXPayCallBackListener(onWXPayCallBackListener);
        getOrder();
    }

    @Override
    protected void initView(View view) {
        mTvPay = view.findViewById(R.id.tv_pay);
        mRvGoodsInfo = view.findViewById(R.id.rv_goods_info);
        mRvPayMode = view.findViewById(R.id.rv_pay_mode);
        mIvClose = view.findViewById(R.id.iv_close);
        mLoadingView = view.findViewById(R.id.loading);
        mTvTitle = view.findViewById(R.id.tv_title);
        mRvGoodsInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTvPay.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mLoadingView.setOnRetryClickListener(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.zb8_dialog_pay_detail;
    }


    @Override
    public void onClick(View v) {
        if (v == mTvPay) {
            if (mPayModeAdapter != null && mPay != null) {
                clickPay();
            }
        } else if (v == mIvClose) {
            getActivity().finish();
        }
    }


    WXPayManager.OnWXPayCallBackListener onWXPayCallBackListener = new WXPayManager.OnWXPayCallBackListener() {
        @Override
        public void onSuccess() {
            callBack.onSuccess(new JSONObject());
        }

        @Override
        public void onFailure() {
            callBack.onFailure(ZB8CodeInfo.CODE_PAY_FAILURE, ZB8CodeInfo.MSG_PAY_FAILURE);
        }

        @Override
        public void onCancel() {
            callBack.onCancel();
        }
    };


    private void clickPay() {
        JSONObject style = mPay.optJSONObject("style");
        Map<String, String> map = new HashMap<>();
        map.put("os", "android");
        if (style != null) {
            map.put("pay_type", style.optString("type"));
        }
        map.put("price", mPay.optString("price"));
        map.put("order_no", mPay.optString("order_no"));
        map.put("access_token", ZB8LoginManager.getInstance().getToken());
        ZB8OkHttpUtils.getInstance().doPost(ZB8Constant.BASE_URL + "/sdk/m_game/pay", map, new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) {
                ZB8LogUtils.d("点击支付失败：" + e.getMessage());
                callBack.onFailure(ZB8CodeInfo.CODE_PAY_FAILURE, ZB8CodeInfo.MSG_PAY_FAILURE);
            }

            @Override
            public void success(String json) throws Exception {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject data = jsonObject.optJSONObject("data");
                if (data != null && TextUtils.equals(jsonObject.optString("status"), "success")) {
                    String payType = data.optString("pay_type");
                    String payContent = data.optString("pay_content");
                    if (TextUtils.equals("alipay", payType)) {
                        toAliPay(payContent);
                    } else if (TextUtils.equals("wechat", payType)) {
                        toWechatPay(payContent);
                    }
                }

            }
        });
    }


    private void getOrder() {
        mLoadingView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", orderInfo.getOrder());
        map.put("price", orderInfo.getPrice());
        map.put("os", "android");
        map.put("app_id", ZB8Game.getConfig().getAppId());
        map.put("access_token", ZB8LoginManager.getInstance().getToken());
        ZB8OkHttpUtils.getInstance().doPost(ZB8Constant.BASE_URL + "/sdk/m_game/order", map, new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) {
                mLoadingView.showError();
                ZB8LogUtils.d("获取商品订单失败：" + e.getMessage());
                callBack.onFailure(ZB8CodeInfo.CODE_GET_GOODS_FAILURE, ZB8CodeInfo.MSG_GET_GOODS_FAILURE);
            }

            @Override
            public void success(String json) throws Exception {
                JSONObject jsonObject = new JSONObject(json);
                if (TextUtils.equals(jsonObject.optString("status"), "success")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data != null) {
                        JSONArray info_list = data.optJSONArray("info_list");
                        mRvGoodsInfo.setAdapter(new ZB8PayGoodsAdapter(info_list));
                        mTvTitle.setText(data.optString("title"));
                        mPay = data.optJSONObject("pay");
                        if (mPay != null) {
                            JSONArray style = mPay.optJSONArray("style");
                            if (style != null) {
                                mRvPayMode.setLayoutManager(new GridLayoutManager(getActivity(), style.length()));
                                mRvPayMode.setAdapter(mPayModeAdapter = new ZB8PayModeAdapter(style));
                            }
                        }
                        mLoadingView.showContent();
                        return;
                    }
                }
                mLoadingView.showError();
            }
        });
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<String, String> result = (Map<String, String>) msg.obj;
            if (result == null) {
                return;
            }
            String resultStatus = result.get("resultStatus");
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                callBack.onSuccess(new JSONObject());
            } else if (TextUtils.equals(resultStatus, "6001")) {
                callBack.onCancel();
            } else {
                callBack.onFailure(ZB8CodeInfo.CODE_PAY_FAILURE, ZB8CodeInfo.MSG_PAY_FAILURE);
            }
            ZB8LogUtils.d("支付宝支付回调： " + " code = " + resultStatus);
        }
    };

    private void toAliPay(String alipay_order_str) {
        ZB8LogUtils.d("开始支付宝支付");
        final String orderInfo = alipay_order_str;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private void toWechatPay(String content) {

        ZB8LogUtils.d("开始微信支付");
        try {
            JSONObject jsonObject = new JSONObject(content);
            IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), jsonObject.optString("appId"), false);
            PayReq payReq = new PayReq();
            payReq.appId = jsonObject.optString("appId");
            payReq.partnerId = jsonObject.optString("partnerId");
            payReq.prepayId = jsonObject.optString("prepayId");
            payReq.nonceStr = jsonObject.optString("nonceStr");
            payReq.timeStamp = jsonObject.optString("timeStamp");
            payReq.packageValue = jsonObject.optString("packageValue");
            payReq.sign = jsonObject.optString("sign");
            payReq.extData = jsonObject.optString("extData");
            payReq.signType = jsonObject.optString("signType");
            boolean sendResult = api.sendReq(payReq);
            ZB8LogUtils.d("微信调起" + (sendResult ? "成功" : "失败"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setCallBack(ZB8RequestCallBack callBack) {
        this.callBack = callBack;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WXPayManager.getInstance().setOnWXPayCallBackListener(null);
    }

    @Override
    public void onRetry() {
        getOrder();
    }
}
