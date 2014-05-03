package com.d567.request;

import android.content.*;

public final class SessionStartRequest 
{
	public final static String ACTION_SESSION_START_REQUEST = "com.d567.request.sessionstartrequest";
	
	/**************************************************
	 *                PARAMETER KEYS
	 **************************************************/
	/**
	 * The ID of the session to be Started/Stopped/Cleared. This parameter is incompatible with the
	 * PARAM_CREATE_SESSION parameter - use one or the other. An error will occur if the specified
	 * session does not exist.
	 */
	public final static String PARAM_SESSION_ID = ACTION_SESSION_START_REQUEST + ".param_session_id";
	
	/**************************************************
	 *                 RESULT CODES
	 **************************************************/		
	/**
	 * This result code is returned if the session was successfully started
	 */
	public final static int RESULT_OK = 1;
	
	/**
	 * This result code indicates that the request was denied. This may occur
	 * if another session is already running. A detailed error message will be
	 * returned by EXTRA_ERROR_MSG in the results bundle
	 * 
	 */
	public final static int RESULT_DENIED = 0;	
	
	/**
	 * This result code indicates that an error occurred while attempting to 
	 * start the session. A detailed error message will be returned
	 * by EXTRA_ERROR_MSG in the results bundle
	 */
	public final static int RESULT_ERROR = -1;
	
	/***************************************************
	 *                  RESULT EXTRAS
	 ***************************************************/
	/**
	 * If an error occurred, this will provide the error message.
	 * However, if no error occurred then the value will be null.
	 */
	public final static String EXTRA_ERROR_MSG = "com.d567.receiver.SessionStartRequest.EXTRA_ERROR_MSG";	
	
	/**************************************************
	 *                 SEND FUNCTION
	 **************************************************/
	/**
	 * Sends a request to the specified application for it to start/resume the specified session.
	 * If the target application is already running a session, this request will fail.
	 * 
	 * @param app The Context from which the request is being made
	 * @param targetPackage The package name of the application to which the request is being made
	 * @param sessionId The Id of the Session to be started/resumed. The session must already exist.
	 * @param resultHandler The Broadcast Receiver used to process the results of the request
	 */
	protected static void send(Context app, String targetPackage, String sessionId, BroadcastReceiver resultHandler)
	{
		if(targetPackage == null || targetPackage.isEmpty())
			throw new IllegalArgumentException("targetPackage is NULL or Empty");
		
		if(resultHandler == null)
			throw new IllegalArgumentException("resultHandler is NULL");
		
		if(sessionId == null)
			throw new IllegalArgumentException("sessionId is NULL");

		Intent request = new Intent(ACTION_SESSION_START_REQUEST);
		request.setPackage(targetPackage);		
		request.putExtra(PARAM_SESSION_ID, sessionId);
		
		app.sendOrderedBroadcast(request, null, resultHandler, null, RESULT_OK, null, null);
	}
}
