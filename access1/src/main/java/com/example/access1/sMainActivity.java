package com.example.access1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.access1.adapter.MyFragmentPagerAdapter;
import com.example.access1.fragments.sMyFragment;
import com.example.access1.util.CommonUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.ViewPagerOnTabSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class sMainActivity extends BaseActivity {
	private Toolbar toolbar;
	private TabLayout tabLayout;// ��������
	// tab
	private ViewPager viewPager;
	private List<Fragment> fragment_list;
	private String[] tabText_array = { "��ҳ", "����" };
	private int[] tabIconId_array = { R.drawable.tab_home_normal, R.drawable.tab_mine_normal };
	private int[] tabIconId_array_pressed = { R.drawable.tab_home_passed, R.drawable.tab_mine_passed };
	private TextView textViewInToolbar;
	private Bitmap bitmap;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ʵ����������
		setContentView(R.layout.activity_main_stu);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[] { Manifest.permission.CAMERA }, 2);
		}
		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 3);
		}
		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			// ����WRITE_EXTERNAL_STORAGEȨ��
			requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 4);
		}

		// ���ù�����
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		// �Ƿ���С��ͷ,Ĭ��falseû�С�
		// getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		textViewInToolbar = (TextView) findViewById(R.id.textViewInToolbar);
		textViewInToolbar.setTextColor(Color.WHITE);
		viewPager.setOffscreenPageLimit(6);
		initFragment();
		initRelation();
		setupTabIcons();
		initEvent();

	}

	private void initFragment() {
		//
		fragment_list = new ArrayList<Fragment>();
		for (int i = 0; i < tabText_array.length; i++)
			fragment_list.add(sMyFragment.newInstance(tabText_array[i]));
	}

	private void initRelation() {
		// tabLayout����viewPager�Ļ����仯��
		// viewPager.setOnPageChangeListener(new
		// TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
		// @Override
		// public void onPageScrolled(int position, float positionOffset, int
		// positionOffsetPixels) {
		// // TODO �Զ����ɵķ������
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
		// // TODO �Զ����ɵķ������
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
		// ����viewPaper��PagerAdapter��pagerAdapter�����洢���ݺͼ���viewPager�仯
		viewPager.setAdapter(myFragmentPagerAdapter);
		// ʹtabLayout��countTab��viewPager��һ��
		tabLayout.setupWithViewPager(viewPager);
	}

	// �����tab�Ķ���view���÷���Ҳ�����ڳ�ʼ��Tag,flagΪtrueʱ��ʾҪ����Ϊѡ��
	private void changeTabView(View view, int position, boolean flag) {
		TextView textView = (TextView) view.findViewById(R.id.tabText_textView);
		ImageView imageView = (ImageView) view.findViewById(R.id.tabIcon_imageView);
		if (flag == true) {
			String string = textView.getText().toString();
			textViewInToolbar.setText(string);
			// if (string.equals("�ҵ�")) {
			// toolbar.setBackgroundColor(Color.WHITE);
			// textViewInToolbar.setTextColor(Color.rgb(51, 204, 204));
			// } else if (string.equals("��ҳ")) {
			// toolbar.setBackgroundColor(Color.rgb(51, 204, 204));
			// textViewInToolbar.setTextColor(Color.WHITE);
			//
			// }
			textView.setTextColor(0xff33cccc);
			imageView.setImageResource(tabIconId_array_pressed[position]);
		} else {
			imageView.setImageResource(tabIconId_array[position]);
			textView.setTextColor(Color.GRAY);
		}
	}

	private void initEvent() {
		tabLayout.setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager) {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				// if (tab.getPosition() != 1) {
				super.onTabSelected(tab);
				changeTabView(tab.getCustomView(), tab.getPosition(), true);
				// }
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				super.onTabUnselected(tab);
				changeTabView(tab.getCustomView(), tab.getPosition(), false);
			}
		});
	}

	// ����tab��ͼ��
	private void setupTabIcons() {
		for (int i = 0; i < fragment_list.size(); i++)
			tabLayout.getTabAt(i).setCustomView(getTabView(i));
		// viewPager.setCurrentItem(0);
	}

	@SuppressLint("InflateParams")
	public View getTabView(int position) {
		// ʵ����tab���Ʋ���
		View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
		TextView tabText_textView = (TextView) view.findViewById(R.id.tabText_textView);
		tabText_textView.setText(tabText_array[position]);
		ImageView tabIcon_imageView = (ImageView) view.findViewById(R.id.tabIcon_imageView);
		if (position == 0) {
			tabIcon_imageView.setImageResource(tabIconId_array_pressed[position]);
			tabText_textView.setTextColor(0xff33cccc);
		} else {
			tabIcon_imageView.setImageResource(tabIconId_array[position]);
		}
		return view;
	}

	@Override
	protected void onResume() {
		super.onResume();
		((sMyFragment) fragment_list.get(0)).isRotate = true;
		if (((sMyFragment) fragment_list.get(0)).handler != null) {
			if (((sMyFragment) fragment_list.get(0)).flag_stop == true)
				((sMyFragment) fragment_list.get(0)).startRotate();
		}
		updatePortrait();
	}

	@Override
	protected void onPause() {
		super.onPause();
		((sMyFragment) fragment_list.get(0)).isRotate = false;
	}
	
	private void updatePortrait() {
		String url = CommonUtil.url + "getImage?sno="
				+ getSharedPreferences("loginStudent", 0).getString("account", "noAccount");
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		final Call call = okHttpClient.newCall(request);
		// setAAA();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				Response response = null;
				try {
					response = call.execute();
				} catch (Exception e) {
					// TODO: handle exception
					return;
				}
				try {
					InputStream iStream = response.body().byteStream();
					bitmap = BitmapFactory.decodeStream(iStream);
					((sMyFragment) fragment_list.get(1)).setPortrait(bitmap);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
	}

}
