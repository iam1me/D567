package com.d567.db;

import java.text.MessageFormat;

import com.d567.app.ApplicationSettings;
import com.d567.state.SavedStateInfo;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.text.format.Time;

public class SavedStateAdapter 
{
	protected final static String LOG_TAG = "D567_SAVED_STATE_ADAPTER";
	private DBHelper _dbHelper = null;
	private SQLiteDatabase _db = null;
	
	public SavedStateAdapter(Context app, ApplicationSettings settings)
	{
		_dbHelper = new DBHelper(app, settings);
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
	
	public boolean isOpen()
	{
		return (_db == null)? false : _db.isOpen(); 
	}
	
	public void close() throws IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		_db.close();
		_db = null;		
	}
	
	public SavedStateInfo CreateSavedState(String desc, String data) throws IllegalStateException,SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		String id = java.util.UUID.randomUUID().toString();
		
		long time = 0;
		Time t = new Time(Time.getCurrentTimezone());
		t.setToNow();
		time = t.toMillis(false);
		
		ContentValues values = new ContentValues();
		values.put(SavedStateTable.KEY_ID, id);
		values.put(SavedStateTable.KEY_DESC,  desc);
		values.put(SavedStateTable.KEY_TIME, time);
		values.put(SavedStateTable.KEY_DATA, data);				
		
		_db.insertOrThrow(SessionTable.TABLE_NAME, null, values);		
		
		SavedStateInfo info = new SavedStateInfo(id, desc, time, data);
		return info;
	}
	
	public SavedStateInfo GetSavedStateById(String id) throws IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		if(id == null)
			throw new IllegalArgumentException("id is NULL");
		
		Cursor c = _db.query(SavedStateTable.TABLE_NAME, 
				new String[] {SavedStateTable.KEY_DESC, SavedStateTable.KEY_TIME, SavedStateTable.KEY_DATA},
				SavedStateTable.KEY_ID + " = ?", new String[] {id}, null, null, null);
		
		if(!c.moveToFirst())
			return null;
		
		SavedStateInfo info = new SavedStateInfo(id, c.isNull(0)? null : c.getString(0), c.isNull(1)? null : c.getLong(1), c.isNull(2)? null : c.getString(2));
		return info;
	}
	
	public void DeleteSavedState(String id) throws IllegalArgumentException, IllegalStateException, SQLiteException
	{
		if(_db == null)
			throw new IllegalStateException("database not open");
		
		if(id == null)
			throw new IllegalArgumentException("id is NULL");
		
		String select = SavedStateTable.KEY_ID + " = ?";
		String[] select_args = new String[] { id };
		
		int rows = _db.delete(SavedStateTable.TABLE_NAME, select, select_args); 
		if(rows == 0)
		{
			throw new SQLiteException(MessageFormat.format("No Saved State with ID \"{0}\" Found For Deletion.", id));
		}	
	}
}
