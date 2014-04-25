package com.d567.app;

import java.util.List;

import com.d567.provider.TraceSessionProvider;
import com.d567.tracesession.SessionAdapter;
import com.d567.tracesession.SessionInfo;
import com.d567.tracesession.TraceLevel;

import android.content.Context;
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
	private static TraceLevel _level = TraceLevel.UNKNOWN;	
	private static List<String> _activeModules = null;
	
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
	
	/**
	 * Returns the trace level of the current session. Any trace commands
	 * with a lower level will not be recorded. Returns TraceLevel.UNKNOWN
	 * if there is no session running.
	 * @return the trace level of the current session, or UNKNOWN if no session is running. 
	 */
	public static TraceLevel getTraceLevel()
	{ return _level; }
	
	/**
	 * Sets the trace level for the current session. Any trace commands
	 * with a lower level will not be recorded. This cannot be modified while
	 * no session is active. When a session is active, this cannot be set
	 * to UNKNOWN.
	 * @param level
	 */
	public static void setTraceLevel(TraceLevel level)
	{
		if(_sessionId == null)
			throw new IllegalStateException("No active session");
		
		if(level == TraceLevel.UNKNOWN)
			throw new IllegalArgumentException("Cannot set trace level to UNKNOWN");
		
		_level = level; 
	}
	
	
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
	 *    Application Initialization
	 ***********************************/	
	/**
	 * This function should be called in the OnCreate function
	 * of the application. 
	 * @param app
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws SQLiteException 
	 */
	public static void Init(Context app, ApplicationSettings settings) throws SQLiteException, IllegalStateException, Exception
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
			if(level == TraceLevel.UNKNOWN)
				throw new Exception("AutoSessionTraceLevel is set to UNKNOWN");
						
			startSession(level);			
			Log.d(LOG_TAG, "Auto Started Session " + _sessionId);
		}
		
		_bInitialized = true;
	}
		
	/**********************************
	 *        Session Functions
	 ***********************************/
	/**
	 * Starts a new trace Session for the application. Throws an
	 * IllegalStateException if a session is not currently active.
	 */
	public static void startSession(TraceLevel level ) throws SQLiteException, IllegalStateException, Exception
	{
		if(_sessionId != null)
			throw new IllegalStateException("A session is already active");
		
		SessionAdapter adapter = new SessionAdapter(getContext());
		adapter.open();		
		SessionInfo info = adapter.CreateSession(null, true, level);		
		adapter.close();
		
		_sessionId = info.getId();
		_level = level;
	}
	
	/**
	 * Closes the application's current trace session. Throws an
	 * IllegalStateException if a session is not currently active.
	 * @throws Exception 
	 * @throws IllegalStateException 
	 * @throws SQLiteException 
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
		_level = TraceLevel.UNKNOWN;
	}
	
}