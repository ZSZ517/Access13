package com.example.access1.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.access1.R;
import com.example.access1.util.CommonUtil;
import com.example.access1.util.TickConfirmShow;

/**
 *Description: ����˸ı�������
 */
public class ChangePwdActivity extends HaveToolBarActivity {
    private EditText et2, et3;
    private TickConfirmShow tickConfirmShow;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_change_pwd);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("�޸�����");
        Button btn1 = f(R.id.change_pwd_btn1);
        Button btn2 = f(R.id.change_pwd_btn2);
        f(R.id.change_pwd_et1);
        et2 = f(R.id.change_pwd_et2);
        et3 = f(R.id.change_pwd_et3);
        tickConfirmShow = new TickConfirmShow(this);
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et2.getText().toString().equals(et3.getText().toString())) {
                    tickConfirmShow.showTick("�޸ĳɹ�");
                } else {
                    CommonUtil.showToast(ChangePwdActivity.this, "�ܴa��һ��");
                }
            }
        });
        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
