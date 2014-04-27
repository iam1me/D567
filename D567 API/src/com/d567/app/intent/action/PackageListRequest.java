package com.d567.app.intent.action;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public final class PackageListRequest 
{
	public final static String ACTION_PACKAGE_LIST_REQUEST = "com.d567.app.intent.action.PackageRequest";
	
	public final static String EXTRA_PACKAGE_LIST = "com.d567.app.intent.action.PackageRequest.PACKAGE_LIST";  
	
	/**
	 * Broadcasts an ACTION_PACKAGE_REQUEST to every listening application. Those applications
	 * that support the D567 API will add their package name to the results bundle under the 
	 * string list with the key EXTRA_PACKAGE_LIST. If no applications respond then the package
	 * list will be empty (not null). The results maybe retrieved by the resultHandler using
	 * its getResultExtras method.
	 *  
	 * @param app The application requesting package names
	 * @param resultHandler This broadcast receiver will be called last, allowing for the calling
	 * 				application to process the results.
	 */
	public static void getPackageList(Context app, BroadcastReceiver resultHandler)
	{
		if(resultHandler == null)
			throw new IllegalArgumentException("resultHandler is NULL");
		
		//Create Intent
		Intent request = new Intent(ACTION_PACKAGE_LIST_REQUEST);
		
		//Initialize results bundle
		Bundle result = new Bundle();
		result.putStringArrayList(EXTRA_PACKAGE_LIST, new ArrayList<String>());
		
		//Send broadcast
		app.sendOrderedBroadcast(request, null, resultHandler,null, Activity.RESULT_OK,null,result);		
	}

}
