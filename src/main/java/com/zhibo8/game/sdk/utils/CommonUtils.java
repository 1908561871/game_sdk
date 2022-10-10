package com.zhibo8.game.sdk.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author : ZhangWeiBo
 * date : 2022/10/08
 * email : 1908561871@qq.com
 * description : TODO
 */
public class CommonUtils {

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id="+packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
