package com.example.access1.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.access1.BrokenLogActivity;
import com.example.access1.ChangePwdActivity;
import com.example.access1.FeedBackActivity;
import com.example.access1.GetApplyActivity;
import com.example.access1.LoginActivity;
import com.example.access1.ManageObjectActivity;
import com.example.access1.R;
import com.example.access1.RotateBean;
import com.example.access1.RotateVpAdapter;
import com.example.access1.VisitLogActivity;
import com.example.access1.sInformationInputActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi", "ValidFragment","ClickableViewAccessibility"})
public class MyFragment extends Fragment implements OnTouchListener, OnClickListener {

	public static final String TYPE = "type";
	private View view;
	public TextView textViewInFragment;
	protected boolean isVisible;
	private String contentName;
	private TextView portrait_tv;
	private ImageView portrait_iv;
	private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;

	// 后期
	private static final int TIME = 3000;
	private ViewPager rotateViewPager;
	private LinearLayout pointLl;// 轮播状态改变的小圆点容器
	private List<RotateBean> datas;
	private RotateVpAdapter vpAdapter;
	private Button btn1, btn2, btn4, btn5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public MyFragment(String contentName) {
		this();
		this.contentName = contentName;
	}

	public MyFragment() {
		// TODO 自动生成的构造函数存根
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (contentName == "首页") {

			view = inflater.inflate(R.layout.home_page_fragment, container, false);

			// 后期添加轮播
			rotateViewPager = (ViewPager) view.findViewById(R.id.rotate_vp);
			pointLl = (LinearLayout) view.findViewById(R.id.rotate_point_container);

			buildDatas();// 构造数据
			vpAdapter = new RotateVpAdapter(getContext());
			vpAdapter.setDatas(datas);
			rotateViewPager.setAdapter(vpAdapter);
			// ViewPager的页数为int最大值,设置当前页多一些,可以上来就向前滑动
			// 为了保证第一页始终为数据的第0条 取余要为0,因此设置数据集合大小的倍数
			rotateViewPager.setCurrentItem(datas.size() * 1);

			// 开始轮播
			handler = new Handler();
			startRotate();
			// 添加轮播小点
			addPoints();
			// 随着轮播改变小点
			changePoints();

			initView(view);
			return view;
		}
		if (contentName == "设置") {
			view = inflater.inflate(R.layout.other_setting_fragment, container, false);
			initView(view);
			return view;
		}
		view = inflater.inflate(R.layout.home_page_fragment, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		if (contentName == "首页") {
			linearLayout1 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_1_left);
			linearLayout2 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_1_right);
			linearLayout3 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_2_left);
			linearLayout4 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_2_right);
			linearLayout1.setOnTouchListener(this);
			linearLayout2.setOnTouchListener(this);
			linearLayout3.setOnTouchListener(this);
			linearLayout4.setOnTouchListener(this);
			return;
		} else if (contentName == "设置") {
			btn1 = (Button) view.findViewById(R.id.other_setting_btn1);
			btn2 = (Button) view.findViewById(R.id.other_setting_btn2);
			btn4 = (Button) view.findViewById(R.id.other_setting_btn4);
			btn5 = (Button) view.findViewById(R.id.other_setting_btn5);
			portrait_iv=(ImageView)view.findViewById(R.id.portrait_iv);
			portrait_tv=(TextView)view.findViewById(R.id.portrait_tv);
			portrait_tv.setText("管理公寓   "+getContext().getSharedPreferences("loginManager", 0).getString("groupId", "13").substring(1)+"栋");
			
			btn1.setOnClickListener(this);
			btn2.setOnClickListener(this);
			btn4.setOnClickListener(this);
			btn5.setOnClickListener(this);

		}

		// textViewInFragment = (TextView) view.findViewById(R.id.textView_content);
		// textViewInFragment.setText(getArguments().getString(TYPE, "Default"));
	}

	public static MyFragment newInstance(String contentName) {
		MyFragment myFragment = new MyFragment(contentName);
		Bundle bundle = new Bundle();
		// 这里只是为了initView中的取出数据
		bundle.putString(TYPE, contentName);
		myFragment.setArguments(bundle);
		return myFragment;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.mainPage_ll2_1_left:
			Intent intent1 = new Intent(getContext(), GetApplyActivity.class);
			startActivity(intent1);
			break;
		case R.id.mainPage_ll2_1_right:
			Intent intent2 = new Intent(getContext(), VisitLogActivity.class);
			startActivity(intent2);
			break;
		case R.id.mainPage_ll2_2_left:
			Intent intent3 = new Intent(getContext(), BrokenLogActivity.class);
			startActivity(intent3);
			break;
		case R.id.mainPage_ll2_2_right:
			Intent intent4 = new Intent(getContext(), sInformationInputActivity.class);
			intent4.putExtra("info", "not open input for manager");
			startActivity(intent4);
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.other_setting_btn1:
			Intent intenta = new Intent(getContext(), ManageObjectActivity.class);
			getActivity().startActivityForResult(intenta,3);

			break;
		case R.id.other_setting_btn2:
			Intent intentb = new Intent(getContext(), ChangePwdActivity.class);
			startActivity(intentb);

			break;
		case R.id.other_setting_btn4:
			Intent intentd = new Intent(getContext(), FeedBackActivity.class);
			startActivity(intentd);

			break;
		case R.id.other_setting_btn5:
			SharedPreferences sp=getContext().getSharedPreferences("loginManager", 0);
			SharedPreferences.Editor editor=sp.edit();
			editor.putString("token", "");
			editor.commit();
			Intent intente = new Intent(getContext(), LoginActivity.class);
			startActivity(intente);
			break;
		}
	}

	// 后期添加轮播
	private void buildDatas() {
		datas = new ArrayList<RotateBean>();
		datas.add(new RotateBean(R.drawable.aa));
		datas.add(new RotateBean(R.drawable.bb));
		datas.add(new RotateBean(R.drawable.cc));
	}

	private void changePoints() {
		rotateViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 滑动则停止之前的postDelayed，重新postDelayed刷新。
				handler.removeCallbacks(rotateRunnable);
				handler.postDelayed(rotateRunnable, 3000);
			}

			@Override
			public void onPageSelected(int position) {
				if (isRotate) {
					// 把所有小点设置为白色
					for (int i = 0; i < datas.size(); i++) {
						ImageView pointIv = (ImageView) pointLl.getChildAt(i);
						pointIv.setImageResource(R.drawable.point_white);
					}
					// 设置当前位置小点为灰色
					ImageView iv = (ImageView) pointLl.getChildAt(position % datas.size());
					iv.setImageResource(R.drawable.point_grey);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/**
	 * 添加轮播切换小点
	 */
	private void addPoints() {
		// 有多少张图加载多少个小点
		for (int i = 0; i < datas.size(); i++) {
			ImageView pointIv = new ImageView(getContext());
			pointIv.setPadding(5, 5, 5, 5);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
			pointIv.setLayoutParams(params);

			// 设置第0页小点的为灰色
			if (i == 0) {
				pointIv.setImageResource(R.drawable.point_grey);
			} else {
				pointIv.setImageResource(R.drawable.point_white);
			}
			pointLl.addView(pointIv);
		}
	}

	public Handler handler;
	public boolean isRotate = false;
	private Runnable rotateRunnable;
	public boolean flag_stop = false;

	/**
	 * 开始轮播
	 */
	public void startRotate() {
		rotateRunnable = new Runnable() {
			@Override
			public void run() {
				int nowIndex = rotateViewPager.getCurrentItem();
				rotateViewPager.setCurrentItem(++nowIndex);
				if (isRotate) {
					handler.postDelayed(rotateRunnable, TIME);
				} else {
					flag_stop = true;
				}
			}
		};
		begin();

	}

	public void begin() {
		handler.postDelayed(rotateRunnable, TIME);
		flag_stop = false;
	}
	
	public void setGroupId(String groupId) {
		portrait_tv.setText("管理公寓   "+groupId.substring(1)+"栋");
	}
	
	public void setPortrait(Bitmap bitmap) {
		portrait_iv.setImageBitmap(bitmap);
	}

}
