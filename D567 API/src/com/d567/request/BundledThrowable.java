package com.d567.request;

import android.os.Bundle;

public class BundledThrowable extends Throwable
{
	private static final long serialVersionUID = 2181713703127724661L;

	public BundledThrowable(Bundle b)
	{
		super(b.getString(DisplayErrorRequest.EXTRA_ERROR_MESSAGE));
	}
	
	public static Bundle toBundle(Throwable t)
	{
		Bundle b = new Bundle();
		
		b.putString(DisplayErrorRequest.EXTRA_ERROR_MESSAGE, t.getMessage());
		
		return b;
	}

}
