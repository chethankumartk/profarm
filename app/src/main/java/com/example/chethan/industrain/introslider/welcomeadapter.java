package com.example.chethan.industrain.introslider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class welcomeadapter extends PagerAdapter {


    private int[] layouts;
    private LayoutInflater layoutInflater;
    private Context context;
    private static final float MIN_SCALE = 0.65f;
    private static final float MIN_ALPHA = 0.3f;


    public welcomeadapter(int[] layouts,Context context)
    {
        this.layouts=layouts;
        this.context=context;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view=layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        View view=(View)object;
        container.removeView(view);
    }


}
