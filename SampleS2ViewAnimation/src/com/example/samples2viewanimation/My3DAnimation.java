package com.example.samples2viewanimation;

import android.graphics.Camera;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class My3DAnimation extends Animation {
	
	Camera mCamera = new Camera();

	int centerX, centerY;
	
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		centerX = width /2;
		centerY = height /2;
		
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		
		mCamera.save();
		
		mCamera.rotateX(60 * interpolatedTime);
		
		mCamera.getMatrix(t.getMatrix());
		
		mCamera.restore();
		
		t.getMatrix().preTranslate(-centerX, -centerY);
		t.getMatrix().postTranslate(centerX, centerY);
	}
}
