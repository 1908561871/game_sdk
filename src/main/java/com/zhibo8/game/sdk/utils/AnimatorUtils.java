package com.zhibo8.game.sdk.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

/**
 * author : LvJianCong
 * date : 2021/2/4
 * email : 974852273@qq.com
 * description : 动画效果工具类
 */
public class AnimatorUtils {

    /**
     * 左右抖动动画提示
     * @param view 要执行动画的控件
     * @param delta 左右抖动距离
     * @param duration 动画时间
     * @return ObjectAnimator实例
     */
    public static ObjectAnimator nope(View view, int delta, int duration) {
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(0.66f, delta),
                Keyframe.ofFloat(1f, 0f)
        );
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).setDuration(duration);
    }
}
