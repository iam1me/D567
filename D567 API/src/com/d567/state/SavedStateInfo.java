package com.d567.state;

public class SavedStateInfo 
{
	/**********************************
	 *        MEMBER VARIABLES
	 **********************************/	
	private String _id = null;
	private String _desc = null;
	private Long _time = null;
	private String _data = null;
	
	/**********************************
	 *        GET/SET FUNCTIONS
	 **********************************/	
	public String getId()
	{ return _id; }
	
	public void setId(String id)
	{
		if(id == null) throw new IllegalArgumentException("id is NULL");
		this._id = id; 
	}
	
	public String getDescription()
	{ return _desc; }
	
	public void setDescription(String desc)
	{
		this._desc = desc; 
	}	
	
	public Long getTime()
	{ return _time; }
	
	public void setTime(Long time)
	{
		if(time == null) throw new IllegalArgumentException("time is NULL");
		this._time = time; 
	}
	
	public String getData()
	{ return _data; }
	
	public void setData(String data)
	{
		if(data == null) throw new IllegalArgumentException("data is NULL");
		this._data = data; 
	}
	
	/**********************************
	 *          CONSTRUCTOR(S)
	 **********************************/	
	public SavedStateInfo(String id, String desc, Long time, String data)
	{
		if(id == null)
			throw new IllegalArgumentException("id is NULL");
		
		if(time == null)
			throw new IllegalArgumentException("time is NULL");
		
		if(data == null)
			throw new IllegalArgumentException("data is NULL");
		
		this._id = id;
		this._desc = desc;
		this._time = time;
		this._data = data;
	}
}
