package com.aquarius.simplev2ex.support;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

/**
 * Created by aquarius on 2017/8/12.
 */
public class ItemAnimationUtil {
    public static LayoutAnimationController getLac(Context context, int animResId, float delay) {
        Animation animation = AnimationUtils.loadAnimation(context, animResId);
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lac.setDelay(delay);
        return lac;
    }
}
