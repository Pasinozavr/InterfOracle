package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.PyramidHandler;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.show;


public class AndroidLauncher extends AndroidApplication {

	private PyramidHandler myPyramidHandler;
	private ShakeListener myShakeListener;
	private MenuActivity menu;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		myPyramidHandler = new PyramidHandler();
		initialize(myPyramidHandler, config);
		menu = new MenuActivity();




		myShakeListener = new ShakeListener(this);
		myShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				Toast.makeText(AndroidLauncher.this, "Shaking ", Toast.LENGTH_LONG).show();
				myPyramidHandler.shake();
			}
		});

	}
}
