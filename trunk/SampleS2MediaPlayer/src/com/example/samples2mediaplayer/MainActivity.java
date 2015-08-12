package com.example.samples2mediaplayer;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	enum PlayState {
		IDLE,
		INITILIZED,
		PREPARED,
		STARTED,
		PAUSED,
		STOPPED,
		END,
		ERROR
	}
	
	PlayState mState = PlayState.IDLE;
	
	MediaPlayer mPlayer;
	
	SeekBar progressView;
	Handler mHandler = new Handler(Looper.getMainLooper());
	private static final int INTERVAL = 100;
	
	boolean progressPressed = false;
	
	Runnable progressRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mState == PlayState.STARTED) {
				if (!progressPressed) {
					int progress = mPlayer.getCurrentPosition();
					progressView.setProgress(progress);
				}
				mHandler.postDelayed(this, INTERVAL);
			}
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = (SeekBar)findViewById(R.id.seek_progress);
        
        mPlayer = new MediaPlayer();
        mState = PlayState.IDLE;
        
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.winter_blues);
        try {
			mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mState = PlayState.INITILIZED;
			mPlayer.prepare();
			mState = PlayState.PREPARED;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
//        mPlayer = MediaPlayer.create(this, R.raw.winter_blues);
//        mState = PlayState.PREPARED;
        if (mState == PlayState.PREPARED) {
        	int duration = mPlayer.getDuration();
        	progressView.setMax(duration);
        }
        
        progressView.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress;
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (progress != -1 && mState == PlayState.STARTED) {
					mPlayer.seekTo(progress);
				}
				progressPressed = false;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				progress = -1;
				progressPressed = true;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					this.progress = progress;
				}
			}
		});
        
        
        Button btn = (Button)findViewById(R.id.btn_play);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mState == PlayState.INITILIZED || mState == PlayState.STOPPED) {
					try {
						mPlayer.prepare();
						mState = PlayState.PREPARED;
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if (mState == PlayState.PREPARED || mState == PlayState.PAUSED) {
					mPlayer.seekTo(progressView.getProgress());
					mPlayer.start();
					mState = PlayState.STARTED;
					mHandler.post(progressRunnable);
				}
			}
		});

        btn = (Button)findViewById(R.id.btn_pause);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mState == PlayState.STARTED) {
					mPlayer.pause();
					mState = PlayState.PAUSED;
				}
			}
		});
        
        btn = (Button)findViewById(R.id.btn_stop);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mState == PlayState.STARTED || mState == PlayState.PAUSED || mState == PlayState.PREPARED) {
					mPlayer.stop();
					mState = PlayState.STOPPED;
					progressView.setProgress(0);
				}
			}
		});
        
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mState = PlayState.PAUSED;
			}
		});
        
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(MainActivity.this, "on error", Toast.LENGTH_SHORT).show();
				mState = PlayState.ERROR;
				return false;
			}
		});
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (mPlayer != null) {
    		mPlayer.release();
    		mPlayer = null;
    		mState = PlayState.END;
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
