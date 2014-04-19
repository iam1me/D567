package d567.example.tracing;

import com.d567.app.*;

public class MySettings extends ApplicationSettings
{
	@Override
	public String getAuthority() 
	{
		return "com.traceexample.d567.provider" ;
	}

	@Override
	public boolean getAutoSession()
	{ return true; }
	
	@Override
	public boolean getSessionPersistence()
	{ return true; }

}
