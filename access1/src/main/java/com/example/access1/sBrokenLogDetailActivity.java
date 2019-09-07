package com.example.access1;

import com.example.access1.bean.MyBrokenBean;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

import android.os.Bundle;
import android.widget.TextView;

public class sBrokenLogDetailActivity extends HaveToolBarActivity {
	private TextView name_tv, sno_tv, time_tv, type_tv, dorm_tv;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.broken_record_detail_stu);
		getSupportActionBar().setTitle("违规详情");
		initView();
		String string = getIntent().getStringExtra("datas");
		MyBrokenBean.ConcreteData data = new Gson().fromJson(string, MyBrokenBean.ConcreteData.class);
		name_tv.setText(data.getStudentName());
		sno_tv.setText(data.getSno());
		dorm_tv.setText(data.getGroup_id());
		try {
			time_tv.setText(CommonUtil.stampToTime(data.getBadTime()));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		type_tv.setText(data.getType() + "");

	}

	private void initView() {
		name_tv = f(R.id.name_tv);
		sno_tv = f(R.id.sno_tv);
		time_tv = f(R.id.time_tv);
		type_tv = f(R.id.type_tv);
		dorm_tv = f(R.id.dorm_tv);
	}
}
