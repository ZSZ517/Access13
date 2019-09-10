package com.example.access1.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.access1.R;
import com.example.access1.bean.MyBrokenBean;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

/**
 *Description: 管理端获取某一学生具体违规日志类
 */
public class BrokenLogDetailActivity extends HaveToolBarActivity {

    private TextView name_tv, sno_tv, time_tv, type_tv, dorm_tv;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.broken_record_detail_stu);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("违规详情");
        initView();
        String string = getIntent().getStringExtra("datas");
        MyBrokenBean.ConcreteData data = new Gson().fromJson(string, MyBrokenBean.ConcreteData.class);
        name_tv.setText(data.getStudentName());
        sno_tv.setText(data.getSno());
        dorm_tv.setText(data.getGroup_id());
        time_tv.setText(CommonUtil.stampToTime(data.getBadTime()));
        type_tv.setText(String.valueOf(data.getType()));

    }

    private void initView() {
        name_tv = f(R.id.name_tv);
        sno_tv = f(R.id.sno_tv);
        time_tv = f(R.id.time_tv);
        type_tv = f(R.id.type_tv);
        dorm_tv = f(R.id.dorm_tv);
    }

}
