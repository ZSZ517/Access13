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
	private CameraManager mCameraManager;// ����ͷ������
	private Handler childHandler, mainHandler;
	private ShowWaitingProgress showWaitingProgress;
	private String mCameraID;// ����ͷId 0 Ϊ�� 1 Ϊǰ
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

	// Ϊ��ʹ��Ƭ��ֱ��ʾ
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
		getSupportActionBar().setTitle("ʶ��");
		initView();
		screenWidth = CommonSize.getScreenWidth(RecognizeActivity.this);
	}

	private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
		@Override
		public void onOpened(CameraDevice camera) {// ������ͷ
			// ��camera�ɹ���֮���ص��÷�������ʱcamera�Ѿ����������Կ�ʼ���������һϵ�еĲ�����
			// ��ͨ������CameraCaptureSession.createCaptureSession���������õ�һ��capture session
			mCameraDevice = camera;
			// ����Ԥ��
			startPreview();
		}

		@Override
		public void onDisconnected(CameraDevice camera) {// �ر�����ͷ
			// ��camera���ٿ��ã������CameraManager.openCamera()�����ʧ��ʱ����
			// ���ô˷���.��ʱ�κγ��Ե���CameraDevice�����Ĳ�������ʧ�ܲ��׳�һ��
			// CameraAccessException�쳣��
			// ��ȫ���Ի�Ȩ�޵ĸı䡢���ƶ�����豸������Ͽ������ߵ���camera��Ҫ������
			// �ȼ���camera API Clientʱ���ᵼ�¸�camera�豸���ӶϿ�
			if (null != mCameraDevice) {// ������пգ���cameraDeviceΪ��ʱ�ͻ��׳��쳣
				mCameraDevice.close();// Ҳͬʱ�ر�imageReader��
				RecognizeActivity.this.mCameraDevice = null;
				if (mImageReader != null) {
					mImageReader.close();
					mImageReader = null;
				}
			}
		}

		@Override
		public void onError(CameraDevice camera, int error) {
			// ����˼�塣��camera����ʱ��ص��÷���
			// Ӧ�ڸ÷�������CameraDevice.close()����һЩ�����ͷ������Դ�Ĳ�������ֹ������������һϵ�����⡣����disconnected�������ں��߻����Իָ����ӡ�
			Toast.makeText(RecognizeActivity.this, "����ͷ��������", Toast.LENGTH_SHORT).show();
		}
	};

	// ��ʼ��view�������������Ϊ���ڲ����Ҫʵ�ּ����ķ��������Ը÷����ϳ������߼��������Ƿ����ġ�ֻ������ʼ���ͼ��������ܻ��Զ��ص�����������
	private void initView() {

		f(R.id.take_iv);
		textureView = f(R.id.texture_view);
		surfaceView = f(R.id.circle_surface_view);
		surfaceView.setZOrderOnTop(true);// ��surfaceView���ڶ���,���ǵײ��TextureView����仰�����ִ�У�Ҫ��textureView׼����֮ǰִ��

		// mSurfaceView��ӻص����ڴ�����ʱ����������������
		textureView.setSurfaceTextureListener(new SurfaceTextureListener() {
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture textureSurface, int width, int height) {
				// ��SurefaceTexture���õ�ʱ��������������������
				initCamera2();
				initSurfaceView();// ����Ҫ��TextureView׼����ϲſ��Ի���SurfaceView

			}

			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture textureSurface, int width, int height) {
				// TODO �Զ����ɵķ������

			}

			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture textureSurface) {
				// TODO �Զ����ɵķ������
				// �ͷ�Camera��Դ
				if (null != mCameraDevice) {
					mCameraDevice.close();
					RecognizeActivity.this.mCameraDevice = null;
				}
				return false;
			}

			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture textureSurface) {
				// TODO �Զ����ɵķ������
			}
		});
	}

	private void initSurfaceView() {

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setFormat(PixelFormat.TRANSPARENT);// ����surfaceΪ͸���������͸����λ�ڶ���Ḳ�ǵײ�
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, screenWidth);
		surfaceView.setLayoutParams(params);
		// ���廭��
		Paint mpaint = new Paint();
		// �õ���app��theme������ɫ
		TypedArray typedArray = getTheme().obtainStyledAttributes(new int[] { android.R.attr.colorBackground });
		int backgroundColor = typedArray.getColor(0, 0xFF00FF);
		typedArray.recycle();
		mpaint.setColor(backgroundColor);
		// mpaint.setAntiAlias(true);//ȥ���
		mpaint.setStyle(Paint.Style.FILL);// ����
		// ����paint�������
		Canvas canvas = surfaceHolder.lockCanvas();
		// ����pathԲ���򣬽��������������,�����ȼ������ٻ��ơ�
		Path path = new Path();
		path.addCircle(screenWidth / 2, screenWidth / 2, (float) (screenWidth / ratioCircle), Path.Direction.CCW);
		canvas.clipPath(path, Op.DIFFERENCE);
		// ���������
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // �������һ�εĻ��򡣻�֮ǰ��Ӧ�����
		Rect r = new Rect(0, 0, screenWidth, screenWidth);
		canvas.drawRect(r, mpaint);
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	// �������
	@SuppressLint("NewApi")
	private void initCamera2() {

		// ��ȡ����ͷ����
		mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;// ǰ������ͷ
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
				// �õ�������Ƭ����
				Image image = reader.acquireNextImage();
				ByteBuffer buffer = image.getPlanes()[0].getBuffer();
				byte[] byte_array = new byte[buffer.remaining()];
				buffer.get(byte_array);// �ɻ����������ֽ�����

				Bitmap bitmapOriginal = BitmapFactory.decodeByteArray(byte_array, 0, byte_array.length);
				// ���ֽ�����ת��Ϊbitmap����bitmap������������
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
					// ����bitmapVertical
					double beginHeightPixel = CommonSize.getDensity(RecognizeActivity.this) * 20;
					double medium = (0.5 - 1 / ratioCircle) * screenWidth;
					beginHeightPixel += medium;
					bitmapCut = Bitmap.createBitmap(bitmapVertical, 0, (int) beginHeightPixel,
							bitmapVertical.getWidth(), (int) (bitmapVertical.getHeight() - beginHeightPixel - 10));
					// bitmapCut = bitmapVertical;
				}

				String picPath = new SaveBitmap(RecognizeActivity.this).saveImageToGallery(bitmapCut);
				showWaitingProgress = new ShowWaitingProgress();
				showWaitingProgress.show(RecognizeActivity.this, "ʶ����");
				image.close();
				sendRequestWithOkHttp(picPath);
				mImageReader.setOnImageAvailableListener(null, null);
			}
		}, mainHandler);

		try {
			// 6���¶�Ĭ����Ȩ
			if (ActivityCompat.checkSelfPermission(this,
					Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				// ����WRITE_EXTERNAL_STORAGEȨ��
				requestPermissions(new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_CODE);
				// return;
			} else {
				// ������ͷ
				mCameraManager.openCamera(mCameraID, stateCallback, childHandler);// ����state�ص�
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
		// TODO �Զ����ɵķ������
		try {
			cameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraID);
		} catch (Exception e) {
			// TODO: handle exception
		}
		StreamConfigurationMap streamConfigurationMap = cameraCharacteristics
				.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
		// ��ȡԤ����������ĳߴ磬TextureView��ΪԤ�������Դ�������SurfaceTexture
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
	// // TODO �Զ����ɵķ������
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
			// ����Ԥ����Ҫ��CaptureRequest.Builder
			previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			// ��SurfaceView��surface��ΪCaptureRequest.Builder��Ŀ��
			previewRequestBuilder.addTarget(textureSurface);// �����Ż�ص�surface��Ӧ�Ļص�������
			// ����CameraCaptureSession���ö����������Ԥ���������������
			mCameraDevice.createCaptureSession(Arrays.asList(textureSurface, mImageReader.getSurface()),
					new CameraCaptureSession.StateCallback() // ��Ϊ���ڵ�����Ҫʹ��imageReader��surface�������յ�ʱ�������session����
					{
						@Override
						public void onConfigured(CameraCaptureSession cameraCaptureSession) {
							if (null == mCameraDevice)
								return;
							// ������ͷ�Ѿ�׼����ʱ����ʼ��ʾԤ��
							mCameraCaptureSession = cameraCaptureSession;
							try {
								// �Զ��Խ�
								previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
										CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
								// �������
								// previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
								// CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
								// ��ʾԤ��
								CaptureRequest previewRequest = previewRequestBuilder.build();
								mCameraCaptureSession.setRepeatingRequest(previewRequest, captureCallback,
										childHandler);
							} catch (CameraAccessException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
							Toast.makeText(RecognizeActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
						}
					}, childHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����
	 */
	private void takePicture() {
		if (mCameraDevice == null) {
			return;
		}
		// ����������Ҫ��CaptureRequest.Builder
		final CaptureRequest.Builder captureRequestBuilder;
		try {
			captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
			// ��imageReader��surface��ΪCaptureRequest.Builder��Ŀ��
			captureRequestBuilder.addTarget(mImageReader.getSurface());

			// �Զ��Խ�
			captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
					CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
			// �Զ��ع�
			captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
			// ��ȡ�ֻ�����
			int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
			// �����豸�������������Ƭ�ķ���
			mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
			captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));
			// ����
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
		// һ�������ͻ��˶���
		OkHttpClient client = new OkHttpClient.Builder().writeTimeout(8, TimeUnit.SECONDS)
				.readTimeout(8, TimeUnit.SECONDS).build();
		// ȷ����post���󣬹���requestBody��requestBody���Ա�Ƕ�׽�multipartBody
		RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
		MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
		multipartBody.addFormDataPart("image", file.getName(), body);
		Request request = new Request.Builder().url(path).header("token", sp.getString("token", "noToken"))
				.post(multipartBody.build()).build();
		// requestBody��Ҳ�����������幹����󣬹���������
		final Call call = client.newCall(request);
		// Ϊ�˲��������̣߳����ǿ������߳�
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
					CommonUtil.showToast(RecognizeActivity.this, "�����е㿨");
				}
				String responseContent = null;
				try {
					// �õ����������ص�����
					responseContent = responce.body().string();
					file.delete();
				} catch (Exception e) {
					CommonUtil.showToast(RecognizeActivity.this, "response��֧��ת��Ϊstring");
					return;
				}
				String success1 = "\"code\":504";//�����ݷ�
				String success2 = "\"code\":500";//ѧ��������Υ��ʶ��
				String success3 = "\"code\":503";//ѧ��Υ��
				if (responseContent.contains(success1)||responseContent.contains(success2)||responseContent.contains(success3)) {
					// ���ʶ��ɹ����򷵻�ʶ����Ϣ
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
