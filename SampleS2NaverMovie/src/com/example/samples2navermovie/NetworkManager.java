package com.example.samples2navermovie;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.begentgroup.xmlparser.XMLParser;

public class NetworkManager {
	private static NetworkManager instance;
	
	public static NetworkManager getInstance() {
		if (instance == null) {
			instance = new NetworkManager();
		}
		return instance;
	}
	
	private NetworkManager() {
		
	}
	
	public interface OnResultListener<T> {
		public void onSuccess(T result);
		public void onFail(int code);
	}
	
	Handler mHandler = new Handler(Looper.getMainLooper());
	
	public void getNaverMovie(final String keyword, final OnResultListener<NaverMovie> listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
	    		String format = "http://openapi.naver.com/search?key=55f1e342c5bce1cac340ebb6032c7d9a&query=%s&display=50&start=1&target=movie";
	    		try {
					String urlText = String.format(format, URLEncoder.encode(keyword, "utf-8"));
					URL url = new URL(urlText);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					int code = conn.getResponseCode();
					if (code == HttpURLConnection.HTTP_OK) {
						InputStream is = conn.getInputStream();
						XMLParser parser = new XMLParser();
						final NaverMovie movie = parser.fromXml(is, "channel", NaverMovie.class);
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								listener.onSuccess(movie);
							}
						});
						return;
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		
	    		mHandler.post(new Runnable() {
					
					@Override
					public void run() {
			    		listener.onFail(0);
					}
				});
			}
		}).start();
	}
}
