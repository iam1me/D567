package com.d567.tracesession;

import android.database.sqlite.*;
import android.util.Log;

import java.text.*;

//import d567.db.IDatabaseTable;

public class TraceTable// implements IDatabaseTable
{
	protected static String LOG_TAG = "TRACE_HELPER";	
		
	public static final String TABLE_NAME = "session_trace_dtl";	
	public static final String KEY_SESSION_ID = "session_id";
	public static final String KEY_ID = "trace_id";
	public static final String KEY_MODULE = "trace_module";
	public static final String KEY_LEVEL = "trace_level";
	public static final String KEY_TIME = "trace_time";
	public static final String KEY_MESSAGE = "trace_message";

	public static void createTable(SQLiteDatabase db) 
	{
		Log.d(LOG_TAG, "createTable");
		
		String tableSQL = MessageFormat.format(
			"create table {0} ({1} text not null, {2} text primary key not null, {3} text, " +
			"{4} string not null, {5} integer not null, {6} text not null," +
			"FOREIGN KEY ({1}) REFERENCES {7} ({8}))",
			TraceTable.TABLE_NAME, KEY_SESSION_ID, KEY_ID, KEY_MODULE, KEY_LEVEL, KEY_TIME, KEY_MESSAGE,
			SessionTable.TABLE_NAME, SessionTable.KEY_ID);
	
		db.execSQL(tableSQL);
	}

	public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion) 
	{		
		Log.d(LOG_TAG, "upgradeTable");
	}
}
