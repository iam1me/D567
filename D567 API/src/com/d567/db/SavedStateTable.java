package com.d567.db;

import android.database.sqlite.*;
import android.util.Log;

import java.text.*;

public class SavedStateTable
{
	protected static String LOG_TAG = "D567_SAVED_STATE_TABLE";	

	public static final String TABLE_NAME = "saved_state_mstr";	
	public static final String KEY_ID = "saved_state_id";
	public static final String KEY_DESC = "saved_state_desc";
	public static final String KEY_TIME = "saved_state_time";
	public static final String KEY_DATA = "saved_state_data";
		
	public static void createTable(SQLiteDatabase db) throws SQLiteException
	{
		Log.d(LOG_TAG, "createTable");

		String tableSQL = MessageFormat.format(
			"create table {0} ({1} text primary key not null, {2} text, {3} integer not null, {4} text not null)",
			SavedStateTable.TABLE_NAME, KEY_ID, KEY_DESC, KEY_TIME, KEY_DATA);
	
		db.execSQL(tableSQL);
	}

	public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//The SavedState Table was added in Version 3
		if(oldVersion <= 2 && newVersion >= 3)
		{
			db.beginTransaction();
			try
			{				
				createTable(db);
				db.setTransactionSuccessful();
			}
			catch(Exception ex)
			{
				Log.e(LOG_TAG, "Failed to update from version 2 to " + newVersion, ex);
			}
			finally
			{
				db.endTransaction();
			}
		}
	}
}
