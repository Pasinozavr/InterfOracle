package com.crowni.gdx.navigationdrawer;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crowni.gdx.navigationdrawer.Functionaly.TempLauncher;

import java.io.File;

public class AndroidLauncher extends AndroidApplication {

	private ShakeListener myShakeListener;
	private TempLauncher tempLauncher;



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		String filePath="/storage/emulated/0/DCIM/" + R.string.app_name;
		File dir=new File(filePath);
		if(!dir.exists()){
			dir.mkdir();
		}

		tempLauncher = new TempLauncher();
		initialize(tempLauncher, config);

/*
		myShakeListener = new ShakeListener(this);
		myShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				Toast.makeText(AndroidLauncher.this, "Shaking ", Toast.LENGTH_LONG).show();

			}
		});
*/
	}
}
