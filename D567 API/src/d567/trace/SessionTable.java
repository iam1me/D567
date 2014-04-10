package d567.trace;

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
	public static final String KEY_LEVEL = "session_trace_level";
	
	public static void createTable(SQLiteDatabase db) throws SQLiteException
	{
		Log.d(LOG_TAG, "createTable");

		String tableSQL = MessageFormat.format(
			"create table {0} ({1} text primary key not null, {2} text, {3} integer not null, {4} integer,"+
		    "{5} string not null)",
			SessionTable.TABLE_NAME, KEY_ID, KEY_DESC, KEY_START, KEY_END, KEY_LEVEL);
	
		db.execSQL(tableSQL);
	}

	public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(LOG_TAG, "updateTable");
	}
}
