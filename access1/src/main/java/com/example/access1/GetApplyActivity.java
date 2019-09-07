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
		getSupportActionBar().setTitle("审核信息");
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
				// TODO 自动生成的方法存根
				RequestBody[] requestBody_array = new RequestBody[all_pass_id.length];
				for (int i = 0; i < all_pass_id.length; i++) {
					requestBody_array[i] = new FormBody.Builder().add("id", all_pass_id[i]).build();
				}
				showWaitingProgress = new ShowWaitingProgress();
				showWaitingProgress.show(GetApplyActivity.this, "处理中");
				String url = CommonUtil.url + "passApply";
				OkHttpClient okHttpClient = new OkHttpClient();
				for (int i = 0; i < all_pass_id.length; i++) {
					Request request = new Request.Builder().url(url).addHeader("token", token)
							.post(requestBody_array[i]).build();
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
								CommonUtil.showToast(GetApplyActivity.this, "网络有点卡...");
								return;
							}
							String responseString = "";
							try {
								responseString = response.body().string();
							} catch (Exception e) {
								// TODO: handle exception
							}
							if (responseString.contains(CommonUtil.requestSuccess)) {
								new TickConfrimShow(GetApplyActivity.this).showTick("处理成功");
								new Handler(getMainLooper()).postDelayed(new Runnable() {

									@Override
									public void run() {
										// TODO 自动生成的方法存根
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
					CommonUtil.showToast(GetApplyActivity.this, "没有网络");
					// 一定要return，否则后面的可能会执行
					return;
				}
				if (response.isSuccessful()) {
					try {// 因为从response到string的转化可能抛出异常
						responseString = response.body().string();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						CommonUtil.showToast(GetApplyActivity.this, "response不可以转换为字符串");
						return;
					}
					String code_701 = "\"code\":701";
					String code_601 = "\"code\":601";
					if (responseString.contains(code_701)) {
						CommonUtil.showToast(GetApplyActivity.this, "暂时没有违规信息");
						return;
					} else if (responseString.contains(code_601)) {
						CommonUtil.showToast(GetApplyActivity.this, JsonCode.getValue(responseString, "$.message"));
						new Handler(getMainLooper()).postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO 自动生成的方法存根
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
							datas[i] = data.getSno() + "      " + "申请关系：  " + data.getRelation();
							all_pass_id[i++] = data.getId();
						}
						// CommonUtil.showToast(GetApplyActivity.this, concreteDatas.size() + "");
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO 自动生成的方法存根
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(GetApplyActivity.this,
										android.R.layout.simple_list_item_1, datas);
								lv.setAdapter(adapter);
								lv.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO 自动生成的方法存根
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
