package com.d567.app;

import java.text.MessageFormat;
import com.d567.db.TraceAdapter;
import com.d567.tracesession.TraceLevel;


public class Trace 
{
	public static void Debug(String message, Object... params)
	{	_Trace("", TraceLevel.DEBUG, message, params); 	}
	
	public static void Debug(String module, String message, Object... params)
	{	_Trace(module, TraceLevel.DEBUG, message, params); 	}
	
	public static void Verbose(String message, Object... params)
	{	_Trace("", TraceLevel.VERBOSE, message, params); }

	public static void Verbose(String module, String message, Object... params)
	{	_Trace(module, TraceLevel.VERBOSE, message, params); }
	
	public static void Info(String module, String message, Object... params)
	{	_Trace(module, TraceLevel.INFORMATION, message, params); }	
	
	public static void Warning(String message, Object... params)
	{	_Trace("", TraceLevel.WARNING, message, params); }
	
	public static void Warning(String module, String message, Object... params)
	{	_Trace(module, TraceLevel.WARNING, message, params); }
	
	public static void Error(String message, Object... params) 
	{	_Trace("", TraceLevel.ERROR, message, params); }
	
	public static void Error(String module, String message, Object... params)
	{	_Trace(module, TraceLevel.ERROR, message, params); }
	
	private static void _Trace(String module, TraceLevel level, String message, Object... parameters)
	{
		if(Application.isSessionRunning())
		{
			String formattedTrace = MessageFormat.format(message, parameters);
			TraceAdapter adapter = new TraceAdapter(Application.getContext());
			adapter.open();		
			adapter.insertTrace(Application.getSessionId(), module, level, formattedTrace);		
			adapter.close();
		}
	}
}
