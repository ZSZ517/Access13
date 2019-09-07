package com.example.access1;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.example.access1.bean.RecognizeResultBean;
import com.example.access1.bean.RecognizeResultBean.ConcreteData;
import com.example.access1.util.CommonUtil;
import com.google.gson.Gson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.miludeer.jsoncode.JsonCode;
import net.sourceforge.pinyin4j.PinyinHelper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecognizeResultActivity extends HaveToolBarActivity {
	private String responseContent;
	private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv0;
	private ImageView result_iv;
	private String relation;
	private TextView result_tv;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_recognize_result);
		getSupportActionBar().setTitle("ʶ����");
		responseContent = getIntent().getStringExtra("responseContent");
		RecognizeResultBean recognizeResultBean = new Gson().fromJson(responseContent, RecognizeResultBean.class);
		ConcreteData data = recognizeResultBean.getData();
		initView();
		if (!TextUtils.isEmpty(data.getRelation())) {
			relation = data.getRelation();
		}
		sendRequest();
		if (TextUtils.isEmpty(data.getRelativeName())) {
			tv0.setVisibility(View.GONE);
		} else {
			tv0.setText("��������:  " + data.getRelativeName());
		}
		tv1.setText("����:  " + data.getStudentName());
		tv2.setText("ѧ��:  " + data.getSno());
		tv3.setText("��Ԣ:  " + data.getGroupId().substring(1) + "��");
		tv4.setText("�ֻ�:  " + data.getPhone());
		tv5.setText("ƥ���:  " + data.getScore() + "");
		tv6.setText("Ժϵ:  " + data.getDept());
		tv7.setText("רҵ:  " + data.getMajor());
		tv8.setText("�Ա�:  " + data.getSex());
		tv9.setText("��ѧʱ��:  " + data.getSchoolTime());
		if (data.getRelation()!=null) {
			result_tv.setText("ʶ������ ����");
			tv10.setText("��ϵ:  " + data.getRelation());
		}else {
			result_tv.setText("ʶ������ ѧ��");
			tv10.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(data.getBadTime())) {
			tv12.setVisibility(View.GONE);
		} else {
			tv12.setText("���ʱ��:  " + CommonUtil.stampToTime(data.getBadTime()));
		}
		if (TextUtils.isEmpty(data.getVisitTime())) {
			tv11.setVisibility(View.GONE);
		} else {
			tv11.setText("�ݷ�ʱ��:  " + CommonUtil.stampToTime(data.getVisitTime()));
		}

		if (TextUtils.isEmpty(data.getSchoolTime())) {
			tv9.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(data.getDept())) {
			tv6.setVisibility(View.GONE);
		}

	}

	private void initView() {
		tv1 = f(R.id.tv1);
		tv2 = f(R.id.tv2);
		tv3 = f(R.id.tv3);
		tv4 = f(R.id.tv4);
		tv5 = f(R.id.tv5);
		tv6 = f(R.id.tv6);
		tv7 = f(R.id.tv7);
		tv8 = f(R.id.tv8);
		tv9 = f(R.id.tv9);
		tv10 = f(R.id.tv10);
		tv11 = f(R.id.tv11);
		tv12 = f(R.id.tv12);
		tv0 = f(R.id.tv0);
		result_iv = f(R.id.result_iv);
		result_tv=f(R.id.result_tv);
	}

	private void sendRequest() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();
		String url = CommonUtil.url + "getImage?sno=" + JsonCode.getValue(responseContent, "$.data.sno");
		if (!TextUtils.isEmpty(relation)) {
			url += getPinYinHeadChar(relation);
		}
		Request request = new Request.Builder().url(url).build();
		final Call call = okHttpClient.newCall(request);
		new Thread(new Runnable() {
			Response response = null;

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				try {
					response = call.execute();

				} catch (Exception e) {
					// TODO: handle exception
					CommonUtil.showToast(RecognizeResultActivity.this, "���ӷ�����ʧ�ܣ����ܻ�ȡ��Ƭ");
				}
				try {
					InputStream is = response.body().byteStream();
					final Bitmap bitmap = BitmapFactory.decodeStream(is);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO �Զ����ɵķ������
							result_iv.setImageBitmap(bitmap);

						}
					});
				} catch (Exception e) {
					// TODO: handle exception
					CommonUtil.showToast(RecognizeResultActivity.this, JsonCode.getValue(responseContent, "$.message"));
				}
			}
		}).start();
	}

	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert.toUpperCase();
	}
}
