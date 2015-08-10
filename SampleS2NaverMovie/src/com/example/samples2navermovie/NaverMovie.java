package com.example.samples2navermovie;

import java.util.ArrayList;

import com.begentgroup.xmlparser.SerializedName;

public class NaverMovie {
	String title;
	String link;
	int total;
	int start;
	int display;
	@SerializedName("item")
	ArrayList<MovieItem> items;
}
