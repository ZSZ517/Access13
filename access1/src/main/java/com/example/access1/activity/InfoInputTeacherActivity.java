package com.example.access1.activity;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.access1.R;
import com.example.access1.util.CommonSize;
import com.example.access1.util.CommonUtil;
import com.example.access1.util.SaveBitmap;
import com.example.access1.util.TickConfirmShow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.miludeer.jsoncode.JsonCode;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Description: 管理端录入教师信息类
 */
public class InfoInputTeacherActivity extends HaveToolBarActivity implements OnClickListener {
    private EditText et1, et2;
    private ImageView imageView;
    private ShowWaitingProgress showWaitingProgress;
    private Button button;
    private int RESULT_LOAD_IMAGE = 1;
    private int RESULT_CAMERA_IMAGE = 2;
    public String groupId;
    File file;
    byte[] byte_array;
    private String responseContent;
    private String token_str;
    private String path;
    private String picturePath;

    public InfoInputTeacherActivity() {
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.info_input_page_tea);
        initView();
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setTitle("教师信息录入");
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE && null != data) {// data为什么不能为null
                Uri selectedImageUri = data.getData();
                // crossFade默认300ms
                // Glide.with(this).load(selectedImage).dontAnimate().diskCacheStrategy(
                // DiskCacheStrategy.NONE ).into(imageView);
                imageView.setImageURI(selectedImageUri);
//                以下为通过uri获取该图片路径
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                picturePath = cursor.getString(columnIndex);
//                cursor.close();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (bitmap != null)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                Bitmap bitmap2 = BitmapFactory.decodeStream(bais);
                picturePath = new SaveBitmap(this).saveImageToGallery(bitmap2);
            } else if (requestCode == RESULT_CAMERA_IMAGE) {
                if (data != null)
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

        if (v.getId() == R.id.info_input_iv) {
            showPopupWindow();
        } else if (v.getId() == R.id.info_input_btn) {
            token_str = getSharedPreferences("loginManager", 0).getString("token", "noToken");
            path = CommonUtil.url + "teacher/add?";
            sendRequestWithOkHttp();

        }
    }

    private void sendRequestWithOkHttp() {

        if (!TextUtils.isEmpty(et1.getText().toString()) && !TextUtils.isEmpty(et2.getText().toString())
                && !TextUtils.isEmpty(picturePath)
                ) {
            String urlParams = "teacherName=" + et1.getText().toString() + "&teacherNum=" + et2.getText().toString();
            file = new File(picturePath);
            path += urlParams;
        } else {
            CommonUtil.showToast(this, "请填写完整的信息和选择图片");
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
        showWaitingProgress.show(this, "录入中");
        new Thread(new Runnable() {
            @Override
            public void run() {

                Response response;
                try {

                    try {
                        response = call.execute();
                        showWaitingProgress.dismiss();
                    } catch (Exception e) {

                        showWaitingProgress.dismiss();
                        CommonUtil.showToast(InfoInputTeacherActivity.this, "连接服务器失败");
                        return;
                    }
                    try {
                        responseContent = response.body().string();
                    } catch (Exception e) {

                        CommonUtil.showToast(InfoInputTeacherActivity.this, "response不能转换为字符串");
                        return;
                    }
                    if (responseContent.contains(CommonUtil.requestSuccess)) {// 请求成功
                        new TickConfirmShow(InfoInputTeacherActivity.this).showTick("录入成功");
//                    file.delete();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        InfoInputTeacherActivity.this.finish();
                                    }
                                }, 600);
                            }
                        });

                    } else if (responseContent.contains("\"code\":400")) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                CommonUtil.showToast(InfoInputTeacherActivity.this,
                                        JsonCode.getValue(responseContent, "$.message"));
                            }
                        });
                    } else {
                        CommonUtil.showToast(InfoInputTeacherActivity.this,
                                JsonCode.getValue(responseContent, "$.message"));
                    }
                }finally {
                    file.delete();
                }
            }
        }).start();
    }

    // 点击imageView时展示弹窗
    private void showPopupWindow() {
        View popView = View.inflate(this, R.layout.popup_choose_photo, null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancel = (Button) popView.findViewById(R.id.btn_pop_cancel);
        // 获取屏幕宽高
        int width = CommonSize.getScreenWidth(this);
        int height = CommonSize.getScreenHeight(this) / 3;

        final PopupWindow popupWindow = new PopupWindow(popView, width, height);
        popupWindow.setAnimationStyle(R.style.choose_photo_pop_anim);

        // 得到焦点，使下层界面失去焦点并不可点击聚焦。
        popupWindow.setFocusable(true);
        // popupWindow.setOutsideTouchable(true);失效了，添加空背景解决
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        bt_album.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoInputTeacherActivity.this, sRecognizeTestActivity.class);
                i.putExtra("forWhat", "giveMeBitmap");
                startActivityForResult(i, RESULT_CAMERA_IMAGE);
                popupWindow.dismiss();

            }
        });
        bt_cancel.setOnClickListener(new OnClickListener() {
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

    private void initView() {
        et1 = f(R.id.info_input_et1);
        et2 = f(R.id.info_input_et2);
        f(R.id.info_input_et3);
        f(R.id.info_input_title_tv);
        imageView = f(R.id.info_input_iv);
        button = f(R.id.info_input_btn);
    }

}
