package com.zhibo8.game.sdk.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author : ZhangWeiBo
 * date : 2022/10/09
 * email : 1908561871@qq.com
 * description : TODO
 */
public abstract class BaseDialogFragment extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            // 透明背景
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // 铺满全屏
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
           // lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
            getDialog().requestWindowFeature(STYLE_NO_TITLE);
            getDialog().setCancelable(false);
        }
        return inflater.inflate(getContentViewId(), container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData(getArguments());
    }

    protected abstract void initData(Bundle arguments);


    protected abstract void initView(View view);

    protected abstract int getContentViewId();


}
