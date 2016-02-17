package edu.cmu.ece.jsphdev.foodie.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for handling imagelist item
 */
public class ImgAdapter  extends PagerAdapter{

    List<NetworkImageView> mImageViews;
    public ImgAdapter(List<NetworkImageView> imageViews){
           mImageViews = new ArrayList<>(imageViews);
    }
    /*
     get the image count
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            ((ViewPager) container).addView(mImageViews.get(position % mImageViews.size()), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mImageViews.get(position % mImageViews.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
