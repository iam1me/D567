package com.d567.state;

public abstract class SaveStateHandler
{		
	/**
	 * Called when the Application receives a CREATE_SAVE_STATE Intent.
	 * 	The implementing code must decide whether or not it wants to handle the
	 * 	request. If not, return null. Else, process the request and return the 
	 * 	ID of the Saved State. If an error occurs, throw the error so that
	 * 	the error maybe reported back to the requester. 
	 * 
	 * @param args				The arguments for the Save State Request
	 * @return 					The id of the Save State. This will be null if the 
	 * 								request is denied.
	 * @throws Exception		If any exception occurs while processing the request 
	 * 								it is thrown so that it maybe sent back to 
	 * 								the requester.
	 */
	public abstract String onSaveState(RequestArgs args) throws Exception;
	
	/**
	 * A helper class for encapsulating any parameters for the Save State Request
	 */
	public static class RequestArgs
	{
		private String _desc;
		public String getDescription()
		{ return _desc; }
		
		public RequestArgs(String desc)
		{ this._desc = desc; }
	}
}
