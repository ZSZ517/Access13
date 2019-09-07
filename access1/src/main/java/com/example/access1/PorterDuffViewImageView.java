package com.example.access1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class PorterDuffViewImageView extends ImageView {
	private Bitmap bitmap;
	private Bitmap dstBmp;
	private int width, height;
	Bitmap.Config config;
	private Paint paint ;

	public PorterDuffViewImageView(Context context) {
		super(context);
		init();
	}

	public PorterDuffViewImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PorterDuffViewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		// �ر�Ӳ������
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		config = Bitmap.Config.ARGB_8888;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		width = getWidth();
		height = getHeight();
		dstBmp = makeDst(width, height);
		if (bitmap == null) {// �����粻��bitmap����ʹ��Ĭ��bitmap
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.portrait_default);
		}
		
		Matrix matrix = new Matrix();
		// ע�⣬���postScaleһ�������Գ������������ܽ��Ϊ0��ȡ����
		matrix.postScale((float) width / bitmap.getWidth(), (float) width / bitmap.getHeight());
		this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
		int layerID = canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG);
		canvas.drawBitmap(dstBmp, 0, 0, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, 0, paint);
		paint.setXfermode(null);
		canvas.restoreToCount(layerID);
	}

	static Bitmap makeDst(int w, int h) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

		p.setColor(0xFFFFCC44);
		// c.drawCircle(w / 2, w / 2, w / 2, p);
		c.drawOval(new RectF(0, 0, w, h), p);
		return bm;
	}

	static Bitmap makeSrc(int w, int h) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

		p.setColor(0xFF66AAFF);
		c.drawRect(0, 0, w, h, p);
		return bm;
	}

	public void setBitMap(Bitmap bitmap) {
		if (bitmap != null) {
			this.bitmap = bitmap;
		}
	}

}