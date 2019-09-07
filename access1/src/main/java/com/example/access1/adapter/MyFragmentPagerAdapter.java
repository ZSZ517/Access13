package com.example.access1.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragment_list;
    private String[] tabText_array;

    public MyFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragment_list, String[] tabText_array) {
        super(fragmentManager);
        this.fragment_list = fragment_list;
        this.tabText_array = tabText_array;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabText_array[position];
    }

    @Override    
    public void destroyItem(ViewGroup container, int position, Object object) {      
    	//super.destroyItem(container, position, object);    
    }
}
