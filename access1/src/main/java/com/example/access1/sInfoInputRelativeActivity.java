//fragments���µ���ʵ��Responce�������⡣
package com.example.access1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.example.access1.util.CommonUtil;
import com.example.access1.util.SaveBitmap;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import cn.miludeer.jsoncode.JsonCode;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class sInfoInputRelativeActivity extends HaveToolBarActivity implements OnClickListener {
	private EditText et1, et2;
	private ImageView imageView;
	private Spinner spinner1;
	private ActionBar bar;
	private ShowWaitingProgress showWaitingProgress;
	private Button button;
	private int RESULT_LOAD_IMAGE = 1;
	private Uri selectedImageUri;
	private int RESULT_CAMERA_IMAGE = 2;
	public String groupId;
	public String phone;
	File file;
	byte[] byte_array;
	private String responseContent;
	private String token_str;
	private String path;
	private String urlParams;
	private String picturePath;
	private RadioButton radio0, radio1;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.info_input_page_rel_stu);
		initView();
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.relation,
				android.R.layout.simple_spinner_item);
		// �����˵�����ʽ
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter);
		// �����˵�����ʽ
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bar = getSupportActionBar();
		bar.setTitle("������Ϣ¼��");
		imageView.setOnClickListener(this);
		button.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == RESULT_LOAD_IMAGE && null != data) {// dataΪʲô����Ϊnull
				selectedImageUri = data.getData();
				// crossFadeĬ��300ms
				// Glide.with(this).load(selectedImage).dontAnimate().diskCacheStrategy(
				// DiskCacheStrategy.NONE ).into(imageView);
				imageView.setImageURI(selectedImageUri);
				// ����Ϊͨ��uri��ȡ��ͼƬ·��
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
				cursor.moveToFirst();

				// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				Bitmap bitmap2 = BitmapFactory.decodeStream(bais);
				picturePath = new SaveBitmap(this).saveImageToGallery(bitmap2);
				// picturePath = cursor.getString(columnIndex);
				// // upload(picturePath);
				// cursor.close();
			} else if (requestCode == RESULT_CAMERA_IMAGE) {
				byte_array = data.getByteArrayExtra("byte_array");
				Bitmap bitmap = BitmapFactory.decodeByteArray(byte_array, 0, byte_array.length);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				Bitmap bitmap2 = BitmapFactory.decodeStream(bais);

				imageView.setImageBitmap(bitmap);
				picturePath = new SaveBitmap(this).saveImageToGallery(bitmap2);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		if (v.getId() == R.id.info_input_iv) {
			showPopueWindow();
		} else if (v.getId() == R.id.info_input_btn) {
			token_str = getSharedPreferences("loginStudent", 0).getString("token", "noToken");
			// sendRuquestWithHttpURLConn();
			path = CommonUtil.url + "relative/add?";
			sendRequestWithOkHttp();

		}
	}

	private void sendRequestWithOkHttp() {

		if (!TextUtils.isEmpty(et1.getText().toString()) && !TextUtils.isEmpty(et2.getText().toString())
				&& spinner1.getSelectedItem() != null && !TextUtils.isEmpty(picturePath)
				&& (radio0.isChecked() || radio1.isChecked())) {
			urlParams = "relativeName=" + et1.getText().toString() + "&phone=" + et2.getText().toString() + "&relation="
					+ spinner1.getSelectedItem().toString(); 
			file = new File(picturePath);
			if (radio0.isChecked()) {
				urlParams += "&sex=��";
			} else {
				urlParams += "&sex=Ů";
			}
			path += urlParams;
		} else {
			CommonUtil.showToast(this, "����д��������Ϣ��ѡ��ͼƬ");
			return;
		}
		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(4, TimeUnit.SECONDS).build();
		RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
		MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
		multipartBody.addFormDataPart("image", file.getName(), body);
		Request request = new Request.Builder().url(path).header("token", token_str).post(multipartBody.build())
				.build();
		final Call call = client.newCall(request);
		showWaitingProgress = new ShowWaitingProgress();
		showWaitingProgress.show(this, "¼����");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				Response response = null;

				try {
					response = call.execute();
					showWaitingProgress.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
					showWaitingProgress.dismiss();
					CommonUtil.showToast(sInfoInputRelativeActivity.this, "���ӷ�����ʧ��");
					return;
				}
				try {
					responseContent = response.body().string();
				} catch (Exception e) {
					// TODO: handle exception
					CommonUtil.showToast(sInfoInputRelativeActivity.this, "response����ת��Ϊ�ַ���");
					return;
				}
				if (responseContent.contains(CommonUtil.requestSuccess)) {// ����ɹ�
					new TickConfrimShow(sInfoInputRelativeActivity.this).showTick("¼��ɹ�");
					// if (isLocal == false) {
					file.delete();
					// }
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO �Զ����ɵķ������
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									// TODO �Զ����ɵķ������
									sInfoInputRelativeActivity.this.finish();
								}
							}, 600);
							return;
						}
					});

				} else if (responseContent.contains("\"code\":400")) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO �Զ����ɵķ������
							CommonUtil.showToast(sInfoInputRelativeActivity.this,
									JsonCode.getValue(responseContent, "$.message"));
							return;
						}
					});
				} else {
					CommonUtil.showToast(sInfoInputRelativeActivity.this,
							JsonCode.getValue(responseContent, "$.message"));
				}
			}
		}).start();
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
				Intent i = new Intent(sInfoInputRelativeActivity.this, s2RecognizeTest.class);
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

	private void initView() {
		et1 = f(R.id.info_input_et1);
		et2 = f(R.id.info_input_et2);
		f(R.id.info_input_et3);
		spinner1 = f(R.id.spinner1);
		f(R.id.info_input_title_tv);
		imageView = f(R.id.info_input_iv);
		button = f(R.id.info_input_btn);
		radio0 = f(R.id.radio0);
		radio1 = f(R.id.radio1);
	}

}
