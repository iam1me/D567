package com.d567.request;

import android.content.Context;
import android.content.Intent;

public class DisplayErrorRequest 
{
	public final static String DISPLAY_ERROR_REQUEST = "com.d567.request.displayerrorrequest"; 
	
	public final static String EXTRA_ERROR_MESSAGE = DISPLAY_ERROR_REQUEST + ".errormessage";
	public final static String EXTRA_STACK_TRACE = DISPLAY_ERROR_REQUEST + ".stacktrace";	
	
	/**
	 * Broadcasts a DISPLAY_ERROR_REQUEST to listening applications and respond.
	 * @param c
	 * @param ex
	 */
	public static void send(Context context, Throwable ex)
	{
		Intent i = new Intent(DISPLAY_ERROR_REQUEST);
		i.putExtras(BundledThrowable.toBundle(ex));
		context.sendBroadcast(i);
	}
}
