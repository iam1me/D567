package com.d567.provider;

import java.text.MessageFormat;

import com.d567.app.Application;


import android.net.Uri;

public class SessionContract 
{	
	public static final String PATH = "/session";
	
	public static final String MIME_SESSION_DIR = "vnd.android.cursor.dir/d567.provider.session";
	public static final String MIME_SESSION_ITEM = "vnd.android.cursor.item/d567.provider.session";
	
	public static final String PARAM_SESSION_ID = "SessionId";
	
	public static Uri GetUri()
	{
		String authority = Application.getSettings().getAuthority();
		String strUri = MessageFormat.format("content://{0}/session", authority);
		return Uri.parse(strUri);
	}
}
