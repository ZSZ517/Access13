package com.example.access1.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


/**
 * Description: ����
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //ֻ������
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T f(int id) {
        return (T) findViewById(id);
    }
    @SuppressWarnings("unchecked")
    public void lAAAAAA(String string) {
        Log.d("hello", string + "\t��ӡ�ࣺ" + this.getClass());
    }
}
