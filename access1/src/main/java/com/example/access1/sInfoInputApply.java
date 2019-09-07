package com.example.access1;

import java.io.IOException;

import com.example.access1.util.CommonUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import cn.miludeer.jsoncode.JsonCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class sInfoInputApply extends HaveToolBarActivity {
	private EditText et1, et2;
	private Button button;
	private Spinner spinner;
	private String responseString;
	private ShowWaitingProgress showWaitingProgress;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_infoinput_apply);
		getSupportActionBar().setTitle("录入申请");
		initView();
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.relation,
				android.R.layout.simple_spinner_item);
		// 下拉菜单的样式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void initView() {
		et1 = f(R.id.student_apply_tv);
		et2 = f(R.id.relative_apply_tv);
		button = f(R.id.apply_btn);
		spinner = f(R.id.spinner);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String token_str = getSharedPreferences("loginStudent", 0).getString("token", "noToken");
				String reasonStu = et1.getText().toString();
				String reasonRel = et2.getText().toString();
				if (TextUtils.isEmpty(reasonRel) && TextUtils.isEmpty(reasonStu)) {
					CommonUtil.showToast(sInfoInputApply.this, "请填写申请原因");
					return;
				}
				showWaitingProgress=new ShowWaitingProgress();
				showWaitingProgress.show(sInfoInputApply.this, "申请中...");
				OkHttpClient client = new OkHttpClient();
				RequestBody body1 = new FormBody.Builder().add("reason", reasonStu).build();
				RequestBody body2 = new FormBody.Builder().add("reason", reasonRel)
						.add("relation", spinner.getSelectedItem().toString()).build();
				Request request1 = new Request.Builder().url(CommonUtil.url + "student/apply")
						.header("token", token_str).post(body1).build();
				Request request2 = new Request.Builder().url(CommonUtil.url + "relative/apply")
						.header("token", token_str).post(body2).build();

				// 申请学生
				if (!TextUtils.isEmpty(reasonStu)) {

					client.newCall(request1).enqueue(new Callback() {

						@Override
						public void onResponse(Call arg0, Response response) throws IOException {
							// TODO 自动生成的方法存根
							showWaitingProgress.dismiss();
							responseString = response.body().string();
							if (responseString.contains("message\": \"申请成功，等待辅导员审核")) {
								new TickConfrimShow(sInfoInputApply.this).showTick("申请成功\n等待审核");
							} else {
								String message=JsonCode.getValue(responseString, "$.message");
								CommonUtil.showToast(sInfoInputApply.this, message );
							}
						}

						@Override
						public void onFailure(Call arg0, IOException arg1) {
							showWaitingProgress.dismiss();
							CommonUtil.showToast(sInfoInputApply.this, "网络有点卡");
						}
					});
				}

				// 申请家属
				if (!TextUtils.isEmpty(reasonRel)) {
					client.newCall(request2).enqueue(new Callback() {

						@Override
						public void onResponse(Call arg0, Response response) throws IOException {
							// TODO 自动生成的方法存根
							showWaitingProgress.dismiss();
							responseString = response.body().string();
							if (responseString.contains("message\": \"申请成功，等待辅导员审核")) {
								new TickConfrimShow(sInfoInputApply.this).showTick("申请成功\n等待审核");
							} else {
								String message=JsonCode.getValue(responseString, "$.message");
								CommonUtil.showToast(sInfoInputApply.this, message );
							}
						}

						@Override
						public void onFailure(Call arg0, IOException arg1) {
							// TODO 自动生成的方法存根
							showWaitingProgress.dismiss();
							CommonUtil.showToast(sInfoInputApply.this, "网络有点卡");
						}
					});
				}
			}
		});
	}
}
