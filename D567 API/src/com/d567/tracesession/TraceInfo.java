package com.d567.tracesession;

public class TraceInfo 
{
	private String _sessionId;
	private String _id;
	private String _module;
	private TraceLevel _level;
	private Long _time;
	private String _msg;
	
	public TraceInfo(String SessionId, String TraceId) throws IllegalArgumentException
	{
		if(SessionId == null) throw new IllegalArgumentException("SessionId is NULL");
		if(TraceId == null) throw new IllegalArgumentException("TraceId is NULL");
		
		_sessionId = SessionId;
		_id = TraceId;
		_module = "";
		_level = TraceLevel.UNKNOWN;
		_time = null;
		_msg = "";
	}
	
	public TraceInfo(String SessionId, String TraceId, String Message) throws IllegalArgumentException
	{
		if(SessionId == null) throw new IllegalArgumentException("SessionId is NULL");
		if(TraceId == null) throw new IllegalArgumentException("TraceId is NULL");
		
		_sessionId = SessionId;
		_id = TraceId;
		_msg = Message;		
		_module = "";
		_level = TraceLevel.UNKNOWN;
		_time = null;
	}
	
	public TraceInfo(String SessionId, String TraceId, String Message, String Module, TraceLevel Level, Long Time) 
			throws IllegalArgumentException
	{
		if(SessionId == null) throw new IllegalArgumentException("SessionId is NULL");
		if(TraceId == null) throw new IllegalArgumentException("TraceId is NULL");
		
		_sessionId = SessionId;
		_id = TraceId;
		_msg = Message;
		_module = Module;
		_level = Level;
		_time = Time;		
	}
	
	public String getSessionId()
	{ return _sessionId; }
	
	public String getId()
	{ return _id; }
	
	public String getMessage()
	{ return _msg; }
	
	public String getModule()
	{ return _module; }
	
	public TraceLevel getTraceLevel()
	{ return _level; }
	
	public Long getTime()
	{ return _time; }
	
	public void setMessage(String message)
	{ _msg = message; }
	
	public void setModule(String module)
	{ _module = module; }
	
	public void setTraceLevel(TraceLevel level)
	{ _level = level; }
	
	public void setTime(Long time)
	{ _time = time; }
}
