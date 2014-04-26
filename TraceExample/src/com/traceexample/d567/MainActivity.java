package com.traceexample.d567;

import com.d567.app.*;
import com.d567.tracesession.*;
import java.text.MessageFormat;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity implements View.OnClickListener 
{
	protected static String LOG_TAG = "D567_TRACE_EXAMPLE";
	
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
			Application.init(this, new MySettings());
			Toast.makeText(this,  "Session ID: " + Application.getSessionId(), Toast.LENGTH_LONG).show();
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, ex.getMessage());
			Toast.makeText(this, "Failed to Init D567 Application. " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onStart()
	{
		Application.onStart();
		super.onStart();
	}
	
	@Override
	public void onPause()
	{
		Application.onPause();
		super.onPause();
	}
	
	@Override
	public void onResume()
	{
		Application.onResume();
		super.onResume();
	}
	
	@Override
	public void onStop()
	{
		Application.onStop();
		super.onStop();
	}
	
	@Override
	public void onDestroy()
	{
		Application.onDestroy();
		super.onDestroy();
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
			List<TraceInfo> entries = adapter.getSessionTrace(Application.getSessionId());
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
			Trace.Verbose(LOG_TAG, line);
			Log.v(LOG_TAG, "Wrote New Trace Record");
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
			long count = adapter.deleteSessionTrace(Application.getSessionId());
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
