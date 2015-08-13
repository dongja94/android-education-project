package com.example.samples2tmap;

import com.skp.Tmap.TMapPOIItem;

public class POIItem {
	TMapPOIItem poi;
	@Override
	public String toString() {
		return poi.getPOIName();
	}
}
