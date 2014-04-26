package d567.example.ContentProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.d567.app.ApplicationSettings;
import com.d567.app.intent.BundledSettings;

public class SettingsRequestHandler extends BroadcastReceiver
{
	private ApplicationSettings _settings = null;
	public ApplicationSettings getResult()
	{	return _settings; }
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.v("D567_SETTINGS_REQUEST_RESULT_HANDLER", "onReceive");
		_settings = new BundledSettings(intent.getExtras());			
	}		
}
