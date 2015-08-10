package com.example.samples2navermovie;

public class MovieItem {
	String title;
	String link;
	String image;
	String director;
	float userRating;
	
	@Override
	public String toString() {
		return title + "(" + director + ")";
	}
}
