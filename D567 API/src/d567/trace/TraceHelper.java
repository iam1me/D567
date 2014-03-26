package d567.trace;

import android.content.*;
import android.database.sqlite.*;
import android.util.Log;

import java.text.*;

public class TraceHelper extends android.database.sqlite.SQLiteOpenHelper
{
	protected static String LOG_TAG = "TRACE_HELPER";	
		
	public static final String TABLE_NAME = "session_trace_dtl";
	public static final int DB_VER = 1;
	
	public static final String KEY_SESSION_ID = "session_id";
	public static final String KEY_ID = "trace_id";
	public static final String KEY_MODULE = "trace_module";
	public static final String KEY_LEVEL = "trace_level";
	public static final String KEY_TIME = "trace_time";
	public static final String KEY_MESSAGE = "trace_message";
	
	private String _dbName;
	public String getDBName()
	{
		return _dbName;
	}
	
	/**
	 * This constructor assumes that the database name is "D567"
	 * @param app	The Context used for accessing the database
	 */
	public TraceHelper(Context app)
	{
		super(app, "D567", null, DB_VER);		
		_dbName = "D567";
	}
	
	/**
	 * This constructor allows the database containing the trace information to be specified
	 * @param app	The Context used for accessing the database
	 * @param D567_DBName	The name of database that contains the tracing information
	 */
	public TraceHelper(Context app, String D567_DBName)
	{
		super(app, D567_DBName, null, DB_VER);		
		_dbName = D567_DBName;
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		try
		{
			String tableSQL = MessageFormat.format(
				"create table {0} ({1} text not null, {2} text primary key not null, {3} text, {4} string not null," +
				"{4} integer not null, {5} integer not null, {6} text not null, FOREIGN KEY {1} REFERENCES {7} ({8}))",
				getDBName(), KEY_SESSION_ID, KEY_ID, KEY_MODULE, KEY_LEVEL, KEY_TIME, KEY_MESSAGE,
				SessionHelper.TABLE_NAME, SessionHelper.KEY_ID);
		
			db.execSQL(tableSQL);
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, ex.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.e(LOG_TAG, MessageFormat.format(
				"Unexpected attempt to upgrade DB from version {0,number,integer} to {1,number,integer}",
				new Object[] {oldVersion, newVersion}));
	}
}
