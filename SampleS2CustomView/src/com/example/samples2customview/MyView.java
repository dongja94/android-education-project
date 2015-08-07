package com.example.samples2customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.view.View;

public class MyView extends View {

	public MyView(Context context) {
		super(context);
		
		init();
	}
	
	Paint mPaint;
	private static final float LINE_LENGTH = 300;
	private static final float INTERVAL = 10;
	float[] points;
	Path shapePath, textPath;
	String message = "Hello, Android";
	
	Bitmap mBitmap;
	Matrix mMatrix;
	
	private void init() {
		mPaint = new Paint();
		initPoints();
		initPath();
		initBitmap();
	}
	
	float[] meshPoints = {100, 100, 150, 150, 200, 150, 250, 100,
						  100, 300, 150, 250, 200, 250, 250, 300};
	
	
	private void initBitmap() {
	
		mMatrix = new Matrix();
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
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(Color.WHITE);
		
//		drawLineAndPoint(canvas);
//		drawPath(canvas);
//		drawText(canvas);
//		drawBitmap(canvas);
		drawBitmapMesh(canvas);
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
