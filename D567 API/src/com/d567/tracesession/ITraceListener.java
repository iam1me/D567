package com.d567.tracesession;

public interface ITraceListener
{	
	void StartTraceSession();
	void EndTraceSession();
	boolean IsSessionRunning();
	
	int GetTraceLevel();
	void SetTraceLevel(int flags);
	
	void OnTrace(int level, String traceMsg);
}
