package com.traceexample.d567;

import com.d567.app.*;
import com.d567.tracesession.TraceLevel;

public class MySettings extends ApplicationSettings
{
	@Override
	public String getAuthority() 
	{ return "com.traceexample.d567.provider" ;	}

	@Override
	public boolean getAutoSession()
	{ return true; }
	
	@Override
	public boolean getSessionPersistence()
	{ return true; }
	
	@Override
	public boolean getAutoRegisterBroadcastReceivers()
	{ return true; }
	
	@Override 
	public TraceLevel getAutoSessionTraceLevel()
	{ return TraceLevel.VERBOSE;}

}
