package com.d567.provider;

import java.text.MessageFormat;
import android.net.Uri;

public class TraceContract 
{
	public static final String PATH = "/trace";	
	public static final String MIME_TRACE_DIR = "vnd.android.cursor.dir/d567.provider.trace";
	public static final String MIME_TRACE_ITEM = "vnd.android.cursor.item/d567.provider.trace";
	
	public static final String PARAM_SESSION_ID = "SessionId";
    public static final String PARAM_TRACE_ID = "TraceId";
    
	public static Uri GetUri(String authority)
	{
		if(authority == null || authority.isEmpty())
			throw new IllegalArgumentException("Authority is Null/Empty");
			
		String strUri = MessageFormat.format("content://{0}/trace", authority);
		return Uri.parse(strUri);
	}
}
