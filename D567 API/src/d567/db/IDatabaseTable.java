package d567.db;

import android.database.sqlite.*;

public interface IDatabaseTable 
{
	public void createTable(SQLiteDatabase db) throws SQLiteException;	
	public void updateTable(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLiteException;
}
