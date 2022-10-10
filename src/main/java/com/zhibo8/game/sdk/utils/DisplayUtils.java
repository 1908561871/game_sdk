package com.zhibo8.game.sdk.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * author : ZhangWeiBo
 * date : 2021年02月24日 9:03
 * email : 1908561871@qq.com
 * description : TODO
 */
public class DisplayUtils {
    public static int dipToPix(Context context, int dip) {
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return size;
    }

}
