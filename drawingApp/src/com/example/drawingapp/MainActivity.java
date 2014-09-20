package com.example.drawingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends Activity implements  OnClickListener {
	
	private DrawingView drawView;
	private ImageButton currentPaint, drawBTN, eraseBTN, newBTN, saveBTN;
	private float smallBrush, mediumBrush, largeBrush;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Used to be able to affect the drawingView class
		drawView = (DrawingView)findViewById(R.id.drawing);
		
		LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
		
		//this is the first imageButton 
		currentPaint = (ImageButton)paintLayout.getChildAt(0);
		
		//show that it is selected
		currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
		
		//instantiate the brush sizes
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);
		
		//define the variable for drawBTN
		drawBTN = (ImageButton)findViewById(R.id.draw_btn);
		//when the button it clicked, it calls this class, specifically the onClick Method
		drawBTN.setOnClickListener(this);
		
		
		//initial brush size is medium
		drawView.setBrushSize(mediumBrush);
		
		//instantiate eraseBTN
		eraseBTN = (ImageButton)findViewById(R.id.erase_btn);
		eraseBTN.setOnClickListener(this);
		
		//instantiate newBTN
		newBTN = (ImageButton)findViewById(R.id.new_btn);
		newBTN.setOnClickListener(this);
		
		//instantiate saveBTN
		saveBTN = (ImageButton)findViewById(R.id.save_btn);
		saveBTN.setOnClickListener(this);
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
	
    //When a paint swatch is selected
	public void paintClicked(View view){

		//Check if colour is different from currentcolour
		if( view != currentPaint){
			
			ImageButton imgView = (ImageButton)view;
			String colour = view.getTag().toString();
			
			drawView.setColor(colour);
			drawView.setErase(false);
			drawView.setBrushSize(drawView.getLastBrushSize());
			
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currentPaint=(ImageButton)view;
			
		}
		
	}

	@Override
	public void onClick(View view) {
		
		if(view.getId()==R.id.draw_btn){
		    //draw button clicked
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush Size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			brushDialog.show();
			
			
			ImageButton smallButton = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallButton.setOnClickListener(new OnClickListener( ){

				@Override
				public void onClick(View v) {
					drawView.setBrushSize(smallBrush);
					drawView.setLastBrushSize(smallBrush);
					drawView.setErase(false);
					brushDialog.dismiss();
					
				}
				
			});
			
			
			
			ImageButton mediumButton = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumButton.setOnClickListener( new OnClickListener( ){

				@Override
				public void onClick(View v) {
					drawView.setBrushSize(mediumBrush);
					drawView.setLastBrushSize(mediumBrush);
					drawView.setErase(false);
					brushDialog.dismiss();
					
				}
				
			});
			
			ImageButton largeButton = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeButton.setOnClickListener( new OnClickListener( ){

				@Override
				public void onClick(View v) {
					drawView.setBrushSize(largeBrush);
					drawView.setLastBrushSize(largeBrush);
					drawView.setErase(false);
					brushDialog.dismiss();
					
				}
				
			});
				
		}
		
		//this is for the erase button listener
		else if(view.getId() == R.id.erase_btn){
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Erase size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			brushDialog.show();
			
			ImageButton smallButton = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallButton.setOnClickListener(new OnClickListener( ){

				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setLastBrushSize(smallBrush);
					brushDialog.dismiss();
					
				}
				
			});
			
			
			
			ImageButton mediumButton = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumButton.setOnClickListener( new OnClickListener( ){

				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setLastBrushSize(mediumBrush);
					brushDialog.dismiss();
					
				}
				
			});
			
			ImageButton largeButton = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeButton.setOnClickListener( new OnClickListener( ){

				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setLastBrushSize(largeBrush);
					brushDialog.dismiss();
					
				}
				
			});
		}
		
		else if(view.getId() == R.id.new_btn){
			
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New Drawing");
			newDialog.setMessage("Create new drawing(Current one will be erased)?");
			newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					drawView.setNew();
					dialog.dismiss();
					
				}
			});
			
			newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			});
			
			newDialog.show();
		}
		
		//in the case of the save button
		else if(view.getId() == R.id.save_btn){
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save");
			saveDialog.setMessage("Do you want to save this drawing to Gallery?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				//save the drawing
					
					//enables the drawing cache on the custom view
					drawView.setDrawingCacheEnabled(true);
					
					String isSaved = MediaStore.Images.Media.insertImage(getContentResolver(), drawView.getDrawingCache(),
							UUID.randomUUID().toString()+ ".png", "drawing");
					
					if(isSaved != null){
						Toast savedToast = Toast.makeText(getApplicationContext(),
								"Drawing saved!", Toast.LENGTH_SHORT);
						savedToast.show();
					}
					
					else{
						Toast unsavedToast = Toast.makeText(getApplicationContext(),
								"Error! Drawing did not save", Toast.LENGTH_SHORT);
						unsavedToast.show();
					}
				}
			});
			
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.cancel();
					
				}
			});
			
			saveDialog.show();
			drawView.destroyDrawingCache();
		}
		
		
	}
	
	

	
}
