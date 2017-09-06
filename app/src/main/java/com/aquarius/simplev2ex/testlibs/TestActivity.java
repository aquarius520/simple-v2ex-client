package com.aquarius.simplev2ex.testlibs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.adapter.SearchNodeAdapter;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.util.GlideUtil;
import com.aquarius.simplev2ex.views.thirdparty.BottomTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by aquarius on 2017/8/3.
 */
public class TestActivity extends Activity {

    private ViewGroup parent;
    //private CircleImageView circleImageView;
    private ImageView imageView;

    private ListView listview;


    private BottomTabLayout bottomTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View empty = View.inflate(this, R.layout.empty_search_node_list, null);

        setContentView(R.layout.test_layout);

        listview = (ListView) findViewById(R.id.listview);

        ((ViewGroup)listview.getParent()).addView(empty);
        listview.setEmptyView(empty);

        List<Node> nodes = new ArrayList<>();

//        nodes.add(new Node.Builder("111", "").build());
//        nodes.add(new Node.Builder("333", "").build());
//        nodes.add(new Node.Builder("111", "").build());
//        nodes.add(new Node.Builder("222", "").build());
//        nodes.add(new Node.Builder("111", "").build());
//        nodes.add(new Node.Builder("444", "").build());

        SearchNodeAdapter adapter = new SearchNodeAdapter(this, nodes);
        listview.setAdapter(adapter);

        //circleImageView = (CircleImageView) findViewById(R.id.avatar);

        //imageView = (ImageView) findViewById(R.id.imageview);
//        GlideUtil.showNetworkImage(this,
//                "https://v2ex.assets.uxengine.net/avatar/a9a5/69e4/139381_normal.png?m=1460618190", circleImageView);

        /*parent = (LinearLayout) findViewById(R.id.container);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubView();
            }
        });*/

        //Setup button tab layout
        bottomTabLayout = (BottomTabLayout) findViewById(R.id.bottomTabLayout);
        //set button text style
        bottomTabLayout.setButtonTextStyle(R.style.TextWhite12);
        // set buttons from menu resource
        bottomTabLayout.setItems(R.menu.menu_bottom_layout);
        //set on selected tab listener.
        bottomTabLayout.setListener(new BottomTabLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                //switchFragment(id);
            }
        });
        //set button that will be select on start activity
        bottomTabLayout.setSelectedTab(R.id.hot_menu_button);
        //enable indicator
        bottomTabLayout.setIndicatorVisible(true);
        //indicator height
        bottomTabLayout.setIndicatorHeight(getResources().getDimension(R.dimen.indicator_height));
        //indicator color
        bottomTabLayout.setIndicatorColor(R.color.green);
        //indicator line color
        bottomTabLayout.setIndicatorLineColor(R.color.dark);
        //bottomTabLayout.setSelectedTab(R.id.menu_button5);

        //setup bubble style
//        bottomTabLayout.setTabBubbleColor(ContextCompat.getColor(this, R.color.blue));
//        bottomTabLayout.setTabBubblePadding(0, 0, 0, 0);
//        bottomTabLayout.setTabBubbleTextStyle(R.style.TextWhite12);
//
//        //show bubble
//        bottomTabLayout.showTabBubbleCount(R.id.menu_button1, 3);
    }


    // 添加子view到父控件中，按照默认排列方向添加
    private void addSubView() {
        TextView view = new TextView(this);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        view.setPadding(40, 10, 40, 10);
        view.setText("Android Android!!");
        view.setTextSize(24);
        view.setGravity(Gravity.CENTER);
        view.setSingleLine();
        parent.addView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
