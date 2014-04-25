package com.d567.UI;

import com.example.d567app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


public class AppMenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//pseudocode
		//Load the Title of the given app into the header (the real name or perhaps a dummy name given by the intent?)

		//Load the trace area
		//Create the start/stop trace button 
		//Create the view trace button
		//Create the Delete Trace button
		
		//Load the Saved States area
		//Create the Save State button. This creates a copy of the debuggable app's current state
		//Create the Load State button. This restores the debuggable app to the current state
		//Create the View States button
			//launches the View States portion of the screen
			//ToDo: include a screenshot of the stored state? 
		
	}

	//I don't know if or how we'll use the options menu, but I'll leave this placeholder
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
