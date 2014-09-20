package com.example.drawingapp;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;




public class DrawingView extends View {
	
	//the path that is drawn by user
	private Path drawPath;
	
	//drawing paint and canvas paint
	private Paint drawPaint, canvasPaint;
	
	//intial colour
	private int paintColour = 0xFF660000;
	
	//canvas
	private Canvas drawCanvas;
	
	//canvas bitmap
	private Bitmap canvasBitmap;
	
	//store the current brush size and last brush size before eraser is used
	private float brushSize, lastBrushSize;
	
	//erase checker
	private boolean erase = false;
	
	//constructor for DrawingView class
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing( );
	
		
		
	}
	
	//helper method for setting up the drawing space
	private void setupDrawing( ){
		
		brushSize = getResources().getInteger(R.integer.medium_size);	
		lastBrushSize = brushSize;
		
		drawPath = new Path( );
		drawPaint = new Paint( );
		
		drawPaint.setColor(paintColour);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		
		
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	//view given size
		super.onSizeChanged(w, h, oldw, oldh);
		
		canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	//draw view
		
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	//detect user touch     
		float touchX = event.getX( );
		float touchY = event.getY( );
		
		switch(event.getAction( )){
		
		//When user initally touches screen, start at that point
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			break;
			
		//When user moves finger, draw that path
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
			break;
		
		//When user lifts finger, reset the path
		case MotionEvent.ACTION_UP:
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		default:
			return false;
		}
		
		//executes onDraw( ) method
		invalidate();
		return true;
		
		
	}
	
	//changing the paintColour
	public void setColor(String newColor){
		//set color   
		invalidate();
		
		paintColour = Color.parseColor(newColor);
		drawPaint.setColor(paintColour);
	}
	
	
	//Change the brush size
	public void setBrushSize(float newSize){
		
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
			    newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}
	
	//set last brush size
	public void setLastBrushSize(float lastSize){
		lastBrushSize = lastSize;
	}
	
	//get last brush size
	public float getLastBrushSize( ){
		return lastBrushSize;
	}
	
	//toggle the erase function
	public void setErase(boolean isErase){
		erase = isErase;
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
	}
	
	//clear the current canvas
	public void setNew(){
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate( );
	}
}
