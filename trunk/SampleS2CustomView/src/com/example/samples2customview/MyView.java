package com.example.samples2customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	private void init() {
		mPaint = new Paint();
		initPoints();
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(Color.WHITE);
		
		drawLineAndPoint(canvas);
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
