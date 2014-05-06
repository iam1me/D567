package com.d567.UI;

import com.d567app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainMenu extends Activity {

	//variables for repeater
	int selectedAppId = 0;
	String selectedAppName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
	
		
		Log.e("MainMenu", "OnCreate Initialized");
		
		
		//create intent to look for active debuggable apps.
//		Intent activeApps = new Intent(getApplicationContext(), SelectedApp.class );

		Log.e("MainMenu", "activeApps Intent Initialized");

		
		//generate list and display it in LVClientApps
		final ListView lvClientApps = (ListView) findViewById(R.id.LVClientApps);
		
		Log.e("MainMenu", "lvClientApps Initialized");

		
		if(lvClientApps.getCount() > 0)
		{
			
			Log.e("MainMenu", "At least one Client app found.");
			Log.e("MainMenu", "initializing lvClientApps onClick");

			//set up listener for 
			lvClientApps.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Log.e("MainMenu", "lvClientApps onClick initialized");
					
					selectedAppId = (int) lvClientApps.getSelectedItemId();
					selectedAppName = lvClientApps.getSelectedItem().toString();

					Log.e("MainMenu", "selectedAppId = " + selectedAppId);
					Log.e("MainMenu", "selectedAppName = " + selectedAppName);
					
					Intent nextScreen = new Intent(getApplicationContext(), SelectedApp.class);
					
					//send info about the selected app to the other activity
					nextScreen.putExtra("selectedAppId", selectedAppId);
					nextScreen.putExtra("selectedAppName", selectedAppName);

					Log.e("MainMenu", "Starting SelectedApp...");
					startActivity(nextScreen);
				}
			});
		}
		else
		{
			Log.e("No apps found", "");
			
		//  else report no debuggable apps found 
		
			//add a "no debuggable apps found" item to the array
			
			//Todo: add a refresh button to the top of the screen
		}
		
	}

	
	
	//I don't know if or how we'll use the options menu, but I'll leave this placeholder	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		//creates the main menu. I don't know if we'll use this just yet.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
