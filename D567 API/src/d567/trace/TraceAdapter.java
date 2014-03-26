package d567.trace;

import java.text.MessageFormat;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.text.format.Time;
import android.util.Log;

public class TraceAdapter 
{
	protected static String LOG_TAG = "D567_TRACE_ADAPTER";
	protected TraceHelper _dbHelper;
	protected SQLiteDatabase _db;
	
	public TraceAdapter(Context app)
	{
		_dbHelper = new TraceHelper(app);
		_db = null;
	}
	
	public TraceAdapter(Context app, String DBName)
	{
		_dbHelper = new TraceHelper(app, DBName);		
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
		values.put(TraceHelper.KEY_SESSION_ID, session_id);
		values.put(TraceHelper.KEY_ID, trace_id);
		values.put(TraceHelper.KEY_MODULE, module);
		values.put(TraceHelper.KEY_LEVEL, level.toString());
		values.put(TraceHelper.KEY_MESSAGE,  message);
		values.put(TraceHelper.KEY_TIME, time);
		
		_db.insertOrThrow(TraceHelper.TABLE_NAME, null, values);
	}
	
	public TraceInfo insertTrace(String session_id, String module, 
			TraceLevel level, String message) throws IllegalArgumentException, IllegalStateException, SQLiteException
	{
		if(session_id == null)
			throw new IllegalArgumentException("session_id is NULL");
		
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		String id = java.util.UUID.randomUUID().toString();
		
		ContentValues values = new ContentValues();
		values.put(TraceHelper.KEY_SESSION_ID, session_id);
		values.put(TraceHelper.KEY_ID, id);
		values.put(TraceHelper.KEY_MODULE, module);
		values.put(TraceHelper.KEY_LEVEL, level.toString());
		values.put(TraceHelper.KEY_MESSAGE,  message);
		
		Time now = new Time(Time.getCurrentTimezone());
		now.setToNow();
		values.put(TraceHelper.KEY_TIME, now.toMillis(false));
		
		_db.insertOrThrow(TraceHelper.TABLE_NAME, null, values);
		
		return new TraceInfo(session_id, id, message, module, level, now.toMillis(false));
	}
	
	public long deleteSessionTrace(String session_id) throws IllegalStateException,SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		long rows = _db.delete(TraceHelper.TABLE_NAME, "? = '?'",
				new String[] {TraceHelper.KEY_SESSION_ID, session_id});
		
		return rows;
	}
	
	public long getSessionTraceCount(String session_id) throws IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		Cursor c =_db.rawQuery("SELECT count(*) FROM ? WHERE ? = '?'", 
					new String[] {TraceHelper.TABLE_NAME, TraceHelper.KEY_ID, session_id}); 
		
		return c.getLong(0);
	}
	
	public TraceInfo getTraceById(String trace_id) throws IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		Cursor c = _db.query(TraceHelper.TABLE_NAME, 
					new String[] {TraceHelper.KEY_SESSION_ID, TraceHelper.KEY_MESSAGE,TraceHelper.KEY_MODULE, TraceHelper.KEY_LEVEL, TraceHelper.KEY_TIME}, 
					"? = '?'", new String[] {TraceHelper.KEY_ID, trace_id}, null, null, null);
		
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
	
	public void updateTrace(TraceInfo info) throws IllegalArgumentException, IllegalStateException, SQLiteException
	{
		if(info == null)
			throw new IllegalArgumentException("info is NULL");
		
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		ContentValues values = new ContentValues();
		values.put(TraceHelper.KEY_MODULE,  info.getModule());
		values.put(TraceHelper.KEY_MESSAGE, info.getMessage());
		values.put(TraceHelper.KEY_LEVEL, info.getTraceLevel().toString());
		values.put(TraceHelper.KEY_TIME,  info.getTime());
		
		int count = _db.update(TraceHelper.TABLE_NAME, values, "? = '?'", 
				new String[] {TraceHelper.KEY_ID, info.getId()});
		
		if(count == 0)
		{
			throw new RecordNotFoundException(
					MessageFormat.format("No Trace Record Found With ID: {0}", info.getId()));
		}		
	}

}
