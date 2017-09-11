package com.aquarius.simplev2ex.support;

import com.aquarius.simplev2ex.entity.TopicItem;

import java.util.Comparator;

/**
 * Created by aquarius on 2017/9/11.
 */
public class ListItemComparator implements Comparator<TopicItem> {

    @Override
    public int compare(TopicItem o1, TopicItem o2) {

        if (o1 == null || o2 == null) {
            return 0;
        }
        if (o1.getCreated() == 0 && o2.getCreated() == 0) {
            return 0;
        }

        if (o1.getCreated() > o2.getCreated()) {
            return -1;
        }

        return 1;
    }
}
