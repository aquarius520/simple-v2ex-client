package com.aquarius.simplev2ex.util;

import android.content.Context;
import android.text.format.DateUtils;

import com.aquarius.simplev2ex.R;

/**
 * Created by aquarius on 2017/8/9.
 */
public class TimeUtil {

    public static CharSequence topicCreatedTime(Context context, long createdTime) {
        CharSequence text = null;
        if(createdTime > 0) {
            long created = createdTime * 1000;
            long now = System.currentTimeMillis();
            long interval = now - created;
            text = (interval >= 0 && interval <= DateUtils.MINUTE_IN_MILLIS)
                    ? context.getString(R.string.just_now) :
                    DateUtils.getRelativeTimeSpanString(
                            created,
                            now,
                            DateUtils.MINUTE_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_RELATIVE);
        }
        return text;
    }

    public static CharSequence replyCreatedTime(Context context, long createtTime) {
        return topicCreatedTime(context, createtTime);
    }

}
