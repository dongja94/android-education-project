package com.example.samples2backpress;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private static final int MESSAGE_BACK_TIMEOUT = 1;
	private static final int TIMEOUT_INTERVAL = 2000;
	
	Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_BACK_TIMEOUT :
				isBackPressed = false;
				break;
			}
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    boolean isBackPressed = false;
    Toast mToast;
    @Override
    public void onBackPressed() {
    	if (!isBackPressed) {
    		mToast = Toast.makeText(this, "one more back key", Toast.LENGTH_SHORT);
    		mToast.show();
    		isBackPressed = true;
    		mHandler.sendEmptyMessageDelayed(MESSAGE_BACK_TIMEOUT, TIMEOUT_INTERVAL);
    	} else {
    		mHandler.removeMessages(MESSAGE_BACK_TIMEOUT);
    		mToast.cancel();
    		super.onBackPressed();
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
