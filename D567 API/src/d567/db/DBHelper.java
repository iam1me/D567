package d567.db;

import android.content.*;
import android.database.sqlite.*;
import android.util.Log;

import java.text.*;

import d567.trace.SessionTable;
import d567.trace.TraceTable;

public class DBHelper extends android.database.sqlite.SQLiteOpenHelper
{
	protected static String LOG_TAG = "D567_DB_HELPER";	
	public static final int DB_VER = 1;
	
	private String _dbName;
	public String getDBName()
	{
		return _dbName;
	}

	/**
	 * This constructor assumes that the database name is "D567"
	 * @param app	The Context used for accessing the database
	 */
	public DBHelper(Context app)
	{
		super(app, "D567", null, DB_VER);	
		_dbName = "D567";
	}
	
	/**
	 * This constructor allows the database containing the trace information to be specified
	 * @param app	The Context used for accessing the database
	 * @param D567_DBName	The name of database that contains the tracing information
	 */
	public DBHelper(Context app, String D567_DBName)
	{
		super(app, D567_DBName, null, DB_VER);
		_dbName = D567_DBName;

		Log.v(LOG_TAG, "Constructor. DB: " + _dbName);
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
		Log.e(LOG_TAG, MessageFormat.format(
				"Unexpected attempt to upgrade DB from version {0,number,integer} to {1,number,integer}",
				new Object[] {oldVersion, newVersion}));
	}
	
	@Override
	public void onOpen(SQLiteDatabase db)
	{
		Log.v(LOG_TAG, "onOpen");
		super.onOpen(db);
	}
}
