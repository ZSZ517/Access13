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
				// TODO 自动生成的方法存根
				Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
				toast.setText(string);// + " 打印类名：" + context.getClass().toString().substring(s.lastIndexOf(".") + 1)
				toast.show();
			}
		});

	}

	public static String stampToTime(String s) {
		String res = "";
		if (!TextUtils.isEmpty(s)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long lt = Long.valueOf(s);// 将时间戳转换为时间
			Date date = new Date(lt);// 将时间调整为yyyy-MM-dd HH:mm:ss时间样式
			res = simpleDateFormat.format(date);
		}
		return res;
	}
}
