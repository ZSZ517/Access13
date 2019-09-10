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
		getSupportActionBar().setTitle("�������");
		imageView = f(R.id.feedback_iv);
		button=f(R.id.commit_feedback_btn);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new TickConfirmShow(sFeedBackActivity.this).showTick("�ύ�ɹ�");
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
			if (requestCode == RESULT_LOAD_IMAGE && null != data) {// dataΪʲô����Ϊnull
				Uri selectedImage = data.getData();
				// crossFadeĬ��300ms
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

	// ���imageViewʱչʾ����
	private void showPopueWindow() {
		View popView = View.inflate(this, R.layout.popup_choose_photo, null);
		Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
		Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
		Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
		// ��ȡ��Ļ���
		int weight = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels * 1 / 3;

		final PopupWindow popupWindow = new PopupWindow(popView, weight, height);
		popupWindow.setAnimationStyle(R.style.choose_photo_pop_anim);

		// �õ����㣬ʹ�²����ʧȥ���㲢���ɵ���۽���
		popupWindow.setFocusable(true);
		// popupWindow.setOutsideTouchable(true);ʧЧ�ˣ���ӿձ������
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
		// popupWindow��ʧ��Ļ��Ϊ��͸��
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// popupWindow������Ļ��Ϊ��͸��
		WindowManager.LayoutParams lp = getWindow().getAttributes();// ����ԭ���Ĳ�����ֻ�޸�alpha
		lp.alpha = 0.5f;
		getWindow().setAttributes(lp);
		popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);

	}
}
