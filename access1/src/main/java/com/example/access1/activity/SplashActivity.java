package com.example.access1.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.access1.R;

public class SplashActivity extends Activity {
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);// Òþ²Ø×´Ì¬À¸
		setContentView(R.layout.activity_splash);
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(10);
					sp = getSharedPreferences("loginStudent", 0);
					Intent intent;
					if (!TextUtils.isEmpty(sp.getString("token", ""))) {
						intent = new Intent(SplashActivity.this, sMainActivity.class);
						startActivity(intent);
						finish();
					} else {
						sp = getSharedPreferences("loginManager", 0);
						if (!TextUtils.isEmpty(sp.getString("token", ""))) {
							intent = new Intent(SplashActivity.this, MainActivity.class);// Æô¶¯MainActivity
							startActivity(intent);
							finish();
						} else {
							intent = new Intent(SplashActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}