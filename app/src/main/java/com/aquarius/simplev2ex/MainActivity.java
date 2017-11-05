package com.aquarius.simplev2ex;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.aquarius.simplev2ex.support.StatusBarLightHelper;
import com.aquarius.simplev2ex.views.TitleTopBar;
import com.aquarius.simplev2ex.views.thirdparty.BottomTabLayout;

public class MainActivity extends Activity {

    private TitleTopBar titleTopBar;
    private BottomTabLayout bottomTabLayout;


    private NewestFragment newestFragment;
    private HotTopicFragment hotTopicFragment;
    private DiscoverFragment discoverFragment;
    private AllNodesFragment allNodesFragment;
    private OwnerFragment ownerFragment;

    private Fragment previousFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        StatusBarLightHelper.setStatusBarLightMode(this, true);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        titleTopBar = (TitleTopBar) findViewById(R.id.topBarTitle);
        bottomTabLayout = (BottomTabLayout) findViewById(R.id.moduleTab);
        titleTopBar.setBackVisibility(false);
        initBottomTab();
    }

    private void initBottomTab() {
        bottomTabLayout.setButtonTextStyle(R.style.TextGray12);
        // set buttons from menu resource
        bottomTabLayout.setItems(R.menu.menu_bottom_layout);
        // set on selected tab listener.
        bottomTabLayout.setListener(new BottomTabLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switchFragment(id);
            }
        });
        // set button that will be select on start activity
        bottomTabLayout.setSelectedTab(R.id.hot_menu_button);
        // switchFragment(R.id.hot_menu_button);
        // enable indicator
        bottomTabLayout.setIndicatorVisible(true);
        bottomTabLayout.setIndicatorHeight(getResources().getDimension(R.dimen.indicator_height));
        bottomTabLayout.setIndicatorColor(R.color.colorIndicatorDark);
        // bottomTabLayout.setIndicatorLineColor(R.color.dark);
        // bottomTabLayout.setSelectedTab(R.id.menu_button5);

        // bottomTabLayout.setTabBubbleColor(ContextCompat.getColor(this, R.color.blue));
        // bottomTabLayout.setTabBubblePadding(0, 0, 0, 0);
        // bottomTabLayout.setTabBubbleTextStyle(R.style.TextWhite12);

        // show bubble
        // bottomTabLayout.showTabBubbleCount(R.id.menu_button1, 3);
    }

    private void switchFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.newest_menu_button:
                if (newestFragment == null) {
                    newestFragment = new NewestFragment();
                }
                titleTopBar.setTitleText(getString(R.string.newest_post));
                fragment = newestFragment;
                break;
            case R.id.hot_menu_button:
                if (hotTopicFragment == null) {
                    hotTopicFragment = new HotTopicFragment();
                }
                titleTopBar.setTitleText(getString(R.string.hot_post));
                fragment = hotTopicFragment;
                break;
            case R.id.discover_menu_button:
                if (discoverFragment == null) {
                    discoverFragment = new DiscoverFragment();
                }
                titleTopBar.setTitleText(getString(R.string.discover));
                fragment = discoverFragment;
                break;
            case R.id.allinfo_menu_button:
                if (allNodesFragment == null) {
                    allNodesFragment = new AllNodesFragment();
                }
                titleTopBar.setTitleText(getString(R.string.all_nodes));
                fragment = allNodesFragment;
                break;
            case R.id.setting_menu_button:
                if (ownerFragment == null) {
                    ownerFragment = new OwnerFragment();
                }
                titleTopBar.setTitleText(getString(R.string.my));
                fragment = ownerFragment;
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            if (!fragment.isAdded()) {
                ft.add(R.id.container, fragment, ""+id);
                if (previousFragment != null) {
                    ft.hide(previousFragment);
                }
                //ft.show(fragment);
            }else {
                if (fragment != previousFragment) {
                    ft.hide(previousFragment);
                    ft.show(fragment);
                }else {
                    return;
                }
            }
            ft.commit();

            previousFragment = fragment;
        }
    }

    private long firstClickBack;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            long secondClickBack = System.currentTimeMillis();
            if(secondClickBack - firstClickBack >1500){
                Toast.makeText(this, R.string.exit_app_prompt, Toast.LENGTH_SHORT).show();
                firstClickBack = secondClickBack;
                return true;
            }else{
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
