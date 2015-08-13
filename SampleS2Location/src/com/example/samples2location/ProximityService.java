package com.example.samples2location;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;

public class ProximityService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		boolean isEnter = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
		String message;
		if (isEnter) {
			message = "enter";
		} else {
			message = "exit";
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker("proximity");
		builder.setContentText(message);
		builder.setAutoCancel(true);
		builder.setDefaults(NotificationCompat.DEFAULT_ALL);
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		nm.notify(1, builder.build());
		
		return Service.START_NOT_STICKY;
	}
}
