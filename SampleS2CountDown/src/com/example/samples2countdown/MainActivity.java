package com.example.samples2countdown;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	TextView messageView;
	Handler mHandler = new Handler(Looper.getMainLooper());
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = (TextView)findViewById(R.id.text_message);
        Button btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mHandler.removeCallbacks(countRunnable);
				startTime = NOT_STARTED;
				mHandler.post(countRunnable);
			}
		});
        
        btn = (Button)findViewById(R.id.btn_stop);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mHandler.removeCallbacks(countRunnable);
			}
		});
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	mHandler.removeCallbacks(countRunnable);
    }
    
    private static final long NOT_STARTED = -1;
    long startTime = NOT_STARTED;
    
    Runnable countRunnable = new Runnable() {
		
		@Override
		public void run() {
			long currentTime = System.currentTimeMillis();
			if (startTime == NOT_STARTED) {
				startTime = currentTime;
			}
			
			int interval = (int)(currentTime - startTime);
			int count = interval / 1000;
			int rest = 1000 - (interval % 1000);
			messageView.setText("count : " + count);
			mHandler.postDelayed(this, rest);		
		}
	};
	
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
