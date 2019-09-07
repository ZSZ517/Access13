package com.example.access1.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

@SuppressLint("SimpleDateFormat")
public class SaveBitmap {
	private Context context;
	private String fileAbsolutePath;

	public SaveBitmap(Context context) {
		this.context = context;
	}

	public String saveImageToGallery(Bitmap bmp) {
		// 构造路径
		String root = Environment.getExternalStorageDirectory().getAbsolutePath();
		String dirName = "aMyPhoto";
		File savePath = new File(root, dirName);
		if (!savePath.exists()) {
			savePath.mkdir();// 创建没有的所有子目录,实际上这里只需要mkdir不用mkdirs，因为只有一个子目录
		}

		// 构造文件名
		long timeStamp = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String pictureName = sdf.format(new Date(timeStamp));
		String fileName = pictureName + ".jpg";

		//申请权限
		if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
		}
		// 构造文件
		File file = new File(savePath, fileName);
		fileAbsolutePath=file.getPath();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileAbsolutePath;
	}
}
