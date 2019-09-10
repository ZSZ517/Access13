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

    private List<RotateBean> datas;//�ֲ�ͼƬ������Դ
    private LayoutInflater inflater;

    public RotateVpAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setDatas(List<RotateBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();//�÷������ã�
    }

    //��ȡ����data������
    @Override
    public int getCount() {
        // Ϊ����ViewPager�����һҳ��������һ���ص���һҳ
        // ����ҳ��Ϊint���ֵ,�������»�����Զ������һҳ
    	//û��ͼƬ��Դʱcount��Ϊ0������Ϊ���ֵ Integer.MAX_VALUE
        return datas == null ? 0 :Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // ��viewPagerҪ��ʾ�����ݿ��Խ��л����ʱ��PagerAdapterĬ��ֻ����ʾ3�����ݣ�Ŀǰ�ĺ����Ҹ�һ������������������������ʾ���ݵĳ�ʼ�������ǽ�Ҫ��ʾ��ImageView���뵽ViewGroup�У�Ȼ����Ϊ����ֵ���ؼ��ɣ�����Ĳ���container��vp��ǰ�������ݵĳ���
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // position��int���ֵ������������Ǽ���������ǧ,���ȡ���������Խ�磬viewPager����Ϊ��������count������Ҫ��ʾ��ʵ����newPosition����ԭ�ش�ת�ˣ�����vp��˵������ʾ��position��ȷ������
        int newPosition = position % datas.size();
        //item_vpΪvpҪ��ʾ�����ݵĲ��֣��Ըò��ֽ���ͼƬ��䣬ʹ��vp��ʾ������ͼƬ
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