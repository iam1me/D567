package com.d567.app;

import java.lang.Thread.UncaughtExceptionHandler;

import com.d567.db.SessionAdapter;
import com.d567.receiver.*;
import com.d567.request.DisplayErrorRequest;
import com.d567.request.PackageListRequest;
import com.d567.request.SessionDeleteRequest;
import com.d567.request.SessionStartRequest;
import com.d567.request.SessionStopRequest;
import com.d567.request.SettingsRequest;
import com.d567.tracesession.*;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

public class Application extends android.app.Application implements UncaughtExceptionHandler
{	
	/**********************************
	 *        Member Variables
	 ***********************************/
	private static String LOG_TAG = "D567_Application";
	
	private static Context _context = null;
	
	private static ApplicationSettings _settings;
	
	private static boolean _bInitialized = false;
	
	private static String _sessionId = null;	
	
	private static PackageListReceiver _packageHandler = null;
	private static SettingsReceiver _settingsHandler = null;
	private static SessionStartReceiver _startHandler = null;
	private static SessionStopReceiver _stopHandler = null;
	private static SessionDeleteReceiver _deleteHandler = null;
	
	private static UncaughtExceptionHandler _defaultUEH = null;
	
	
	/**********************************
	 *         Getters/Setters
	 **********************************/
	public static Context getContext()
	{
		if(_context == null)
			throw new IllegalStateException("Application Context has not been initialized");
			
		return _context; 
	}
	
	public static ApplicationSettings getSettings()
	{
		if(_settings == null)
			throw new IllegalStateException("Application Settings have not been initialized");
		
		return _settings; 
	}	
	
	public static boolean isInitialized()
	{ return _bInitialized;  }
	
	/**
	 * The Id of the current trace session for the application
	 * 
	 * @return The current session Id. Returns Null if no session
	 * 			is currently active
	 */
	public static String getSessionId()
	{ return _sessionId; }
	
	/**********************************
	 *  Application Lifecycle Events
	 ***********************************/	
	@Override
	public void onCreate()
	{
		super.onCreate();		
		try
		{
			Log.d(LOG_TAG, "Initializing D567 API");
			init(getApplicationContext());
			_defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(this);
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, "Failed to Initialize the D567 API", ex);
		}
	}
	
	
	/**
	 * Initializes the D567 API.
	 * @param app
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws SQLiteException 
	 */
	public static void init(Context app) throws Exception
	{
		if(app == null)
			throw new IllegalArgumentException("Context cannot be null");
		
		try
		{
			_context = app.getApplicationContext();
			_settings = ResourceSettings.loadSettings(_context);
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, "Failed to load settings", ex);
			throw ex;
		}
		
		//Check for a persisted session
		if(_settings.getSessionPersistence())
		{
			Log.d(LOG_TAG, "Checking for an existing session to persist...");
			SessionAdapter adapter = new SessionAdapter(app, _settings);
			adapter.open();
			SessionInfo info = adapter.GetLastSession();
			adapter.close();
			
			if(info != null && info.getEndTime() == null)
			{				
				_sessionId = info.getId();
				Log.d(LOG_TAG, "Persisting Session " + _sessionId);
			}
		}
		
		//Auto start a session
		if(_sessionId == null && _settings.getAutoSession())
		{
			String sessionId = startSession("D567 AutoStarted Session");
			
			if(sessionId != null)
				Log.d(LOG_TAG, "Auto Started Session " + sessionId);
			else
				Log.e(LOG_TAG, "Failed to auto create session");
		}

		if(_settings.getAutoRegisterBroadcastReceivers())
			registerReceivers();
		
		_bInitialized = true;
	}
	
	/**********************************
	 *     (Un)Register Receivers
	 **********************************/
	protected static void registerReceivers()
	{
		//Initialize Broadcast Receivers
		_packageHandler = new PackageListReceiver();
		_settingsHandler = new SettingsReceiver();
		_startHandler = new SessionStartReceiver();
		_stopHandler = new SessionStopReceiver();
		_deleteHandler = new SessionDeleteReceiver();
		
		//Register Broadcast Receivers
		_context.registerReceiver(_packageHandler, new IntentFilter(PackageListRequest.ACTION_PACKAGE_LIST_REQUEST));
		_context.registerReceiver(_settingsHandler, new IntentFilter(SettingsRequest.ACTION_SETTINGS_REQUEST));
		_context.registerReceiver(_startHandler, new IntentFilter(SessionStartRequest.ACTION_SESSION_START_REQUEST));
		_context.registerReceiver(_stopHandler, new IntentFilter(SessionStopRequest.ACTION_SESSION_STOP_REQUEST));
		_context.registerReceiver(_deleteHandler, new IntentFilter(SessionDeleteRequest.ACTION_SESSION_DELETE_REQUEST));
	}
	
	protected static void unregisterReceivers()
	{		
		//Unregister Broadcast Receivers
		_context.unregisterReceiver(_packageHandler);
		_context.unregisterReceiver(_settingsHandler);
		_context.unregisterReceiver(_startHandler);
		_context.unregisterReceiver(_stopHandler);
		_context.unregisterReceiver(_deleteHandler);
		
		//Cleanup
		_packageHandler = null;
		_settingsHandler = null;
		_startHandler = null;
		_stopHandler = null;
		_deleteHandler = null;
	}
		
	/**********************************
	 *        Session Functions
	 ***********************************/	
	/**
	 * Creates a new trace session for the application and optionally starts it.
	 * An IllegalStateException will be thrown if it attempts to start the session
	 * while another session is already running
	 * 
	 * @param desc the description of the trace session
	 * @param bStart determines whether or not to immediately start the session
	 * @param level the trace level for the session
	 * @return the Id of the new session
	 */
	public static String startSession(String desc)
	{
		if(isSessionRunning())
			throw new IllegalStateException("A new session cannot be started while one is already running");
		
		SessionAdapter adapter = new SessionAdapter(getContext(),_settings);
		adapter.open();		
		SessionInfo info = adapter.CreateSession(desc);		
		adapter.close();
		
		_sessionId = info.getId();		
		return _sessionId;
	}
	
	/**
	 * Closes the application's current trace session. 
	 * @throws Exception thrown if an error occurs while attempting to stop the session
	 */
	public static void stopSession() throws Exception
	{
		if(!isSessionRunning())
			throw new IllegalStateException("No session is currently running");
		
		SessionAdapter adapter = new SessionAdapter(getContext(),_settings);
		adapter.open();		
		adapter.EndSession(_sessionId);		
		adapter.close();
		
		_sessionId = null;
	}
	
	/**
	 * Used to delete old sessions.
	 * 
	 * @param sessionId The ID of the session to delete
	 * @throws Exception thrown if an error occurs while attempting to delete the session
	 */
	public static void deleteSession(String sessionId) throws Exception
	{
		if(sessionId == null)
		{
			throw new IllegalArgumentException("sessionId is NULL");
		}
		
		if(_sessionId.compareTo(sessionId) == 0)
		{
			throw new IllegalStateException("Cannot delete the active session");
		}
		
		SessionAdapter adapter = new SessionAdapter(getContext(),_settings);
		adapter.open();
		adapter.DeleteSession(sessionId);
		adapter.close();
	}
	
	/**
	 * Indicates whether or not a session is currently running
	 * @return true if a session is running, else false
	 */
	public static boolean isSessionRunning()
	{ return (_sessionId != null); }

	
	/** uncaughtException
	 * The defaultUncaughtExceptionHandler for the main thread of the application.
	 * If such a fatal exception occurs, this will catch it and write out the exception
	 * and stack trace to the currently running trace session, if applicable. It will then
	 * send a request to the D567 App to display the exception information 
	 **/
	@Override
	public void uncaughtException(Thread thread, Throwable ex) 
	{
		//Log.e(LOG_TAG, "D567 API - Tracing Uncaught Exception Handler", ex);		
		Trace.Error(LOG_TAG, "A Fatal Exception Occured in the Application's Main Thread: " + ex.getMessage());

		//Log.d(LOG_TAG, "Sending Display Error Request");
		DisplayErrorRequest.send(this, ex);		
		
		//Log.d(LOG_TAG, "Killing Process");
		Process.killProcess(Process.myPid());
	}
}
