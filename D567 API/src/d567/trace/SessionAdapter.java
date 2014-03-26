package d567.trace;

import java.text.MessageFormat;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.text.format.Time;
import android.util.Log;

public class SessionAdapter 
{
	protected static String LOG_TAG = "D567_SESSION_ADAPTER";
	private SessionHelper _dbHelper = null;
	private SQLiteDatabase _db = null;
	
	public SessionAdapter(Context app)
	{
		_dbHelper = new SessionHelper(app);
	}
	
	protected void finalize() throws Throwable
	{
		if(_db != null)
			close();
		
		super.finalize();
	}
	
	public void open() throws IllegalStateException, SQLiteException
	{
		if(_db != null)
			throw new IllegalStateException("database already open");
		
		_db = _dbHelper.getWritableDatabase();
	}
	
	public void close() throws IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		_db.close();
		_db = null;		
	}
	
	public SessionInfo CreateSession(String desc, boolean bStart, TraceLevel session_level) throws IllegalStateException,SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		String session_id = java.util.UUID.randomUUID().toString();
		
		long start_time = 0;
		if(bStart)
		{
			Time t = new Time(Time.getCurrentTimezone());
			t.setToNow();
			start_time = t.toMillis(false);
		}
		
		ContentValues values = new ContentValues();
		values.put(SessionHelper.KEY_ID, session_id);
		values.put(SessionHelper.KEY_DESC,  desc);
		values.put(SessionHelper.KEY_LEVEL, session_level.toString());
		values.putNull(SessionHelper.KEY_END);
		
		if(bStart)
			values.put(SessionHelper.KEY_START, (bStart)? start_time : null);
		else 
			values.putNull(SessionHelper.KEY_START);		
				
		
		_db.insertOrThrow(SessionHelper.TABLE_NAME, null, values);		
		
		SessionInfo info = new SessionInfo(session_id, desc, session_level, (bStart)? start_time : null, null);
		return info;
	}
	
	public SessionInfo GetSessionById(String session_id) throws IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		Cursor c = _db.query(SessionHelper.TABLE_NAME, 
				new String[] {SessionHelper.KEY_DESC, SessionHelper.KEY_LEVEL, SessionHelper.KEY_START, SessionHelper.KEY_END},
				"? = '?'", new String[] {SessionHelper.KEY_ID, session_id}, null, null, null);
		
		if(!c.moveToFirst())
			return null;
		
		TraceLevel level = TraceLevel.UNKNOWN;		
		try
		{
			level = TraceLevel.valueOf(c.getString(1));
		}
		catch(Exception ex)
		{
			Log.e(LOG_TAG, MessageFormat.format("Failed to convert \"{0}\" to a TraceLevel. ERROR: {1}", c.getString(1), ex.getMessage()));
		}
		
		SessionInfo s = new SessionInfo(session_id, c.getString(0), level, c.getLong(2), c.getLong(3));
		return s;
	}
	
	public void StartSession(String session_id) throws Exception, IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		Time t = new Time(Time.getCurrentTimezone());
		t.setToNow();
		
		ContentValues values = new ContentValues();
		values.put(SessionHelper.KEY_START, t.toMillis(false));
		
		int count = _db.update(SessionHelper.TABLE_NAME, values, "? = '?' and ? is null", 
				new String[] {SessionHelper.KEY_ID,session_id, SessionHelper.KEY_START});
		
		if(count == 0)
		{
			throw new Exception(MessageFormat.format("Failed Start Session {0}", session_id));
		}		
	}

	public void EndSession(String session_id) throws Exception, IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		Time t = new Time(Time.getCurrentTimezone());
		t.setToNow();
		
		ContentValues values = new ContentValues();
		values.put(SessionHelper.KEY_END, t.toMillis(false));
		
		int count = _db.update(SessionHelper.TABLE_NAME, values, "? = '?'", 
				new String[] {SessionHelper.KEY_ID,session_id});
		
		if(count == 0)
		{
			throw new Exception(MessageFormat.format("No Session with ID {0} was found to be updated", session_id));
		}		
	}
	
	public void DeleteSession(String session_id) throws Exception, IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		_db.beginTransaction();
		
		long rows = _db.delete(TraceHelper.TABLE_NAME, "? = '?'",
				new String[] {TraceHelper.KEY_SESSION_ID, session_id});
		
		Log.v(LOG_TAG, MessageFormat.format("Deleted {0 number integer} rows from {1}" , rows, TraceHelper.TABLE_NAME));
		
		rows = _db.delete(SessionHelper.TABLE_NAME, "? = '?'", 
				new String[] {SessionHelper.TABLE_NAME, session_id});
		
		if(rows == 0)
		{
			throw new Exception(MessageFormat.format("No Session with ID {0} was found to be deleted", session_id));
		}
		
		_db.setTransactionSuccessful();
	}
}
