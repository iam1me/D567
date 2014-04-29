package com.d567.db;

import android.database.sqlite.*;
import android.util.Log;

import java.text.*;

//import d567.db.IDatabaseTable;

public class SessionTable// implements IDatabaseTable
{
	protected static String LOG_TAG = "SESSION_HELPER";	
		
	public static final String TABLE_NAME = "session_mstr";	
	public static final String KEY_ID = "session_id";
	public static final String KEY_DESC = "session_desc";
	public static final String KEY_START = "session_start_time";
	public static final String KEY_END = "session_end_time";
	
	/**
	 * DEPRECATED. This column is no longer valid as of Database Version 2
	 */
	@Deprecated
	public static final String KEY_LEVEL = "session_trace_level";
	
	public static void createTable(SQLiteDatabase db) throws SQLiteException
	{
		Log.d(LOG_TAG, "createTable");

		String tableSQL = MessageFormat.format(
			"create table {0} ({1} text primary key not null, {2} text, {3} integer not null, {4} integer)",
			SessionTable.TABLE_NAME, KEY_ID, KEY_DESC, KEY_START, KEY_END);
	
		db.execSQL(tableSQL);
	}

	public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if(oldVersion == 1)
		{
			//NOTE: SQLite has limited ALTER TABLE support. You cannot drop columns.
			//As a work around, we can backup the table, drop it, recreate it, and copy
			//the data back over from the backup - minus the content from the dropped
			//column(s).
			db.beginTransaction();
			try
			{
				String tempTable = TABLE_NAME + "_backup";
				String columnList = MessageFormat.format("{0},{1},{2},{3}",
						KEY_ID, KEY_DESC, KEY_START, KEY_END);
				
				//Create temp table
				db.execSQL(MessageFormat.format("CREATE TEMPORARY TABLE {0} ({1})", tempTable, columnList));
				
				//Copy data to temp table
				db.execSQL(MessageFormat.format("INSERT INTO {0} SELECT {1} FROM {2}",
						tempTable, columnList, TABLE_NAME));
				
				//Drop session_mstr table
				db.execSQL(MessageFormat.format("DROP TABLE {0}", TABLE_NAME));
				
				//Recreate session_mstr table, minus the session_trace_level column
				createTable(db);
				
				//Copy the data over from the backup table
				db.execSQL(MessageFormat.format("INSERT INTO {0} SELECT {1} FROM {2}",
						TABLE_NAME, columnList, tempTable));
				
				//Drop temp table
				db.execSQL(MessageFormat.format("DROP TABLE {0}", tempTable));
				
				//DONE!
				db.setTransactionSuccessful();
			}
			catch(Exception ex)
			{
				Log.e(LOG_TAG, "Failed to update table from version 1", ex);
			}
			finally
			{
				db.endTransaction();
			}
		}
	}
}
