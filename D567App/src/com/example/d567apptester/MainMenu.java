package com.example.d567apptester;

import java.util.ArrayList;
import java.util.List;

import com.d567.app.ApplicationSettings;
import com.d567.provider.*;
import com.d567.request.*;
import com.d567.db.SessionTable;
import com.d567.db.TraceTable;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainMenu extends Activity implements OnItemClickListener
{
	int selectedAppId = 0;
	String selectedAppName = "";

	List<String> appList = new ArrayList<String>();
	
	//D567 code
	protected static String LOG_TAG = "D567_APP";
	protected static String PACKAGE_NAME = "com.d567app.package_name";
	private BroadcastReceiver _packageHandler = null;
	private BroadcastReceiver _settingsHandler = null;
	
	private ArrayList<String> _packages = null;
	private ApplicationSettings _settings = null;

	private ListView lvClientApps;		

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		_packageHandler = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				Bundle extras = this.getResultExtras(false);
				if(extras == null)
				{					
					Log.e(LOG_TAG, "No packages retrieved");
					return;
				}
				
				_packages = extras.getStringArrayList(PackageListRequest.EXTRA_PACKAGE_LIST);
				if(_packages == null)
				{
					//note: this shouldn't happen
					Log.e(LOG_TAG, "Package List is NULL");
					return;
				}
				
				initListView();			
			}
		};
			
	}
	
	private class MyAdapter extends ArrayAdapter<String>
	{
		private ArrayList<String> items;
		
		public MyAdapter(Context context, int textViewResourceId, ArrayList<String> items) 
		{
			super(context, textViewResourceId, items);
			this.items = items;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = convertView;
			if (v == null)
			{
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.activity_main_row, null);
			}
			
			String s = items.get(position);
			if(s != null)
			{
				TextView tv = (TextView) v.findViewById(R.id.packageName);
				tv.setText(s);
			}
			return v;
		}
	}
	
	//generate list and display it in LVClientApps
	void initListView()
	{
		
		Log.v(LOG_TAG, "initListView started");
		
		for(int i=0; i < _packages.size(); i++){
			appList.add(_packages.get(i));
			Log.v(LOG_TAG, "package " + appList.get(i) + " added to list");
		}

		Log.v(LOG_TAG, "all packages received");

//		ListAdapter adapter = new ListAdapter(this,appList, android.R.layout.simple_list_item_1);
		MyAdapter adapter = new MyAdapter(this, R.layout.activity_main_row, _packages);

		Log.v(LOG_TAG, "adapter initialized");
		
		try 
		{
			lvClientApps = (ListView) findViewById(R.id.LVClientApps);

			lvClientApps.setAdapter( adapter);
			
			lvClientApps.setOnItemClickListener(this);
		}
		catch(Exception e)
		{
			Log.e(LOG_TAG, "Failed to set adapter", e);
			return;
		}
		Log.v("MainMenu", "lvClientApps Initialized");		
		
		
		/*
		if(lvClientApps.getCount() > 0)
		{			
			Log.v("MainMenu", "At least one Client app found.");
			Log.v("MainMenu", "initializing lvClientApps onClick");

			//set up listener for 
			lvClientApps.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					

				}
			});
		}
		else
		{
			Log.e("No apps found", "");
			
			TextView msg = (TextView) findViewById(R.id.TVMainMenuTitle);
			msg.setText("No active debuggable apps found");
				
			//Todo: add a refresh button to the top of the screen
		}
		
		*/
		

	}

	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.v(LOG_TAG, "Requesting Packages...");
		PackageListRequest.send(this,  this._packageHandler);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent i = new Intent(this, CurrentSession.class);
		
		i.putExtra(PACKAGE_NAME, _packages.get(position));

		this.startActivity(i);
		
	}

}



