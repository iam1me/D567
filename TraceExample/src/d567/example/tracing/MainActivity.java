package d567.example.tracing;

import d567.db.*;
import d567.trace.*;
import java.text.MessageFormat;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity implements View.OnClickListener 
{
	protected static String LOG_TAG = "FILE_ASSIGNMENT";
	private String _sessionId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnRead = (Button)findViewById(R.id.btnRead);
		Button btnAdd = (Button)findViewById(R.id.btnAdd);
		Button btnClear = (Button)findViewById(R.id.btnClear);
		
		btnRead.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btnClear.setOnClickListener(this);
		
		try
		{
			GetSessionId();
			Toast.makeText(this,  "Session ID: " + _sessionId, Toast.LENGTH_LONG).show();
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, ex.getMessage());
			Toast.makeText(this, "Failed to get session id. " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void GetSessionId() throws Exception
	{
		Log.v(LOG_TAG, "GetSessionId");
		DBHelper dbHelper = new DBHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Log.v(LOG_TAG, "DB Name: " + dbHelper.getDBName());
		
		Log.v(LOG_TAG, "GetSessionId - Running Query");		
		Cursor c = db.query(SessionTable.TABLE_NAME, new String[] {SessionTable.KEY_ID},
				null, null, null, null, SessionTable.KEY_START);
		
		if(c.moveToFirst())
		{
			_sessionId = c.getString(0);
			Log.v(LOG_TAG, "GetSessionId - Found SessionId: " + _sessionId);			
		}
		else
		{
			Log.v(LOG_TAG, "GetSessionId - Creating New Session");
			SessionAdapter adapter = new SessionAdapter(this);
			adapter.open();
			
			SessionInfo info = adapter.CreateSession("default", true, TraceLevel.DEBUG);
			_sessionId = info.getId();
			
			adapter.close();
		}
		
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) 
	{
		EditText txt = (EditText)findViewById(R.id.editEntry);
		TextView log = (TextView)findViewById(R.id.txtLog);
		
		switch(v.getId())
		{
			case R.id.btnAdd:
				WriteToLog(txt.getText().toString());
				txt.setText("");
				break;
				
			case R.id.btnClear:
				ClearLog();
				break;
				
			case R.id.btnRead:
				log.setText(ReadLog());
				break;		
		}		
	}
	
	protected String ReadLog()
	{
		String log = "";
		
		try
		{
			TraceAdapter adapter = new TraceAdapter(this);
			adapter.open();
			List<TraceInfo> entries = adapter.getSessionTrace(_sessionId);
			adapter.close();
			
			Log.v(LOG_TAG, "Constructing Log from Trace Entries");
			for(int i = 0; i < entries.size(); i++)
			{
				TraceInfo cur = entries.get(i);
				/*Time t = new Time(Time.getCurrentTimezone());
				t.set(cur.getTime());//t.toMillis(false)*/
				log += MessageFormat.format("{0,date,short} {0,time,short} \t {1}\n", cur.getTime(), cur.getMessage());
			}
			
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, "ReadLog - " + ex.getMessage());
			Toast.makeText(this, "An error occured while reading the log", Toast.LENGTH_SHORT).show();
			return log;
		}
		
		return log;		
	}
	
	protected void WriteToLog(String line)
	{						
		try
		{		
			TraceAdapter adapter = new TraceAdapter(this);
			adapter.open();
			TraceInfo info = adapter.insertTrace(_sessionId, LOG_TAG, TraceLevel.VERBOSE, line);
			adapter.close();
			
			Log.v(LOG_TAG, "Wrote New Trace Record. ID: " + info.getId());
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, "WriteLogEntry - " + ex.getMessage());
			Toast.makeText(this, "An error occured while writing to the log", Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void ClearLog()
	{
		try
		{
			TraceAdapter adapter = new TraceAdapter(this);
			adapter.open();
			long count = adapter.deleteSessionTrace(_sessionId);
			adapter.close();
			
			Toast.makeText(this, MessageFormat.format("Deleted {0, number, integer} Log Entries",
					count), Toast.LENGTH_LONG).show();	
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, "ClearLog - " + ex.getMessage());
			Toast.makeText(this,  "An error occured while clearing the log", Toast.LENGTH_SHORT).show();
		}
	}
	
}
