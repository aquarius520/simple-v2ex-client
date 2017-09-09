package com.aquarius.simplev2ex.views;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aquarius.simplev2ex.config.AppConfig;
import com.aquarius.simplev2ex.support.URLImageGetter;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/8/16.
 */
public class RichTextView extends TextView implements View.OnAttachStateChangeListener {

    private URLImageGetter mURLImageGetter;

    private OnRichTextImageClickListener onRichTextImageClickListener;//图片点击回调
//    private OnRichTextLinkClickListener onRichTextLinkClickListener; // 链接点击回调

    public RichTextView(Context context) {
        super(context);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRichText(String text) {
//        text = text.replace("href=\"/member/", "href=\"http://www.v2ex.com/member/")
//                .replace("href=\"/t/", "href=\"http://www.v2ex.com/t/")
//                .replace("href=\"/i/", "href=\"https://i.v2ex.co/")
//                .replace("href=\"/go/", "href=\"http://www.v2ex.com/go/");
        mURLImageGetter = new URLImageGetter(getContext(), this);
        Spanned spanned = Html.fromHtml(text, mURLImageGetter, null);
        SpannableStringBuilder htmlSsb;
        if (spanned instanceof SpannableStringBuilder) {
            htmlSsb = (SpannableStringBuilder) spanned;
        }else {
            htmlSsb = new SpannableStringBuilder(spanned);
        }

        // 如果是移动网络 且打开了移动网络下不加载图片的开关
//        if (NetWorkUtil.isFastMobileNetwork(getContext()) && AppConfig.isDownloadImageInMobileNetwork())  {
//
//        } else {
            // 否则显示图片 并处理图片点击事件
//            ImageSpan[] imageSpans = htmlSsb.getSpans(0, htmlSsb.length(), ImageSpan.class);
//            final ArrayList<String> imgUrls = new ArrayList<>();
//            for (int i= 0 ; i < imageSpans.length; i++) {
//                ImageSpan span = imageSpans[i];
//                String imgUrl = span.getSource();
//                int start = htmlSsb.getSpanStart(span);
//                int end = htmlSsb.getSpanEnd(span);
//                imgUrls.add(imgUrl);
//                final int position = i;
//                ClickableSpan clickableSpan = new ClickableSpan() {
//                    @Override
//                    public void onClick(View widget) {
//                        if( onRichTextImageClickListener != null){
//                            onRichTextImageClickListener.imageClicked(imgUrls, position);
//                        }
//                    }
//                };
//
//                ClickableSpan[] clickableSpans = htmlSsb.getSpans(start, end, ClickableSpan.class);
//                if (clickableSpans != null && clickableSpans.length != 0) {
//                    for (ClickableSpan cs : clickableSpans) {
//                        htmlSsb.removeSpan(cs);
//                    }
//                }
//                htmlSsb.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }

        // TODO: 处理文字中的链接点击事件
        URLSpan[] urlSpans = htmlSsb.getSpans(0, htmlSsb.length(), URLSpan.class);
        for(int i = 0; i < urlSpans.length; i++) {
            final URLSpan span = urlSpans[i];
            final String url = span.getURL();
            int start = htmlSsb.getSpanStart(span);
            int end = htmlSsb.getSpanEnd(span);
            String name = url.substring(url.lastIndexOf("/") + 1);
            Log.d("aquarius", "url=" + url+", start=" + start + ", end=" +end + ",name=" + name);
            // TODO: 设置的clickablespan并没有被执行
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Log.d("aquarius", "onClick()");
                    if(url.startsWith("href=\"/member/") || url.startsWith("href=\"http://www.v2ex.com/member/")) {
                        Context context = widget.getContext();
                        Intent intent = new Intent();
                        intent.setAction(Constants.ACTION_USER_HOMEPAGE);
                        intent.setPackage(context.getPackageName());
                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
                        }
                    }else {
                        Log.d("aquarius", "use the brower open link!");
                        span.onClick(widget);
                    }
                }
            };

            htmlSsb.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.setText(htmlSsb);
        setMovementMethod(LinkMovementMethod.getInstance());
    }


    public void setOnRichTextImageClickListener(OnRichTextImageClickListener onRichTextImageClickListener) {
        this.onRichTextImageClickListener = onRichTextImageClickListener;
    }

    public interface OnRichTextImageClickListener{
        public abstract void imageClicked(List<String> imgUrls, int position);
    }

//    public void setOnRichTextLinkClickListener(OnRichTextLinkClickListener onRichTextLinkClickListener) {
//        this.onRichTextLinkClickListener = onRichTextLinkClickListener;
//    }
//
//    public interface OnRichTextLinkClickListener{
//        void textLinkClicked(List<String> linkUrls, int position);
//    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        mURLImageGetter.recycle();
    }

}
