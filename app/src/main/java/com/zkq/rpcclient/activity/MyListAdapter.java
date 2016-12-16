package com.zkq.rpcclient.activity;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zkq.rpcclient.R;

import java.util.List;

/**
 * Created by Drew on 2016-12-17.
 */

public class MyListAdapter extends BaseAdapter {
    List<Bitmap> imgs;
    Context context;

    public MyListAdapter(Context context, List<Bitmap> imgs) {
        this.imgs = imgs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int i) {
        return imgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater =  LayoutInflater.from(context);
        view = inflater.inflate(R.layout.list_item, viewGroup, false);
        ImageView imgview = (ImageView) view.findViewById(R.id.img);
        imgview.setImageBitmap(imgs.get(i));
        return imgview;
    }
}
