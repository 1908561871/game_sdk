package com.zhibo8.game.sdk.base;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.zhibo8.game.sdk.R;

public abstract class BaseDialog extends Dialog implements View.OnClickListener {

    private Activity activity;

    public BaseDialog(Activity activity) {
        this(activity, false, R.style.base_dialog);
    }


    public BaseDialog(Activity activity, boolean canHide, int style) {
        super(activity, style);
        this.activity = activity;
        setContentView(getContentViewId());
        setCanceledOnTouchOutside(canHide);
        setCancelable(canHide);
    }

    protected abstract int getContentViewId();


    public Activity getActivity() {
        return activity;
    }

}
