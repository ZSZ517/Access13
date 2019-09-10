package com.example.access1.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.access1.activity.BrokenLogActivity;
import com.example.access1.activity.ChangePwdActivity;
import com.example.access1.activity.FeedBackActivity;
import com.example.access1.activity.GetApplyActivity;
import com.example.access1.activity.InformationInputActivity;
import com.example.access1.activity.LoginActivity;
import com.example.access1.activity.ManageObjectActivity;
import com.example.access1.R;
import com.example.access1.bean.RotateBean;
import com.example.access1.adapter.RotateVpAdapter;
import com.example.access1.activity.GetVisitLogActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 *Description: �Զ���fragment�࣬ʵ���ϻ���ݴ������Ĳ������ز�ͬ�Ĳ��֣�������ͬ��fragment
 */
@SuppressLint({"NewApi", "ValidFragment", "ClickableViewAccessibility"})
public class MyFragment extends Fragment implements OnClickListener {

    public static final String TYPE = "type";
    private String contentName;
    private TextView portrait_tv;
    private ImageView portrait_iv;

    // ����
    private static final int TIME = 3000;
    private ViewPager rotateViewPager;
    private LinearLayout pointLl;// �ֲ�״̬�ı��СԲ������
    private List<RotateBean> datas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MyFragment(String contentName) {
        this();
        this.contentName = contentName;
    }

    public MyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (Objects.equals(contentName, "��ҳ")) {

            view = inflater.inflate(R.layout.home_page_fragment, container, false);

            // ��������ֲ�
            rotateViewPager = (ViewPager) view.findViewById(R.id.rotate_vp);
            pointLl = (LinearLayout) view.findViewById(R.id.rotate_point_container);

            buildDatas();// ��������
            RotateVpAdapter vpAdapter = new RotateVpAdapter(getContext());
            vpAdapter.setDatas(datas);
            rotateViewPager.setAdapter(vpAdapter);
            // ViewPager��ҳ��Ϊint���ֵ,���õ�ǰҳ��һЩ,������������ǰ����
            // Ϊ�˱�֤��һҳʼ��Ϊ���ݵĵ�0�� ȡ��ҪΪ0,����������ݼ��ϴ�С�ı���
            rotateViewPager.setCurrentItem(datas.size());

            // ��ʼ�ֲ�
            handler = new Handler();
            startRotate();
            // ����ֲ�С��
            addPoints();
            // �����ֲ��ı�С��
            changePoints();

            initView(view);
            return view;
        }
        if (Objects.equals(contentName, "����")) {
            view = inflater.inflate(R.layout.other_setting_fragment, container, false);
            initView(view);
            return view;
        }
        view = inflater.inflate(R.layout.home_page_fragment, container, false);
        initView(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        if (Objects.equals(contentName, "��ҳ")) {
            LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_1_left);
            LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_1_right);
            LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_2_left);
            LinearLayout linearLayout4 = (LinearLayout) view.findViewById(R.id.mainPage_ll2_2_right);
            linearLayout1.setOnClickListener(this);
            linearLayout2.setOnClickListener(this);
            linearLayout3.setOnClickListener(this);
            linearLayout4.setOnClickListener(this);
        } else if (Objects.equals(contentName, "����")) {
            Button btn1 = (Button) view.findViewById(R.id.other_setting_btn1);
            Button btn2 = (Button) view.findViewById(R.id.other_setting_btn2);
            Button btn4 = (Button) view.findViewById(R.id.other_setting_btn4);
            Button btn5 = (Button) view.findViewById(R.id.other_setting_btn5);
            portrait_iv = (ImageView) view.findViewById(R.id.portrait_iv);
            portrait_tv = (TextView) view.findViewById(R.id.portrait_tv);
            portrait_tv.setText("����Ԣ   " + getContext().getSharedPreferences("loginManager", 0).getString("groupId", "13")+ "��");

            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);
            btn4.setOnClickListener(this);
            btn5.setOnClickListener(this);

        }

        // textViewInFragment = (TextView) view.findViewById(R.id.textView_content);
        // textViewInFragment.setText(getArguments().getString(TYPE, "Default"));
    }

    public static MyFragment newInstance(String contentName) {
        MyFragment myFragment = new MyFragment(contentName);
        Bundle bundle = new Bundle();
        // ����ֻ��Ϊ��initView�е�ȡ������
        bundle.putString(TYPE, contentName);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.other_setting_btn1:
                Intent intent1 = new Intent(getContext(), ManageObjectActivity.class);
                getActivity().startActivityForResult(intent1, 3);

                break;
            case R.id.other_setting_btn2:
                Intent intent2 = new Intent(getContext(), ChangePwdActivity.class);
                startActivity(intent2);

                break;
            case R.id.other_setting_btn4:
                Intent intent3 = new Intent(getContext(), FeedBackActivity.class);
                startActivity(intent3);

                break;
            case R.id.other_setting_btn5:
                SharedPreferences sp = getContext().getSharedPreferences("loginManager", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", "");
                editor.apply();
                Intent intent4 = new Intent(getContext(), LoginActivity.class);
                startActivity(intent4);
                break;

            case R.id.mainPage_ll2_1_left:
                Intent intent5 = new Intent(getContext(), GetApplyActivity.class);
                startActivity(intent5);
                break;
            case R.id.mainPage_ll2_1_right:
                Intent intent6 = new Intent(getContext(), GetVisitLogActivity.class);
                startActivity(intent6);
                break;
            case R.id.mainPage_ll2_2_left:
                Intent intent7 = new Intent(getContext(), BrokenLogActivity.class);
                startActivity(intent7);
                break;
            case R.id.mainPage_ll2_2_right:
                Intent intent8 = new Intent(getContext(), InformationInputActivity.class);
                startActivity(intent8);
                break;
        }
    }

    // ��������ֲ�
    private void buildDatas() {
        datas = new ArrayList<>();
        datas.add(new RotateBean(R.drawable.aa));
        datas.add(new RotateBean(R.drawable.bb));
        datas.add(new RotateBean(R.drawable.cc));
    }

    private void changePoints() {
        rotateViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // ������ֹ֮ͣǰ��postDelayed������postDelayedˢ�¡�
                handler.removeCallbacks(rotateRunnable);
                handler.postDelayed(rotateRunnable, 3000);
            }

            @Override
            public void onPageSelected(int position) {
                if (isRotate) {
                    // ������С������Ϊ��ɫ
                    for (int i = 0; i < datas.size(); i++) {
                        ImageView pointIv = (ImageView) pointLl.getChildAt(i);
                        pointIv.setImageResource(R.drawable.point_white);
                    }
                    // ���õ�ǰλ��С��Ϊ��ɫ
                    ImageView iv = (ImageView) pointLl.getChildAt(position % datas.size());
                    iv.setImageResource(R.drawable.point_grey);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * ����ֲ��л�С��
     */
    private void addPoints() {
        // �ж�����ͼ���ض��ٸ�С��
        for (int i = 0; i < datas.size(); i++) {
            ImageView pointIv = new ImageView(getContext());
            pointIv.setPadding(5, 5, 5, 5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
            pointIv.setLayoutParams(params);

            // ���õ�0ҳС���Ϊ��ɫ
            if (i == 0) {
                pointIv.setImageResource(R.drawable.point_grey);
            } else {
                pointIv.setImageResource(R.drawable.point_white);
            }
            pointLl.addView(pointIv);
        }
    }

    public Handler handler;
    public boolean isRotate = false;
    private Runnable rotateRunnable;
    public boolean flag_stop = false;

    /**
     * ��ʼ�ֲ�
     */
    public void startRotate() {
        rotateRunnable = new Runnable() {
            @Override
            public void run() {
                int nowIndex = rotateViewPager.getCurrentItem();
                rotateViewPager.setCurrentItem(++nowIndex);
                if (isRotate) {
                    handler.postDelayed(rotateRunnable, TIME);
                } else {
                    flag_stop = true;
                }
            }
        };
        begin();

    }

    public void begin() {
        handler.postDelayed(rotateRunnable, TIME);
        flag_stop = false;
    }

    @SuppressLint("SetTextI18n")
    public void setGroupId(String groupId) {
        portrait_tv.setText("����Ԣ   " +groupId+"��");
    }

   /**
    *@param :
    *@param :
    *@param :
    *@return:
    *@Description: �÷������ڳ��и�fragment�����е���
    */
    public void setPortrait(Bitmap bitmap) {
        portrait_iv.setImageBitmap(bitmap);
    }

}
