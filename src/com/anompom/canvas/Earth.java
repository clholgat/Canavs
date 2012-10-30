package com.anompom.canvas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class Earth extends View {
	
	//The important variables for the canvas.
	Bitmap earth;
	float xOffset = 0;
	float yOffset = 0;
	float scale = 1;
	
	//Keeping track of values for panning.
	float previousX = 0;
	float previousY = 0;
	
	//Gesture detector.
	ScaleGestureDetector gestureDetector;

	public Earth(Context context) {
		super(context);
		init();
	}
	
	public Earth(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public Earth(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void init() {
		Resources res = getContext().getResources();
		earth = BitmapFactory.decodeResource(res, R.drawable.earth);
		gestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//Scale the canvas.
		canvas.scale(scale, scale);
		
		//Draw the bitmap.
		canvas.drawBitmap(earth, xOffset/scale, yOffset/scale, null);
	}
	
	public boolean onTouchEvent(MotionEvent e) {
		//Hand off event to the scale listener
		gestureDetector.onTouchEvent(e);
		
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if(!gestureDetector.isInProgress()) {
				xOffset += x - previousX;
				yOffset += y - previousY;
			}
		}
		
		previousX = x;
		previousY = y;
		invalidate();
		return true;
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float scaleFactor = detector.getScaleFactor();
			float x = detector.getFocusX();
			float y = detector.getFocusY();
			
			//Adjust the offsets based on the center of the gesture.
			xOffset += x*(1 - scaleFactor);
			yOffset += y*(1 - scaleFactor);
			scale *= scaleFactor;
			invalidate();
			return true;
		}
	}
}
