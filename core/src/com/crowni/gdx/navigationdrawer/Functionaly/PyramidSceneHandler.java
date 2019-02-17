package com.crowni.gdx.navigationdrawer.Functionaly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;


import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.crowni.gdx.navigationdrawer.utils.*;
import com.crowni.gdx.navigationdrawer.NavigationDrawer;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PyramidSceneHandler implements Screen {
    private Stage stage;
    private static final float NAV_WIDTH = 200F, NAV_HEIGHT = 1920F, speed = 2F;

    private AssetManager assets ;
    private Label label = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE)), questionLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    private PerspectiveCamera cam;

    private long startTime;
    private float width = 200f, height = width / 2f, depth = width, bound = 45f;
    private final float[] startPos = {250f, 50f, 0f}, Vpos = new float[3], pos = {startPos[0], startPos[1], startPos[2]};

    private Model model;
    private ModelInstance instance;
    private ModelBatch modelBatch ;
    private Environment environment ;
    private static int counter = 1;

    private boolean loading, showFPS;

    private Random rnd ;

    private TextureAtlas atlas = new TextureAtlas("data/menu_ui.atlas");
    private static final String TAG = PyramidSceneHandler.class.getSimpleName();
    private final Image logo_crowni = new Image(atlas.findRegion("logo_crowni"));
    private final Image icon_rate = new Image(atlas.findRegion("icon_rate"));
    private final Image icon_share = new Image(atlas.findRegion("icon_share"));
    private final Image icon_music = new Image(atlas.findRegion("icon_music"));
    private final Image icon_off_music = new Image(atlas.findRegion("icon_off_music"));
    private final Image icon_question = new Image(atlas.findRegion("quest"));
    private final Image image_background = new Image(Utils.getTintedDrawable(atlas.findRegion("image_background"), Color.BLACK));
    private final Image button_menu = new Image(atlas.findRegion("button_menu"));

    private final NavigationDrawer drawer = new NavigationDrawer(NAV_WIDTH, NAV_HEIGHT);

    private final List<Toast> toasts = new LinkedList<Toast>();
    private Toast.ToastFactory toastFactory;
    private BitmapFont btmFond;
    private ScreenshotSaver scrSvr;

    private String question = "";

    private SpriteBatch spriteBatch = new SpriteBatch();
    private int posX = Gdx.graphics.getWidth() - 20;
    private int posY = Gdx.graphics.getHeight() - 20;
    private float angle = -90;
    private Matrix4 oldTransformMatrix, mx4Font = new Matrix4();

    @Override
    public void show() {

        btmFond = new BitmapFont(Gdx.files.internal("1.fnt"));
        btmFond.getData().setScale(width/NAV_WIDTH);


        oldTransformMatrix = spriteBatch.getTransformMatrix().cpy();
        mx4Font.rotate(new Vector3(0, 0, 1), angle);
        mx4Font.trn(posX, posY, 0);
        question = "There's no question yet";


        stage = new Stage(new ExtendViewport(600, 800));
        Gdx.input.setInputProcessor(stage);

        drawer.add(logo_crowni).size(63, 85).pad(0, 52, 5, 52).expandX().row();
        drawer.add().height(950F).row();
        drawer.add(icon_question).pad(35, 52, 35, 42).expandX().row();
        drawer.add(icon_rate).pad(35, 52, 35, 42).expandX().row();
        drawer.add(icon_share).pad(35, 52, 35, 42).expandX().row();



        icon_off_music.setVisible(false);
        drawer.stack(icon_music, icon_off_music).pad(32, 52, 185, 52).expandX().row();


        // setup attributes for menu navigation drawer.
        drawer.setBackground(image_background.getDrawable());
        drawer.bottom().left();
        drawer.setWidthStartDrag(40f);
        drawer.setWidthBackDrag(0F);
        drawer.setTouchable(Touchable.enabled);

        /* z-index = 1 */
        // add image_background as a separating actor into stage to make smooth shadow with dragging value.
        image_background.setFillParent(true);
        stage.addActor(image_background);


        /* z-index = 2 */
        drawer.setFadeBackground(image_background, 0.5f);
        stage.addActor(drawer);

        /* z-index = 3 */
        // add button_menu as a separating actor into stage to rotates with dragging value.
        button_menu.setOrigin(Align.center);
        stage.addActor(button_menu);
        drawer.setRotateMenuButton(button_menu, 90f);




        /** Optional **/
        final Image image_shadow = new Image(atlas.findRegion("image_shadow"));
        image_shadow.setHeight(NAV_HEIGHT);
        image_shadow.setX(NAV_WIDTH);
        drawer.setAreaWidth(NAV_WIDTH + image_shadow.getWidth());
        drawer.addActor(image_shadow);

        stage.addActor(image_shadow);
        // show the panel
        drawer.showManually(true);

        /************ add item listener ***********/
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

                } else if (actor.getName().equals("SHARE")) {
                    Image screenShot = new Image(ScreenUtils.getFrameBufferTexture());

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

        assets =  new AssetManager();
        modelBatch = new ModelBatch();
        environment = new Environment();
        rnd = new Random();

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 10f, 10f, 20f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(startPos[0], startPos[1], startPos[2]);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        for (int i = 0; i < 3; i++){
            Vpos[i] = getSpeed();
        }

        label = new Label(" ", new Label.LabelStyle(btmFond, Color.WHITE));
        showFPS = true;

        startTime = System.currentTimeMillis();

        assets = new AssetManager();
        assets.load("blue.g3db", Model.class);
        loading = true;

        toastFactory = new Toast.ToastFactory.Builder()
                .font(btmFond)
                .build();
    }


    public void render(float delta) {
        if (loading)
            if (assets.update()) {
                model = assets.get("blue.g3db", Model.class);
                instance = new ModelInstance(model);
                loading = false;
            } else {
                return;
            }


        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(24 / 255F, 168 / 255F, 173 / 255F, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        if (showFPS) {
            StringBuilder builder = new StringBuilder();
            builder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
            long time = System.currentTimeMillis() - startTime;
            builder.append("| Game time: ").append(time);
            label.setText(builder);

            stage.addActor(label);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setTransformMatrix(mx4Font);
        spriteBatch.begin();
        btmFond.draw(spriteBatch, question, 0, 0);
        spriteBatch.end();
        spriteBatch.setTransformMatrix(oldTransformMatrix);

/*
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
        */
        cam.position.set(pos[0], pos[1], pos[2]);
        cam.update();

        modelBatch.begin(cam);
        modelBatch.render(instance, environment);
        modelBatch.end();

        stage.act(delta);

        stage.draw();

        // handle toast queue and display
        Iterator<Toast> it = toasts.iterator();
        while(it.hasNext()) {
            Toast t = it.next();
            if (!t.render(Gdx.graphics.getDeltaTime())) {
                it.remove(); // toast finished -> remove
            } else {
                break; // first toast still active, break the loop
            }
        }


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
