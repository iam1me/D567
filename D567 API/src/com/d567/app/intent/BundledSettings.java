package com.d567.app.intent;

import java.text.MessageFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;

import com.d567.app.ApplicationSettings;
import com.d567.app.intent.action.SettingsRequest;
import com.d567.tracesession.TraceLevel;

public class BundledSettings extends ApplicationSettings 
{
	private static String LOG_TAG = "D567_BUNDLED_SETTINGS";
		
	/**********************************
	 *        Member Variables
	 ***********************************/
	private String _dbName;
	private String _authority;
	
	private boolean _bAutoSession;
	private boolean _bPersistSession;
	private boolean _bModuleFiltering;
	private boolean _bAutoRegisterReceivers;
	
	private TraceLevel _autoTraceLevel; 
	
	private ArrayList<String> _modules;
	
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
	public TraceLevel getAutoSessionTraceLevel()
	{ return _autoTraceLevel; }
	
	@Override
	public boolean getSessionPersistence()
	{ return _bPersistSession; }
	
	@Override
	public boolean useTraceModuleFiltering()
	{ return _bModuleFiltering; }
	
	@Override
	public ArrayList<String> getTraceModules()
	{ return _modules; }
	
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
		this._bModuleFiltering = b.getBoolean(SettingsRequest.EXTRA_USE_MODULE_FILTERING);
		
		String strLevel = b.getString(SettingsRequest.EXTRA_AUTO_SESSION_LEVEL, TraceLevel.UNKNOWN.toString());		
		try
		{
			_autoTraceLevel = TraceLevel.valueOf(strLevel);
		}
		catch(Exception ex)
		{
			_autoTraceLevel = TraceLevel.UNKNOWN;
			Log.e(LOG_TAG, MessageFormat.format("Failed to convert \"{0}\" to a TraceLevel", strLevel), ex);
		}
		
		this._modules = b.getStringArrayList(SettingsRequest.EXTRA_TRACE_MODULES);
		
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
		ret.putBoolean(SettingsRequest.EXTRA_USE_MODULE_FILTERING, settings.useTraceModuleFiltering());
		
		ret.putString(SettingsRequest.EXTRA_AUTO_SESSION_LEVEL, settings.getAutoSessionTraceLevel().toString());
		
		if(settings.getTraceModules() != null)
		{
			ret.putStringArrayList(SettingsRequest.EXTRA_TRACE_MODULES, settings.getTraceModules());
		}
		
		ret.putBoolean(SettingsRequest.EXTRA_AUTO_REGISTER_RECEIVERS, settings.getAutoRegisterBroadcastReceivers());
		
		return ret;
	}
}
