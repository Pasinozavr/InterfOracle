package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.Random;


public class PyramidHandler extends InputAdapter implements ApplicationListener {

	private AssetManager assets;
	private Label label;
	private BitmapFont font;
	private Stage stage;
	private PerspectiveCamera cam;

	private long startTime;
	private float width = 200f, height = width / 2f, depth = width, bound = 45f;
	private final float[] startPos = {150f, -9f, 0f}, Vpos = new float[3], pos = {startPos[0], startPos[1], startPos[2]};
	private final float speed = 2f;

	private Model model;
	private ModelInstance instance;
	private ModelBatch modelBatch;
	private Environment environment;

	private boolean loading;

	private Color clr;
	private Random rnd = new Random();

	private float getSpeed() {
		return speed * Math.signum((float) Math.random() - 0.5f) * Math.max((float) Math.random(), 0.5f);
	}

/*
	public void setColor(Color newColor)
	{
		model = new ModelBuilder().createCone(width, height, depth, 3,
				new Material(ColorAttribute.createDiffuse(newColor)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		instance = new ModelInstance(model);
		instance.transform.setToRotation(Vector3.Z, 90).translate(-5,0,0);
	}
*/

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

		//setColor(Color.GREEN);

		for (int i = 0; i < 3; i++){
			Vpos[i] = getSpeed();
		}

		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));

		stage = new Stage();
		stage.addActor(label);

		startTime = System.currentTimeMillis();

		Gdx.input.setInputProcessor(new InputMultiplexer(this));

		assets = new AssetManager();
		assets.load("blue.g3db", Model.class);
		loading = true;
	}

	@Override
	public void render() {
		if (loading)
			if (assets.update()) {
				model = assets.get("blue.g3db", Model.class);
				instance = new ModelInstance(model);
				loading = false;
			} else {
				return;
			}
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
/*
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
*/

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