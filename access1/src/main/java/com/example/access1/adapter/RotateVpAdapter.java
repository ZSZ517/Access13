package com.example.access1.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.access1.R;
import com.example.access1.bean.RotateBean;

import java.util.List;

public class RotateVpAdapter extends PagerAdapter{

    private List<RotateBean> datas;//轮播图片数据来源
    private LayoutInflater inflater;

    public RotateVpAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setDatas(List<RotateBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();//该方法作用？
    }

    //获取滑动data的数量
    @Override
    public int getCount() {
        // 为了让ViewPager到最后一页不会像翻书一样回到第一页
        // 设置页数为int最大值,这样向下滑动永远都是下一页
    	//没有图片来源时count置为0，否则为最大值 Integer.MAX_VALUE
        return datas == null ? 0 :Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // 当viewPager要显示的数据可以进行缓存的时候（PagerAdapter默认只能显示3个数据，目前的和左右各一个），会调用这个方法进行显示数据的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可，传入的参数container是vp当前容纳数据的抽象。
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // position是int最大值所以这里可能是几百甚至上千,因此取余避免数组越界，viewPager误认为有无数个count的数据要显示，实际上newPosition让他原地打转了，但对vp来说，他显示的position的确增加了
        int newPosition = position % datas.size();
        //item_vp为vp要显示的数据的布局，对该布局进行图片填充，使得vp显示该填充的图片
        View convertView = inflater.inflate(R.layout.item_vp, container, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_iv);
        imageView.setImageResource(datas.get(newPosition).getImgId());

        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}