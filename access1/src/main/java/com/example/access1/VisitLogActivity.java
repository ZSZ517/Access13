package com.example.access1;

import java.io.IOException;
import java.util.List;

import com.example.access1.bean.VisitBean;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.miludeer.jsoncode.JsonCode;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VisitLogActivity extends HaveToolBarActivity {

	private SharedPreferences sp;
	private String token;
	private ListView lv;
	private String[] datas;
	private String responseString;
	private List<VisitBean.ConcreteData> concreteData_list;

	private ShowWaitingProgress showWaitingProgress;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_visit_log);
		getSupportActionBar().setTitle("������־");
		sp = getSharedPreferences("loginManager", 0);
		token = sp.getString("token", "noToken");
		initView();
		forInfo();
	}

	private void initView() {
		lv = f(R.id.visit_log_lv);
	}

	// ���������������
	private void forInfo() {
		String url = CommonUtil.url + "getVisit" + "?pn=1";
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
					CommonUtil.showToast(VisitLogActivity.this, "û������");
					// һ��Ҫreturn���������Ŀ��ܻ�ִ��
					return;
				}

				if (response.isSuccessful()) {
					try {// ��Ϊ��response��string��ת�������׳��쳣
						responseString = response.body().string();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						CommonUtil.showToast(VisitLogActivity.this, "response������ת��Ϊ�ַ���");
						return;
					}

					if (responseString.contains(CommonUtil.requestSuccess)) {
						VisitBean visitBean = new Gson().fromJson(responseString, VisitBean.class);
						concreteData_list = visitBean.getData();
						int size = concreteData_list.size();
						int i = 0;
						datas = new String[size];
						for (VisitBean.ConcreteData data : concreteData_list) {
							datas[i++] = data.getSno() + "   " + CommonUtil.stampToTime(data.getVisitTime());
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO �Զ����ɵķ������
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(VisitLogActivity.this,
										android.R.layout.simple_list_item_1, datas);
								lv.setAdapter(adapter);
								lv.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO �Զ����ɵķ������
										Intent intent = new Intent(VisitLogActivity.this, VisitLogDetailActivity.class);
										intent.putExtra("datas", new Gson().toJson(concreteData_list.get(position)));
										startActivity(intent);
										return;
									}
								});
							}
						});
					} else {
						CommonUtil.showToast(VisitLogActivity.this, JsonCode.getValue(responseString, "$.message"));
					}
				}

			}
		}).start();
	}
}
