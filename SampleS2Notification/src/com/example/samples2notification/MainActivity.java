package com.example.samples2notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

	NotificationManager mNM;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mNM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        
        Button btn =(Button)findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendNotification(null);
			}
		});
        
        btn = (Button)findViewById(R.id.btn_progress);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendProgress();
			}
		});
        
        btn = (Button)findViewById(R.id.btn_style);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
				style.setBigContentTitle("Email....");
				style.addLine("mail 1");
				style.addLine("mail 2");
				style.setSummaryText("email summary...");
				sendNotification(style);
			}
		});
    }
    
    
    Handler mHandler = new Handler(Looper.getMainLooper());
    
    int progress = 0;
    
    Runnable progressRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (progress <= 100) {
		    	NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
		    	builder.setSmallIcon(android.R.drawable.ic_dialog_info);
		    	builder.setTicker("download...");
		    	builder.setContentTitle("image download : " + progress);
		    	builder.setContentText("download...");
		    	builder.setDefaults(NotificationCompat.DEFAULT_ALL);
		    	builder.setOngoing(true);
		    	builder.setProgress(100, progress, false);
		    	
		    	mNM.notify(100, builder.build());
	
		    	progress+=5;
		    	
		    	mHandler.postDelayed(this, 500);
			} else {
				mNM.cancel(100);
			}
	    	
	    	
		}
		
	};
	
    private void sendProgress() {
    	mHandler.removeCallbacks(progressRunnable);
    	progress = 0;
    	mHandler.post(progressRunnable);    	
    }
    private int mId = 1;
    
    private void sendNotification(NotificationCompat.Style style) {
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    	builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
    	builder.setTicker("notification test...");
    	
    	builder.setContentTitle("title..." + mId);
    	builder.setContentText("text..." + mId);
    	builder.setDefaults(NotificationCompat.DEFAULT_ALL);
    	builder.setAutoCancel(true);

    	if (style != null) {
    		builder.setStyle(style);
    	}
    	Intent intent = new Intent(this, NotificationActivity.class);
    	intent.setData(Uri.parse("myscheme://mydomain/"+mId));
    	intent.putExtra("message", "message : " + mId);
    	TaskStackBuilder tsb = TaskStackBuilder.create(this);
    	tsb.addNextIntent(intent);
    	
    	
    	PendingIntent pi = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    	
    	builder.setContentIntent(pi);
    	Notification n = builder.build();
    	
    	
    	
    	
    	mNM.notify(mId, n);
    	
    	mId++;
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
