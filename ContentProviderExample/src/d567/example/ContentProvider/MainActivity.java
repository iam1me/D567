package d567.example.ContentProvider;

import com.d567.provider.*;
import com.d567.tracesession.*;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity 
{	
	protected static String LOG_TAG = "D567_EXAMPLE_CONTENT_PROVIDER";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String[] sessions = GetSessionIds();
		PrintTrace(sessions);
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
			c = getContentResolver().query(SessionContract.GetUri(),columns, null, null, null);
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
		
		return sessions;		
	}
	
	protected void PrintTrace(String[] sessions)
	{
		Log.v(LOG_TAG, "PrintTrace");
		
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
				
				Uri traceUri = TraceContract.GetUri()
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
				continue;
			}
			
			if(!c.moveToFirst())
			{
				Log.e(LOG_TAG, "Failed to move to first result");
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
		}
		
	}

}
