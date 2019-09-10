package com.example.access1.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.example.access1.R;

 /**
  *Description:带toolbar的activity基类
  */
public class HaveToolBarActivity extends BaseActivity {

     @Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}
	@Override
	public void setContentView(int resId) {
	    //基于禁用系统自带的toolbar.
		View view = View.inflate(this, R.layout.have_toolbar_layout, null);
        FrameLayout content_frame = (FrameLayout) view.findViewById(R.id.content_frame);
		content_frame.addView(View.inflate(this, resId, null));
		super.setContentView(view);
        Toolbar toolbar = f(R.id.toolbar_base);
		setSupportActionBar(toolbar);
		if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 左侧添加一个默认的返回图标
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();// 返回
                }
            });
        }
	}

}
