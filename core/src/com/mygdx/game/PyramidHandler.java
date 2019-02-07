package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class PyramidHandler extends InputAdapter implements ApplicationListener {

	protected Label label;
	protected BitmapFont font;
	protected Stage stage;
	protected long startTime;
	public PerspectiveCamera cam;
	final float[] startPos = {150f, -9f, 0f};

	public Model model;
	public ModelInstance instance;

	public ModelBatch modelBatch;

	public Environment environment;

	public float width = 200f, height = width / 2f, depth = width;

	Color clr;
	Random rnd = new Random();


	final float bound = 45f;
	float[] pos = {startPos[0], startPos[1], startPos[2]};
	float[] Vpos = new float[3];
	final float speed = 2f;

	private float getSpeed() {
		return speed * Math.signum((float) Math.random() - 0.5f) * Math.max((float) Math.random(), 0.5f);
	}

	public void setColor(Color newColor)
	{
		model = new ModelBuilder().createCone(width, height, depth, 3,
				new Material(ColorAttribute.createDiffuse(newColor)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		instance = new ModelInstance(model);
		//instance.transform.setToRotation(Vector3.Z, 90).translate(-5,0,0);

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void create() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 10f, 10f, 20f));

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(startPos[0], startPos[1], startPos[2]);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		setColor(Color.GREEN);


		// initialize speed
		for (int i = 0; i < 3; i++){
			Vpos[i] = getSpeed();
		}


		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));

		stage = new Stage();
		stage.addActor(label);

		startTime = System.currentTimeMillis();

		Gdx.input.setInputProcessor(new InputMultiplexer(this));
	}

	@Override
	public void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();

		StringBuilder builder = new StringBuilder();
		builder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
		long time = System.currentTimeMillis() - startTime;
		builder.append("| Game time: ").append(time);
		label.setText(builder);
		stage.draw();

	}

	@Override
	public void dispose() {
		model.dispose();
		modelBatch.dispose();

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		clr = new Color(0xFF000000 | rnd.nextInt(0xFFFFFF));
		setColor(clr);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return true;
	}

	public void shake()
	{
		for (int i = 0; i < 3; i++) {
			pos[i] += Vpos[i];
			pos[i] = startPos[i] + (-1) * rnd.nextInt(200) + rnd.nextInt(200);
			Vpos[i] = getSpeed();
		}
		cam.position.set(pos[0], pos[1], pos[2]);
		cam.update();
	}
}