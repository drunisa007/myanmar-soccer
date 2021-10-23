package com.upper.team10.soccer.viewpagerAdapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 10/28/2017.
 */

public class ImageViewPagerAdapter extends PagerAdapter {
    List<String> list=new ArrayList<>();
    Context context;

    public ImageViewPagerAdapter(List<String> list, Context context){
        this.list=list;
       this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.imageviewflipper,container,false);
        ImageView imageview= (ImageView) view.findViewById(R.id.imageviewflipper);
        Picasso.with(context).load(list.get(position)).into(imageview);
        container.addView(imageview);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
