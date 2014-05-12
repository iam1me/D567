package com.d567.receiver;

import com.d567.app.Application;
import com.d567.request.SaveStateRequest;
import com.d567.state.SaveStateHandler;

import android.content.*;
import android.os.Bundle;

public class SaveStateReceiver extends BroadcastReceiver 
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		SaveStateRequest.Parameters params = new SaveStateRequest.Parameters();
		params.readFromBundle(intent.getExtras());
		
		SaveStateRequest.Results results = new SaveStateRequest.Results();
		
		SaveStateHandler handler = Application.getSaveStateRequestHandler();
		
		if(handler != null)
		{
			try
			{
				SaveStateHandler.RequestArgs args = new SaveStateHandler.RequestArgs(params.getDescription());
				String id = handler.onSaveState(args);
				
				if(id != null)
				{
					results.setId(id);
					
					Bundle b = new Bundle();
					results.writeToBundle(b);
					
					setResult(SaveStateRequest.RESULT_OK, null, b);
				}
				else
				{
					setResult(SaveStateRequest.RESULT_DENIED, null, null);
				}
				
			}
			catch(Exception ex)
			{
				results.setError(ex.getMessage());
				
				Bundle b = new Bundle();
				results.writeToBundle(b);
				
				setResult(SaveStateRequest.RESULT_ERROR, null, b);
			}
		}
		else
		{
			setResult(SaveStateRequest.RESULT_DENIED, null, null);
		}		
	}	

}
