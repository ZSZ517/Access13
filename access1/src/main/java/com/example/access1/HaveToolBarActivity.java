package com.example.access1;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

public class HaveToolBarActivity extends BaseActivity {
	private FrameLayout content_frame;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	@Override
	public void setContentView(int resId) {
		View view = View.inflate(this, R.layout.have_toolbar_layout, null);
		content_frame = (FrameLayout) view.findViewById(R.id.content_frame);
		content_frame.addView(View.inflate(this, resId, null));
		super.setContentView(view);
		toolbar = f(R.id.toolbar_base);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);// ������һ��Ĭ�ϵķ���ͼ��
		getSupportActionBar().setHomeButtonEnabled(true); // ���÷��ؼ�����
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();// ����
			}
		});
	}

}
