package com.example.access1.activity;

import com.example.access1.R;
import com.example.access1.util.CommonUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class sInformationInputActivity extends HaveToolBarActivity implements OnClickListener {
	private Toolbar toobar;
	private Button btn1, btn2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_input_layout_stu);
		toobar = f(R.id.toolbar_base);
		toobar.setTitle("信息录入");
		btn1 = f(R.id.student_information_btn);
		btn2 = f(R.id.relative_information_btn);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if(getIntent().getStringExtra("info")!=null) {
			CommonUtil.showToast(this, "管理端暂未开放录入功能");
			return;
		}
		switch (v.getId()) {
		case R.id.student_information_btn:
			Intent intent1 = new Intent(sInformationInputActivity.this, sInfoInputStudentActivity.class);
			startActivity(intent1);

			break;
		case R.id.relative_information_btn:
			Intent intent2 = new Intent(sInformationInputActivity.this, sInfoInputRelativeActivity.class);
			startActivity(intent2);
			break;
		}
	}
}
