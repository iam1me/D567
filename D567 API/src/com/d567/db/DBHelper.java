package com.d567.db;

import android.content.*;
import android.database.sqlite.*;
import android.util.Log;

import java.text.*;

import com.d567.app.Application;
import com.d567.tracesession.SessionTable;
import com.d567.tracesession.TraceTable;


public class DBHelper extends android.database.sqlite.SQLiteOpenHelper
{
	protected static String LOG_TAG = "D567_DB_HELPER";	
	public static final int DB_VER = 1;

	/**
	 * Opens the Database specified in the ApplicationSettings
	 * @param app	The Context used for accessing the database
	 */
	public DBHelper(Context app)
	{
		super(app, Application.getSettings().getDatabaseName(), null, DB_VER);	
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
	
	/*@Override
	public void onOpen(SQLiteDatabase db)
	{
		Log.v(LOG_TAG, "onOpen");
		super.onOpen(db);
	}*/
}
