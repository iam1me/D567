package com.d567.request;

import android.content.*;

public class SessionStopRequest 
{
	public final static String ACTION_SESSION_STOP_REQUEST 	= "com.d567.request.sessionstoprequest";
	
	/*************************************************
	 *                 RESULT CODES
	 *************************************************/
	/**
	 * The session was stopped successfully as requested
	 */
	public final static int RESULT_OK = 1;	
	
	/**
	 * The request to stop the session was denied by the application
	 */
	public final static int RESULT_DENIED = 0;	
	
	/**
	 * An error occurred while attempting to stop the session
	 */
	public final static int RESULT_ERROR = -1;
	
	/**
	 * If the request failed, this will provide a detailed error message. Else it will be null.
	 */
	public final static String EXTRA_ERROR_MSG = ACTION_SESSION_STOP_REQUEST + ".extra_error_msg";
	
	/**
	 * Sends an ACTION_SESSION_STOP_REQUEST to the application with the specified package name.
	 * The resultHandler can determine if the request succeeded or failed based upon the
	 * result code. If it fails, a detailed error message can be retrieved from the result
	 * Extras under EXTRA_ERROR_MSG.
	 * 
	 * @param app The context from which the request is being made
	 * @param targetPackage The package name of the target application
	 * @param resultHandler The BroadcastReceiver which will process the results
	 */	
	public static void send(Context app, String targetPackage, BroadcastReceiver resultHandler)
	{
		if(resultHandler == null)
			throw new IllegalArgumentException("resultHandler is NULL");
		
		if(targetPackage == null || targetPackage.isEmpty())
			throw new IllegalArgumentException("targetPackage is NULL or Empty");
		
		Intent request = new Intent(ACTION_SESSION_STOP_REQUEST);
		request.setPackage(targetPackage);
		app.sendOrderedBroadcast(request, null, resultHandler, null, RESULT_OK, null, null);		
	}
	
	

}
