package com.d567.request;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class SettingsRequest 
{
	public final static String ACTION_SETTINGS_REQUEST = "com.d567.request.settingsrequest";
	
	/**********************************
	 *        Keys for Settings
	 ***********************************/
	public final static String EXTRA_AUTHORITY = ACTION_SETTINGS_REQUEST + "extra_authority";
	public final static String EXTRA_AUTO_SESSION = ACTION_SETTINGS_REQUEST + "extra_auto_session";
	public final static String EXTRA_SESSION_PERSISTENCE = ACTION_SETTINGS_REQUEST + "extra_session_persistence";
	public final static String EXTRA_DATABASE_NAME = ACTION_SETTINGS_REQUEST + "extra_database_name";
	public final static String EXTRA_AUTO_REGISTER_RECEIVERS = ACTION_SETTINGS_REQUEST + "extra_auto_register_receivers";
	
	public final static int RESULT_OK = 1;
	public final static int RESULT_DENIED = 0;
	public final static int RESULT_ERROR = -1;
	
	/**
	 * Broadcasts an ACTION_SETTINGS_REQUEST to the specified application. If running, the target application will bundle
	 * its settings, which can be retrieved with the resultHandler via its getResultExtras function. The bundle can be 
	 * used to construct an instance of the com.d567.request.BundledSettings class. On success the result code will be
	 * set to RESULT_OK. Else it will be set to RESULT_DENIED or RESULT_ERROR respectively.
	 * 
	 * @param app The app requesting the settings
	 * @param packageName The unique packageName of the application you would like the settings for. Cannot be null. 
	 * @param resultHandler The Broadcast Receiver used to handle the results sent back
	 */
	public static void send(Context app, String packageName, BroadcastReceiver resultHandler)
	{
		Intent request = new Intent(ACTION_SETTINGS_REQUEST);
		
		if(packageName != null)
			request.setPackage(packageName);
		
		app.sendOrderedBroadcast(request, null, resultHandler, null, RESULT_OK, null, null);		
	}	
}


