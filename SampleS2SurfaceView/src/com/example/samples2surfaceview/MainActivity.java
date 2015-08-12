package com.example.samples2surfaceview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback {

	SurfaceView surfaceView;
	Surface mSurface;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
        surfaceView.getHolder().addCallback(this);
        
        Button btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isRunning = true;
				new Thread(new DrawRunnable()).start();
			}
		});
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	isRunning = false;
    }
    
    boolean isRunning = false;
    
    class DrawRunnable implements Runnable {
    	@Override
    	public void run() {
    		while(isRunning) {
    			Surface s = mSurface;
    			if (s == null) {
    				try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				continue;
    			}
    			Canvas c = null;
    			try {
    				c = s.lockCanvas(null);
    				draw(c);
    			} catch (Exception e) {
    				
    			} finally {
    				if (c != null) {
    					s.unlockCanvasAndPost(c);
    				}
    			}
    		}
    	}
    }

    float MAX_VALUE = 300;
    float startX = 0, startY = 0, endX = MAX_VALUE, endY = 0;
    Paint mPaint = new Paint();
    
    private void draw(Canvas canvas) {
    	canvas.drawColor(Color.WHITE);
    	mPaint.setColor(Color.RED);
    	mPaint.setStrokeWidth(5);
    	
    	canvas.drawLine(startX, startY, endX, endY, mPaint);
    	
    	startY += 3;
    	endX -= 3;
    	if (startY > MAX_VALUE) {
    		startY = 0;
    	}
    	if (endX < 0) {
    		endX = MAX_VALUE;
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

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mSurface = holder.getSurface();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mSurface = holder.getSurface();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mSurface = null;
	}
}
