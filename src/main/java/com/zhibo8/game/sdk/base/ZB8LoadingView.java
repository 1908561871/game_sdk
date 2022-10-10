package com.zhibo8.game.sdk.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhibo8.game.sdk.R;

/**
 * @author : ZhangWeiBo
 * date : 2022/10/08
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8LoadingView extends FrameLayout {

    private View mLoading;
    private View mError;
    private OnRetryClickListener onRetryClickListener;

    public ZB8LoadingView(@NonNull Context context) {
        super(context);
        init();
    }

    public ZB8LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZB8LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public ZB8LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(),R.layout.zb8_loading_view,this);
        mLoading = findViewById(R.id.loading);
        mError = findViewById(R.id.error);
        mError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRetryClickListener != null){
                    onRetryClickListener.onRetry();
                }
            }
        });
        showLoading();
    }


    public void showLoading() {
        mLoading.setVisibility(VISIBLE);
        mError.setVisibility(GONE);
    }


    public void showError() {
        mError.setVisibility(VISIBLE);
        mLoading.setVisibility(GONE);
    }


    public interface  OnRetryClickListener{
        void onRetry();
    }

    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
    }



}
