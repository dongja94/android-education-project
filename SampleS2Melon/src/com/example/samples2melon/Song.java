package com.example.samples2melon;

public class Song {
	int songId;
	String songName;
	int albumId;
	String albumName;
	int currentRank;
	@Override
	public String toString() {
		return "["+currentRank + "]" + songName + "("+albumName+")";
	}
}
