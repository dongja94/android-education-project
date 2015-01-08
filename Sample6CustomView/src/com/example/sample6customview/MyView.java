package com.example.sample6customview;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {

	public MyView(Context context) {
		super(context);
		init();
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private static final int LENGTH = 300;
	private static final int COUNT = 30;

	float[] points = new float[(COUNT + 1) * 2 * 2];

	Paint mPaint = new Paint();

	private void init() {
		initLinePoint();
		initPath();
		initBitmap();
	}

	Bitmap mBitmap;
	Matrix mMatrix = new Matrix();

	private void initBitmap() {
		InputStream is = getContext().getResources().openRawResource(
				R.raw.photo1);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;
		mBitmap = BitmapFactory.decodeStream(is, null, opts);
		
		Bitmap bm = Bitmap.createScaledBitmap(mBitmap, 200, 200, false);
		
		mBitmap.recycle();
		
		mBitmap = bm;
		

	}

	Path mPath;
	Path mBasePath;
	Path mArrowPath;
	
	float[] vertixs = {100, 100, 200, 50, 300, 100,
					   50, 200, 200, 200, 350, 200,
					   100, 300, 200, 350, 300, 300
	};

	private void initPath() {
		mPath = new Path();
		mPath.moveTo(200, 200);
		mPath.lineTo(100, 100);
		mPath.lineTo(300, 100);
		mPath.lineTo(400, 200);
		mPath.lineTo(300, 300);
		mPath.lineTo(100, 300);

		mBasePath = new Path();
		mBasePath.addCircle(300, 300, 200, Direction.CW);
		
		mArrowPath = new Path();
		mArrowPath.moveTo(0, 0);
		mArrowPath.lineTo(-5, -5);
		mArrowPath.lineTo(0, -5);
		mArrowPath.lineTo(5, 0);
		mArrowPath.lineTo(0, 5);
		mArrowPath.lineTo(-5, 5);
		
	}

	private void initLinePoint() {

		float delta = (float) LENGTH / COUNT;

		for (int i = 0; i <= COUNT; i++) {
			points[i * 4] = 0;
			points[i * 4 + 1] = i * delta;
			points[i * 4 + 2] = LENGTH - i * delta;
			points[i * 4 + 3] = 0;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		// drawLineAndPoint(canvas);
		// drawRect(canvas);
		// drawCircle(canvas);
		// drawArc(canvas);
		// drawPath(canvas);
//		drawText(canvas);
//		drawBitmap(canvas);
//		drawBitmapMesh(canvas);
//		drawPathEffect(canvas);
//		drawColor(canvas);
//		drawShader(canvas);
		drawColorFilter(canvas);
	}
	
	private void drawColorFilter(Canvas canvas) {
		ColorMatrix m = new ColorMatrix();
		m.setSaturation(0);
		
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(m);
		mPaint.setColorFilter(filter);
		
		canvas.drawBitmap(mBitmap, 100, 100, mPaint);
	}
	
	private void drawShader(Canvas canvas) {
//		int[] colors = {Color.RED, Color.YELLOW, Color.BLUE};
//		float[] position = {0, 0.3f, 1};
//		LinearGradient shader = new LinearGradient(50, 0, 150, 0, colors, position, TileMode.REPEAT);
//		RadialGradient shader = new RadialGradient(100, 100, 100, Color.RED, Color.BLUE, TileMode.CLAMP);
		int[] colors = {Color.RED, Color.BLUE,Color.RED};
		SweepGradient shader = new SweepGradient(100, 100, colors, null);
		mPaint.setShader(shader);
		canvas.save();
		canvas.translate(100, 100);
		canvas.drawCircle(100, 100, 100, mPaint);
		canvas.restore();
	}
	
	
	private void drawColor(Canvas canvas) {
		int color = Color.argb(0x80, 0xFF, 0x80, 0x00);
		mPaint.setColor(color);
		canvas.drawRect(100, 100, 300, 300, mPaint);
		mPaint.setAlpha(0x80);
		canvas.drawBitmap(mBitmap, 200, 200, mPaint);
	}
	
	private void drawPathEffect(Canvas canvas) {
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(10);
		
//		float[] intervals = {20, 10, 10, 5};
//		DashPathEffect patheffect = new DashPathEffect(intervals, 10);
		
		PathDashPathEffect patheffect = new PathDashPathEffect(mArrowPath, 10, 0, PathDashPathEffect.Style.ROTATE);
		mPaint.setPathEffect(patheffect);
//		canvas.drawRect(100, 100, 300, 300, mPaint);
		canvas.drawCircle(200, 200, 100, mPaint);
	}
	
	int delta = 0;
	boolean direction = true;
	private void drawBitmapMesh(Canvas canvas) {
		if (direction) {
			delta++;
			if (delta > 20) {
				direction = false;
			}
		} else {
			delta--;
			if (delta < -20) {
				direction = true;
			}
		}
		vertixs[8] = 200 + delta;
		vertixs[9] = 200 - delta;
		canvas.drawBitmapMesh(mBitmap, 2, 2, vertixs, 0, null, 0, mPaint);
		invalidate();
	}

	private void drawBitmap(Canvas canvas) {
		mMatrix.setScale(1, -1, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
		mMatrix.postTranslate(100, 300);
		mMatrix.postSkew(0.5f, 0, 100, 300);
		canvas.drawBitmap(mBitmap, mMatrix, mPaint);
		
		canvas.drawBitmap(mBitmap, 100,  100, mPaint);
	}
	
	float hOffset;
	private static final String HELLO_ANDROID = "Hello, Android!!!";

	private void drawText(Canvas canvas) {
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(60);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(2);
		mPaint.setTextSkewX(-0.5f);

		canvas.drawText(HELLO_ANDROID, 0, 60, mPaint);

		canvas.drawTextOnPath(HELLO_ANDROID, mBasePath, hOffset, 0, mPaint);
		hOffset += 5;
		if (hOffset > 200 * Math.PI)
			hOffset = 0;
		invalidate();
	}

	private void drawPath(Canvas canvas) {
		mPaint.setColor(Color.GREEN);

		canvas.drawPath(mPath, mPaint);
	}

	private void drawArc(Canvas canvas) {

		mPaint.setColor(Color.CYAN);

		RectF rect = new RectF(100, 100, 300, 200);
		canvas.drawArc(rect, 30, 120, true, mPaint);

		rect = new RectF(100, 300, 300, 400);
		canvas.drawArc(rect, 30, 120, false, mPaint);

	}

	private void drawCircle(Canvas canvas) {
		mPaint.setColor(Color.BLUE);

		canvas.drawCircle(200, 200, 100, mPaint);

		RectF rect = new RectF(100, 400, 300, 500);
		canvas.drawOval(rect, mPaint);
	}

	private void drawRect(Canvas canvas) {
		mPaint.setColor(Color.DKGRAY);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(10);

		canvas.drawRect(100, 100, 300, 300, mPaint);

		RectF rect = new RectF(100, 400, 300, 600);
		canvas.drawRoundRect(rect, 20, 30, mPaint);
	}

	private void drawLineAndPoint(Canvas canvas) {
		canvas.save();

		canvas.translate(100, 100);
		canvas.rotate(30);

		mPaint.setAntiAlias(true);

		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(2);
		canvas.drawLines(points, mPaint);

		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(5);
		canvas.drawPoints(points, mPaint);

		canvas.restore();
	}

}
