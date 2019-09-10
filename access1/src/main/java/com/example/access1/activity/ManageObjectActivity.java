package com.example.access1.activity;

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

import com.example.access1.R;
import com.example.access1.util.TickConfirmShow;

public class ManageObjectActivity extends HaveToolBarActivity {
    private TickConfirmShow tickConfirmShow;
    private TextView tv;
    private Spinner spinner;
    private TextView textView;
    private Intent intent;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_manage_object);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("管理对象");
        initView();
        tv.setText(getSharedPreferences("loginManager", 0).getString("groupId", "") + "栋");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.groupId,
                android.R.layout.simple_spinner_item);
        // 下拉菜单的样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        tickConfirmShow = new TickConfirmShow(this);

    }

    @SuppressLint("InflateParams")
    private void initView() {
        Button button = (Button) findViewById(R.id.manage_object_btn);
        tv = f(R.id.manage_object_tv);
        spinner = f(R.id.spinner);
        if (button != null)
            button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    String string = spinner.getSelectedItem().toString();
                    string = string.substring(0, string.length() - 1);
                    getSharedPreferences("loginManager", 0).edit().putString("groupId", string).apply();
                    tv.setText(String.valueOf(string + "栋"));
                    View view = LayoutInflater.from(ManageObjectActivity.this).inflate(R.layout.other_setting_fragment, null);
                    textView = (TextView) view.findViewById(R.id.portrait_tv);
                    textView.setText(String.valueOf("管理公寓  " + string + "栋"));
                    tickConfirmShow.showTick();

                    intent = new Intent();
                    intent.putExtra("groupId", string);
                    setResult(RESULT_OK, intent);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, 700);
                }
            });
    }

}
