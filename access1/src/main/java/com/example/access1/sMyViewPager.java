package com.example.access1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
 
/**
 * Created by Fly on 2017/11/25.
 */
 
public class sMyViewPager extends ViewPager {
 
 
    public sMyViewPager(@NonNull Context context) {
        super(context);
    }
 
    public sMyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;//�������¼�����Ƕ�׵���viewpager�л�����Ӧ�����¼�
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // ��дViewPager�����¼�����ʲô������
        return true;
    }
 
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);//��ʾ�л���ʱ�򣬲���Ҫ�л�ʱ�䡣
    }
}