package com.example.access1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.access1.R;
import com.example.access1.util.TickConfirmShow;

public class sFeedBackActivity extends HaveToolBarActivity {
	private int RESULT_LOAD_IMAGE = 1;
	private int RESULT_CAMERA_IMAGE=2;
	private ImageView imageView;
	private Button button;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_feedback);
		getSupportActionBar().setTitle("意见反馈");
		imageView = f(R.id.feedback_iv);
		button=f(R.id.commit_feedback_btn);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new TickConfirmShow(sFeedBackActivity.this).showTick("提交成功");
			}
		});
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showPopueWindow();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == RESULT_LOAD_IMAGE && null != data) {// data为什么不能为null
				Uri selectedImage = data.getData();
				// crossFade默认300ms
				// Glide.with(this).load(selectedImage).dontAnimate().diskCacheStrategy(
				// DiskCacheStrategy.NONE ).into(imageView);
				imageView.setImageURI(selectedImage);
			}else if (requestCode == RESULT_CAMERA_IMAGE) {
				byte[] bytes = data.getByteArrayExtra("bitmap");
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	// 点击imageView时展示弹窗
	private void showPopueWindow() {
		View popView = View.inflate(this, R.layout.popup_choose_photo, null);
		Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
		Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
		Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
		// 获取屏幕宽高
		int weight = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels * 1 / 3;

		final PopupWindow popupWindow = new PopupWindow(popView, weight, height);
		popupWindow.setAnimationStyle(R.style.choose_photo_pop_anim);

		// 得到焦点，使下层界面失去焦点并不可点击聚焦。
		popupWindow.setFocusable(true);
		// popupWindow.setOutsideTouchable(true);失效了，添加空背景解决
		popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
		bt_album.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				i.setType("image/*");
				startActivityForResult(i, RESULT_LOAD_IMAGE);
				popupWindow.dismiss();

			}
		});
		bt_camera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(sFeedBackActivity.this, sRecognizeTestActivity.class);
				i.putExtra("forWhat", "giveMeBitmap");
				startActivityForResult(i, RESULT_CAMERA_IMAGE);
				popupWindow.dismiss();

			}
		});
		bt_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();

			}
		});
		// popupWindow消失屏幕变为不透明
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// popupWindow出现屏幕变为半透明
		WindowManager.LayoutParams lp = getWindow().getAttributes();// 引用原来的参数，只修改alpha
		lp.alpha = 0.5f;
		getWindow().setAttributes(lp);
		popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);

	}
}
