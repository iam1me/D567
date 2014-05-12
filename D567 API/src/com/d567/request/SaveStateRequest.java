package com.d567.request;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SaveStateRequest
{
	public final static String SAVE_STATE_REQUEST = "com.d567.request.savestaterequest";
	
	public final static String PARAM_DESC = SAVE_STATE_REQUEST + ".params.desc";
	
	public final static String EXTRA_ID = SAVE_STATE_REQUEST + ".results.id";
	public final static String EXTRA_ERROR = SAVE_STATE_REQUEST + ".results.error";
	public final static String EXTRA_RESULT_CODE = SAVE_STATE_REQUEST + ".results.code";
	
	public final static int RESULT_OK = 1;
	public final static int RESULT_DENIED = 0;
	public final static int RESULT_ERROR = -1;

	/**
	 * Broadcasts a SAVE_STATE_REQUEST Intent to the targetPackage. The parameters are sent as
	 * part of the Intent's Extras Bundle. Once processed, the results are passed to the resultHandler.
	 * The results can be retrieved by reading the bundle using the CreateSavedStateRequest.Results helper
	 * class. If the request succeeds, the Result Code will be set to RESULT_OK. If the request is denied,
	 * the Result Code will be RESULT_DENIED. If an error occurs while attempting to process the request,
	 * the Result Code will be RESULT_ERROR and an Error Message will be returned via the Result Extras Bundle.
	 * 
	 * @param app				The Context from which the request is being made
	 * @param targetPackage		The package of the target application to which the request is being made
	 * @param input				Contains all of the parameters required for the request
	 * @param resultHandler		The Broadcast receiver to call after the request has been processed
	 */
	public static void send(Context app,String targetPackage, Parameters input, BroadcastReceiver resultHandler)
	{
		Intent request = new Intent(SAVE_STATE_REQUEST);
		
		Bundle b = new Bundle();
		input.writeToBundle(b);
		
		request.putExtras(b);
		request.setPackage(targetPackage);
		
		app.sendOrderedBroadcast(request, null, resultHandler, null, RESULT_DENIED, null, null);
	}
	
	/** CreateSavedStateRequest.Parameters
	 * A helper class used to encapsulate all parameters for the CREATE_SAVED_STATE request.
	 * It has functions for writing the parameters to, and reading them from, a Bundle.
	 */
	public static class Parameters
	{
		private String _desc;
		public String getDescription()
		{ return _desc; }
		
		public void setDescription(String desc)
		{ _desc = desc; }
		
		public Parameters()
		{
			_desc = null;
		}
		
		public Parameters(String desc)
		{
			_desc = desc;
		}
		
		public void writeToBundle(Bundle b)
		{
			b.putString(PARAM_DESC, _desc);
		}
		
		public void readFromBundle(Bundle b)
		{
			setDescription(b.getString(PARAM_DESC));			
		}
	}
	
	/** CreateSavedStateRequest.Results
	 * A helper class used to encapsulate any results returned via the Results Bundle.
	 * It has methods for writing the results to, and reading them from, a Bundle.	 *
	 */
	public static class Results
	{
		private String _id = null;
		private String _error = null;
		
		/********************************
		 *       GET/SET FUNCTIONS
		 ********************************/		
		public String getId()
		{
			return _id;
		}
		
		public void setId(String id)
		{
			_id = id;
		}
		
		public String getError()
		{
			return _error;
		}
		
		public void setError(String error)
		{ _error = error; }				
		
		/********************************
		 *        CONSTRUCTOR(S)
		 ********************************/
		public Results()
		{}
		
		/********************************
		 *      BUNDLE FUNCTIONS
		 ********************************/		
		public void writeToBundle(Bundle b)
		{
			b.putString(EXTRA_ID, this._id);
			b.putString(EXTRA_ERROR, this._error);
		}
		
		public void readFromBundle(Bundle b)
		{
			setId(b.getString(EXTRA_ID));
			setError(b.getString(EXTRA_ERROR));
		}
	}

}
