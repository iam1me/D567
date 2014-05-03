package com.d567.receiver;

import com.d567.app.Application;
import com.d567.app.ApplicationSettings;
import com.d567.app.ResourceSettings;
import com.d567.request.BundledSettings;
import com.d567.request.SettingsRequest;

import android.content.*;
import android.os.Bundle;
import android.util.Log;

public class SettingsReceiver extends BroadcastReceiver 
{
	private final static String LOG_TAG = "D567_SETTINGS_REQUEST_HANDLER";
	
	@Override
	public void onReceive(Context app, Intent request) 
	{
		ApplicationSettings settings;
		if(Application.isInitialized())
		{
			settings = Application.getSettings();
		}
		else
		{
			try 
			{
				settings = ResourceSettings.loadSettings(app);
			}
			catch (Exception e) 
			{
				Log.e(LOG_TAG, "Failed to load settings", e);
				setResult(SettingsRequest.RESULT_ERROR, null,null);
				return;
			}
		}
		
		Bundle bundle = BundledSettings.bundleSettings(settings);				 
		setResult(SettingsRequest.RESULT_OK, null, bundle);
	}
}
