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
		getSupportActionBar().setTitle("来访日志");
		sp = getSharedPreferences("loginManager", 0);
		token = sp.getString("token", "noToken");
		initView();
		forInfo();
	}

	private void initView() {
		lv = f(R.id.visit_log_lv);
	}

	// 向服务器发出请求
	private void forInfo() {
		String url = CommonUtil.url + "getVisit" + "?pn=1";
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).header("token", token) // 默认为GET请求，可以不写
				.build();
		final Call call = client.newCall(request);
		showWaitingProgress = new ShowWaitingProgress();
		showWaitingProgress.show(this, "请稍等");
		// 通过 call.excute() 方法来提交同步请求，这种方式会阻塞线程，而为了避免 ANR 异常，Android3.0
		// 之后已经不允许在主线程中访问网络了
		// 所以 OkHttp 的同步 get 请求需要开启一个子线程：

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
					CommonUtil.showToast(VisitLogActivity.this, "没有网络");
					// 一定要return，否则后面的可能会执行
					return;
				}

				if (response.isSuccessful()) {
					try {// 因为从response到string的转化可能抛出异常
						responseString = response.body().string();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						CommonUtil.showToast(VisitLogActivity.this, "response不可以转换为字符串");
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
								// TODO 自动生成的方法存根
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(VisitLogActivity.this,
										android.R.layout.simple_list_item_1, datas);
								lv.setAdapter(adapter);
								lv.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO 自动生成的方法存根
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
