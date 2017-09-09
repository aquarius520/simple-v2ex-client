package com.aquarius.simplev2ex.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.config.AppConfig;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.aquarius.simplev2ex.util.ScreenUtil;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

/**
 * Created by aquarius on 2017/8/16.
 *
 * see : https://github.com/jesson1989/AndroidRichText
 */
public class URLImageGetter implements Html.ImageGetter {

    private HashSet<Target> targets;
    private HashSet<GifDrawable> gifDrawables;
    private TextView mContainer;
    private Context mContext;
    private int maxWidth;

    public URLImageGetter(Context context, TextView container) {
        this.mContainer = container;
        this.mContext = context;
        targets = new HashSet<>();
        gifDrawables = new HashSet<>();
        maxWidth = ScreenUtil.getScreenWidth(context) - ScreenUtil.dp2px(context, 60);
    }

    public void recycle() {
        targets.clear();
        for (GifDrawable drawable : gifDrawables) {
            drawable.setCallback(null);
            drawable.recycle();
        }
        gifDrawables.clear();
    }

    @Override
    public Drawable getDrawable(String source) {
//        URLDrawable urlDrawable = new URLDrawable();
//        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
//        asyncTask.execute(source);
        final URLDrawable urlDrawable = new URLDrawable();
        if (NetWorkUtil.isFastMobileNetwork(mContext) && !AppConfig.isDownloadImageInMobileNetwork()){
            return urlDrawable;
        }

        final GenericRequestBuilder load;
        final Target target;
//        if(isGif(url)){
//            load = Glide.with(mContext).load(url).asGif();
//            target = new GifTarget(urlDrawable);
//        }else {
            load = Glide.with(mContext).load(source).asBitmap();
            target = new BitmapTarget(urlDrawable);
//        }
        targets.add(target);
        load.into(target);
        return urlDrawable;
    }

    /*class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable>{

        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            // set the correct bound according to the result from HTTP call
            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0
                    + result.getIntrinsicHeight());

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            URLImageGetter.this.container.invalidate();
        }

        private Drawable fetchDrawable(String url) {
            Drawable drawable = null;
            try {
                URL source = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)source.openConnection();
                InputStream in = conn.getInputStream();
                drawable =  Drawable.createFromStream(in, "src");
                drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0
                        + drawable.getIntrinsicHeight());
                //drawable = new BitmapDrawable(BitmapFactory.decodeStream(conn.getInputStream()));
                return drawable;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }*/

    /* @Override
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable();
            Glide.with(context).load(source).error(R.drawable.ic_avatar).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                               boolean isFromMemoryCache, boolean isFirstResource) {
                    if (resource != null) {
                        int width = 0;
                        int height = 0;
                        if (resource.getIntrinsicWidth() > maxWidth) {
                            width = maxWidth;
                            height = maxWidth * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
                        } else{
                            width = resource.getIntrinsicWidth();
                            height = resource.getIntrinsicHeight();
                        }
                        Drawable drawable = resource;
                        drawable.setBounds(0, 0, width, height);
                        urlDrawable.setBounds(0, 0, width, height);
                        urlDrawable.mDrawable = drawable;
                        container.setText(container.getText());
                    }
                    return true;
                }
            });
            return urlDrawable;

        }
    */
    static class URLDrawable extends BitmapDrawable {

        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    class BitmapTarget extends SimpleTarget<Bitmap> {

        URLDrawable urlDrawable ;

        public BitmapTarget(URLDrawable urlDrawable){
            this.urlDrawable = urlDrawable;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            Drawable drawable = new BitmapDrawable(mContext.getResources(), resource);
            int h =drawable.getIntrinsicHeight();
            int w =drawable.getIntrinsicWidth();
            if (w > maxWidth) {
                h = maxWidth * h / w;
                w = maxWidth;
            }
            Rect rect = new Rect(0, 4, w, h);
            drawable.setBounds(rect);
            urlDrawable.setBounds(rect);
            urlDrawable.setDrawable(drawable);
            mContainer.setText(mContainer.getText());
            mContainer.invalidate();
        }
    }

    private class GifTarget extends SimpleTarget<GifDrawable> {
        private final URLDrawable urlDrawable;

        private  GifTarget(URLDrawable urlDrawable) {
            this.urlDrawable = urlDrawable;
        }
        @Override
        public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
            int h =resource.getIntrinsicHeight();
            int w =resource.getIntrinsicWidth();
            if (w > maxWidth) {
                h = maxWidth * h / w;
                w = maxWidth;
            }
            Rect rect = new Rect(0, 4, w, h);
            resource.setBounds(rect);
            urlDrawable.setBounds(rect);
            urlDrawable.setDrawable(resource);
            gifDrawables.add(resource);
            resource.setCallback(mContainer);
            resource.start();
            resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
            mContainer.setText(mContainer.getText());
            mContainer.invalidate();
        }
    }

}
