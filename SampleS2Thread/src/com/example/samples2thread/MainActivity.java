package com.example.samples2thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	TextView messageView;
	ProgressBar progressView;
	
	private static final int MESSAGE_INITIAL = 0;
	private static final int MESSAGE_UPDATE = 1;
	private static final int MESSAGE_DONE = 2;
	
	Handler mHandler = new Handler(Looper.getMainLooper()) {
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_INITIAL :
				messageView.setText("progress start...");
				progressView.setMax(100);
				progressView.setProgress(0);
				break;
			case MESSAGE_UPDATE :
				int progress = msg.arg1;
				messageView.setText("progress : " + progress);
				progressView.setProgress(progress);
				break;
			case MESSAGE_DONE :
				messageView.setText("progress done");
				progressView.setProgress(100);
				break;
			}
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = (TextView)findViewById(R.id.text_message);
        progressView = (ProgressBar)findViewById(R.id.progressBar1);
        Button btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				new Thread(progressRunnable).start();
				new MyTask().execute();
			}
		});
    }
    
    class MyTask extends AsyncTask<String, Integer, Boolean> {

    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
			messageView.setText("progress start...");
			progressView.setMax(100);
			progressView.setProgress(0);
    	}
    	
    	@Override
    	protected Boolean doInBackground(String... params) {
			for (int i = 0; i <= 20; i++) {
				int progress = i * 5;
				publishProgress(progress);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		return true;
    	}
    
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		super.onProgressUpdate(values);
    		int progress = values[0];
			messageView.setText("progress : " + progress);
			progressView.setProgress(progress);
    	}
    	
    	@Override
    	protected void onPostExecute(Boolean result) {
    		super.onPostExecute(result);
			messageView.setText("progress done");
			progressView.setProgress(100);
    	}
    }
    
    Runnable progressRunnable = new Runnable() {
		
		@Override
		public void run() {
//			messageView.setText("progress start...");
//			progressView.setMax(100);
//			progressView.setProgress(0);

			// sendMessage
//			Message msg = mHandler.obtainMessage(MESSAGE_INITIAL);
//			mHandler.sendMessage(msg);
			
			mHandler.post(new InitRunnable());
			
			for (int i = 0; i <= 20; i++) {
				int progress = i * 5;
//				messageView.setText("progress : " + progress);
//				progressView.setProgress(progress);

				// sendMessage
//				msg = mHandler.obtainMessage(MESSAGE_UPDATE, progress, 0);
//				mHandler.sendMessage(msg);
				
				mHandler.post(new UpdateRunnable(progress));
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			messageView.setText("progress done");
//			progressView.setProgress(100);
			//sendMessage
//			mHandler.sendEmptyMessage(MESSAGE_DONE);
			
			mHandler.post(new DoneRunnable());
		}
	};
	
	class InitRunnable implements Runnable {
		@Override
		public void run() {
			messageView.setText("progress start...");
			progressView.setMax(100);
			progressView.setProgress(0);
		}
	}

	class UpdateRunnable implements Runnable {
		int progress;
		public UpdateRunnable(int progress) {
			this.progress = progress;
		}
		@Override
		public void run() {
			messageView.setText("progress : " + progress);
			progressView.setProgress(progress);
		}
	}
	
	class DoneRunnable implements Runnable {
		@Override
		public void run() {
			messageView.setText("progress done");
			progressView.setProgress(100);
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
