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

import com.example.access1.LoginActivity;
import com.example.access1.PorterDuffViewImageView;
import com.example.access1.R;
import com.example.access1.RotateBean;
import com.example.access1.RotateVpAdapter;
import com.example.access1.s2RecognizeTest;
import com.example.access1.sBrokenLogActivity;
import com.example.access1.sChangePwdActivity;
import com.example.access1.sFeedBackActivity;
import com.example.access1.sInfoInputApply;
import com.example.access1.sInformationInputActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi", "ValidFragment"})
public class sMyFragment extends Fragment implements OnTouchListener, OnClickListener {

	public static final String TYPE = "type";
	private View view;
	public TextView textViewInFragment;
	protected boolean isVisible;
	private String contentName;
	private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
	// ����
	private static final int TIME = 2500;
	private ViewPager rotateViewPager;
	private LinearLayout pointLl;// �ֲ�״̬�ı��СԲ������
	private List<RotateBean> datas;
	private RotateVpAdapter vpAdapter;
	private Button btn1, btn3, btn4, btn5;
	private TextView portrait_tv;
	PorterDuffViewImageView porterDuffViewImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public sMyFragment(String contentName) {
		this();
		this.contentName = contentName;
	}

	public sMyFragment() {
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (contentName == "��ҳ") {

			view = inflater.inflate(R.layout.home_page_fragment_stu, container, false);
			// ��������ֲ�
			rotateViewPager = (ViewPager) view.findViewById(R.id.rotate_vp);
			pointLl = (LinearLayout) view.findViewById(R.id.rotate_point_container);

			buildDatas();// ��������
			vpAdapter = new RotateVpAdapter(getContext());
			vpAdapter.setDatas(datas);
			rotateViewPager.setAdapter(vpAdapter);
			// ViewPager��ҳ��Ϊint���ֵ,���õ�ǰҳ��һЩ,������������ǰ����
			// Ϊ�˱�֤��һҳʼ��Ϊ���ݵĵ�0�� ȡ��ҪΪ0,����������ݼ��ϴ�С�ı���
			rotateViewPager.setCurrentItem(datas.size() * 1);

			// ��ʼ�ֲ�
			handler = new Handler();
			startRotate();
			// ����ֲ�С��
			addPoints();
			// �����ֲ��ı�С��
			changePoints();
			initView(view);
			return view;
		}
		if (contentName == "����") {
			view = inflater.inflate(R.layout.other_setting_fragment_stu, container, false);
			initView(view);
			return view;
		}
		// view = inflater.inflate(R.layout.home_page_fragment, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		if (contentName == "��ҳ") {
			linearLayout1 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_1_left);
			linearLayout2 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_1_right);
			linearLayout3 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_2_left);
			linearLayout4 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_2_right);
			linearLayout1.setOnTouchListener(this);
			linearLayout2.setOnTouchListener(this);
			linearLayout3.setOnTouchListener(this);
			linearLayout4.setOnTouchListener(this);
			return;
		} else if (contentName == "����") {
			btn1 = (Button) view.findViewById(R.id.other_setting_btn1);
			btn3 = (Button) view.findViewById(R.id.other_setting_btn3);
			btn4 = (Button) view.findViewById(R.id.other_setting_btn4);
			btn5 = (Button) view.findViewById(R.id.other_setting_btn5);
			portrait_tv = (TextView) view.findViewById(R.id.portrait_tv);
			porterDuffViewImageView = (PorterDuffViewImageView) view.findViewById(R.id.porterDuffViewImageView);
			portrait_tv.setText(getActivity().getSharedPreferences("loginStudent", 0).getString("account", "ѧ��"));
			btn1.setOnClickListener(this);
			btn3.setOnClickListener(this);
			btn4.setOnClickListener(this);
			btn5.setOnClickListener(this);
			return;

		}
	}

	public static sMyFragment newInstance(String contentName) {
		sMyFragment sMyFragment = new sMyFragment(contentName);
		Bundle bundle = new Bundle();
		// ����ֻ��Ϊ��initView�е�ȡ������
		bundle.putString(TYPE, contentName);
		sMyFragment.setArguments(bundle);
		return sMyFragment;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO �Զ����ɵķ������
		switch (v.getId()) {
		case R.id.mainPage_ll2_1_left:
			Intent intent1 = new Intent(getContext(), s2RecognizeTest.class);
			startActivity(intent1);
			break;
		case R.id.mainPage_ll2_1_right:
			Intent intent2 = new Intent(getContext(), sInfoInputApply.class);
			startActivity(intent2);
			break;
		case R.id.mainPage_ll2_2_left:
			Intent intent3 = new Intent(getContext(), sBrokenLogActivity.class);
			startActivity(intent3);
			break;
		case R.id.mainPage_ll2_2_right:
			Intent intent4 = new Intent(getContext(), sInformationInputActivity.class);
			startActivity(intent4);
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.other_setting_btn1:
			Intent intenta = new Intent(getContext(), sChangePwdActivity.class);
			startActivity(intenta);

			break;
		// case R.id.other_setting_btn2:
		//// Intent intentb = new Intent(getContext(), ChangePwdActivity.class);
		//// startActivity(intentb);
		//
		// break;
		case R.id.other_setting_btn3:
			Intent intentd = new Intent(getContext(), sFeedBackActivity.class);
			startActivity(intentd);

			break;
		case R.id.other_setting_btn5:
		case R.id.other_setting_btn4:
			SharedPreferences sp = getContext().getSharedPreferences("loginStudent", 0);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("token", "");
			editor.commit();
			Intent intente = new Intent(getContext(), LoginActivity.class);
			startActivity(intente);
			break;
		}
	}

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
				// ������ֹ֮ͣǰ��postDelayed������postDelayedˢ�¡�
				handler.removeCallbacks(rotateRunnable);
				handler.postDelayed(rotateRunnable, 3000);
			}

			@Override
			public void onPageSelected(int position) {
				if (isRotate) {
					// ������С������Ϊ��ɫ
					for (int i = 0; i < datas.size(); i++) {
						ImageView pointIv = (ImageView) pointLl.getChildAt(i);
						pointIv.setImageResource(R.drawable.point_white);
					}
					// ���õ�ǰλ��С��Ϊ��ɫ
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
	 * ����ֲ��л�С��
	 */
	private void addPoints() {
		// �ж�����ͼ���ض��ٸ�С��
		for (int i = 0; i < datas.size(); i++) {
			ImageView pointIv = new ImageView(getContext());
			pointIv.setPadding(5, 5, 5, 5);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
			pointIv.setLayoutParams(params);

			// ���õ�0ҳС���Ϊ�ҵ�
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
	 * ��ʼ�ֲ�
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

	// MainActivity��ȡ��
	public void setPortrait(final Bitmap bitmap) {
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				porterDuffViewImageView.setBitMap(bitmap);
				
			}
		});
	}

}
