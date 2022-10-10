package com.zhibo8.game.sdk.verify;

import static com.zhibo8.game.sdk.ZB8Constant.BASE_URL;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhibo8.game.sdk.R;
import com.zhibo8.game.sdk.ZB8CodeInfo;
import com.zhibo8.game.sdk.ZB8Game;
import com.zhibo8.game.sdk.ZB8RequestCallBack;
import com.zhibo8.game.sdk.base.BaseDialog;
import com.zhibo8.game.sdk.base.BaseDialogFragment;
import com.zhibo8.game.sdk.base.ZB8LoadingLayout;
import com.zhibo8.game.sdk.base.ZB8LoadingView;
import com.zhibo8.game.sdk.bean.ZBOrderInfo;
import com.zhibo8.game.sdk.net.ZB8OkHttpUtils;
import com.zhibo8.game.sdk.pay.ZB8PayDetailFragment;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/30
 * email : 1908561871@qq.com
 * description : 用户认证
 */
public class ZB8UserVerifyFragment extends BaseDialogFragment implements TextWatcher, ZB8LoadingView.OnRetryClickListener {

    private String mToken;
    private EditText mEtUserIdentify;
    private EditText mEtUserName;
    private TextView mTvSubmit;

    private ZB8RequestCallBack callBack;
    private ImageView mIvClose;
    private ZB8LoadingLayout mLoadingView;
    private JSONObject mJsonEntity;

    public static ZB8UserVerifyFragment getInstance(JSONObject jsonObject){
        ZB8UserVerifyFragment fragment = new ZB8UserVerifyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("auth_info",jsonObject.toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData(Bundle arguments) {
        String auth_info = arguments.getString("auth_info");
        try {
            this.mJsonEntity = new JSONObject(auth_info);
            this.mToken = mJsonEntity.optString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        checkUserVerify();
    }

    @Override
    protected void initView(View view) {
        mEtUserIdentify = view.findViewById(R.id.et_user_identify);
        mEtUserName = view.findViewById(R.id.et_user_name);
        mTvSubmit = view.findViewById(R.id.tv_submit);
        mIvClose = view.findViewById(R.id.iv_close);
        mEtUserName.addTextChangedListener(this);
        mEtUserIdentify.addTextChangedListener(this);
        mLoadingView = view.findViewById(R.id.loading);
        mTvSubmit.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mLoadingView.setOnRetryClickListener(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.zb8_dialog_user_verify;
    }


    @Override
    public void onClick(View v) {
        if (v == mTvSubmit) {
            submitInfo(mEtUserIdentify.getText().toString(), mEtUserName.getText().toString(), mToken);
        } else if (v == mIvClose) {
            getActivity().finish();
        }
    }

    /**
     * 检查用户是否认证
     */
    private void checkUserVerify() {
        mLoadingView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("access_token", mToken);
        map.put("token", mToken);
        map.put("appid", ZB8Game.getConfig().getAppId());
        ZB8OkHttpUtils.getInstance().doPost(BASE_URL + "/sdk/m_game/isAuth", map, new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) {
                mLoadingView.showError();
                callBack.onFailure(ZB8CodeInfo.CODE_VERIFY_FAILURE, ZB8CodeInfo.MSG_VERIFY_FAILURE);
            }

            @Override
            public void success(String json) throws Exception {
                JSONObject jsonObject = new JSONObject(json);
                if (TextUtils.equals(jsonObject.optString("status"), "success")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data != null) {
                        int is_auth = data.optInt("is_auth");
                        int is_adulth = data.optInt("is_adulth");
                        if (is_auth == 1) {
                            if (is_adulth == 1) {
                                ZB8LogUtils.d("用户认证成功，且用户已成年");
                                callBack.onSuccess(mJsonEntity);
                            } else {
                                //未成年
                                ZB8LogUtils.d("用户认证成功，用户未成年");
                                callBack.onFailure(ZB8CodeInfo.CODE_TEENAGER_PROTECT,ZB8CodeInfo.MSG_CODE_TEENAGER_PROTECT);
                                Toast.makeText(getActivity(),"未成年暂时无法体验游戏,拦截用户进入游戏",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mLoadingView.showContent();
                        }
                    }
                } else {
                    mLoadingView.showError();
                    callBack.onFailure(ZB8CodeInfo.CODE_VERIFY_FAILURE, ZB8CodeInfo.MSG_VERIFY_FAILURE);
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTvSubmit.setEnabled(!TextUtils.isEmpty(mEtUserIdentify.getText()) && !TextUtils.isEmpty(mEtUserName.getText()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private void submitInfo(String identify, String userName, String access_token) {
        mLoadingView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("access_token", access_token);
        map.put("identify", identify);
        map.put("real_name", userName);
        map.put("appid", ZB8Game.getConfig().getAppId());
        ZB8OkHttpUtils.getInstance().doPost(BASE_URL + "/sdk/m_game/auth", map, new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) {
                mLoadingView.showContent();
                callBack.onFailure(ZB8CodeInfo.CODE_VERIFY_FAILURE, ZB8CodeInfo.MSG_VERIFY_FAILURE);
            }

            @Override
            public void success(String json) throws Exception{
                JSONObject jsonObject = new JSONObject(json);
                if (TextUtils.equals(jsonObject.optString("status"), "success")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    int is_adulth = data.optInt("is_adulth");
                    if (is_adulth == 1) {
                        ZB8LogUtils.d("用户认证成功，且用户已成年");
                        callBack.onSuccess(mJsonEntity);
                    } else {
                        //未成年
                        ZB8LogUtils.d("用户认证成功，用户未成年");
                        callBack.onFailure(ZB8CodeInfo.CODE_TEENAGER_PROTECT,ZB8CodeInfo.MSG_CODE_TEENAGER_PROTECT);
                        Toast.makeText(getActivity(),"未成年暂时无法体验游戏,拦截用户进入游戏",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    String msg = jsonObject.optString("msg");
                    if (!TextUtils.isEmpty(msg)){
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                    }
                    mLoadingView.showContent();
                    callBack.onFailure(ZB8CodeInfo.CODE_VERIFY_FAILURE, ZB8CodeInfo.MSG_VERIFY_FAILURE);
                }
            }
        });
    }


    public void setCallBack(ZB8RequestCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onRetry() {
        checkUserVerify();
    }
}
