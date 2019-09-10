package com.example.access1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.access1.R;


/**
 *Description: 管理端录入信息类（会在这里启动录入教师信息类）
 */
public class InformationInputActivity extends HaveToolBarActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_input_layout);
        Toolbar toolbar = f(R.id.toolbar_base);
        toolbar.setTitle("信息录入");
        Button btn = f(R.id.teacher_information_btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(InformationInputActivity.this, InfoInputTeacherActivity.class);
        startActivity(intent);

    }
}
