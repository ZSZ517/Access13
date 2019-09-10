package com.example.access1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.access1.R;


/**
 *Description: �����¼����Ϣ�ࣨ������������¼���ʦ��Ϣ�ࣩ
 */
public class InformationInputActivity extends HaveToolBarActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_input_layout);
        Toolbar toolbar = f(R.id.toolbar_base);
        toolbar.setTitle("��Ϣ¼��");
        Button btn = f(R.id.teacher_information_btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(InformationInputActivity.this, InfoInputTeacherActivity.class);
        startActivity(intent);

    }
}
