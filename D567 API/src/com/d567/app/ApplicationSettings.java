package com.d567.app;

import java.util.List;

import com.d567.tracesession.TraceLevel;


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
	 * Specifies the Trace Level to use when auto-starting a session.
	 * 
	 * Default Value is TraceLevel.VERBOSE
	 * 
	 * @return
	 */
	public TraceLevel getAutoSessionTraceLevel()
	{ return TraceLevel.VERBOSE; }
	
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
	 * Trace Modules allow the client application to categorize trace
	 * messages using user-defined labels. When enabled, a given trace
	 * module must first be added to the list of active modules before
	 * the trace messages for that module will recorded. 
	 * 
	 * Default value is false
	 * 
	 * @return true if trace messages should only be recorded if an active
	 * 			trace module is specified to the trace command
	 */
	public boolean useTraceModuleFiltering()
	{ 
		return false; 
	}
	
	/**
	 * Returns a list of all the applications supported Trace Modules.
	 * When Trace Module Filter is enabled, this list will be supplied
	 * to the D567 Application to enable tracing on specific modules.
	 * 
	 * Default value is null
	 * 
	 * @return the list of supported trace modules
	 */
	public List<String> getTraceModules()
	{ return null; }
}
