package d567.provider;

import android.net.Uri;

public class SessionContract 
{
	public static final Uri URI_SESSION = Uri.parse("content://d567.provider/session");
	public static final String PATH = "/session";
	
	public static final String MIME_SESSION_DIR = "vnd.android.cursor.dir/d567.provider.session";
	public static final String MIME_SESSION_ITEM = "vnd.android.cursor.item/d567.provider.session";
	
	public static final String PARAM_SESSION_ID = "SessionId";
}
