package com.example.samples2navermovie;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.begentgroup.xmlparser.XMLParser;


public class MainActivity extends ActionBarActivity {

	ListView listView;
	ArrayAdapter<MovieItem> mAdapter;
	EditText inputView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputView = (EditText)findViewById(R.id.edit_input);
        
        listView = (ListView)findViewById(R.id.listView1);
        mAdapter = new ArrayAdapter<MovieItem>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);
        
        Button btn = (Button)findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String keyword = inputView.getText().toString();
				if (!TextUtils.isEmpty(keyword)) {
					new MovieTask().execute(keyword);
				}
			}
		});
    }

    class MovieTask extends AsyncTask<String, Integer, NaverMovie> {
    	@Override
    	protected NaverMovie doInBackground(String... params) {
    		String format = "http://openapi.naver.com/search?key=55f1e342c5bce1cac340ebb6032c7d9a&query=%s&display=10&start=1&target=movie";
    		String keyword = params[0];
    		try {
				String urlText = String.format(format, URLEncoder.encode(keyword, "utf-8"));
				URL url = new URL(urlText);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				int code = conn.getResponseCode();
				if (code == HttpURLConnection.HTTP_OK) {
					InputStream is = conn.getInputStream();
					XMLParser parser = new XMLParser();
					NaverMovie movie = parser.fromXml(is, "channel", NaverMovie.class);
					return movie;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(NaverMovie result) {
    		super.onPostExecute(result);
    		if (result != null) {
    			for (MovieItem item : result.items) {
    				mAdapter.add(item);
    			}
    		}
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
