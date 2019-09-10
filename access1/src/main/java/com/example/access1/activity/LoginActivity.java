package com.example.access1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.access1.R;
import com.example.access1.util.CommonUtil;
import com.example.access1.util.StreamTools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *Description: 登陆类，在学生端和管理端都通用
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText account_et;
	private EditText pwd_et;
	private Button login_button, login_which_button;
	private CheckBox remember_check_box;
	private String account;
	private String password;
	private SharedPreferences sp;
	private String loginContent;
	private String which;
	private ShowWaitingProgress showWaitingProgress;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_login);
		initView();
		login_button.setOnClickListener(this);
		login_which_button.setOnClickListener(this);
		sp = getSharedPreferences("loginStudent", 0);
		account_et.setText(sp.getString("account", ""));
		if (!TextUtils.isEmpty(pwd_et.getText().toString())) {
			remember_check_box.setChecked(true);
		}
		pwd_et.setText(sp.getString("password", ""));
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_which_button:
			if (login_which_button.getText().toString().equals("切换至管理端")) {
				login_which_button.setText("切换至学生端");
				sp = getSharedPreferences("loginManager", 0);
				account_et.setText(sp.getString("account", ""));
				if (!TextUtils.isEmpty(pwd_et.getText().toString())) {
					remember_check_box.setChecked(true);
				}
				pwd_et.setText(sp.getString("password", ""));
			} else {
				login_which_button.setText("切换至管理端");
				sp = getSharedPreferences("loginStudent", 0);
				account_et.setText(sp.getString("account", ""));
				if (!TextUtils.isEmpty(pwd_et.getText().toString())) {
					remember_check_box.setChecked(true);
				}
				pwd_et.setText(sp.getString("password", ""));
			}
			break;

		case R.id.login_button:
			which = login_which_button.getText().toString();
			account = account_et.getText().toString().trim();
			password = pwd_et.getText().toString().trim();
			if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
				CommonUtil.showToast(this, "账号和密码不能为空");
			} else {
				showWaitingProgress=new ShowWaitingProgress();
				showWaitingProgress.show(LoginActivity.this, "正在登陆");
				new Thread() {
					@Override
					public void run() {
						String path;
						try {
							if (which.equals("切换至学生端")) {
								path = "http://47.100.209.59:8080/rlsb/manager/login";
							} else {
								path = "http://47.100.209.59:8080/rlsb/student/login";
							}
							URL url = new URL(path);
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.setRequestMethod("POST");
							conn.setReadTimeout(5000);
							conn.setConnectTimeout(5000);
							conn.setUseCaches(false);
							String data;
							if (which.equals("切换至学生端")) {
								data = "account=" + account + "&password=" + password;
							} else {
								data = "sno=" + account + "&password=" + password;
							}
							conn.setDoOutput(true);
							
							try {
								conn.getOutputStream().write(data.getBytes());
							} catch (Exception e) {
								runOnUiThread(new Runnable() {

									@Override
									public void run() {

										showWaitingProgress.getWaitingDialog().dismiss();
										CommonUtil.showToast(LoginActivity.this, "没有网络或网络信号较差");
									}
								});
							}

							// 获取服务器返回的状态吗
							int code = conn.getResponseCode();
							if (code == 200) {
								// 获取服务器返回的输入流对象
								InputStream in = conn.getInputStream();
								loginContent = StreamTools.readString(in);
								Log.d("hello", loginContent);
								String login_success = "\"code\":200";
								if (loginContent.contains(login_success)) {// 登陆成功
									showWaitingProgress.getWaitingDialog().dismiss();
									if (which.equals("切换至学生端")) {
										Intent intent = new Intent(LoginActivity.this, MainActivity.class);
										startActivity(intent);
										saveLoginInfo("manager");
									} else {
										Intent intent = new Intent(LoginActivity.this, sMainActivity.class);
										startActivity(intent);
										saveLoginInfo("student");
									}

								} else {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											showWaitingProgress.getWaitingDialog().dismiss();
											CommonUtil.showToast(LoginActivity.this, "账号或密码错误");
										}
									});
								}
							}
						} catch (Exception e) {

							e.printStackTrace();
						}
					}
				}.start();
			}
			break;
		}
	}

	private void saveLoginInfo(String string) {
		int index_original = loginContent.indexOf("\"token\":\"") + 9;
		String token_string = loginContent.substring(index_original, loginContent.indexOf("\"", index_original));
		if (string.equals("manager")) {
			sp = getSharedPreferences("loginManager", 0);// 0为默认模式，即覆盖
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("token", token_string);
			editor.putString("account", account);
			if (remember_check_box.isChecked()) {
				editor.putString("password", password);
				if (TextUtils.isEmpty(sp.getString("groupId", ""))) {
					editor.putString("groupId", "13");
				}
				editor.apply();
			} else {
				editor.putString("password", "");
				editor.apply();
			}
		} else {
			sp = getSharedPreferences("loginStudent", 0);// 0为默认模式，即覆盖
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("token", token_string);
			editor.putString("account", account);
			if (remember_check_box.isChecked()) {
				editor.putString("password", password);
				editor.apply();
			} else {
				editor.putString("password", "");
				editor.commit();
			}
		}
	}

	private void initView() {
		account_et = (EditText) findViewById(R.id.account_et);
		pwd_et = (EditText) findViewById(R.id.pwd_et);
		login_button = (Button) findViewById(R.id.login_button);
		login_which_button = (Button) findViewById(R.id.login_which_button);
		remember_check_box = (CheckBox) findViewById(R.id.remember_check_box);
	}
}
