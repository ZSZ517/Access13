package com.example.access1.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.ViewPagerOnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.access1.R;
import com.example.access1.adapter.MyFragmentPagerAdapter;
import com.example.access1.fragments.MyFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;// 管理所有tab
    private ViewPager viewPager;
    private List<Fragment> fragment_list;
    private String[] tabText_array = {"首页", "", "设置"};
    private int[] tabIconId_array = {R.drawable.tab_home_normal, R.drawable.tab_info_normal,
            R.drawable.tab_mine_normal};
    private int[] tabIconId_array_pressed = {R.drawable.tab_home_passed, R.drawable.tab_info_passed,
            R.drawable.tab_mine_passed};
    private TextView textViewInToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 实例化主布局
        setContentView(R.layout.activity_main);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        ImageView takePhoto_iv = new ImageView(this);
        takePhoto_iv.setImageResource(R.drawable.take_photo);
        addContentView(takePhoto_iv, layoutParams);
        takePhoto_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RecognizeActivity.class);
                startActivity(intent);
            }
        });
        // 设置工具栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initFragment();
        initRelation();
        setupTabIcons();
        initEvent();

    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        textViewInToolbar = (TextView) findViewById(R.id.textViewInToolbar);
        if (textViewInToolbar != null)
            textViewInToolbar.setTextColor(Color.WHITE);
        viewPager.setOffscreenPageLimit(6);
    }

    private void initFragment() {
        //
        fragment_list = new ArrayList<>();
        fragment_list.add(MyFragment.newInstance(tabText_array[0]));
        fragment_list.add(MyFragment.newInstance(tabText_array[1]));
        fragment_list.add(MyFragment.newInstance(tabText_array[2]));
    }

    private void initRelation() {
        // tabLayout监听viewPager的滑动变化。
        // viewPager.setOnPageChangeListener(new
        // TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
        // @Override
        // public void onPageScrolled(int position, float positionOffset, int
        // positionOffsetPixels) {
        //
        // super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        // }
        //
        // int position_after_drag;
        //
        // @Override
        // public void onPageScrollStateChanged(int state) {
        // // super.onPageScrollStateChanged(state);
        // if (state == 2) {
        // position_after_drag = tabLayout.getSelectedTabPosition();
        // }
        // }
        //
        // @Override
        // public void onPageSelected(int position) {
        //
        // super.onPageSelected(position);
        // if (position_after_drag != position) {
        // changeTabView(tabLayout.getTabAt(position).getCustomView(),
        // position_after_drag, false);
        // changeTabView(tabLayout.getTabAt(position).getCustomView(), position, true);
        // }
        // }
        // });

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragment_list, tabText_array);
        // 关联viewPaper和PagerAdapter，pagerAdapter用来存储数据和监听viewPager变化
        viewPager.setAdapter(myFragmentPagerAdapter);
        // 使tabLayout的countTab和viewPager的一致
        tabLayout.setupWithViewPager(viewPager);
    }

    // 传入该tab的定制view，该方法也会用于初始化Tag,flag为true时表示要设置为选择
    private void changeTabView(View view, int position, boolean flag) {
        TextView textView = (TextView) view.findViewById(R.id.tabText_textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.tabIcon_imageView);
        if (flag) {
            String string = textView.getText().toString();
            textViewInToolbar.setText(string);
            if (string.equals("设置") || string.equals("首页")) {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                textViewInToolbar.setTextColor(Color.WHITE);
            }
            if (position != 1) {
                imageView.setImageResource(tabIconId_array_pressed[position]);
            }
        } else {
            if (position != 1) {
                imageView.setImageResource(tabIconId_array[position]);
            }
            // textView.setTextColor(Color.GRAY);
        }
    }

    private void initEvent() {
        tabLayout.setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 1) {
                    super.onTabSelected(tab);
                    changeTabView(tab.getCustomView(), tab.getPosition(), true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                changeTabView(tab.getCustomView(), tab.getPosition(), false);
            }
        });
    }

    // 设置tab的图标
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setCustomView(getTabView(0));
        tabLayout.getTabAt(1).setCustomView(getTabView(1));
        // ((tabLayout.getTabAt(1)).view).setBackgroundColor(ContextCompat.getColor(getActivity()
        // , R.color.transparent));
        // viewPager.setCurrentItem(1);
        tabLayout.getTabAt(2).setCustomView(getTabView(2));
    }

    @SuppressLint("InflateParams")
    public View getTabView(int position) {
        // 实例化tab定制布局
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView tabText_textView = (TextView) view.findViewById(R.id.tabText_textView);
        tabText_textView.setText(tabText_array[position]);
        if (position != 1) {
            ImageView tabIcon_imageView = (ImageView) view.findViewById(R.id.tabIcon_imageView);
            tabIcon_imageView.setImageResource(tabIconId_array[position]);
        }
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyFragment) fragment_list.get(0)).isRotate = true;
        if (((MyFragment) fragment_list.get(0)).handler != null) {
            if (((MyFragment) fragment_list.get(0)).flag_stop) {
                ((MyFragment) fragment_list.get(0)).startRotate();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((MyFragment) fragment_list.get(0)).isRotate = false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String newGroupId = data.getStringExtra("groupId");
                ((MyFragment) fragment_list.get(2)).setGroupId(newGroupId);
//				Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.aa);
//				((MyFragment)fragment_list.get(2)).setPortrait(bitmap);
            }
        }
    }

}
