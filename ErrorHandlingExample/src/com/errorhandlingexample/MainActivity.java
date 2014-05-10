package com.errorhandlingexample;

import com.d567.app.Application;
import com.d567.app.Trace;
import com.errorhandlingexample.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener
{
	private final static String LOG_TAG = "TRANSITION_EXAMPLE_MAIN_ACTIVITY";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try 
		{
			if(!Application.isSessionRunning())
			{
				Log.d(LOG_TAG, "Starting New Session...");
				Application.startSession("MainActivity");
			}
		} 
		catch (Exception e) 
		{
			Log.e(LOG_TAG, "Failed to start session", e);
		}
		
		Trace.Verbose(LOG_TAG, "onCreate. Class: \"{0}\" Package: \"{1}\"",
				this.getClass().getName(), this.getPackageName());
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		
		Trace.Verbose(LOG_TAG, "onStart - Class: \"{0}\" Package: \"{1}\"",
				this.getClass().getName(), this.getPackageName());
		
		Trace.Verbose(LOG_TAG, "Setting up Error Button Event Handler");
		Button btnNext = (Button)findViewById(R.id.btnError);
		if(btnNext != null)
		{
			btnNext.setOnClickListener(this);
		}
		else
		{
			Trace.Error(LOG_TAG, "Failed to find Error Button");
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Trace.Verbose(LOG_TAG, "onPause - Class: \"{0}\" Package: \"{1}\"",
				this.getClass().getName(), this.getPackageName());
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Trace.Verbose(LOG_TAG, "onStop - Class: \"{0}\" Package: \"{1}\"",
				this.getClass().getName(), this.getPackageName());
	}
	
	@Override
	protected void onDestroy()
	{
		Trace.Verbose(LOG_TAG, "onDestroy - Finishing: {0} Class: \"{1}\" Package: \"{2}\"",
				this.isFinishing()? "true" : "false", this.getClass().getName(), this.getPackageName());
		
		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btnError:
				Trace.Verbose(LOG_TAG, "Attempting to Divide By Zero...");
				int z = 4/0;
				break;
				
			default:
				Trace.Warning(LOG_TAG, "An unknown view was clicked");
		}		
	}

}
