package d567.provider;

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
	
	public static final String AUTHORITY = "d567.provider";
	public static final Uri ContentUri = Uri.parse("content://" + AUTHORITY);
		
	protected static final int CONTENT_SESSION = 1;
	protected static final int CONTENT_TRACE = 2;
	
	protected static final UriMatcher _matcher;
	static
	{
		_matcher = new UriMatcher(UriMatcher.NO_MATCH);
		_matcher.addURI(AUTHORITY, SessionContract.PATH, CONTENT_SESSION);
		_matcher.addURI(AUTHORITY, TraceContract.PATH, CONTENT_TRACE);
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
			{
				String session_id = uri.getQueryParameter(SessionContract.PARAM_SESSION_ID);
				
				if(session_id == null)		
				{
					return SessionContract.MIME_SESSION_DIR;
				}
				else
				{
					return SessionContract.MIME_SESSION_ITEM;
				}
			}
				
			case CONTENT_TRACE:
			{
				String trace_id = uri.getQueryParameter(TraceContract.PARAM_TRACE_ID);
				
				if(trace_id == null)
				{
					return TraceContract.MIME_TRACE_DIR;
				}
				else
				{
					return TraceContract.MIME_TRACE_ITEM;
				}
			}
				
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
			{
				String session_id = uri.getQueryParameter(SessionContract.PARAM_SESSION_ID);
				
				if(session_id == null)
				{
					return queryForSessions(projection,selection,selectionArgs,sortOrder);
				}
				else
				{
					return queryForSessionById(session_id,projection,selection,selectionArgs,sortOrder);
				}
			}
				
				
			case CONTENT_TRACE:
			{
				String session_id = uri.getQueryParameter(SessionContract.PARAM_SESSION_ID);
				String trace_id = uri.getQueryParameter(TraceContract.PARAM_TRACE_ID);
				
				if(session_id != null && trace_id == null)
				{
					return queryForTrace(session_id,projection,selection,selectionArgs,sortOrder);
				}
				else if(trace_id != null)
				{
					return queryForTraceById(trace_id, projection, selection, selectionArgs, sortOrder);
				}
				
				throw new IllegalArgumentException("Missing URI Parameters");
			}
				
			default:
				throw new IllegalArgumentException("Invalid Query URI: " + uri.toString());		
		}
	}
	
	protected Cursor queryForSessions(String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(SessionTable.TABLE_NAME);
		
		return qb.query(_db, projection, selection, selectionArgs, null, null, sortOrder);		
	}
	
	protected Cursor queryForSessionById(String session_id, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(SessionTable.TABLE_NAME);
		qb.appendWhere(MessageFormat.format("{0} = ''{1}''", SessionTable.KEY_ID, session_id));
		
		return qb.query(_db, projection, selection, selectionArgs, null, null, sortOrder);
	}
	
	protected Cursor queryForTrace(String session_id, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TraceTable.TABLE_NAME);
		qb.appendWhere(MessageFormat.format("{0} = ''{1}''", TraceTable.KEY_SESSION_ID, session_id));
		
		return qb.query(_db, projection, selection, selectionArgs, null, null, sortOrder);		
	}
	
	protected Cursor queryForTraceById(String trace_id, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TraceTable.TABLE_NAME);
		qb.appendWhere(MessageFormat.format("{0} = ''{1}''", TraceTable.KEY_ID, trace_id));
		
		return qb.query(_db, projection, selection, selectionArgs, null, null, sortOrder);		
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) 
	{
		throw new UnsupportedOperationException("The TraceSessionProvider is Read-Only");
	}

}
