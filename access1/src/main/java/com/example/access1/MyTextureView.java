package com.example.access1;

import com.example.access1.util.CommonSize;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

public class MyTextureView extends TextureView {
	private Context context;
    public MyTextureView(Context context){
        this(context,null);
    }
    public MyTextureView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public MyTextureView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        this.context=context;
    }
   
    @Override
    public void onMeasure(int width,int height) {
    	int sreenWidth=CommonSize.getScreenWidth(context);
//    	int screenHeight=CommonSize.getScreenHeight(context);
    	setMeasuredDimension(sreenWidth, sreenWidth);
    }
}
