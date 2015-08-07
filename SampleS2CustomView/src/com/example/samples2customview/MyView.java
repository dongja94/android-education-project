package com.example.samples2customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MyView extends View {

	public MyView(Context context) {
		super(context);
		
		init();
	}
	
	Paint mPaint;
	private static final float LINE_LENGTH = 300;
	private static final float INTERVAL = 10;
	float[] points;
	Path shapePath, textPath, arrowPath;
	String message = "Hello, Android";
	
	Bitmap mBitmap;
	Matrix mMatrix;

	GestureDetector mDetector;
	
	private void init() {
		mPaint = new Paint();
		mDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				mMatrix.postTranslate(-distanceX, -distanceY);
				invalidate();
				return true;
			}
			
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				mMatrix.postScale(2, 2, e.getX(), e.getY());
				invalidate();
				return true;
			}
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				mMatrix.postScale(0.5f, 0.5f, e.getX(), e.getY());
				invalidate();
				return true;
			}
		});
		
		initPoints();
		initPath();
		initBitmap();
	}
	
	float[] meshPoints = {100, 100, 150, 150, 200, 150, 250, 100,
						  100, 300, 150, 250, 200, 250, 250, 300};
	
	
	private void initBitmap() {
	
		mMatrix = new Matrix();
		mMatrix.reset();
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo1);
//		InputStream is = getResources().openRawResource(R.drawable.photo1);
//		mBitmap = BitmapFactory.decodeStream(is);
	}
	
	private void initPoints() {
		int count = (int)(LINE_LENGTH / INTERVAL) + 1;
		points = new float[count * 4];
		for (int i = 0; i < count; i++) {
			points[i * 4] = 0;
			points[i * 4 + 1] = i * INTERVAL;
			points[i * 4 + 2] = LINE_LENGTH - i * INTERVAL;
			points[i * 4 + 3] = 0;
		}
	}
	
	private void initPath() {
		shapePath = new Path();
		
		shapePath.moveTo(100,100);
		shapePath.lineTo(50, 50);
		shapePath.lineTo(150, 50);
		shapePath.lineTo(200, 100);
		shapePath.lineTo(150, 150);
		shapePath.lineTo(50, 150);
		
		textPath = new Path();
		textPath.addCircle(200, 200, 100, Direction.CW);
		
		arrowPath = new Path();
		arrowPath.moveTo(0, 0);
		arrowPath.lineTo(-5, -5);
		arrowPath.lineTo(0, -5);
		arrowPath.lineTo(5, 0);
		arrowPath.lineTo(0, 5);
		arrowPath.lineTo(-5, 5);;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean isConsumed = mDetector.onTouchEvent(event);
		return isConsumed | super.onTouchEvent(event);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(Color.TRANSPARENT);
		
//		drawLineAndPoint(canvas);
//		drawPath(canvas);
//		drawText(canvas);
//		drawBitmap(canvas);
//		drawBitmapMesh(canvas);
//		drawPathEffect(canvas);
//		drawShader(canvas);
		
		drawColorFilter(canvas);
	}
	
	
	private void drawColorFilter(Canvas canvas) {

		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorFilter cf = new ColorMatrixColorFilter(cm);
		mPaint.setColorFilter(cf);
		canvas.drawBitmap(mBitmap, mMatrix, mPaint);
	}

	private void drawShader(Canvas canvas) {
		
		int[] colors = {Color.RED, Color.YELLOW, Color.BLUE};
		float[] positions = {0, 0.3f, 1};
//		Shader shader = new LinearGradient(100, 200, 300, 200, Color.RED, Color.BLUE, TileMode.CLAMP);
		Shader shader = new LinearGradient(100, 200, 300, 200, colors, positions, TileMode.CLAMP);
		mPaint.setShader(shader);
		canvas.drawCircle(200, 200, 100, mPaint);
	}

	private void drawPathEffect(Canvas canvas) {
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(10);
		
		float[] intervals = {10, 10, 20, 10};
		int phase = 10;
//		PathEffect effect = new DashPathEffect(intervals, phase);
		PathEffect effect = new PathDashPathEffect(arrowPath, 10, 0, PathDashPathEffect.Style.ROTATE);
		mPaint.setPathEffect(effect);
		
		canvas.drawCircle(200, 200, 100, mPaint);
	}

	private void drawBitmapMesh(Canvas canvas) {
		canvas.drawBitmapMesh(mBitmap, 3, 1, meshPoints, 0, null, 0, mPaint);
	}

	private void drawBitmap(Canvas canvas) {
		
//		canvas.drawBitmap(mBitmap, 100, 100, mPaint);
//		mMatrix.setRotate(45, mBitmap.getWidth()/2, mBitmap.getHeight()/2);
		mMatrix.setScale(1, -1, mBitmap.getWidth()/2, mBitmap.getHeight()/2);
		mMatrix.postTranslate(100, 100);
		canvas.drawBitmap(mBitmap, mMatrix, mPaint);
	}

	private void drawText(Canvas canvas) {
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.STROKE);
		mPaint.setTextSkewX(-0.5f);
		mPaint.setTextSize(80);
		mPaint.setStrokeWidth(3);
//		canvas.drawText(message, 100, 100, mPaint);
		canvas.drawTextOnPath(message, textPath, 0, 0, mPaint);
	}

	private void drawPath(Canvas canvas) {
		mPaint.setColor(Color.GREEN);
		canvas.drawPath(shapePath, mPaint);
	}

	private void drawLineAndPoint(Canvas canvas) {
		
		canvas.save();
		
		canvas.translate(100, 100);
//		canvas.rotate(45);
//		canvas.skew(0, 1);
//		canvas.scale(1, -0.5f);
		
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(3);
		canvas.drawLines(points, mPaint);

		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(5);
		canvas.drawPoints(points, mPaint);
		
		canvas.restore();
		
	}
	

}
