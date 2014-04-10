package d567.trace;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import d567.db.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.text.format.Time;
import android.util.Log;

public class TraceAdapter 
{
	protected static String LOG_TAG = "D567_TRACE_ADAPTER";
	protected DBHelper _dbHelper;
	protected SQLiteDatabase _db;
	
	public TraceAdapter(Context app)
	{
		Log.d(LOG_TAG, "Constructor");
		_dbHelper = new DBHelper(app);
		_db = null;
	}
	
	public TraceAdapter(Context app, String DBName)
	{
		Log.d(LOG_TAG, "Constructor. DB: " + DBName);
		_dbHelper = new DBHelper(app, DBName);		
		_db = null;
	}
	
	protected void finalize() throws Throwable
	{
		if(_db != null)
			_db.close();
		
		super.finalize();		
	}
	
	public void open() throws IllegalStateException,SQLiteException
	{
		if(_db != null)
			throw new IllegalStateException("database already open");
		
		_db = _dbHelper.getWritableDatabase();
	}
	
	public void close() throws IllegalStateException,SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		_db.close();
	}
	
	public void insertTrace(String session_id, String trace_id, String module, 
			TraceLevel level, String message, long time) throws IllegalStateException,SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		ContentValues values = new ContentValues();
		values.put(TraceTable.KEY_SESSION_ID, session_id);
		values.put(TraceTable.KEY_ID, trace_id);
		values.put(TraceTable.KEY_MODULE, module);
		values.put(TraceTable.KEY_LEVEL, level.toString());
		values.put(TraceTable.KEY_MESSAGE,  message);
		values.put(TraceTable.KEY_TIME, time);
		
		_db.insertOrThrow(TraceTable.TABLE_NAME, null, values);
	}
	
	public TraceInfo insertTrace(String session_id, String module, 
			TraceLevel level, String message) throws IllegalArgumentException, IllegalStateException, SQLiteException
	{
		if(session_id == null)
			throw new IllegalArgumentException("session_id is NULL");
		
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		Log.d(LOG_TAG, "insertTrace");
		
		String id = java.util.UUID.randomUUID().toString();
		
		ContentValues values = new ContentValues();
		values.put(TraceTable.KEY_SESSION_ID, session_id);
		values.put(TraceTable.KEY_ID, id);
		values.put(TraceTable.KEY_MODULE, module);
		values.put(TraceTable.KEY_LEVEL, level.toString());
		values.put(TraceTable.KEY_MESSAGE,  message);
		
		Time now = new Time(Time.getCurrentTimezone());
		now.setToNow();
		values.put(TraceTable.KEY_TIME, now.toMillis(false));
		
		Log.d(LOG_TAG, "insertTrace - Inserting Trace Record");
		
		long row = _db.insertOrThrow(TraceTable.TABLE_NAME, null, values);
		
		Log.d(LOG_TAG, "insertTrace - Insert Complete. Row = " + row);
		
		return new TraceInfo(session_id, id, message, module, level, now.toMillis(false));
	}
	
	public long deleteSessionTrace(String session_id) throws IllegalStateException,SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		long rows = _db.delete(TraceTable.TABLE_NAME, TraceTable.KEY_SESSION_ID + " = ?",
				new String[] {session_id});
		
		return rows;
	}
	
	public long getSessionTraceCount(String session_id) throws IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		String sql = MessageFormat.format("SELECT count(*) FROM {0} WHERE {1} = ?",
						TraceTable.TABLE_NAME, TraceTable.KEY_ID);
		
		Cursor c =_db.rawQuery(sql, new String[] {session_id}); 
		
		return c.getLong(0);
	}
	
	public TraceInfo getTraceById(String trace_id) throws IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		Cursor c = _db.query(TraceTable.TABLE_NAME, 
					new String[] {TraceTable.KEY_SESSION_ID, TraceTable.KEY_MESSAGE,TraceTable.KEY_MODULE, TraceTable.KEY_LEVEL, TraceTable.KEY_TIME}, 
					TraceTable.KEY_ID + " = ?", new String[] {trace_id}, null, null, null);
		
		if(c.getCount() == 0)
			return null;
		
		TraceLevel level = TraceLevel.UNKNOWN;
		try
		{
			level = TraceLevel.valueOf(c.getString(3));
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, MessageFormat.format("Failed to parse TraceLevel from string {0} [Trace Id: {1}]", c.getString(3), trace_id));
		}
		
		TraceInfo info = new TraceInfo(c.getString(0), trace_id, c.getString(1), c.getString(2), level, c.getLong(4));
		return info;
	}
	
	public List<TraceInfo> getSessionTrace(String session_id) throws Exception, IllegalStateException, IllegalArgumentException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		if(session_id == null)
			throw new IllegalArgumentException("session_id cannot be null");
		
		Log.d(LOG_TAG, "getSessionTrace - SessionId: " + session_id);
		
		String[] columns = new String[] {TraceTable.KEY_ID, TraceTable.KEY_MESSAGE,TraceTable.KEY_MODULE, TraceTable.KEY_LEVEL, TraceTable.KEY_TIME};
		
		String selection = MessageFormat.format("{0} = ?", TraceTable.KEY_SESSION_ID);
		String[] selectArgs = new String[] {session_id};
		
		String orderBy = TraceTable.KEY_TIME + " DESC";
		
		Cursor c = _db.query(TraceTable.TABLE_NAME, columns, selection, selectArgs, 
				null, null, orderBy);
		
		Log.d(LOG_TAG, "getSessionTrace - " + c.getCount() + " records found");
				
		List<TraceInfo> traceList = new ArrayList<TraceInfo>();
		if(!c.moveToFirst())
		{
			return traceList;
		}		
		
		Log.d(LOG_TAG, "getSessionTrace - constructing List");
		while(!c.isAfterLast())
		{		
			TraceLevel level = TraceLevel.UNKNOWN;
			try
			{
				level = TraceLevel.valueOf(c.getString(3));
			}
			catch(Exception ex)
			{
				Log.e(LOG_TAG, MessageFormat.format("Failed to parse TraceLevel from string {0} [Session Id: {1}]", c.getString(3), session_id));
				throw ex;
			}
			
			TraceInfo info = new TraceInfo(session_id, c.getString(0), c.getString(1), c.getString(2), level, c.getLong(4));
			traceList.add(info);
			
			c.moveToNext();
		}
		
		return traceList;
	}
	
	
	public void updateTrace(TraceInfo info) throws IllegalArgumentException, IllegalStateException, SQLiteException
	{
		if(info == null)
			throw new IllegalArgumentException("info is NULL");
		
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		ContentValues values = new ContentValues();
		values.put(TraceTable.KEY_MODULE,  info.getModule());
		values.put(TraceTable.KEY_MESSAGE, info.getMessage());
		values.put(TraceTable.KEY_LEVEL, info.getTraceLevel().toString());
		values.put(TraceTable.KEY_TIME,  info.getTime());
		
		int count = _db.update(TraceTable.TABLE_NAME, values, TraceTable.KEY_ID + " = ?", 
				new String[] {info.getId()});
		
		if(count == 0)
		{
			throw new RecordNotFoundException(
					MessageFormat.format("No Trace Record Found With ID: {0}", info.getId()));
		}		
	}

}
