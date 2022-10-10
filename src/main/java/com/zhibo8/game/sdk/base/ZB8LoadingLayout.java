package com.zhibo8.game.sdk.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author : ZhangWeiBo
 * date : 2022/10/08
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8LoadingLayout extends FrameLayout {

    private ZB8LoadingView mLoadingView;
    private View mContentView;
    private ZB8LoadingView.OnRetryClickListener onRetryClickListener;

    public ZB8LoadingLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ZB8LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZB8LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public ZB8LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
    }


    public void showLoading() {
        mLoadingView.setVisibility(VISIBLE);
        mLoadingView.showLoading();
        mContentView.setVisibility(GONE);
    }


    public void showError() {
        mLoadingView.setVisibility(VISIBLE);
        mLoadingView.showError();
        mContentView.setVisibility(GONE);
    }


    public void showContent() {
        mLoadingView.setVisibility(GONE);
        mContentView.setVisibility(VISIBLE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mLoadingView = new ZB8LoadingView(getContext());
        setOnRetryClickListener(onRetryClickListener);
        addView(mLoadingView);
        showLoading();
    }


    public void setOnRetryClickListener(ZB8LoadingView.OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
        if (mLoadingView != null) {
            mLoadingView.setOnRetryClickListener(onRetryClickListener);
        }
    }
}
