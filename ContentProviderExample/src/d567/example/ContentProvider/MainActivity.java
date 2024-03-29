package d567.example.ContentProvider;

import java.util.ArrayList;

import com.d567.app.ApplicationSettings;
import com.d567.provider.*;
import com.d567.request.*;
import com.d567.db.SessionTable;
import com.d567.db.TraceTable;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity 
{	
	protected static String LOG_TAG = "D567_EXAMPLE_CONTENT_PROVIDER";
	
	private BroadcastReceiver _packageHandler = null;
	private BroadcastReceiver _settingsHandler = null;
	
	private ArrayList<String> _packages = null;
	private ApplicationSettings _settings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_packageHandler = new BroadcastReceiver()
			{
				@Override
				public void onReceive(Context context, Intent intent) 
				{
					Bundle extras = this.getResultExtras(false);
					if(extras == null)
					{
						Log.e(LOG_TAG, "No packages were retreived");
						return;
					}
					
					_packages = extras.getStringArrayList(PackageListRequest.EXTRA_PACKAGE_LIST);
					if(_packages == null)
					{
						Log.e(LOG_TAG, "Package List is NULL");
						return;
					}
					
					Log.v(LOG_TAG, "--------Package List--------");
					for(int i = 0; i < _packages.size(); i ++)
					{
						Log.v(LOG_TAG, i + ". " + _packages.get(i));
					}
					Log.v(LOG_TAG, "----------------------------\n\n");				
										
					for(int i = 0; i < _packages.size(); i++)
					{
						Log.v(LOG_TAG, "Getting Settings for " + _packages.get(i));
						SettingsRequest.send(context, _packages.get(i), _settingsHandler);
						Log.v(LOG_TAG,"\n");
					}
				}			
			};		
		
		_settingsHandler = new BroadcastReceiver() 
			{
				@Override
				public void onReceive(Context context, Intent intent) 
				{						
					Bundle extras = this.getResultExtras(false);			
					if(extras == null)
					{
						Log.e(LOG_TAG, "No settings were retrieved.");
						return;
					}
					
					_settings = new BundledSettings(extras);
					BundledSettings.LogApplicationSettings(LOG_TAG, _settings);
					
					String[] sessions = GetSessionIds();
					PrintTrace(sessions);
				}
			};
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.v(LOG_TAG, "Requesting Packages...");
		PackageListRequest.send(this, this._packageHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected String[] GetSessionIds()
	{
		Log.v(LOG_TAG, "GetSessionIds");
				
		Cursor c;		
		try
		{		
			String[] columns = new String[] {SessionTable.KEY_ID};
			c = getContentResolver().query(SessionContract.GetUri(_settings.getAuthority()),columns, null, null, null);
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
		
		String[] sessions = new String[c.getCount()];
		int i = 0;
		while(!c.isAfterLast())
		{
			sessions[i] = c.getString(0);
			Log.v(LOG_TAG, (i + 1) + ". " + sessions[i]);
			i++;
			c.moveToNext();
		}		
		
		c.close();
		return sessions;		
	}
	
	protected void PrintTrace(String[] sessions)
	{		
		if(sessions == null)
		{
			Log.e(LOG_TAG, "PrintTrace - sessions is NULL");
		}
		
		String cur;
		for(int i = 0; i < sessions.length; i++)
		{
			cur = sessions[i];
			Log.v(LOG_TAG, "----------------------");
			Log.v(LOG_TAG, "Trace for Session " + cur);
			
			Cursor c;
			try
			{
				String columns[] = new String[] {TraceTable.KEY_MESSAGE};
				String sortBy = TraceTable.KEY_TIME + " DESC";
				
				Uri traceUri = TraceContract.GetUri(_settings.getAuthority())
								.buildUpon()
								.appendQueryParameter(TraceContract.PARAM_SESSION_ID, cur)
								.build();								
				
				c = getContentResolver().query(traceUri, columns, null, null, sortBy);
			}
			catch(Exception ex)
			{
				Log.e(LOG_TAG, "Failed to get Session Trace", ex);
				continue;
			}
			
			if(c.getCount() == 0)
			{
				Log.i(LOG_TAG, "No Trace for this Session");
				c.close();
				continue;
			}
			
			if(!c.moveToFirst())
			{
				Log.e(LOG_TAG, "Failed to move to first result");
				c.close();
				continue;
			}
			
			int j = 1;
			while(!c.isAfterLast())
			{
				String message = c.getString(0);
				Log.v(LOG_TAG, j + ". " + message);
				
				j++;				
				c.moveToNext();
			}
			
			c.close();
		}
		
	}

}
