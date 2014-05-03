package com.d567.request;

import android.os.Bundle;
import android.util.Log;

import com.d567.app.ApplicationSettings;

public class BundledSettings extends ApplicationSettings 
{
	private final static String LOG_TAG = "D567_BUNDLED_SETTINGS";
		
	/**********************************
	 *        Member Variables
	 ***********************************/
	private String _dbName;
	private String _authority;
	
	private boolean _bAutoSession;
	private boolean _bPersistSession;
	private boolean _bAutoRegisterReceivers;
	
	/**********************************
	 *      Accessor Functions
	 ***********************************/
	@Override
	public String getDatabaseName()
	{ return _dbName; }
		
	@Override
	public String getAuthority()
	{ return _authority; }
	
	@Override
	public boolean getAutoSession()
	{ return _bAutoSession; }
	
	@Override
	public boolean getSessionPersistence()
	{ return _bPersistSession; }
	
	@Override
	public boolean getAutoRegisterBroadcastReceivers()
	{ return _bAutoRegisterReceivers; }
			
	
	/**********************************
	 *         Constructor(s)
	 ***********************************/
	public BundledSettings(Bundle b)
	{
		if(b == null)
		{
			NullPointerException ex = new NullPointerException("Bundle is NULL");
			Log.e(LOG_TAG, "Bundle is NULL", ex);
			throw ex;
		}
		
		this._authority = b.getString(SettingsRequest.EXTRA_AUTHORITY);
		this._dbName = b.getString(SettingsRequest.EXTRA_DATABASE_NAME);
		
		this._bAutoSession = b.getBoolean(SettingsRequest.EXTRA_AUTO_SESSION);
		this._bPersistSession = b.getBoolean(SettingsRequest.EXTRA_SESSION_PERSISTENCE);		
		this._bAutoRegisterReceivers = b.getBoolean(SettingsRequest.EXTRA_AUTO_REGISTER_RECEIVERS);
	}
	
	/**********************************
	 *        Helper Function(s)
	 ***********************************/
	public static Bundle bundleSettings(ApplicationSettings settings)
	{
		Bundle ret = new Bundle();
		
		ret.putString(SettingsRequest.EXTRA_AUTHORITY, settings.getAuthority());
		ret.putString(SettingsRequest.EXTRA_DATABASE_NAME, settings.getDatabaseName());
		
		ret.putBoolean(SettingsRequest.EXTRA_AUTO_SESSION, settings.getAutoSession());
		ret.putBoolean(SettingsRequest.EXTRA_SESSION_PERSISTENCE, settings.getSessionPersistence());
		ret.putBoolean(SettingsRequest.EXTRA_AUTO_REGISTER_RECEIVERS, settings.getAutoRegisterBroadcastReceivers());
		
		return ret;
	}
}
