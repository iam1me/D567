package d567.providers;

import java.text.MessageFormat;

import d567.db.DBHelper;
import d567.trace.*;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class TraceSessionProvider extends ContentProvider 
{
	protected static final String LOG_TAG = "D567_TRACE_SESSION_PROVIDER";
	static final String PROVIDER_NAME = "d567.providers.tracesession";
	static final String URL = "content://" + PROVIDER_NAME + "/sessions";
	static final Uri CONTENT_URI = Uri.parse(URL);
		
	protected static final int CONTENT_SESSION = 0;
	protected static final int CONTENT_SESSION_ID = 1;
	protected static final int CONTENT_TRACE = 2;
	protected static final int CONTENT_TRACE_ID = 3;
	
	protected static final String MIME_SESSION = "vnd.android.cursor.dir/d567.trace.session";
	protected static final String MIME_SESSION_ID = "vnd.android.cursor.item/d567.trace.session";
	protected static final String MIME_TRACE = "vnd.android.cursor.dir/d567.trace.trace";
	protected static final String MIME_TRACE_ID = "vnd.android.cursor.item/d567.trace.trace";	
	
	protected static final UriMatcher _matcher;
	static
	{
		_matcher = new UriMatcher(UriMatcher.NO_MATCH);
		_matcher.addURI(PROVIDER_NAME, "sessions", CONTENT_SESSION);
		_matcher.addURI(PROVIDER_NAME, "sessions/*", CONTENT_SESSION_ID);
		_matcher.addURI(PROVIDER_NAME, "sessions/*/trace", CONTENT_TRACE);
		_matcher.addURI(PROVIDER_NAME, "sessions/*/trace/*", CONTENT_TRACE_ID);
	}
		
	private DBHelper _dbHelper = null;
	private SQLiteDatabase _db = null;
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) 
	{
		throw new UnsupportedOperationException("The TraceSessionProvider is Read-Only");
	}

	@Override
	public String getType(Uri uri)
	{
		switch(_matcher.match(uri))
		{
			case CONTENT_SESSION:
				return MIME_SESSION;
				
			case CONTENT_SESSION_ID:
				return MIME_SESSION_ID;
				
			case CONTENT_TRACE:
				return MIME_TRACE;
				
			case CONTENT_TRACE_ID:
				return MIME_TRACE_ID;
				
			default:
				throw new IllegalArgumentException("Invalid URI: " + uri.toString());		
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) 
	{
		throw new UnsupportedOperationException("The TraceSessionProvider is Read-Only");
	}

	@Override
	public boolean onCreate() 
	{
		try
		{
			Context c = getContext();
			_dbHelper = new DBHelper(c);	
			_db = _dbHelper.getReadableDatabase();
		}
		catch(Exception ex)
		{
			_db = null;
			_dbHelper = null;
			
			Log.e(LOG_TAG, MessageFormat.format("failed to open databases. {0}", ex.getMessage()));
			return false;
		}	
		
		return true;
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		_db.close();
		_db = null;
		_dbHelper = null;
		
		super.finalize();
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) 
	{
		switch(_matcher.match(uri))
		{
			case CONTENT_SESSION:
				return queryForSessions(uri,projection,selection,selectionArgs,sortOrder);
				
			case CONTENT_SESSION_ID:
				return queryForSessionById(uri,projection,selection,selectionArgs,sortOrder);
				
			case CONTENT_TRACE:
				return queryForTrace(uri,projection,selection,selectionArgs,sortOrder);
				
			case CONTENT_TRACE_ID:
				return queryForTraceById(uri,projection,selection,selectionArgs,sortOrder);
				
			default:
				throw new IllegalArgumentException("Invalid Query URI: " + uri.toString());		
		}
	}
	
	protected Cursor queryForSessions(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(SessionTable.TABLE_NAME);
		
		return qb.query(_db, projection, selection, selectionArgs, null, null, sortOrder);		
	}
	
	protected Cursor queryForSessionById(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		String session_id = uri.getPathSegments().get(1);
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(SessionTable.TABLE_NAME);
		qb.appendWhere(MessageFormat.format("? = '?'", SessionTable.KEY_ID, session_id));
		
		return qb.query(_db, projection, selection, selectionArgs, null, null, sortOrder);
	}
	
	protected Cursor queryForTrace(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		String session_id = uri.getPathSegments().get(1);
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TraceTable.TABLE_NAME);
		qb.appendWhere(MessageFormat.format("? = '?'", TraceTable.KEY_SESSION_ID, session_id));
		
		return qb.query(_db, projection, selection, selectionArgs, null, null, sortOrder);		
	}
	
	protected Cursor queryForTraceById(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		String session_id = uri.getPathSegments().get(1);
		String trace_id = uri.getPathSegments().get(3);
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TraceTable.TABLE_NAME);
		qb.appendWhere(MessageFormat.format("? = '?'", TraceTable.KEY_SESSION_ID, session_id));
		qb.appendWhere(MessageFormat.format("? = '?'", TraceTable.KEY_ID, trace_id));
		
		return qb.query(_db, projection, selection, selectionArgs, null, null, sortOrder);		
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) 
	{
		throw new UnsupportedOperationException("The TraceSessionProvider is Read-Only");
	}

}
