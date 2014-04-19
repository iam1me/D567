package com.d567.app;

import java.text.MessageFormat;

import com.d567.tracesession.TraceAdapter;
import com.d567.tracesession.TraceLevel;


public class Trace 
{
	public static void Debug(String module, String message, Object... parameters)
	{
		//Check to see if a session is running
		if(Application.getSessionId() == null)
			return;
		
		if(!checkTraceLevel(TraceLevel.DEBUG))
			return;
		
		//Check to see if Trace Module Filtering is enabled
		if(Application.getSettings().useTraceModuleFiltering())
		{
			//Check to see if module is Active
			if(!Application.isActiveTraceModule(module))
			{
				return;
			}
		}
		
		String formattedTrace = MessageFormat.format(message, parameters);	
		writeTrace(module, TraceLevel.DEBUG, formattedTrace);		
	}

	public static void Verbose(String module, String message, Object... parameters)
	{
		//Check to see if a session is running
		if(Application.getSessionId() == null)
			return;
		
		if(!checkTraceLevel(TraceLevel.VERBOSE))
			return;
		
		//Check to see if Trace Module Filtering is enabled
		if(Application.getSettings().useTraceModuleFiltering())
		{
			//Check to see if module is Active
			if(!Application.isActiveTraceModule(module))
			{
				return;
			}
		}
		
		String formattedTrace = MessageFormat.format(message, parameters);	
		writeTrace(module, TraceLevel.VERBOSE, formattedTrace);		
	}
	
	public static void Info(String module, String message, Object... parameters)
	{
		//Check to see if a session is running
		if(Application.getSessionId() == null)
			return;
		
		if(!checkTraceLevel(TraceLevel.INFORMATION))
			return;
		
		//Check to see if Trace Module Filtering is enabled
		if(Application.getSettings().useTraceModuleFiltering())
		{
			//Check to see if module is Active
			if(!Application.isActiveTraceModule(module))
			{
				return;
			}
		}
		
		String formattedTrace = MessageFormat.format(message, parameters);	
		writeTrace(module, TraceLevel.INFORMATION, formattedTrace);		
	}
	
	public static void Warning(String module, String message, Object... parameters)
	{
		//Check to see if a session is running
		if(Application.getSessionId() == null)
			return;
		
		if(!checkTraceLevel(TraceLevel.WARNING))
			return;
		
		//Check to see if Trace Module Filtering is enabled
		if(Application.getSettings().useTraceModuleFiltering())
		{
			//Check to see if module is Active
			if(!Application.isActiveTraceModule(module))
			{
				return;
			}
		}
		
		String formattedTrace = MessageFormat.format(message, parameters);	
		writeTrace(module, TraceLevel.WARNING, formattedTrace);		
	}
	
	public static void Error(String module, String message, Object... parameters)
	{
		//Check to see if a session is running
		if(Application.getSessionId() == null)
			return;
		
		if(!checkTraceLevel(TraceLevel.ERROR))
			return;
		
		//Check to see if Trace Module Filtering is enabled
		if(Application.getSettings().useTraceModuleFiltering())
		{
			//Check to see if module is Active
			if(!Application.isActiveTraceModule(module))
			{
				return;
			}
		}
		
		String formattedTrace = MessageFormat.format(message, parameters);	
		writeTrace(module, TraceLevel.ERROR, formattedTrace);		
	}
	
	private static void writeTrace(String module, TraceLevel level, String message)
	{
		TraceAdapter adapter = new TraceAdapter(Application.getContext());
		adapter.open();		
		adapter.insertTrace(Application.getSessionId(), module, level, message);		
		adapter.close();
	}
	
	private static boolean checkTraceLevel(TraceLevel level)
	{
		return (level.ordinal() >= Application.getTraceLevel().ordinal());
	}
}
