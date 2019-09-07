package com.example.access1;

import com.example.access1.util.CommonSize;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class TickConfrimShow {
	private Handler handler;
	private View view;
	private boolean isIn = false;
	private TextView textView;
	private Activity activity;

	public TickConfrimShow(Context context) {
		// TODO 自动生成的构造函数存根
		this.activity = (Activity)context;
		handler = new Handler(Looper.getMainLooper());
		view = LayoutInflater.from(activity).inflate(R.layout.tick_success, null, false);
		textView = (TextView) view.findViewById(R.id.tick_success_tv);
	}

	public void showTick() {
		showTick("修改成功");
	}

	public void showTick(final String string) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				textView.setText(string);
				float density = CommonSize.getDensity(activity);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) density * 133,
						(int) density * 133);
				params.gravity = Gravity.CENTER;
				if (isIn == false) {
					activity.addContentView(view, params);
				}
				isIn = true;
				view.setVisibility(View.VISIBLE);
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						view.setVisibility(View.GONE);
					}
				}, 600);
			}
		});

	}

}
