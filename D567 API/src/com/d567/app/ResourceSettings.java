package com.d567.app;

import java.text.MessageFormat;

import android.net.Uri;
import android.util.Log;
import android.content.Context;


public class ResourceSettings extends ApplicationSettings 
{
	private final static String LOG_TAG = "D567_RESOURCE_SETTINGS";
	
	public final static String RES_PROVIDER_AUTHORITY = "d567.provider.authority";
	public final static String RES_DB_NAME = "d567.db.name";
	public final static String RES_SESSION_AUTO_START = "d567.session.autostart";
	public final static String RES_SESSION_PERSISTENCE = "d567.session.persist";
	public final static String RES_RECEIVERS_AUTO_REGISTER = "d567.receivers.autoregister";
		
	/****************************************
	 *           MEMBER VARIABLES
	 ****************************************/
	private String _authority = null;
	private String _db = "D567";
	private boolean _bAutoSession = false;
	private boolean _bSessionPersistence = false;
	private boolean _bAutoRegister = false;
	
	/****************************************
	 *            GET FUNCTIONS
	 ****************************************/
	@Override
	public String getAuthority() 
	{ return _authority; }
	
	@Override
	public String getDatabaseName()
	{ return _db; }
	
	@Override
	public boolean getAutoSession()
	{ return _bAutoSession; }
	
	@Override
	public boolean getAutoRegisterBroadcastReceivers()
	{ return _bAutoRegister; }
	
	@Override
	public boolean getSessionPersistence()
	{ return _bSessionPersistence; }
	
	public static Uri getSettingsUri(Context app)
	{
		return Uri.parse(MessageFormat.format("asset://{0}/d567_settings.xml",
				app.getPackageName()));
	}

	/****************************************
	 *             CONSTRUCTORS
	 ****************************************/
	private ResourceSettings()
	{}
	
	/*
	 * Loads the Application Settings from the res/d567/settings.xml file
	 */
	public static ApplicationSettings loadSettings(Context app) throws Exception
	{
		String authority;
		String dbName;
		boolean bAutoSession = false;
		boolean bPersistSession = false;
		boolean bAutoRegister = false;
		
		int id = app.getResources().getIdentifier("d567.provider.authority", "string", app.getPackageName());			
		if(id > 0)
		{
			authority = app.getResources().getString(id);
		}
		else
		{
			Log.d(LOG_TAG, "d567.provider.authority Not Found");
			authority = app.getPackageName() + ".d567.provider";
		}
		
		id = app.getResources().getIdentifier("d567.db.name", "string", app.getPackageName());
		if(id > 0)
		{
			dbName = app.getResources().getString(id);
		}
		else
		{
			dbName = "D567";
		}
		
		id = app.getResources().getIdentifier("d567.session.autostart", "bool", app.getPackageName());
		if(id > 0)
		{
			bAutoSession = app.getResources().getBoolean(id);
		}
		
		id = app.getResources().getIdentifier("d567.session.persist", "bool", app.getPackageName());
		if(id > 0)
		{
			bPersistSession = app.getResources().getBoolean(id);
		}
		
		id = app.getResources().getIdentifier("d567.receivers.autoregister", "bool", app.getPackageName());
		if(id > 0)
		{
			bAutoRegister = app.getResources().getBoolean(id);
		}
		
		ResourceSettings ret = new ResourceSettings();
		ret._authority = authority;
		ret._db = dbName;
		ret._bAutoSession = bAutoSession;
		ret._bSessionPersistence = bPersistSession;
		ret._bAutoRegister = bAutoRegister;
								
		return ret;
	}
}
