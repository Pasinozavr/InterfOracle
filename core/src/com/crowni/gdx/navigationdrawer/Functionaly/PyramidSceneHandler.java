package com.crowni.gdx.navigationdrawer.Functionaly;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.crowni.gdx.navigationdrawer.utils.*;
import com.crowni.gdx.navigationdrawer.NavigationDrawer;
import com.badlogic.gdx.math.Interpolation;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PyramidSceneHandler extends ApplicationAdapter implements Screen {

    private TextureAtlas atlas = new TextureAtlas("data/menu_ui.atlas");
    private final Image logo_crowni = new Image(atlas.findRegion("logo_crowni")), icon_rate = new Image(atlas.findRegion("icon_rate")), icon_share = new Image(atlas.findRegion("icon_share")), icon_music = new Image(atlas.findRegion("icon_music")), icon_off_music = new Image(atlas.findRegion("icon_off_music")), image_background = new Image(Utils.getTintedDrawable(atlas.findRegion("image_background"), Color.BLACK)), button_menu = new Image(atlas.findRegion("button_menu")), icon_question = new Image(atlas.findRegion("quest"));
    private final float[] plutot_non = {250f, 25f, -40f, -30f, 40f, 20f}, non = {-150f, 200f, 0f, 0f, 100f, 0f}, again = {-150f, -90f, 0f, 50f, 50f, 0f}, plutot_oui = {75f, 75f, 220f, -150f, 60f, -350f}, oui = {50f, 150f, -200f, 0f, 0f, 0f};
    private static final float NAV_WIDTH = 200F, NAV_HEIGHT = 1920F, speed = 2F;
    private final List<Toast> toasts = new LinkedList<Toast>();
    private static final String TAG = PyramidSceneHandler.class.getSimpleName();
    private final NavigationDrawer drawer = new NavigationDrawer(NAV_WIDTH, NAV_HEIGHT);

    private Label label = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE)), questionLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    private PerspectiveCamera cam;
    private Stage stage;
    private AssetManager assets ;

    private long startTime;
    private float width = 200f, height = width / 2f, depth = width, bound = 45f, angle = -90, newX, newY, newZ, toX, toY, toZ, oldX = 250f, oldY = 50f, oldZ = 0, oldToX = 0, oldToY = 0, oldToZ = 0, steps = 13456, step = 0, smooth = 0.6f;

    private Interpolation interpolation;

    private Model model;
    private ModelInstance instance;
    private ModelBatch modelBatch ;
    private Environment environment ;

    private boolean loading, showFPS, moving = false;
    private Random rnd ;

    private Toast.ToastFactory toastFactory;
    private BitmapFont btmFond;
    private String question = "";

    private SpriteBatch spriteBatch1 = new SpriteBatch(), spriteBatch2 = new SpriteBatch(), spriteBatch3 = new SpriteBatch();
    private int posX1 = Gdx.graphics.getWidth() - 20, posY1 = Gdx.graphics.getHeight() - 20, posX2 = Gdx.graphics.getWidth() - 70, posY2 = Gdx.graphics.getHeight() - 20, posX3 = Gdx.graphics.getWidth() - 120, posY3 = Gdx.graphics.getHeight() - 20;
    private Matrix4 oldTransformMatrix1, oldTransformMatrix2, oldTransformMatrix3, mx4Font1 = new Matrix4(), mx4Font2 = new Matrix4(), mx4Font3 = new Matrix4();

    private Dialog colorDialog;
    private Skin uiSkin;
    private long clr;


    public void setMatrixForTextRotation()
    {
        btmFond = new BitmapFont(Gdx.files.internal("1.fnt"));
        btmFond.getData().setScale(width/NAV_WIDTH);

        oldTransformMatrix1 = spriteBatch1.getTransformMatrix().cpy();
        mx4Font1.rotate(new Vector3(0, 0, 1), angle);
        mx4Font1.trn(posX1, posY1, 0);
        oldTransformMatrix2 = spriteBatch2.getTransformMatrix().cpy();
        mx4Font2.rotate(new Vector3(0, 0, 1), angle);
        mx4Font2.trn(posX2, posY2, 0);
        oldTransformMatrix3 = spriteBatch3.getTransformMatrix().cpy();
        mx4Font3.rotate(new Vector3(0, 0, 1), angle);
        mx4Font3.trn(posX3, posY3, 0);

        question = "There's no question yet";
    }

    public void setMenu()
    {
        drawer.add(logo_crowni).size(63, 85).pad(0, 52, 5, 52).expandX().row();
        drawer.add().height(950F).row();
        drawer.add(icon_question).pad(35, 52, 35, 42).expandX().row();
        drawer.add(icon_rate).pad(35, 52, 35, 42).expandX().row();
        drawer.add(icon_share).pad(35, 52, 35, 42).expandX().row();


        icon_off_music.setVisible(false);
        drawer.stack(icon_music, icon_off_music).pad(32, 52, 185, 52).expandX().row();


        drawer.setBackground(image_background.getDrawable());
        drawer.bottom().left();
        drawer.setWidthStartDrag(40f);
        drawer.setWidthBackDrag(0F);
        drawer.setTouchable(Touchable.enabled);


        image_background.setFillParent(true);
        stage.addActor(image_background);


        drawer.setFadeBackground(image_background, 0.5f);
        stage.addActor(drawer);


        button_menu.setOrigin(Align.center);
        stage.addActor(button_menu);
        drawer.setRotateMenuButton(button_menu, 90f);


        final Image image_shadow = new Image(atlas.findRegion("image_shadow"));
        image_shadow.setHeight(NAV_HEIGHT);
        image_shadow.setX(NAV_WIDTH);
        drawer.setAreaWidth(NAV_WIDTH + image_shadow.getWidth());
        drawer.addActor(image_shadow);

        stage.addActor(image_shadow);

        drawer.showManually(true);

        logo_crowni.setName("LOGO");
        icon_question.setName("QUESTION");
        icon_rate.setName("RATE");
        icon_share.setName("SHARE");
        icon_music.setName("MUSIC_ON");
        icon_off_music.setName("MUSIC_OFF");
        button_menu.setName("BUTTON_MENU");
        image_background.setName("IMAGE_BACKGROUND");

        ClickListener listener = new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                boolean closed = drawer.isCompletelyClosed();
                Actor actor = event.getTarget();

                if (actor.getName().equals("RATE")) {
                    Gdx.app.debug(TAG, "Rate button clicked.");
                    int rnd_sol = rnd.nextInt(5);
                    if(rnd_sol == 1)
                    {
                        newX = plutot_non[0];
                        newY = plutot_non[1];
                        newZ = plutot_non[2];
                        toX = plutot_non[3];
                        toY = plutot_non[4];
                        toZ = plutot_non[5];
                    }
                    else if(rnd_sol == 2)
                    {
                        newX = plutot_oui[0];
                        newY = plutot_oui[1];
                        newZ = plutot_oui[2];
                        toX = plutot_oui[3];
                        toY = plutot_oui[4];
                        toZ = plutot_oui[5];
                    }
                    else if(rnd_sol == 3)
                    {
                        newX = non[0];
                        newY = non[1];
                        newZ = non[2];
                        toX = non[3];
                        toY = non[4];
                        toZ = non[5];
                    }
                    else if(rnd_sol == 4)
                    {
                        newX = oui[0];
                        newY = oui[1];
                        newZ = oui[2];
                        toX = oui[3];
                        toY = oui[4];
                        toZ = oui[5];
                    }
                    else
                    {
                        newX = again[0];
                        newY = again[1];
                        newZ = again[2];
                        toX = again[3];
                        toY = again[4];
                        toZ = again[5];
                    }
                    moving = true;
                    step = 1;



                } else if (actor.getName().equals("SHARE")) {

                    //Image screenShot = new Image(ScreenUtils.getFrameBufferTexture());

                    try {
                        ScreenshotSaver.saveScreenshot("wow.png");
                        toasts.add(toastFactory.create("Screenshot saved", Toast.Length.LONG));
                    }catch (Exception e)
                    {

                    }

                    Gdx.app.debug(TAG, "Share button clicked.");

                }
                else if(actor.getName().contains("QUESTION")) {

                    Input.TextInputListener textListener = new Input.TextInputListener()
                    {
                        @Override
                        public void input(String input)
                        {
                            toasts.add(toastFactory.create("Question is asked", Toast.Length.LONG));
                            question = input;

                        }

                        @Override
                        public void canceled()
                        {
                            System.out.println("Question asking aborted");
                        }
                    };

                    Gdx.input.getTextInput(textListener, "Your question is : ", "Will I pass this semester?", "Something that can be answered \"yes\" or \"no\" ");
                }
                else if (actor.getName().contains("LOGO"))
                {
                    toasts.add(toastFactory.create("Interface Programming TP", Toast.Length.LONG));
                }
                else if (actor.getName().equals("BUTTON_MENU") || actor.getName().equals("IMAGE_BACKGROUND")) {
                    Gdx.app.debug(TAG, "Menu button clicked.");

                    image_background.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                    drawer.showManually(closed);
                    image_shadow.setVisible(!image_shadow.isVisible());

                } else if (actor.getName().contains("MUSIC")) {
                    Gdx.app.debug(TAG, "Music button clicked.");

                    icon_music.setVisible(!icon_music.isVisible());
                    icon_off_music.setVisible(!icon_off_music.isVisible());
                    showFPS  = !showFPS;

                    label.setText(" ");
                    stage.addActor(label);
                }
            }
        };

        Utils.addListeners(listener, logo_crowni, icon_question, icon_rate, icon_share, icon_music, icon_off_music, button_menu, image_background);
    }

    public void showText()
    {
        if (showFPS) {
            StringBuilder builder = new StringBuilder();
            builder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
            long time = System.currentTimeMillis() - startTime;
            builder.append(" | Game time: ").append(time);
            label.setText(builder);

            stage.addActor(label);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch1.setTransformMatrix(mx4Font1);
        spriteBatch1.begin();
        btmFond.draw(spriteBatch1, question, 0, 0);
        spriteBatch1.end();
        spriteBatch1.setTransformMatrix(oldTransformMatrix1);

        spriteBatch2.setTransformMatrix(mx4Font2);
        spriteBatch2.begin();
        btmFond.draw(spriteBatch2, "Place at "+oldX+" "+oldY+" "+oldZ, 0, 0);
        spriteBatch2.end();
        spriteBatch2.setTransformMatrix(oldTransformMatrix2);

        spriteBatch3.setTransformMatrix(mx4Font3);
        spriteBatch3.begin();
        btmFond.draw(spriteBatch3, "Look at "+oldToX+" "+oldToY+" "+oldToZ, 0, 0);
        spriteBatch3.end();
        spriteBatch3.setTransformMatrix(oldTransformMatrix3);
    }

    public void showNotification()
    {
        Iterator<Toast> it = toasts.iterator();
        while(it.hasNext()) {
            Toast t = it.next();
            if (!t.render(Gdx.graphics.getDeltaTime())) {
                it.remove();
            } else {
                break;
            }
        }
    }

    public void visionUpdate(float delta)
    {
        showText();

        cam.lookAt(oldToX, oldToY, oldToZ);
        cam.position.set(oldX, oldY, oldZ);
        cam.update();

        if(!loading) {
            modelBatch.begin(cam);
            modelBatch.render(instance, environment);
            modelBatch.end();
        }

        stage.act(delta);
        stage.draw();
    }

    public void chooseColor()
    {

    }
    @Override
    public void show() {
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ExtendViewport(600, 800));
        interpolation = new Interpolation.Elastic(5,5,2,0.7f);

        setMatrixForTextRotation();

        Gdx.input.setInputProcessor(stage);

        setMenu();

        assets =  new AssetManager();
        modelBatch = new ModelBatch();
        environment = new Environment();
        rnd = new Random();

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 10f, 10f, 20f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(oldX, oldY, oldZ);
        cam.lookAt(oldToX, oldToY, oldToZ);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        label = new Label(" ", new Label.LabelStyle(btmFond, Color.WHITE));
        showFPS = true;
        moving = false;

        startTime = System.currentTimeMillis();

       /* assets.load("blue.g3db", Model.class);
        assets.load("red.g3db", Model.class);
        assets.load("green.g3db", Model.class);
        assets.load("white.g3db", Model.class);*/

        loading = true;

        toastFactory = new Toast.ToastFactory.Builder()
                .font(btmFond)
                .build();

        //assets.load("red.g3db", Model.class);


        colorDialog = new Dialog("Choose Color", uiSkin)
        {
            protected void result(Object object)
            {
                if (object.equals(1L))
                {
                    assets.load("red.g3db", Model.class);
                    clr = 1L;
                } else  if (object.equals(2L))
                {
                    assets.load("blue.g3db", Model.class);
                    clr = 2L;
                } else  if (object.equals(3L))
                {
                    assets.load("green.g3db", Model.class);
                    clr = 3L;
                } else
                {
                    assets.load("white.g3db", Model.class);
                    clr = 4L;
                }
            }
        };

        colorDialog.button("Red", 1L);
        colorDialog.button("Blue", 2L);
        colorDialog.button("Green", 3L);
        colorDialog.button("White", 4L);

        //colorDialog.getTitleLabel().setFontScale(3);
        //colorDialog.add(new TextButton("Red", uiSkin)).size(100,100);
        colorDialog.show(stage);

    }


    public void render(float delta) {
        if (loading)
            if (assets.update()) {
                if(clr == 1L) {
                    model = assets.get("red.g3db", Model.class);
                    instance = new ModelInstance(model);
                    loading = false;
                } else  if(clr == 2L) {
                    model = assets.get("blue.g3db", Model.class);
                    instance = new ModelInstance(model);
                    loading = false;
                }
                    else if(clr == 3L) {
                    model = assets.get("green.g3db", Model.class);
                    instance = new ModelInstance(model);
                    loading = false;

                } else if (clr == 4L){
                    model = assets.get("white.g3db", Model.class);
                    instance = new ModelInstance(model);
                    loading = false;
                }
                else {
                    model = new ModelBuilder().createBox(300f, 300f, 300f, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),VertexAttributes.Usage.Position );
                    /*model = new ModelBuilder().createCone(20f, 120f, 20f, 3,
                            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);*/
                    instance = new ModelInstance(model);
                }
            } else {
                return;
            }


        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(24 / 255F, 168 / 255F, 173 / 255F, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        visionUpdate(delta);

            if(moving)
            {
                oldX = Interpolation.bounceIn.apply(oldX, newX, smooth);
                oldY = Interpolation.bounceIn.apply(oldY, newY, smooth);
                oldZ = Interpolation.bounceIn.apply(oldZ, newZ, smooth);
                oldToX = Interpolation.bounceIn.apply(oldToX, oldX, smooth);
                oldToX = Interpolation.bounceIn.apply(oldToY, oldY, smooth);
                oldToX = Interpolation.bounceIn.apply(oldToZ, oldZ, smooth);

                if(step++ >= steps)
                {
                    moving = false;
                    step = 0;
                }
            }
        //visionUpdate(delta);
        showNotification();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        model.dispose();
        modelBatch.dispose();

        modelBatch.begin(cam);
        modelBatch.render(instance, environment);

        stage.dispose();
        //modelBatch.end();
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

			public void setColor(Color newColor)
	{
		model = new ModelBuilder().createCone(width, height, depth, 3,
				new Material(ColorAttribute.createDiffuse(newColor)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		instance = new ModelInstance(model);
		instance.transform.setToRotation(Vector3.Z, 90).translate(-5,0,0);
	}
	}
*/

    @Override
    public void hide() {
    }

    private float getSpeed() {
        return speed * Math.signum((float) Math.random() - 0.5f) * Math.max((float) Math.random(), 0.5f);
    }



}
