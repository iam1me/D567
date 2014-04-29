package com.d567.tracesession;

public class SessionInfo 
{
	private String _id;
	private String _desc = "";
	private Long _start = null;
	private Long _end = null;
	
	public SessionInfo(String id) throws IllegalArgumentException
	{
		if(id == null) throw new IllegalArgumentException("id is NULL");
		
		_id = id;
	}
	
	public SessionInfo(String id, String desc) throws IllegalArgumentException
	{
		if(id == null) throw new IllegalArgumentException("id is NULL");
		
		_id = id;
		_desc = desc;
	}
	
	public SessionInfo(String id, String desc, Long start, Long end) throws IllegalArgumentException
	{
		if(id == null) throw new IllegalArgumentException("id is NULL");
		
		_id = id;
		_desc = desc;
		_start = start;
		_end = end;
	}
	
	public String getId()
	{
		return _id;
	}
	
	public String getDesc()
	{
		return _desc;
	}
	
	public Long getStartTime()
	{
		return _start;
	}
	
	public Long getEndTime()
	{
		return _end;
	}
	
	public void setDesc(String desc)
	{
		_desc = desc;
	}
	
	public void setStartTime(Long start)
	{
		_start = start;
	}
	
	public void setEndTime(Long end)
	{
		_end = end;
	}	
}
