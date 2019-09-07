package com.example.access1;

import com.example.access1.bean.VisitBean;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

import android.os.Bundle;
import android.widget.TextView;

public class VisitLogDetailActivity extends HaveToolBarActivity {
	private TextView groupId_tv, relation_tv, relativeName_tv, sno_tv, studentName_tv, type_tv, visitTime_tv;
	private VisitBean.ConcreteData data;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_visit_log_detail);
		getSupportActionBar().setTitle("日志详情");
		data = new Gson().fromJson(getIntent().getStringExtra("datas"), VisitBean.ConcreteData.class);// 不可以在成员变量里面赋值
		initView();
		initData();

	}

	private void initData() {
		groupId_tv.setText(data.getGroupId());
		relation_tv.setText(data.getRelation());
		relativeName_tv.setText(data.getRelativeName());
		sno_tv.setText(data.getSno());
		studentName_tv.setText(data.getStudentName());
		type_tv.setText(data.getType() + "");
		visitTime_tv.setText(CommonUtil.stampToTime(data.getVisitTime()));
	}

	private void initView() {
		groupId_tv = f(R.id.groupId_tv);
		relation_tv = f(R.id.relation_tv);
		relativeName_tv = f(R.id.relativeName_tv);
		sno_tv = f(R.id.sno_tv);
		studentName_tv = f(R.id.studentName_tv);
		type_tv = f(R.id.type_tv);
		visitTime_tv = f(R.id.visitTime_tv);
	}
}
