package com.crowni.gdx.navigationdrawer;

import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crowni.gdx.navigationdrawer.Functionaly.TempLauncher;

public class AndroidLauncher extends AndroidApplication {

	private ShakeListener myShakeListener;
	private TempLauncher tempLauncher;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		tempLauncher = new TempLauncher();
		initialize(tempLauncher, config);


		myShakeListener = new ShakeListener(this);
		myShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				Toast.makeText(AndroidLauncher.this, "Shaking ", Toast.LENGTH_LONG).show();
				//tstScr.shake();
			}
		});
	}
}
