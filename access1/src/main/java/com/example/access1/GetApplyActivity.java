package com.example.access1;

import java.io.IOException;
import java.util.List;

import com.example.access1.bean.InfoInputApplyBean;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import cn.miludeer.jsoncode.JsonCode;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetApplyActivity extends HaveToolBarActivity {
	private ShowWaitingProgress showWaitingProgress;
	private ListView lv;
	private String token;
	private Button all_pass;
	private String responseString;
	private List<InfoInputApplyBean.ConcreteData> concreteDatas;
	private String[] datas;
	private String[] all_pass_id;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_get_apply);
		getSupportActionBar().setTitle("�����Ϣ");
		initView();
		token = getSharedPreferences("loginManager", 0).getString("token", "noToken");
		forInfo();
	}

	private void initView() {
		lv = f(R.id.get_apply_lv);
		all_pass = f(R.id.all_pass);
		all_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				RequestBody[] requestBody_array = new RequestBody[all_pass_id.length];
				for (int i = 0; i < all_pass_id.length; i++) {
					requestBody_array[i] = new FormBody.Builder().add("id", all_pass_id[i]).build();
				}
				showWaitingProgress = new ShowWaitingProgress();
				showWaitingProgress.show(GetApplyActivity.this, "������");
				String url = CommonUtil.url + "passApply";
				OkHttpClient okHttpClient = new OkHttpClient();
				for (int i = 0; i < all_pass_id.length; i++) {
					Request request = new Request.Builder().url(url).addHeader("token", token)
							.post(requestBody_array[i]).build();
					final Call call = okHttpClient.newCall(request);
					// һ��Ҫ�����̣߳���Ȼ������ʲô�����᷵��
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO �Զ����ɵķ������
							Response response = null;

							try {
								response = call.execute();// Ҫ�����߳���
								showWaitingProgress.dismiss();
							} catch (Exception e) {
								// TODO: handle exception
								showWaitingProgress.dismiss();
								CommonUtil.showToast(GetApplyActivity.this, "�����е㿨...");
								return;
							}
							String responseString = "";
							try {
								responseString = response.body().string();
							} catch (Exception e) {
								// TODO: handle exception
							}
							if (responseString.contains(CommonUtil.requestSuccess)) {
								new TickConfrimShow(GetApplyActivity.this).showTick("����ɹ�");
								new Handler(getMainLooper()).postDelayed(new Runnable() {

									@Override
									public void run() {
										// TODO �Զ����ɵķ������
										setResult(RESULT_OK);
										finish();
									}
								}, 800);
							} else {
								CommonUtil.showToast(GetApplyActivity.this,
										JsonCode.getValue(responseString, "$.message"));
							}
						}
					}).start();
				}
			}
		});
	}

	private void forInfo() {
		String url = CommonUtil.url + "getApply";
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).header("token", token) // Ĭ��ΪGET���󣬿��Բ�д
				.build();
		final Call call = client.newCall(request);
		showWaitingProgress = new ShowWaitingProgress();
		showWaitingProgress.show(this, "���Ե�");
		// ͨ�� call.excute() �������ύͬ���������ַ�ʽ�������̣߳���Ϊ�˱��� ANR �쳣��Android3.0
		// ֮���Ѿ������������߳��з���������
		// ���� OkHttp ��ͬ�� get ������Ҫ����һ�����̣߳�

		new Thread(new Runnable() {
			@Override
			public void run() {
				Response response = null;
				try {
					response = call.execute();
					showWaitingProgress.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
					showWaitingProgress.dismiss();
					CommonUtil.showToast(GetApplyActivity.this, "û������");
					// һ��Ҫreturn���������Ŀ��ܻ�ִ��
					return;
				}
				if (response.isSuccessful()) {
					try {// ��Ϊ��response��string��ת�������׳��쳣
						responseString = response.body().string();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						CommonUtil.showToast(GetApplyActivity.this, "response������ת��Ϊ�ַ���");
						return;
					}
					String code_701 = "\"code\":701";
					String code_601 = "\"code\":601";
					if (responseString.contains(code_701)) {
						CommonUtil.showToast(GetApplyActivity.this, "��ʱû��Υ����Ϣ");
						return;
					} else if (responseString.contains(code_601)) {
						CommonUtil.showToast(GetApplyActivity.this, JsonCode.getValue(responseString, "$.message"));
						new Handler(getMainLooper()).postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO �Զ����ɵķ������
								Intent intent = new Intent(GetApplyActivity.this, LoginActivity.class);
								startActivity(intent);
							}
						}, 2000);
						return;
					} else {
						InfoInputApplyBean applyBean = new Gson().fromJson(responseString, InfoInputApplyBean.class);
						concreteDatas = applyBean.getData();
						int i = 0;
						datas = new String[concreteDatas.size()];
						all_pass_id = new String[concreteDatas.size()];
						for (InfoInputApplyBean.ConcreteData data : concreteDatas) {
							datas[i] = data.getSno() + "      " + "�����ϵ��  " + data.getRelation();
							all_pass_id[i++] = data.getId();
						}
						// CommonUtil.showToast(GetApplyActivity.this, concreteDatas.size() + "");
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO �Զ����ɵķ������
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(GetApplyActivity.this,
										android.R.layout.simple_list_item_1, datas);
								lv.setAdapter(adapter);
								lv.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO �Զ����ɵķ������
										Intent intent = new Intent(GetApplyActivity.this, ApplyHandleActivity.class);
										intent.putExtra("datas", new Gson().toJson(concreteDatas.get(position)));
										startActivityForResult(intent, 1);
										return;
									}
								});
							}
						});
					}
				}

			}
		}).start();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				forInfo();
			}
		}
	}
}
