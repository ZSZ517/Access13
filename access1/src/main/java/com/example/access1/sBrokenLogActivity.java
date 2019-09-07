package com.example.access1;

import java.io.IOException;
import java.util.List;

import com.example.access1.bean.MyBrokenBean;
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
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class sBrokenLogActivity extends HaveToolBarActivity {
	private SharedPreferences sp;
	private String token;
	private ListView lv;
	private String[] datas;
	private String responceString;
	private List<MyBrokenBean.ConcreteData> concreteDatas;
	private ShowWaitingProgress showWaitingProgress;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broken_record_stu);
		getSupportActionBar().setTitle("�ҵ�Υ��");
		sp = getSharedPreferences("loginStudent", 0);
		token = sp.getString("token", "noToken");
		forInfo();
		lv = f(R.id.lv);

	}

	// ���������������
	private void forInfo() {
		String url = "http://47.100.209.59:8080/rlsb/getMyBad";
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
					CommonUtil.showToast(sBrokenLogActivity.this, "û������");
					// һ��Ҫreturn���������Ŀ��ܻ�ִ��
					return;
				}

				if (response.isSuccessful()) {
					try {// ��Ϊ��response��string��ת�������׳��쳣
						responceString = response.body().string();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						CommonUtil.showToast(sBrokenLogActivity.this, "response������ת��Ϊ�ַ���");
						return;
					}
					String code_701 = "\"code\":701";
					if (responceString.contains(code_701)) {
						CommonUtil.showToast(sBrokenLogActivity.this, "��ʱû��Υ����Ϣ");
						return;
					} else {
						MyBrokenBean myBrokenBean = new Gson().fromJson(responceString, MyBrokenBean.class);
						concreteDatas = myBrokenBean.getData();
						int i = 0;
						datas = new String[concreteDatas.size()];
						for (MyBrokenBean.ConcreteData data : concreteDatas) {
							datas[i++] = CommonUtil.stampToTime(data.getBadTime());
						}
						// CommonUtil.showToast(sBrokenLogActivity.this, concreteDatas.size() + "");
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO �Զ����ɵķ������
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(sBrokenLogActivity.this,
										android.R.layout.simple_list_item_1, datas);
								lv.setAdapter(adapter);
								lv.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO �Զ����ɵķ������
										Intent intent = new Intent(sBrokenLogActivity.this,
												sBrokenLogDetailActivity.class);
										intent.putExtra("datas", new Gson().toJson(concreteDatas.get(position)));
										startActivity(intent);
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
}