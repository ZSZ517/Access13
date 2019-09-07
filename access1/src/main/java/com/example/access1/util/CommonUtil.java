package com.example.access1.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class CommonUtil {
	public static String requestSuccess = "\"code\":200";
	public static String url = "http://47.100.209.59:8080/rlsb/";

	public static void showToast(final Context context, final String string) {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
				toast.setText(string);// + " ��ӡ������" + context.getClass().toString().substring(s.lastIndexOf(".") + 1)
				toast.show();
			}
		});

	}

	public static String stampToTime(String s) {
		String res = "";
		if (!TextUtils.isEmpty(s)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long lt = Long.valueOf(s);// ��ʱ���ת��Ϊʱ��
			Date date = new Date(lt);// ��ʱ�����Ϊyyyy-MM-dd HH:mm:ssʱ����ʽ
			res = simpleDateFormat.format(date);
		}
		return res;
	}
}
