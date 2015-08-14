package com.example.samples2appwidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	int[] images = {R.drawable.gallery_photo_1,
			R.drawable.gallery_photo_2,
			R.drawable.gallery_photo_3,
			R.drawable.gallery_photo_4,
			R.drawable.gallery_photo_5,
			R.drawable.gallery_photo_6,
			R.drawable.gallery_photo_7,
			R.drawable.gallery_photo_8
	};

	AppWidgetManager mAWM;
	@Override
	public void onCreate() {
		super.onCreate();
		mAWM = AppWidgetManager.getInstance(this);
	}
	
	int index = 0;
	
	Handler mHandler = new Handler(Looper.getMainLooper());
	
	Runnable updateRunnalbe = new Runnable() {
		
		@Override
		public void run() {
			// appwidget update
			RemoteViews view = new RemoteViews(getPackageName(), R.layout.appwidget_layout);
			view.setImageViewResource(R.id.image_icon, images[index % images.length]);
			view.setTextViewText(R.id.text_title, "gallery : " + index);
			ComponentName cn = new ComponentName(UpdateService.this, MyAppWidgetProvider.class);
			int[] ids = mAWM.getAppWidgetIds(cn);
			mAWM.updateAppWidget(ids, view);
			
			index++;
			mHandler.postDelayed(this, 2000);
		}
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mHandler.removeCallbacks(updateRunnalbe);
		index = 0;
		mHandler.post(updateRunnalbe);
		return Service.START_STICKY;
	}
}
