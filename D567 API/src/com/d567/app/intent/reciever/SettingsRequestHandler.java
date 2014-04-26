package com.d567.app.intent.reciever;

import com.d567.app.Application;
import com.d567.app.intent.BundledSettings;

import android.content.*;
import android.os.Bundle;
import android.util.Log;

public class SettingsRequestHandler extends BroadcastReceiver 
{
	private static String LOG_TAG = "D567_SETTINGS_REQUEST_HANDLER";
	
	@Override
	public void onReceive(Context app, Intent request) 
	{
		Log.d(LOG_TAG, "Creating Bundle from Settings");
		Bundle bundle = BundledSettings.bundleSettings(Application.getSettings());				 
		
		if(bundle == null)
			Log.e(LOG_TAG, "Settings are NULL");
		else
		{
			BundledSettings settings = new BundledSettings(bundle);
			BundledSettings.LogApplicationSettings(LOG_TAG, settings);
		}
		
		Log.d(LOG_TAG, "Bundle Created, Setting as ResultExtras");
		this.setResultExtras(bundle);
	}
}
