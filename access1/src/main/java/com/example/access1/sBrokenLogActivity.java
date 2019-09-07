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
		getSupportActionBar().setTitle("我的违规");
		sp = getSharedPreferences("loginStudent", 0);
		token = sp.getString("token", "noToken");
		forInfo();
		lv = f(R.id.lv);

	}

	// 向服务器发出请求
	private void forInfo() {
		String url = "http://47.100.209.59:8080/rlsb/getMyBad";
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
					CommonUtil.showToast(sBrokenLogActivity.this, "没有网络");
					// 一定要return，否则后面的可能会执行
					return;
				}

				if (response.isSuccessful()) {
					try {// 因为从response到string的转化可能抛出异常
						responceString = response.body().string();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						CommonUtil.showToast(sBrokenLogActivity.this, "response不可以转换为字符串");
						return;
					}
					String code_701 = "\"code\":701";
					if (responceString.contains(code_701)) {
						CommonUtil.showToast(sBrokenLogActivity.this, "暂时没有违规信息");
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
								// TODO 自动生成的方法存根
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(sBrokenLogActivity.this,
										android.R.layout.simple_list_item_1, datas);
								lv.setAdapter(adapter);
								lv.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO 自动生成的方法存根
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