package com.example.android.movietiles;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.movietiles.model.MovieInfo;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private MovieInfo[] mThumbIds = new MovieInfo[0];

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public void setMoviesInfo(MovieInfo[] movieInfos) {
        mThumbIds = movieInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    // Create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        String imageLink = mThumbIds[position].getImageLink();
        Picasso.with(mContext).load(imageLink).into(imageView);
        return imageView;
    }
}
