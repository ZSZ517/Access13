package com.example.access1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.access1.R;
import com.example.access1.bean.MyBrokenBean;
import com.example.access1.bean.MyBrokenBean.ConcreteData;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import cn.miludeer.jsoncode.JsonCode;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Description: 管理端获取违规日志列表类
 */
public class BrokenLogActivity extends HaveToolBarActivity {
    private String token;
    private ListView lv;
    private String[] datas;
    private String[] concreteDatas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broken_record);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("违规日志");
        SharedPreferences sp = getSharedPreferences("loginManager", 0);
        token = sp.getString("token", "noToken");
        forInfo();
        lv = f(R.id.lv);
    }

    // 向服务器发出请求
    private void forInfo() {
        String url = CommonUtil.url + "getBad?pn=1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("token", token) // 默认为GET请求，可以不写
                .build();
        final Call call = client.newCall(request);
        // 通过 call.execute() 方法来提交同步请求，这种方式会阻塞线程，而为了避免 ANR 异常，Android3.0
        // 之后已经不允许在主线程中访问网络了
        // 所以 OkHttp 的同步 get 请求需要开启一个子线程：

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;

                try {
                    response = call.execute();
                } catch (Exception e) {

                    CommonUtil.showToast(BrokenLogActivity.this, "网络有点卡");
                    return;
                }
                try {
                    if (response.isSuccessful()) {
                        String responseString = response.body().string();
                        String code_601 = "\"code\":601";
                        //token过期，需要重新登陆
                        if (responseString.contains(code_601)) {
                            CommonUtil.showToast(BrokenLogActivity.this,
                                    JsonCode.getValue(responseString, "$.message"));
                            new Handler(getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent(BrokenLogActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }, 1500);
                        } else if (responseString.contains(CommonUtil.requestSuccess)) {
                            MyBrokenBean myBrokenBean = new Gson().fromJson(responseString, MyBrokenBean.class);
                            List<ConcreteData> concreteData_list = myBrokenBean.getData();
                            int size = concreteData_list.size();
                            datas = new String[size];
                            concreteDatas = new String[size];
                            int i = 0;
                            for (ConcreteData concreteData : concreteData_list) {
                                concreteDatas[i] = new Gson().toJson(concreteData);
                                datas[i++] = concreteData.getSno() + "   "
                                        + CommonUtil.stampToTime(concreteData.getBadTime());
                            }
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BrokenLogActivity.this,
                                            android.R.layout.simple_list_item_1, datas);
                                    lv.setAdapter(adapter);
                                    lv.setOnItemClickListener(new OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                long id) {

                                            Intent intent = new Intent(BrokenLogActivity.this,
                                                    BrokenLogDetailActivity.class);
                                            intent.putExtra("datas", concreteDatas[position]);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}