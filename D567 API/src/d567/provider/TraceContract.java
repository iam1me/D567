package d567.provider;

import android.net.Uri;

public class TraceContract 
{
	public static final Uri URI_TRACE = Uri.parse("content://d567.provider/trace");
	public static final String PATH = "/trace";
	
	public static final String MIME_TRACE_DIR = "vnd.android.cursor.dir/d567.provider.trace";
	public static final String MIME_TRACE_ITEM = "vnd.android.cursor.item/d567.provider.trace";
	
	public static final String PARAM_SESSION_ID = "SessionId";
    public static final String PARAM_TRACE_ID = "TraceId";
}
