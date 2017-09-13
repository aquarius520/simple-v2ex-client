package com.aquarius.simplev2ex.util;

import android.content.Context;
import android.widget.ImageView;

import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.V2exApplication;
import com.aquarius.simplev2ex.support.GlideRoundTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by aquarius on 2017/8/9.
 */
public class GlideUtil {

    private static final GlideRoundTransform ROUND_TRANSFORM = new GlideRoundTransform(V2exApplication.getInstance());

    // if use CircleImageView need call dontAnimate()
    public static void showNetworkImage(Context context, String imgUrl, ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .transform(ROUND_TRANSFORM)
                .placeholder(R.drawable.ic_avatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .dontAnimate()
                .into(imageView);
    }


    public static void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }
}
