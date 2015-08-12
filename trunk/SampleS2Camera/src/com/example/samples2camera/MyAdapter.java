package com.example.samples2camera;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class MyAdapter extends BaseAdapter {
	List<Bitmap> items = new ArrayList<Bitmap>();
	

	public void add(Bitmap bm) {
		items.add(bm);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView view;
		if (convertView == null) {
			view = new ImageView(parent.getContext());
			view.setLayoutParams(new Gallery.LayoutParams(100, 100));
		} else {
			view = (ImageView)convertView;
		}
		view.setImageBitmap(items.get(position));
		return view;
	}

}
