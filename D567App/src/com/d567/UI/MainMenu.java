package com.d567.UI;

import com.example.d567app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//pseudocode 
		//	look for intents from debuggable apps
		//	if at least one app is present
		//		generate list and display it in LVClientApps
		//  else report no debuggable apps found 
	}
	
	//I don't know if or how we'll use the options menu, but I'll leave this placeholder	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		//creates the main menu. I don't know if we'll use this just yet.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
