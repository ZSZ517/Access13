package com.example.access1;

import com.example.access1.util.CommonUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChangePwdActivity extends HaveToolBarActivity {
	private Button btn1, btn2;
	private EditText et2, et3;
	private TickConfrimShow tickConfrimShow;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_change_pwd);
		getSupportActionBar().setTitle("�޸�����");
		btn1=f(R.id.change_pwd_btn1);
		btn2=f(R.id.change_pwd_btn2);
		f(R.id.change_pwd_et1);
		et2=f(R.id.change_pwd_et2);
		et3=f(R.id.change_pwd_et3);
		tickConfrimShow=new TickConfrimShow(this);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				if(et2.getText().toString().equals(et3.getText().toString())) {
					tickConfrimShow.showTick("�޸ĳɹ�");
				}else {
					CommonUtil.showToast(ChangePwdActivity.this, "�ܴa��һ��");
				}
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}
		});
	}
}
