package com.d567.app.intent.receiver;

import com.d567.app.Application;
import com.d567.app.intent.action.SessionStartRequest;
import android.content.*;
import android.os.Bundle;

public class SessionStartRequestHandler extends BroadcastReceiver 
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{		
		//Only one session can run at a time
		if(Application.isSessionRunning())
		{
			setResult(SessionStartRequest.RESULT_DENIED, null, null);
			return;
		}
		
		//Get SessionId
		String sessionId = null;		
		Bundle extras = intent.getExtras();		
		if(extras != null)
		{
			sessionId = extras.getString(SessionStartRequest.PARAM_SESSION_ID);
		}
		else
		{		
			setResult(SessionStartRequest.RESULT_ERROR,null, createErrorBundle("SessionId is NULL"));
			return;
		}
		
		//Start the Session		
		try
		{
			Application.startSession(sessionId);
		}
		catch(Exception ex)
		{
			setResult(SessionStartRequest.RESULT_ERROR, null, createErrorBundle(ex.getMessage()));
			return;
		}
		
		setResult(SessionStartRequest.RESULT_OK, null, null);		
	}
	
	private Bundle createErrorBundle(String errMsg)
	{
		Bundle results = new Bundle();
		results.putString(SessionStartRequest.EXTRA_ERROR_MSG, errMsg);	
		return results;		
	}

}
