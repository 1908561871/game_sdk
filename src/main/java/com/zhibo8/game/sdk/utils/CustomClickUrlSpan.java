package com.zhibo8.game.sdk.utils;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class CustomClickUrlSpan extends ClickableSpan {
    private String url;
    private OnLinkClickListener mListener;

    public CustomClickUrlSpan(String url, OnLinkClickListener listener) {
        this.url = url;
        this.mListener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null) {
            mListener.onLinkClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (ds != null) {
            ds.setUnderlineText(false);
            ds.setColor(Color.parseColor("#2e9fff"));
        }
    }

    /**
     * 跳转链接接口
     */
    public interface OnLinkClickListener {
        void onLinkClick(View view);
    }
}