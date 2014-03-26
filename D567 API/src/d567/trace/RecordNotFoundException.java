package d567.trace;

import android.database.sqlite.SQLiteException;

public class RecordNotFoundException extends SQLiteException
{
	private static final long serialVersionUID = -6270687689012668799L;

	public RecordNotFoundException()
	{
		super();
	}	
	
	public RecordNotFoundException(String error)
	{
		super(error);
	}
	
	public RecordNotFoundException(String error, Throwable t)
	{
		super(error,t);
	}
}
