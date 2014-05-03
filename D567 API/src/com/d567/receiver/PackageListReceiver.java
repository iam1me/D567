package com.d567.receiver;

import java.util.ArrayList;

import com.d567.request.PackageListRequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PackageListReceiver extends BroadcastReceiver
{
	protected final static String LOG_TAG = "D567_PACKAGE_REQUEST_HANDLER"; 
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.d(LOG_TAG, "onReceive");
		Bundle results = this.getResultExtras(false);
		if(results == null)
		{
			Log.d(LOG_TAG, "Results are null. Creating new Bundle");
			results = new Bundle();			
		}
		
		ArrayList<String> packages = results.getStringArrayList(PackageListRequest.EXTRA_PACKAGE_LIST);
		if(packages == null)
		{
			Log.d(LOG_TAG, "Package List is NULL. Creating Package List");
			packages = new ArrayList<String>();
		}
		
		packages.add(context.getPackageName());
		results.putStringArrayList(PackageListRequest.EXTRA_PACKAGE_LIST, packages);
		setResultExtras(results);
	}

}
