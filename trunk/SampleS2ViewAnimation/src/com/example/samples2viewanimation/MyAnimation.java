package com.example.samples2viewanimation;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MyAnimation extends Animation {
	int widthLength, heightLength;
	
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		widthLength = parentWidth - width;
		heightLength = parentHeight - height;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		
		float x = interpolatedTime * widthLength;
		float y = interpolatedTime * interpolatedTime * heightLength;
		t.getMatrix().setTranslate(x, y);
		
	}
}
