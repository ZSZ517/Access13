package com.example.access1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ManageObjectActivity extends HaveToolBarActivity {
	private Button button;
	private TickConfrimShow tickConfrimShow;
	private TextView tv;
	private Spinner spinner;
	private TextView textView;
	private Intent intent;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_manage_object);
		getSupportActionBar().setTitle("管理对象");
		initView();
		tv.setText(getSharedPreferences("loginManager", 0).getString("groupId", ""));
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.groupId,
				android.R.layout.simple_spinner_item);
		// 下拉菜单的样式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		tickConfrimShow = new TickConfrimShow(this);

	}

	@SuppressLint("InflateParams")
	private void initView() {
		button = (Button) findViewById(R.id.manage_object_btn);
		tv = f(R.id.manage_object_tv);
		spinner = f(R.id.spinner);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String string = spinner.getSelectedItem().toString();
				getSharedPreferences("loginManager", 0).edit().putString("groupId", string).commit();
				tv.setText(string.substring(1) + "栋");
				View view=LayoutInflater.from(ManageObjectActivity.this).inflate(R.layout.other_setting_fragment, null);
				textView=(TextView)view.findViewById(R.id.portrait_tv);
				textView.setText("管理公寓  "+string.substring(1)+"栋");
				tickConfrimShow.showTick();
				
				intent=new Intent();
				intent.putExtra("groupId", string);
				setResult(RESULT_OK, intent);
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						setResult(RESULT_OK, intent);
						finish();
					}
				}, 700);
			}
		});
	}

}
