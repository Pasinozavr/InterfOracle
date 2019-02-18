package com.crowni.gdx.navigationdrawer;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crowni.gdx.navigationdrawer.Functionaly.TempLauncher;


public class AndroidLauncher extends AndroidApplication {
	private TempLauncher tempLauncher;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		/*
		String filePath="/storage/emulated/0/DCIM/" + R.string.app_name;
		File dir=new File(filePath);
		if(!dir.exists()){
			dir.mkdir();
		}
		*/

		tempLauncher = new TempLauncher();
		initialize(tempLauncher, config);
	}
}
