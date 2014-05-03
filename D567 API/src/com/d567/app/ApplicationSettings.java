package com.d567.app;

import android.util.Log;

public abstract class ApplicationSettings 
{	
	/**
	 * The name of the database that the D567 API will use. The D567 API has code
	 * to setup this database - so a client application should not attempt to setup
	 * this database in place of the API.
	 * 
	 * Default Value is "D567"
	 * 
	 * @return the name of the database used by the D567 API
	 */
	public String getDatabaseName()
	{
		return "D567";
	}
		
	/**
	 * Each application that wants to use the D567 API will need to
	 * specify the authority that the content provider will be
	 * registered under. This needs to be unique for each application.
	 * Example: com.myapp.d567.provider
	 */	
	public abstract String getAuthority();
	
	/**
	 * Determines whether or not to start a new session when D567_Init 
	 * is called. This setting is ignored when persisting an existing
	 * session.
	 * 
	 * Default Value is false
	 * 
	 * @return true if D567 is configured to auto-start a session upon initialization
	 */
	public boolean getAutoSession()
	{
		return false;
	}
	
	/**
	 * Determines whether or not to persist a session across different
	 * instantiations of the application. This only applies if a 
	 * session is running when the application exits. If no session is
	 * running then a new one will need to be started for tracing in
	 * a subsequent instantiation of the application.
	 * 
	 * Default value is false
	 * 
	 * @return true if D567 is configured to persist a session across 
	 * 			different instantiations of the application
	 */
	public boolean getSessionPersistence()
	{
		return false;
	}
	
	/**
	 * If true, the D567 API will dynamically register all of its broadcast 
	 * receivers. While this simplifies things, dynamically registered
	 * broadcast receivers do not persist after an application has been destroyed.
	 * However, implicit broadcast receivers, which are registered with the 
	 * application's manifest file, will persist after the application has
	 * been destroyed.
	 * 
	 * Default value false
	 * 
	 * @return true if the D567 API will dynamically register its broadcast
	 * 			receivers for the application, or else false if the broadcast
	 * 			receivers will be registered with the application manifest
	 */
	public boolean getAutoRegisterBroadcastReceivers()
	{ return false; }
	
	/**
	 * Logs out the specified application settings under the provided tag 
	 * @param log_tag
	 * @param settings
	 */
	public static void LogApplicationSettings(String log_tag, ApplicationSettings settings)
	{		
		if(settings == null)
		{
			Log.e(log_tag, "settings are NULL");
			return;
		}
		
		Log.v(log_tag, "Authority: " + settings.getAuthority());
		Log.v(log_tag, "Database Name: " + settings.getDatabaseName());
		Log.v(log_tag, "AutoRegisterBroadcastReceivers: " + ((settings.getAutoRegisterBroadcastReceivers())? "TRUE" : "FALSE"));
		Log.v(log_tag, "AutoSession: " + ((settings.getAutoSession())? "TRUE" : "FALSE"));
		Log.v(log_tag, "SessionPersistence: " + ((settings.getSessionPersistence())? "TRUE" : "FALSE"));
	}
}
