package com.example.access1.util;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

//本类用于获取当前窗口的宽高像素点
public class CommonSize {				
	public static int getScreenWidth(Context context ) {
		WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics=new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}
	public static int getScreenHeight(Context context) {
		WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics=new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}
	public static float getDensity(Context context) {
		WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics=new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.density;
	}

}
