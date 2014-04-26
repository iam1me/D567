package com.d567.provider;

import java.text.MessageFormat;
import android.net.Uri;

public class SessionContract 
{	
	public static final String PATH = "/session";
	
	public static final String MIME_SESSION_DIR = "vnd.android.cursor.dir/d567.provider.session";
	public static final String MIME_SESSION_ITEM = "vnd.android.cursor.item/d567.provider.session";
	
	public static final String PARAM_SESSION_ID = "SessionId";
	
	public static Uri GetUri(String authority)
	{
		if(authority == null || authority.isEmpty())
			throw new IllegalArgumentException("Authority is Null/Empty");
		
		String strUri = MessageFormat.format("content://{0}/session", authority);
		return Uri.parse(strUri);
	}
}
