package com.d567.app.intent.action;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class SettingsRequest 
{
	public static String ACTION_SETTINGS_REQUEST = "com.d567.app.intent.action.SettingsRequest";
	
	/**********************************
	 *        Keys for Settings
	 ***********************************/
	public static String EXTRA_AUTHORITY = "com.d567.app.intent.action.SettingsRequest.AUTHORITY";
	public static String EXTRA_AUTO_SESSION = "com.d567.app.intent.action.SettingsRequest.AUTO_SESSION";
	public static String EXTRA_AUTO_SESSION_LEVEL = "com.d567.app.intent.action.SettingsRequest.AUTO_SESSION_LEVEL";
	public static String EXTRA_SESSION_PERSISTENCE = "com.d567.app.intent.action.SettingsRequest.SESSION_PERSISTENCE";
	public static String EXTRA_TRACE_MODULES = "com.d567.app.intent.action.SettingsRequest.TRACE_MODULES";
	public static String EXTRA_USE_MODULE_FILTERING = "com.d567.app.intent.action.SettingsRequest.USE_MODULE_FILTERING";
	public static String EXTRA_DATABASE_NAME = "com.d567.app.intent.action.SettingsRequest.DATABASE_NAME";
	public static String EXTRA_AUTO_REGISTER_RECEIVERS = "com.d567.app.intent.action.SettingsRequest.AUTO_REGISTER_RECEIVERS";
	
	/**
	 * Broadcasts an ACTION_SETTINGS_REQUEST to the specified application. If running, the target application will bundle
	 * its settings, which can be retrieved with the resultHandler via its getResultExtras function. The bundle can be 
	 * used to construct an instance of the com.d567.app.intent.BundledSettings class.
	 * 
	 * @param app The app requesting the settings
	 * @param packageName The unique packageName of the application you would like the settings for. Cannot be null. 
	 * @param resultHandler The Broadcast Receiver used to handle the results sent back
	 */
	public static void getSettings(Context app, String packageName, BroadcastReceiver resultHandler)
	{
		Intent request = new Intent(ACTION_SETTINGS_REQUEST);
		
		if(packageName != null)
			request.setPackage(packageName);
		
		app.sendOrderedBroadcast(request, null, resultHandler, null, Activity.RESULT_OK, null, null);		
	}	
}


