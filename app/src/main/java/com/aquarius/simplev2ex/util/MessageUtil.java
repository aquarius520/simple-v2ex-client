package com.aquarius.simplev2ex.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

/**
 * Created by aquarius on 2017/9/6.
 */
public class MessageUtil {

    private static final String TAG = "MessageUtil";

    public static void showNetworkErrorMsg(Context context, String error, String btnLabel) {
        showMessageBar(context, error, btnLabel);
    }

    public static void showMessageBar(Context context, String text, String label) {
        SnackbarManager.show(
                Snackbar.with(context) // context
                        .text(text) // text to display
                        .type(SnackbarType.MULTI_LINE)
                        .actionLabel(label) // action button label
                        .actionColor(Color.RED)
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Log.d(TAG, "action clicked !");
                            }
                        }) // action button's ActionClickListener
                , (Activity) context); // activity where it is displayed
    }
}
