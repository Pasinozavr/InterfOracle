package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
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

public class PyramidHandler extends ApplicationAdapter {
	public PerspectiveCamera cam;
	final float[] startPos = {150f, -9f, 0f};

	public Model model;
	public ModelInstance instance;

	public ModelBatch modelBatch;

	public Environment environment;

	final float bound = 45f;
	float[] pos = {startPos[0], startPos[1], startPos[2]};
	float[] Vpos = new float[3];
	final float speed = 2f;

	private float getSpeed() {
		return speed * Math.signum((float) Math.random() - 0.5f) * Math.max((float) Math.random(), 0.5f);
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

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createCone(20f, 120f, 20f, 3,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		instance = new ModelInstance(model);
		instance.transform.setToRotation(Vector3.Z, 120);

		// initialize speed
		for (int i = 0; i < 3; i++){
			Vpos[i] = getSpeed();
		}
	}


	@Override
	public void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		for (int i = 0; i < 3; i++) {
			pos[i] += Vpos[i];
			if (pos[i] <= startPos[i] - bound) {
				pos[i] = startPos[i] - bound;
				Vpos[i] = getSpeed();
			}
			if (pos[i] >= startPos[i] + bound) {
				pos[i] = startPos[i] + bound;
				Vpos[i] = getSpeed();
			}
		}
		cam.position.set(pos[0], pos[1], pos[2]);
		cam.update();

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
	}

	@Override
	public void dispose() {
		model.dispose();
		modelBatch.dispose();

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
	}
}