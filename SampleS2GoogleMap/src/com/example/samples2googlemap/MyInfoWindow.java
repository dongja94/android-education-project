package com.example.samples2googlemap;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWindow implements InfoWindowAdapter {
	View infoView;
	TextView titleView, subtitleView, descView;
	Map<Marker,ItemData> itemResolver;
	
	public MyInfoWindow(Context context, Map<Marker,ItemData> itemResolver) {
		infoView = LayoutInflater.from(context).inflate(R.layout.info_window, null);
		titleView = (TextView)infoView.findViewById(R.id.text_title);
		subtitleView = (TextView)infoView.findViewById(R.id.text_subtitle);
		descView = (TextView)infoView.findViewById(R.id.text_description);
		this.itemResolver = itemResolver;
	}
	
	@Override
	public View getInfoContents(Marker marker) {
		ItemData data = itemResolver.get(marker);
		titleView.setText(data.title);
		subtitleView.setText(data.subtitle);
		descView.setText(data.description);
		return infoView;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

}
