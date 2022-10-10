package com.zhibo8.game.sdk.utils;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/30
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8HtmlUtils {

    public static void setHtml(String htmlLinkText, TextView textView){
        textView.setText(Html.fromHtml(htmlLinkText));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = textView.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) textView.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (final URLSpan url : urls) {
                //最主要的一点
                CustomClickUrlSpan myURLSpan = new CustomClickUrlSpan(url.getURL(), new CustomClickUrlSpan.OnLinkClickListener() {
                    @Override
                    public void onLinkClick(View view) {
                        Uri uri = Uri.parse(url.getURL());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        textView.getContext().startActivity(intent);
                    }
                });
                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.setText(style);
        }
    }
}
