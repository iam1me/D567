package com.d567.receiver;

import com.d567.app.Application;
import com.d567.request.SessionStopRequest;

import android.content.*;
import android.os.Bundle;

public class SessionStopReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if(Application.isSessionRunning())
		{
			try
			{
				Application.stopSession();
				this.setResult(SessionStopRequest.RESULT_OK, null, null);
			}
			catch(Exception ex)
			{
				this.setResult(SessionStopRequest.RESULT_ERROR,null,createErrorBundle(ex.getMessage()));
			}
		}
		else
		{
			this.setResult(SessionStopRequest.RESULT_DENIED, null, createErrorBundle("No session is currently running"));
		}
	}
	
	private Bundle createErrorBundle(String errMsg)
	{
		Bundle ret = new Bundle();
		ret.putString(SessionStopRequest.EXTRA_ERROR_MSG, errMsg);
		return ret;
	}

}
