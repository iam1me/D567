package com.d567.receiver;

import com.d567.app.Application;
import com.d567.request.SessionDeleteRequest;

import android.content.*;
import android.os.Bundle;

public class SessionDeleteReceiver extends BroadcastReceiver 
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String sessionId = null;
		Bundle extras = intent.getExtras();
		if(extras != null)
		{
			sessionId = extras.getString(SessionDeleteRequest.PARAM_SESSION_ID);
		}
		
		if(sessionId == null)
		{
			setResult(SessionDeleteRequest.RESULT_DENIED, null, createErrorBundle("SessionId is NULL"));
			return;
		}
		
		try
		{
			Application.deleteSession(sessionId);
			setResult(SessionDeleteRequest.RESULT_OK, null, null);
		}
		catch(Exception ex)
		{
			setResult(SessionDeleteRequest.RESULT_ERROR, null, createErrorBundle(ex.getMessage()));
		}		
	}
	
	private Bundle createErrorBundle(String errMsg)
	{
		Bundle ret = new Bundle();
		ret.putString(SessionDeleteRequest.EXTRA_ERROR_MSG, errMsg);
		return ret;
	}
	

}
