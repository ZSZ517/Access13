package com.example.access1;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class BaseActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		lAAAAAA(this.getClass()+"\n");
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T f(int id) {
		return (T) findViewById(id);
	}

	public void lAAAAAA(String string) {
		Log.d("hello", string+"\t¥Ú”°¿‡£∫"+this.getClass());
	}
}
