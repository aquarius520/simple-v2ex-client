package com.aquarius.simplev2ex.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aquarius.simplev2ex.R;

/**
 * Created by aquarius on 2017/8/5.
 */
public class TitleTopBar extends RelativeLayout {

    private ImageView back;
    private TextView title;
    private TextView action;

    public TitleTopBar(Context context) {
        this(context, null, 0);
    }

    public TitleTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.title_top_bar_layout, this, true);
        back = (ImageView) view.findViewById(R.id.back);
        title = (TextView) view.findViewById(R.id.title);
        action = (TextView) view.findViewById(R.id.action);
    }

    public void setTitleText(String text) {
        if (title != null) {
            title.setText(text);
        }
    }

    public void setActionText(String text) {
        if (action != null) {
            action.setText(text);
        }
    }

    public void setBackImageView(int resId) {
        if (back != null) {
            back.setImageResource(resId);
        }
    }

    public void setBackVisibility(boolean visible) {
        if (back != null) {
            back.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void setActionBtnVisibility(boolean visible) {
        if (action != null) {
            action.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void setBackButtonOnClickListener(OnClickListener listener) {
        if (back != null) {
            back.setOnClickListener(listener);
        }
    }

    public void setActionBtnOnClickListener(OnClickListener listener) {
        if (action != null) {
            action.setOnClickListener(listener);
        }
    }
}
