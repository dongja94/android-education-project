package com.example.samples2camera;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;


public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback {

	SurfaceView surfaceView;
	SurfaceHolder mHolder;
	Camera mCamera;
	int direction = CameraInfo.CAMERA_FACING_FRONT;
	Gallery gallery;
	MyAdapter mAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallery = (Gallery)findViewById(R.id.gallery1);
        mAdapter = new MyAdapter();
        gallery.setAdapter(mAdapter);
        
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
        surfaceView.getHolder().addCallback(this);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        openCamera();
        
        Button btn = (Button)findViewById(R.id.btn_change);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openCamera();
				if (mHolder != null) {
					try {
						mCamera.setPreviewDisplay(mHolder);
						mCamera.startPreview();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
        
        btn = (Button)findViewById(R.id.btn_effect);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Camera.Parameters params = mCamera.getParameters();
				List<String> effectlist = params.getSupportedColorEffects();
				final String[] effects = effectlist.toArray(new String[effectlist.size()]);
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("select color effect");
				builder.setItems(effects, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String effect = effects[which];
						Camera.Parameters params = mCamera.getParameters();
						params.setColorEffect(effect);
						mCamera.setParameters(params);
					}
				});
				builder.create().show();
				
			}
		});
        
        btn = (Button)findViewById(R.id.btn_picture);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCamera.takePicture(shutter, raw, jpeg);
			}
		});
    }
    
    
    Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mCamera.startPreview();
				}
			}, 500);
		}
	};
	
	Handler mHandler = new Handler(Looper.getMainLooper());
	
	Camera.PictureCallback raw = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
		}
	};
	
	Camera.PictureCallback jpeg = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 4;
			Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			mAdapter.add(bm);
		}
	};
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (mCamera != null) {
    		mCamera.release();
    		mCamera = null;
    	}
    }
    
    private void openCamera() {
    	if (mCamera != null) {
    		mCamera.release();
    		mCamera = null;
    	}
    	direction = (direction == CameraInfo.CAMERA_FACING_FRONT)? CameraInfo.CAMERA_FACING_BACK : CameraInfo.CAMERA_FACING_FRONT;
    	mCamera = Camera.open(direction);
    	mCamera.setDisplayOrientation(90);
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

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mHolder = holder;
		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mCamera.stopPreview();
		
		mHolder = holder;
		
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHolder = null;
		mCamera.stopPreview();
	}
}
