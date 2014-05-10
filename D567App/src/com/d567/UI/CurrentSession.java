package com.d567.ui;

import java.util.ArrayList;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CurrentSession extends Activity 
{

	protected static String LOG_TAG = "D567_APP";
	protected static String EXTRA_PACKAGE_NAME = "com.d567app.package_name";
	
	private ArrayList<String> _sessions = null;
	
	private ListView lvSessions;
	
	private String _package_name = "";
	
	private BroadcastReceiver _settingsHandler = null;
	private ApplicationSettings _settings = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_session);
		
		Log.v(LOG_TAG, "onCreate");
		
		Intent i = getIntent();
		
		Log.v(LOG_TAG, "got intent");
		
		_package_name = i.getStringExtra(EXTRA_PACKAGE_NAME);
				
		Log.v(LOG_TAG, "onCreate: _package_name = " + _package_name);
		
		_settingsHandler = new BroadcastReceiver() 
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{			
				Log.v(LOG_TAG, "OnRecieve");

				Bundle extras = this.getResultExtras(false);			
				if(extras == null)
				{
					Log.e(LOG_TAG, "No settings were retrieved.");
					return;
				}
				
				_settings = new BundledSettings(extras);
				BundledSettings.LogApplicationSettings(LOG_TAG, _settings);
				
				try 
				{
					String session = GetSessionId();
					
					ArrayList<String> trace = getTrace(session);
					
					MyAdapter adapter = new MyAdapter(context, R.layout.current_session_row, trace);
					
					ListView lv = (ListView) findViewById(R.id.LVCurrentSessionTrace);
					lv.setAdapter(adapter);
				}
				catch(Exception e)
				{
					Log.e(LOG_TAG, "Exception: ", e);
				
				}
				
				
			}			
		};
		
		Log.v(LOG_TAG, "onCreate complete");
	}
	
	private String GetSessionId() 
	{
		Log.v(LOG_TAG, "GetSessionId");
		
		Cursor c;
		try {
			String[] columns = new String[] {SessionTable.KEY_ID};
			c = getContentResolver().query(SessionContract.GetUri(_settings.getAuthority()),columns, null, null, SessionTable.KEY_START+" DESC");
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG,  "Failed to Query for Sessions", ex);
			return null;
		}
		
		Log.v(LOG_TAG, "Found " + c.getCount() + " Sessions:");
		
		if(!c.moveToFirst())
		{
			Log.v(LOG_TAG, "Failed to move to first result");
			c.close();
			return null;
		}	
		
		String sessionId = c.getString(0);
		
		c.close();
		
		return sessionId;		
	}
	
	@Override
	public void onStart()
	{
		Log.v(LOG_TAG, "_package_name = \"" + _package_name + "\"");
		
		super.onStart();
		Log.v(LOG_TAG, "Requesting settings...");
		SettingsRequest.send(this, _package_name, _settingsHandler);
		Log.v(LOG_TAG, "Request sent");
	
	}
	
	
	protected ArrayList<String> getTrace(String session)
	{				
		Log.v(LOG_TAG, "----------------------");
		Log.v(LOG_TAG, "Trace for Session " + session);
		
		Cursor c;
		try
		{
			String columns[] = new String[] {TraceTable.KEY_MESSAGE};
			String sortBy = TraceTable.KEY_TIME + " DESC";
			
			Uri traceUri = TraceContract.GetUri(_settings.getAuthority())
							.buildUpon()
							.appendQueryParameter(TraceContract.PARAM_SESSION_ID, session)
							.build();								
			
			c = getContentResolver().query(traceUri, columns, null, null, sortBy);
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, "Failed to get Session Trace", ex);
			return null;
		}
		
		if(c.getCount() == 0)
		{
			Log.i(LOG_TAG, "No Trace for this Session");
			c.close();
			return null;
		}
		
		if(!c.moveToFirst())
		{
			Log.e(LOG_TAG, "Failed to move to first result");
			c.close();
			return null;
		}
		
		int j = 0;
		
		ArrayList<String> ret = new ArrayList<String>();  
		
		
		while(!c.isAfterLast())
		{
			ret.add(j, c.getString(0));
			
			j++;
			
			c.moveToNext();
			
		}
		
		c.close();
		return ret;
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
				v = vi.inflate(R.layout.current_session_row, null);
			}
			
			String s = items.get(position);
			if(s != null)
			{
				TextView tv = (TextView) v.findViewById(R.id.traceMsg);
				tv.setText(s);
			}
			return v;
		}
	}

	
}


