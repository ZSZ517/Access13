package com.example.access1;

import com.example.access1.bean.InfoInputApplyBean;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.miludeer.jsoncode.JsonCode;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApplyHandleActivity extends HaveToolBarActivity implements OnClickListener {

	private TextView id_tv, name_tv, sno_tv, major_tv, phone_tv, reason_tv, sex_tv, status_tv, relation_tv;
	private String jsonString;
	InfoInputApplyBean.ConcreteData concreteData;
	private Button pass_btn, unpass_btn;
	private ShowWaitingProgress showWaitingProgress;
	private String token;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_apply_handle);
		getSupportActionBar().setTitle("审核处理");
		initView();
		token = getSharedPreferences("loginManager", 0).getString("token", "noToken");
		jsonString = getIntent().getStringExtra("datas");
		concreteData = new Gson().fromJson(jsonString, InfoInputApplyBean.ConcreteData.class);
		initDatas();
	}

	private void initView() {
		id_tv = f(R.id.id_tv);
		name_tv = f(R.id.name_tv);
		sno_tv = f(R.id.sno_tv);
		major_tv = f(R.id.major_tv);
		phone_tv = f(R.id.phone_tv);
		reason_tv = f(R.id.reason_tv);
		sex_tv = f(R.id.sex_tv);
		status_tv = f(R.id.status_tv);
		relation_tv = f(R.id.relation_tv);
		pass_btn = f(R.id.pass_btn);
		unpass_btn = f(R.id.unpass_btn);
		pass_btn.setOnClickListener(this);
		unpass_btn.setOnClickListener(this);
	}

	private void initDatas() {
		id_tv.setText(concreteData.getId());
		name_tv.setText(concreteData.getStudentName());
		sno_tv.setText(concreteData.getSno());
		major_tv.setText(concreteData.getMajor());
		phone_tv.setText(concreteData.getPhone());
		reason_tv.setText(concreteData.getReason());
		status_tv.setText(concreteData.getStatus() + "");
		relation_tv.setText(concreteData.getRelation());
		if(concreteData.getSex().equals("man"))
			sex_tv.setText("男");
		else 
			sex_tv.setText("女");
	}

	@Override
	public void onClick(View view) {

		showWaitingProgress = new ShowWaitingProgress();
		showWaitingProgress.show(ApplyHandleActivity.this, "处理中");
		String url1 = CommonUtil.url + "passApply";
		String url2 = CommonUtil.url + "unpassApply";
		RequestBody requestBody = new FormBody.Builder().add("id", id_tv.getText().toString()).build();
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = null;
		switch (view.getId()) {
		case R.id.pass_btn:
			request = new Request.Builder().url(url1).addHeader("token", token).post(requestBody).build();
			break;

		case R.id.unpass_btn:
			request = new Request.Builder().url(url2).header("token", token).post(requestBody).build();
			break;
		}

		final Call call = okHttpClient.newCall(request);
		// 一定要在子线程，不然服务器什么都不会返回
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				Response response = null;

				try {
					response = call.execute();// 要在子线程中
					showWaitingProgress.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
					showWaitingProgress.dismiss();
					CommonUtil.showToast(ApplyHandleActivity.this, "网络有点卡...");
					return;
				}
				String responseString = "";
				try {
					responseString = response.body().string();
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (responseString.contains(CommonUtil.requestSuccess)) {
					new TickConfrimShow(ApplyHandleActivity.this).showTick("处理成功");
					new Handler(getMainLooper()).postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							setResult(RESULT_OK);
							finish();
						}
					}, 1000);
				} else {
					CommonUtil.showToast(ApplyHandleActivity.this, JsonCode.getValue(responseString, "$.message"));
				}
			}
		}).start();
	}

}
