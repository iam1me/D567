package com.d567.request;

import android.content.*;

public class SessionDeleteRequest 
{
	public final static String ACTION_SESSION_DELETE_REQUEST = "com.d567.request.sessiondeleterequest";
	
	/**
	 * The ID of the session to be deleted
	 */
	public final static String PARAM_SESSION_ID = ACTION_SESSION_DELETE_REQUEST + ".PARAM_SESSION_ID";
	
	/**
	 * If the request fails, a detailed error message will be returned in the
	 * result Extras under EXTRA_ERROR_MSG
	 */
	public final static String EXTRA_ERROR_MSG = ACTION_SESSION_DELETE_REQUEST + ".EXTRA_ERROR_MSG";
	
	/**
	 * The result code returned when the specified session was deleted successfully
	 */
	public final static int RESULT_OK = 1;
	
	/**
	 * The result code returned when the application denies the request
	 */
	public final static int RESULT_DENIED = 0;
	
	/**
	 * The result code returned when an error occured while attempting to
	 * fulfill the request
	 */
	public final static int RESULT_ERROR = -1;
	
	/**
	 * Broadcasts an ACTION_SESSION_DELETE_REQUEST to the application with 
	 * the specified package Name. If the application successfully deletes 
	 * the session with the provided ID, then the result code will be set 
	 * to RESULT_OK. If the request is denied or an error occurs, a detailed
	 * error message can be retrieved from the result Extras under EXTRA_ERROR_MSG
	 * 
	 * @param app The context from which the request is being made
	 * @param targetPackage The package Name of the target application
	 * @param sessionId The ID of the session to be deleted
	 * @param resultHandler The Broadcast Receiver used to process the results
	 */
	public static void send(Context app, String targetPackage, String sessionId, BroadcastReceiver resultHandler)
	{
		if(targetPackage == null || targetPackage.isEmpty())
			throw new IllegalArgumentException("targetPackage is NULL or Empty");
		
		if(sessionId == null || sessionId.isEmpty())
			throw new IllegalArgumentException("sessionId is NULL or Empty");
		
		Intent request = new Intent(ACTION_SESSION_DELETE_REQUEST);
		request.putExtra(PARAM_SESSION_ID, sessionId);
		request.setPackage(targetPackage);
		app.sendOrderedBroadcast(request, null, resultHandler, null, RESULT_OK, null, null);
	}
	
}
