package com.example.access1;

import java.io.IOException;
import java.util.List;

import com.example.access1.bean.MyBrokenBean;
import com.example.access1.bean.MyBrokenBean.ConcreteData;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

public class BrokenLogActivity extends HaveToolBarActivity {
	private SharedPreferences sp;
	private String token;
	private ListView lv;
	private String[] datas;
	private String[] concreteDatas;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broken_record);
		getSupportActionBar().setTitle("Υ����־");
		sp = getSharedPreferences("loginManager", 0);
		token = sp.getString("token", "noToken");
		forInfo();
		lv = f(R.id.lv);

		// WindowManager
		// windowManager=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
		// DisplayMetrics displayMetrics=new DisplayMetrics();
		// windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		// DisplayMetrics displayMetrics2=new DisplayMetrics();

	}

	// ���������������
	private void forInfo() {
		String url = CommonUtil.url + "getBad?pn=1";
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).header("token", token) // Ĭ��ΪGET���󣬿��Բ�д
				.build();
		final Call call = client.newCall(request);
		// ͨ�� call.excute() �������ύͬ���������ַ�ʽ�������̣߳���Ϊ�˱��� ANR �쳣��Android3.0
		// ֮���Ѿ������������߳��з���������
		// ���� OkHttp ��ͬ�� get ������Ҫ����һ�����̣߳�

		new Thread(new Runnable() {
			@Override
			public void run() {
				Response response = null;

				try {
					response = call.execute();
				} catch (Exception e) {
					// TODO: handle exception
					CommonUtil.showToast(BrokenLogActivity.this, "�����е㿨");
					return;
				}
				try {
					if (response.isSuccessful()) {
						String responseString = response.body().string();
						String code_601 = "\"code\":601";
						//token���ڣ���Ҫ���µ�½
						if (responseString.contains(code_601)) {
							CommonUtil.showToast(BrokenLogActivity.this,
									JsonCode.getValue(responseString, "$.message"));
							new Handler(getMainLooper()).postDelayed(new Runnable() {
								@Override
								public void run() {
									// TODO �Զ����ɵķ������
									Intent intent = new Intent(BrokenLogActivity.this, LoginActivity.class);
									startActivity(intent);
								}
							}, 1500);
							return;
						} else if (responseString.contains(CommonUtil.requestSuccess)) {
							MyBrokenBean myBrokenBean = new Gson().fromJson(responseString, MyBrokenBean.class);
							List<ConcreteData> concreteData_list = myBrokenBean.getData();
							int size=concreteData_list.size();
							datas = new String[size];
							concreteDatas=new String[size];
							int i = 0;
							for (ConcreteData concreteData : concreteData_list) {
								concreteDatas[i] = new Gson().toJson(concreteData);
								datas[i++] = concreteData.getSno() + "   "
										+ CommonUtil.stampToTime(concreteData.getBadTime());
							}
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO �Զ����ɵķ������
									ArrayAdapter<String> adapter = new ArrayAdapter<String>(BrokenLogActivity.this,
											android.R.layout.simple_list_item_1, datas);
									lv.setAdapter(adapter);
									lv.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(AdapterView<?> parent, View view, int position,
												long id) {
											// TODO �Զ����ɵķ������
											Intent intent = new Intent(BrokenLogActivity.this,
													BrokenLogDetailActivity.class);
											intent.putExtra("datas", concreteDatas[position]);
											startActivity(intent);
										}
									});
								}
							});
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}