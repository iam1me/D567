package com.d567.UI;


import com.d567app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;



public class SelectedApp extends Activity {
	
	int selectedAppId = 0;
	String selectedAppName = "";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selected_app);
				
		//read the intent from main menu
		Intent i = getIntent();
		
		final int selectedAppId = i.getIntExtra("selectedAppId", 0);
		final String selectedAppName = i.getStringExtra("selectedAppName");
		
		
		Button btnSessions = (Button) findViewById(R.id.btnSessions);
		Button btnStates = (Button) findViewById(R.id.btnStates); 
		Button btnViewSettings = (Button) findViewById(R.id.btnViewSettings); //toDo: verify this button.
		//Button btnClose = (Button) findViewById(R.id.btnClose);
		

		
		if(selectedAppId > 0)
		{
			//set up listener for btnSessions 
			btnSessions.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						
					Intent openSessionsMenu = new Intent(getApplicationContext(), SessionMenu.class);
					
					//send info about the selected app to the other activity
					openSessionsMenu.putExtra("selectedAppId", selectedAppId);
					openSessionsMenu.putExtra("selectedAppName", selectedAppName);
					
					startActivity(openSessionsMenu);
				}
			});
			
			
			//set up listener for btnStates (Wip)
			btnSessions.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						
					Intent openSessionsMenu = new Intent(getApplicationContext(), SessionMenu.class);
					
					//send info about the selected app to the other activity
					openSessionsMenu.putExtra("selectedAppId", selectedAppId);
					openSessionsMenu.putExtra("selectedAppName", selectedAppName);
					
					startActivity(openSessionsMenu);
				}
			});		
			
			
			//set up listener for btnSessions (Wip)
			btnSessions.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						
					Intent openSessionsMenu = new Intent(getApplicationContext(), SessionMenu.class);
					
					//send info about the selected app to the other activity
					openSessionsMenu.putExtra("selectedAppId", selectedAppId);
					openSessionsMenu.putExtra("selectedAppName", selectedAppName);
					
					startActivity(openSessionsMenu);
				}
			});
			
		}
		else
		{
		
		//  else report no debuggable apps found 
		
			//add a "no debuggable apps found" item to the array
			
			//Todo: add a refresh button to the top of the screen
		}

		
	}

	//I don't know if or how we'll use the options menu, but I'll leave this placeholder
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
