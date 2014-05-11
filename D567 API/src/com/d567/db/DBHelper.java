package com.d567.db;

import android.content.*;
import android.database.sqlite.*;
import android.util.Log;

import java.text.*;

import com.d567.app.ApplicationSettings;

/** CHANGE LOG
 * 4/28/2014 - Updated to DB Version 2. Removed the session_trace_level
 * 				from the session_mstr table. 
 * 
 * 5/10/2014 - Updated to DB Version 3. Added the saved_state_mstr table.
 * 
 */


public class DBHelper extends android.database.sqlite.SQLiteOpenHelper
{
	private final static String LOG_TAG = "D567_DB_HELPER";	
	public static final int DB_VER = 3;

	/**
	 * Opens the Database specified in the ApplicationSettings
	 * @param app	The Context used for accessing the database
	 */
	public DBHelper(Context app, ApplicationSettings settings)
	{
		super(app, settings.getDatabaseName(), null, DB_VER);	
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.beginTransaction();		
		try
		{
			Log.d(LOG_TAG, "Create Session Table");
			SessionTable.createTable(db);
			
			Log.d(LOG_TAG, "Create Trace Table");
			TraceTable.createTable(db);
			
			Log.d(LOG_TAG, "Create Saved State Table");
			SavedStateTable.createTable(db);
			
			db.setTransactionSuccessful();
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, "Error while creating tables", ex);
		}
		finally
		{
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(LOG_TAG, MessageFormat.format("Upgrading from version {0, number, integer} to {1, number, integer}",
				oldVersion, newVersion));
		
		db.beginTransaction();
		try
		{
			Log.d(LOG_TAG, "Update Session Table");
			SessionTable.updateTable(db,  oldVersion, newVersion);
			
			Log.d(LOG_TAG, "Update Trace Table");
			TraceTable.updateTable(db, oldVersion, newVersion);
			
			Log.d(LOG_TAG, "Update Saved State Table");
			SavedStateTable.updateTable(db, oldVersion, newVersion);
			
			db.setTransactionSuccessful();
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, MessageFormat.format("Error while upgrading table from version {0, number, integer} to {1, number, integer}",
					oldVersion, newVersion));
		}
		finally
		{
			db.endTransaction();
		}
	}
}
