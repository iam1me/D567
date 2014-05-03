package com.transitionexample;

import com.d567.app.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SecondActivity extends Activity implements OnClickListener
{
	private final static String LOG_TAG = "TRANSITION_EXAMPLE_SECOND_ACTIVITY";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		if(Application.isSessionRunning())
		{
			Trace.Verbose(LOG_TAG, "A session is already running");
		}
		else
		{
			try
			{
				Application.startSession("starting from SecondActivity::onCreate");
			}
			catch(Exception ex)
			{
				Log.e(LOG_TAG, "Failed to start a new session", ex);
			}		
			
			Trace.Verbose(LOG_TAG,  "Started a new session!");
		}
		
		Trace.Verbose(LOG_TAG, "Setting up event handlers");
		Button btnPrev = (Button)findViewById(R.id.btnPrevious);
		if(btnPrev != null)
		{
			btnPrev.setOnClickListener(this);
		}
		else
		{
			Trace.Error(LOG_TAG, "Failed to retrieve btnPrevious");
		}
	}
	

	@Override
	protected void onStart()
	{
		super.onStart();
		Trace.Verbose(LOG_TAG, "onStart - Class: \"{0}\" Package: \"{1}\"",
				this.getClass().getName(), this.getPackageName());
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
			case R.id.btnPrevious:
				Trace.Verbose(LOG_TAG, "Moving back to the Main Activity...");
				Intent transition = new Intent(this, MainActivity.class);
				startActivity(transition);
				break;
				
			default:
				Trace.Warning(LOG_TAG, "onClick - Unrecognized ID");
		}		
	}
}
