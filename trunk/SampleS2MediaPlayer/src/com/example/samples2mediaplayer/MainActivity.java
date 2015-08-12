package com.example.samples2mediaplayer;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
	
	SeekBar progressView, volumeView;
	AudioManager mAM;
	CheckBox muteView;
	
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
	
	float volume = 1.0f;
	
	Runnable volumeUp = new Runnable() {
		
		@Override
		public void run() {
			if (volume < 1.0f) {
				mPlayer.setVolume(volume, volume);
				volume += 0.1f;
				mHandler.postDelayed(this, INTERVAL);
			} else {
				volume = 1.0f;
				mPlayer.setVolume(volume, volume);
			}
		}
	};
	
	Runnable volumeDown = new Runnable() {
		
		@Override
		public void run() {
			if (volume > 0) {
				mPlayer.setVolume(volume, volume);
				volume -= 0.1f;
				mHandler.postDelayed(this, INTERVAL);
			} else {
				volume = 0;
				mPlayer.setVolume(volume, volume);
			}
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = (SeekBar)findViewById(R.id.seek_progress);
        volumeView = (SeekBar)findViewById(R.id.seek_volume);
        mAM = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAM.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAM.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeView.setMax(maxVolume);
        volumeView.setProgress(currentVolume);
        volumeView.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					mAM.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
				}
			}
		});
        
        muteView = (CheckBox)findViewById(R.id.check_mute);
        muteView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
//					mPlayer.setVolume(0, 0);
					mHandler.removeCallbacks(volumeUp);
					mHandler.post(volumeDown);
				} else {
//					mPlayer.setVolume(1, 1);
					mHandler.removeCallbacks(volumeDown);
					mHandler.post(volumeUp);
				}
			}
		});
        
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
        
        btn = (Button)findViewById(R.id.btn_list);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent  intent = new Intent(MainActivity.this, MusicListActivity.class);
				startActivityForResult(intent, 0);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
    		Uri uri = data.getData();
    		mPlayer.reset();
    		mState = PlayState.IDLE;
    		try {
				mPlayer.setDataSource(this, uri);
				mState = PlayState.INITILIZED;
				mPlayer.prepare();
				mState = PlayState.PREPARED;
				progressView.setMax(mPlayer.getDuration());
				progressView.setProgress(0);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
		if (mState == PlayState.STARTED) {
			mPlayer.pause();
			mState = PlayState.PAUSED;
		}
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
