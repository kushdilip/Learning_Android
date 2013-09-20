package com.dk.helloworld;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	 private static Context context;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MyApplication.context = getApplicationContext();
	}
	
	public static Context getAppContext() {
		return MyApplication.context;
	}
	 
	 
	
}
