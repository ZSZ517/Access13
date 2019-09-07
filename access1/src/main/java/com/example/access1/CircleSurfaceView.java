package com.example.access1;



import com.example.access1.util.CommonSize;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class CircleSurfaceView extends SurfaceView{
	private int mRatioWidth=0;
	private int mRatioHeight=0;
	//�������캯��
	public CircleSurfaceView(Context context) {
		super(context);
		initView();
	}
	public CircleSurfaceView(Context context,AttributeSet attributeSet) {
		super(context, attributeSet);
		initView();
	}
	public CircleSurfaceView(Context context,AttributeSet attributeSet,int defStyle) {
		super(context,attributeSet,defStyle);
		initView();
	}
	//���ø���ͼ�ɾ۽�
	public void initView() {
		setFocusable(true);
		setFocusableInTouchMode(true);
		CommonSize.getScreenWidth(getContext());
	}
//	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
		  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	        int width = MeasureSpec.getSize(widthMeasureSpec);
	        int height = MeasureSpec.getSize(heightMeasureSpec);
	        if (0 == mRatioWidth || 0 == mRatioHeight) {
	            setMeasuredDimension(width, height);
	        } else {
	            if (width < height * mRatioWidth / mRatioHeight) {
	                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
	            } else {
	                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
	            }
	        }
	}
	@Override
	public void draw(Canvas canvas) {
//		Path path=new Path();
//		path.addCircle(screenWidth/2, screenWidth/2, (int)(screenWidth/2.5), Path.Direction.CCW);//screenWidth
////		Log.d("hello","  "+screenWidth);
//		canvas.clipPath(path,Op.REPLACE);
////		canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);//����͸��?:Color.TRANSPARENT,Mode.CLEAR
		super.draw(canvas);
	}
//	@Override
//	protected void onDraw(Canvas canvas) {
//		// TODO �Զ����ɵķ������
//		super.onDraw(canvas);//?���Բ��ã�
//	}
	
	//�����������ʲô��
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        int screenWidth = CommonSize.getScreenWidth(getContext());
        int screenHeight = CommonSize.getScreenHeight(getContext());
        w = screenWidth;
        h = screenHeight;
        super.onSizeChanged(w, h, oldw, oldh);
    }
	
	public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size can't be negative");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }
}
