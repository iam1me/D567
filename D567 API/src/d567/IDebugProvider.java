package d567;

import d567.state.IStateListener;
import d567.trace.ITraceListener;
import android.content.Intent;
import android.os.Bundle;

public interface IDebugProvider
{	
	boolean RegisterTraceListener(ITraceListener l);
	void UnregisterTraceListener(ITraceListener l);
	boolean RegisterStateListener(IStateListener l);
	void UnregisterStateListener(IStateListener l);
	
	String GetManagementXML();	
	void processManagementIntent(Intent i);
	
	void Trace(String tag, String message, String... messageParameters);
	
	Bundle getState();	
	void loadState(Bundle state);
}
