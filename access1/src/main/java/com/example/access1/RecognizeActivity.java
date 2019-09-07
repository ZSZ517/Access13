package com.example.access1;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.example.access1.util.CommonSize;
import com.example.access1.util.CommonUtil;
import com.example.access1.util.SaveBitmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.FrameLayout;
import android.widget.Toast;
import cn.miludeer.jsoncode.JsonCode;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecognizeActivity extends HaveToolBarActivity {

	private File file;
	public static final int REQUEST_CAMERA_CODE = 100;
	public static final String PACKAGE = "package:";
	private TextureView textureView;
	private CameraManager mCameraManager;// 摄像头管理器
	private Handler childHandler, mainHandler;
	private ShowWaitingProgress showWaitingProgress;
	private String mCameraID;// 摄像头Id 0 为后 1 为前
	private ImageReader mImageReader;
	private CameraCaptureSession mCameraCaptureSession;
	private CameraDevice mCameraDevice;
	private float ratioCircle = 2.6f;
	private SurfaceView surfaceView;
	private CaptureRequest.Builder previewRequestBuilder = null;
	private SurfaceHolder surfaceHolder;
	private int screenWidth;
	private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
	private int[] mPreviewsize = new int[2];
	private int[] mCapturesize = new int[2];
	private int mSensorOrientation;
	private CameraCharacteristics cameraCharacteristics;

	// 为了使照片竖直显示
	static {
		ORIENTATIONS.append(Surface.ROTATION_0, 90);
		ORIENTATIONS.append(Surface.ROTATION_90, 0);
		ORIENTATIONS.append(Surface.ROTATION_180, 270);
		ORIENTATIONS.append(Surface.ROTATION_270, 180);
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_recognize);
		getSupportActionBar().setTitle("识别");
		initView();
		screenWidth = CommonSize.getScreenWidth(RecognizeActivity.this);
	}

	private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
		@Override
		public void onOpened(CameraDevice camera) {// 打开摄像头
			// 当camera成功打开之后会回调该方法。此时camera已经就绪，可以开始对相机进行一系列的操作，
			// 可通过调用CameraCaptureSession.createCaptureSession方法来设置第一个capture session
			mCameraDevice = camera;
			// 开启预览
			startPreview();
		}

		@Override
		public void onDisconnected(CameraDevice camera) {// 关闭摄像头
			// 当camera不再可用，或调用CameraManager.openCamera()打开相机失败时都会
			// 调用此方法.此时任何尝试调用CameraDevice方法的操作都会失败并抛出一个
			// CameraAccessException异常。
			// 安全策略或权限的改变、可移动相机设备的物理断开、或者当该camera需要更高优
			// 先级的camera API Client时都会导致该camera设备连接断开
			if (null != mCameraDevice) {// 如果不判空，当cameraDevice为空时就会抛出异常
				mCameraDevice.close();// 也同时关闭imageReader？
				RecognizeActivity.this.mCameraDevice = null;
				if (mImageReader != null) {
					mImageReader.close();
					mImageReader = null;
				}
			}
		}

		@Override
		public void onError(CameraDevice camera, int error) {
			// 顾名思义。当camera出错时会回调该方法
			// 应在该方法调用CameraDevice.close()和做一些其他释放相机资源的操作，防止相机出错而导致一系列问题。【和disconnected区别在于后者还可以恢复连接】
			Toast.makeText(RecognizeActivity.this, "摄像头遭遇错误", Toast.LENGTH_SHORT).show();
		}
	};

	// 初始化view并监听组件，因为用内部类和要实现监听的方法，所以该方法较长，但逻辑、界限是分明的。只包括初始化和监听【可能会自动回调其他方法】
	private void initView() {

		f(R.id.take_iv);
		textureView = f(R.id.texture_view);
		surfaceView = f(R.id.circle_surface_view);
		surfaceView.setZOrderOnTop(true);// 将surfaceView置于顶层,覆盖底层的TextureView，这句话最好先执行，要在textureView准备好之前执行

		// mSurfaceView添加回调，在创建的时候调用启动相机方法
		textureView.setSurfaceTextureListener(new SurfaceTextureListener() {
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture textureSurface, int width, int height) {
				// 当SurefaceTexture可用的时候，设置相机参数并打开相机
				initCamera2();
				initSurfaceView();// 必须要在TextureView准备完毕才可以绘制SurfaceView

			}

			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture textureSurface, int width, int height) {
				// TODO 自动生成的方法存根

			}

			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture textureSurface) {
				// TODO 自动生成的方法存根
				// 释放Camera资源
				if (null != mCameraDevice) {
					mCameraDevice.close();
					RecognizeActivity.this.mCameraDevice = null;
				}
				return false;
			}

			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture textureSurface) {
				// TODO 自动生成的方法存根
			}
		});
	}

	private void initSurfaceView() {

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setFormat(PixelFormat.TRANSPARENT);// 设置surface为透明，如果不透明，位于顶层会覆盖底层
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, screenWidth);
		surfaceView.setLayoutParams(params);
		// 定义画笔
		Paint mpaint = new Paint();
		// 得到该app的theme背景颜色
		TypedArray typedArray = getTheme().obtainStyledAttributes(new int[] { android.R.attr.colorBackground });
		int backgroundColor = typedArray.getColor(0, 0xFF00FF);
		typedArray.recycle();
		mpaint.setColor(backgroundColor);
		// mpaint.setAntiAlias(true);//去锯齿
		mpaint.setStyle(Paint.Style.FILL);// 空心
		// 设置paint的外框宽度
		Canvas canvas = surfaceHolder.lockCanvas();
		// 构造path圆区域，将其剪出不作绘制,必须先剪出，再绘制。
		Path path = new Path();
		path.addCircle(screenWidth / 2, screenWidth / 2, (float) (screenWidth / ratioCircle), Path.Direction.CCW);
		canvas.clipPath(path, Op.DIFFERENCE);
		// 剪出后绘制
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // 清除掉上一次的画框。画之前都应该清除
		Rect r = new Rect(0, 0, screenWidth, screenWidth);
		canvas.drawRect(r, mpaint);
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	// 启动相机
	@SuppressLint("NewApi")
	private void initCamera2() {

		// 获取摄像头管理
		mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;// 前置摄像头
		getMatchThisDeviceSizeForBuffer();
		mainHandler = new Handler(getMainLooper());
		HandlerThread handlerThread = new HandlerThread("Camera2");
		handlerThread.start();
		childHandler = new Handler(handlerThread.getLooper());
		mImageReader = ImageReader.newInstance(mCapturesize[0], mCapturesize[1], ImageFormat.JPEG, 1);
		mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
			@Override
			public void onImageAvailable(ImageReader reader) {
				// mCameraDevice.close();
				// 拿到拍照照片数据
				Image image = reader.acquireNextImage();
				ByteBuffer buffer = image.getPlanes()[0].getBuffer();
				byte[] byte_array = new byte[buffer.remaining()];
				buffer.get(byte_array);// 由缓冲区存入字节数组

				Bitmap bitmapOriginal = BitmapFactory.decodeByteArray(byte_array, 0, byte_array.length);
				// 将字节数组转化为bitmap，对bitmap进行竖屏处理
				Bitmap bitmapCut = null;
				if (bitmapOriginal != null) {
					Matrix matrix;
					Bitmap bitmapVertical = null;
					if (bitmapOriginal.getWidth() > bitmapOriginal.getHeight()) {
						matrix = new Matrix();
						matrix.postRotate(270);
						bitmapVertical = Bitmap.createBitmap(bitmapOriginal, 0, 0, bitmapOriginal.getWidth(),
								bitmapOriginal.getHeight(), matrix, true);
					} else {
						bitmapVertical = bitmapOriginal;
					}

					bitmapVertical = bitmapOriginal;
					// 剪裁bitmapVertical
					double beginHeightPixel = CommonSize.getDensity(RecognizeActivity.this) * 20;
					double medium = (0.5 - 1 / ratioCircle) * screenWidth;
					beginHeightPixel += medium;
					bitmapCut = Bitmap.createBitmap(bitmapVertical, 0, (int) beginHeightPixel,
							bitmapVertical.getWidth(), (int) (bitmapVertical.getHeight() - beginHeightPixel - 10));
					// bitmapCut = bitmapVertical;
				}

				String picPath = new SaveBitmap(RecognizeActivity.this).saveImageToGallery(bitmapCut);
				showWaitingProgress = new ShowWaitingProgress();
				showWaitingProgress.show(RecognizeActivity.this, "识别中");
				image.close();
				sendRequestWithOkHttp(picPath);
				mImageReader.setOnImageAvailableListener(null, null);
			}
		}, mainHandler);

		try {
			// 6以下都默认授权
			if (ActivityCompat.checkSelfPermission(this,
					Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				// 申请WRITE_EXTERNAL_STORAGE权限
				requestPermissions(new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_CODE);
				// return;
			} else {
				// 打开摄像头
				mCameraManager.openCamera(mCameraID, stateCallback, childHandler);// 绑定了state回调
			}
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CAMERA_CODE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission Granted
				try {
					mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
				} catch (CameraAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			} else {
				// Permission Denied
			}
		}
	}

	private void getMatchThisDeviceSizeForBuffer() {
		// TODO 自动生成的方法存根
		try {
			cameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraID);
		} catch (Exception e) {
			// TODO: handle exception
		}
		StreamConfigurationMap streamConfigurationMap = cameraCharacteristics
				.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
		// 获取预览画面输出的尺寸，TextureView作为预览，所以传入该类的SurfaceTexture
		Size[] sizes = streamConfigurationMap.getOutputSizes(SurfaceTexture.class);
		int[] divideSizeInt = new int[2];
		String[] divideSize;

		int max[] = { 0, 0 };
		for (Size size : sizes) {
			divideSize = size.toString().split("x");
			divideSizeInt[0] = Integer.parseInt(divideSize[0]);
			divideSizeInt[1] = Integer.parseInt(divideSize[1]);
			if (divideSizeInt[1] >= max[1]) {
				max[1] = divideSizeInt[1];
				max[0] = divideSizeInt[0];
			}
		}
		mPreviewsize[0] = max[0];
		mPreviewsize[1] = max[1];
		sizes = streamConfigurationMap.getOutputSizes(ImageFormat.JPEG);
		for (Size size : sizes) {
			divideSize = size.toString().split("x");
			divideSizeInt[0] = Integer.parseInt(divideSize[0]);
			divideSizeInt[1] = Integer.parseInt(divideSize[1]);
			if (divideSizeInt[0] >= 500 && divideSizeInt[0] <= 800 && divideSizeInt[1] < divideSizeInt[0]) {
				mCapturesize[0] = divideSizeInt[0];
				mCapturesize[1] = divideSizeInt[1];
				break;
			}
		}

	}

	private CaptureCallback captureCallback = new CaptureCallback() {
		@Override
		public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
				TotalCaptureResult result) {
			int afState = result.get(CaptureResult.CONTROL_AF_STATE);
			if (afState == CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED) {
				takePicture();
				try {
					session.stopRepeating();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
			// checkState(result);
		}

		@Override
		public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request,
				CaptureResult partialResult) {
			// checkState(partialResult);
		}
	};

	// private void checkState(CaptureResult result) {
	// // TODO 自动生成的方法存根
	// switch (mState) {
	// case STATE_PREVIEW:
	// // NOTHING
	// break;
	// case STATE_WAITING_CAPTURE:
	// int afState = result.get(CaptureResult.CONTROL_AF_STATE);
	//
	// if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState
	// || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState
	// || CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED == afState
	// || CaptureResult.CONTROL_AF_STATE_PASSIVE_UNFOCUSED == afState) {
	// // do something like save picture
	// }
	// break;
	// }
	// }

	private void startPreview() {

		SurfaceTexture texture = textureView.getSurfaceTexture();
		texture.setDefaultBufferSize(mPreviewsize[0], mPreviewsize[1]);// textureView.getWidth(),
																		// textureView.getHeight()
		Surface textureSurface = new Surface(texture);
		try {
			// 创建预览需要的CaptureRequest.Builder
			previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			// 将SurfaceView的surface作为CaptureRequest.Builder的目标
			previewRequestBuilder.addTarget(textureSurface);// 这样才会回调surface对应的回调方法。
			// 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
			mCameraDevice.createCaptureSession(Arrays.asList(textureSurface, mImageReader.getSurface()),
					new CameraCaptureSession.StateCallback() // 因为后期的拍照要使用imageReader的surface，且拍照的时候还是这个session对象。
					{
						@Override
						public void onConfigured(CameraCaptureSession cameraCaptureSession) {
							if (null == mCameraDevice)
								return;
							// 当摄像头已经准备好时，开始显示预览
							mCameraCaptureSession = cameraCaptureSession;
							try {
								// 自动对焦
								previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
										CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
								// 打开闪光灯
								// previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
								// CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
								// 显示预览
								CaptureRequest previewRequest = previewRequestBuilder.build();
								mCameraCaptureSession.setRepeatingRequest(previewRequest, captureCallback,
										childHandler);
							} catch (CameraAccessException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
							Toast.makeText(RecognizeActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
						}
					}, childHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拍照
	 */
	private void takePicture() {
		if (mCameraDevice == null) {
			return;
		}
		// 创建拍照需要的CaptureRequest.Builder
		final CaptureRequest.Builder captureRequestBuilder;
		try {
			captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
			// 将imageReader的surface作为CaptureRequest.Builder的目标
			captureRequestBuilder.addTarget(mImageReader.getSurface());

			// 自动对焦
			captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
					CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
			// 自动曝光
			captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
			// 获取手机方向
			int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
			// 根据设备方向计算设置照片的方向
			mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
			captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));
			// 拍照
			CaptureRequest mCaptureRequest = captureRequestBuilder.build();

			mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void sendRequestWithOkHttp(String picturePath) {
		SharedPreferences sp = getSharedPreferences("loginManager", 0);
		String path = CommonUtil.url + "search?groupId=" + sp.getString("groupId", "13");
		file = new File(picturePath);
		// 一、建立客户端对象
		OkHttpClient client = new OkHttpClient.Builder().writeTimeout(8, TimeUnit.SECONDS)
				.readTimeout(8, TimeUnit.SECONDS).build();
		// 确认是post请求，构建requestBody，requestBody可以被嵌套进multipartBody
		RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
		MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
		multipartBody.addFormDataPart("image", file.getName(), body);
		Request request = new Request.Builder().url(path).header("token", sp.getString("token", "noToken"))
				.post(multipartBody.build()).build();
		// requestBody，也就是请求主体构建完后，构建请求本身
		final Call call = client.newCall(request);
		// 为了不阻塞主线程，于是开启子线程
		new Thread(new Runnable() {

			@SuppressLint("ShowToast")
			@Override
			public void run() {
				Response responce = null;
				try {
					responce = call.execute();
					showWaitingProgress.dismiss();
				} catch (Exception e) {
					showWaitingProgress.dismiss();
					CommonUtil.showToast(RecognizeActivity.this, "网络有点卡");
				}
				String responseContent = null;
				try {
					// 得到服务器返回的数据
					responseContent = responce.body().string();
					file.delete();
				} catch (Exception e) {
					CommonUtil.showToast(RecognizeActivity.this, "response不支持转换为string");
					return;
				}
				String success1 = "\"code\":504";//家属拜访
				String success2 = "\"code\":500";//学生正常不违规识别
				String success3 = "\"code\":503";//学生违规
				if (responseContent.contains(success1)||responseContent.contains(success2)||responseContent.contains(success3)) {
					// 如果识别成功，则返回识别信息
					Intent intent = new Intent(RecognizeActivity.this, RecognizeResultActivity.class);
					intent.putExtra("responseContent", responseContent);
					startActivity(intent);
					return;

				} else {
					CommonUtil.showToast(RecognizeActivity.this, JsonCode.getValue(responseContent, "$.message"));
				}

				new Handler(getMainLooper()).postDelayed(new Runnable() {

					@Override
					public void run() {
						finish();
					}
				}, 2000);

			}
		}).start();
	}

	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// finish();
	// }

	private int getOrientation(int rotation) {
		// Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus
		// 5X)
		// We have to take that into account and rotate JPEG properly.
		// For devices with orientation of 90, we simply return our mapping from
		// ORIENTATIONS.
		// For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
		return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
	}
	
	protected void onRestart() {
		super.onRestart();
		finish();
		Intent intent = new Intent(RecognizeActivity.this, RecognizeActivity.class);
		startActivity(intent);
	}

}
