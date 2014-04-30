package com.d567.app;

import java.util.List;

import com.d567.app.intent.action.*;
import com.d567.app.intent.receiver.*;
import com.d567.db.SessionAdapter;
import com.d567.provider.*;
import com.d567.tracesession.*;

import android.content.Context;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Application 
{	
	/**********************************
	 *        Member Variables
	 ***********************************/
	private static String LOG_TAG = "D567_Application";
	private static Context _context = null;
	private static ApplicationSettings _settings;
	private static boolean _bInitialized = false;
	
	private static String _sessionId = null;		
	private static List<String> _activeModules = null;
	
	private static PackageListRequestHandler _packageHandler = null;
	private static SettingsRequestHandler _settingsHandler = null;
	private static SessionStartRequestHandler _startHandler = null;
	
	
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
	 *         Trace Modules
	 **********************************/	
	/**
	 * Adds a trace module to the list of active modules. Any trace command
	 * using an inactive module will not be recorded. The module must be 
	 * listed in the Application Setting list of Trace Modules.
	 * @param module
	 */
	public static void addTraceModule(String module)
	{
		if(module == null || module.isEmpty())
			throw new IllegalArgumentException("Module cannot be NULL/Empty");
		
		if(_settings.getTraceModules() == null || 
				!_settings.getTraceModules().contains(module))
		{
			throw new IllegalArgumentException("Module not found in the Application Settings");
		}
		
		if(_activeModules.contains(module))
			return;
		
		_activeModules.add(module);			
	}	
	
	/**
	 * removes the specified module from the list of active modules.
	 * @param module
	 * @return true if the module was removed, false otherwise.
	 */
	public static boolean removeTraceModule(String module)
	{		
		return _activeModules.remove(module);
	}
	
	/**
	 * determines if the specified module is an active tracing module
	 * @param module
	 * @return true if the module is active, else false
	 */
	public static boolean isActiveTraceModule(String module)
	{
		return _activeModules.contains(module);
	}
	
	/**
	 * clears the list of active modules
	 */
	public static void clearActiveTraceModules()
	{
		_activeModules.clear();
	}
	
	public static void activateAllTraceModules()
	{
		_activeModules.clear();
		_activeModules.addAll(_settings.getTraceModules());
	}	
	
	/**********************************
	 *  Application Lifecycle Events
	 ***********************************/	
	/**
	 * This function should be called in the OnCreate function
	 * of the client application. Initializes the D567 Library.
	 * @param app
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws SQLiteException 
	 */
	public static void init(Context app, ApplicationSettings settings) throws SQLiteException, IllegalStateException, Exception
	{
		if(app == null)
			throw new IllegalArgumentException("Context cannot be null");
		
		if(settings == null)
			throw new IllegalArgumentException("Settings cannot be null");
		
		_context = app;		
		_settings = settings;
		
		//Initialize the Content Provider
		TraceSessionProvider.Init(settings);

		//Check for a persisted session
		if(_settings.getSessionPersistence())
		{
			Log.d(LOG_TAG, "Checking for an existing session to persist...");
			SessionAdapter adapter = new SessionAdapter(app);
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
			TraceLevel level = _settings.getAutoSessionTraceLevel();
						
			createSession("D567 AutoStarted Session",true);			
			Log.d(LOG_TAG, "Auto Started Session " + _sessionId);
		}

		if(_settings.getAutoRegisterBroadcastReceivers())
			registerReceivers();
				
		_bInitialized = true;
	}
	
	public static void onStart()
	{
		Log.d(LOG_TAG, "onStart");
		//registerReceivers();			
	}
	
	public static void onPause()
	{
		Log.d(LOG_TAG, "onPause");
		//unregisterReceivers();
	}
	
	public static void onResume()
	{
		Log.d(LOG_TAG, "onResume");
		//registerReceivers();
	}
	
	public static void onStop()
	{
		Log.d(LOG_TAG, "onStop");
		//unregisterReceivers();
	}
	
	public static void onDestroy()
	{
		Log.d(LOG_TAG, "onDestroy");		
		
		if(_settings.getAutoRegisterBroadcastReceivers())
			unregisterReceivers();
	}	
	
	/**********************************
	 *     (Un)Register Receivers
	 **********************************/
	protected static void registerReceivers()
	{
		//Init Broadcast Receivers
		_packageHandler = new PackageListRequestHandler();
		_settingsHandler = new SettingsRequestHandler();
		_startHandler = new SessionStartRequestHandler();
		
		//Register Broadcast Receivers
		_context.registerReceiver(_packageHandler, new IntentFilter(PackageListRequest.ACTION_PACKAGE_LIST_REQUEST));
		_context.registerReceiver(_settingsHandler, new IntentFilter(SettingsRequest.ACTION_SETTINGS_REQUEST));
		_context.registerReceiver(_startHandler, new IntentFilter(SessionStartRequest.ACTION_SESSION_START_REQUEST));
	}
	
	protected static void unregisterReceivers()
	{		
		//Unregister Broadcast Receivers
		_context.unregisterReceiver(_packageHandler);
		_context.unregisterReceiver(_settingsHandler);
		_context.unregisterReceiver(_startHandler);
		
		//Cleanup
		_packageHandler = null;
		_settingsHandler = null;
		_startHandler = null;
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
	public static String createSession(String desc, boolean bStart)
	{
		if(bStart && isSessionRunning())
			throw new IllegalStateException("A new session cannot be started while one is already running");
		
		SessionAdapter adapter = new SessionAdapter(getContext());
		adapter.open();		
		SessionInfo info = adapter.CreateSession(desc, bStart);		
		adapter.close();
		
		if(bStart)
		{
			_sessionId = info.getId();
		}
		
		return info.getId();		
	}
	
	/**
	 * Starts/Resumes an existing trace Session for the application.
	 * 
	 * @param sessionId The Id of the session to be started/resumed
	 * @throws SQLiteException thrown if an error occurs while updating the session_mstr record
	 * @throws IllegalStateException thrown if a session is already active
	 */
	public static void startSession(String sessionId ) throws SQLiteException, IllegalStateException
	{
		if(_sessionId != null)
			throw new IllegalStateException("A session is already active");

		//Start the session
		SessionAdapter adapter = new SessionAdapter(getContext());
		adapter.open();
		adapter.StartSession(sessionId);
		adapter.close();
		
		_sessionId = sessionId;
	}
	
	/**
	 * Closes the application's current trace session. 
	 * @throws IllegalStateException thrown if a session is not currently running
	 * @throws SQLiteException thrown if an error occurs while updating the session_mstr record
	 */
	public static void closeSession() throws SQLiteException, IllegalStateException, Exception
	{
		if(_sessionId == null)
			throw new IllegalStateException("No session is currently active");
		
		SessionAdapter adapter = new SessionAdapter(getContext());
		adapter.open();		
		adapter.EndSession(_sessionId);		
		adapter.close();
		
		_sessionId = null;
	}

	/**
	 * Indicates whether or not a session is currently running
	 * @return true if a session is running, else false
	 */
	public static boolean isSessionRunning()
	{ return (_sessionId != null); }
}
