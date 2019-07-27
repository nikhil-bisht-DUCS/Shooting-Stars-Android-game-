package com.gdxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.gdxgame.entities.Background;
import com.gdxgame.entities.Player;
import com.gdxgame.entities.Stars;
//import com.gdxgame.miscellaneous.ScoreTimer;
import com.gdxgame.stars.ShootingStars;


public class DemoScreen implements Screen {

	Player p1;
	Background bg;
	Array<Stars> entity;
	
	//ScoreTimer timer;

	int flames_count = 12;

	Stage stage;
	TextureAtlas atlas;
	Skin skin;
	BitmapFont black;
	SpriteBatch batch;
	TextButton button;
	OrthographicCamera camera2;

	final float width = 200, height = 200;
	
	ShootingStars stars;
	

	public DemoScreen(ShootingStars stars) {

		bg = new Background();
        p1 = new Player();
        this.stars = stars;

		batch = new SpriteBatch();
       // timer = new ScoreTimer();
		

		entity = new Array<Stars>();

		for (int i = 0; i < flames_count; i++) {
			float x = MathUtils.random(-80, 770);
			float y = MathUtils.random(700, 700 * 3);
			float speed = MathUtils.random(6, 10);
			entity.add(new Stars(new Vector2(x, y), new Vector2(300, 190), new Vector2(0, -speed)));
		}

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getHeight();

		camera2 = new OrthographicCamera(height * aspectRatio, height);
		camera2.position.set(width / 2, height / 2, 0);
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		bg.runBatch();
		
		//timer.update();
		p1.update();
		
		for (int i = 0; i < flames_count; i++) {
			entity.get(i).update();
			if (p1.getStandingBounds().overlaps(entity.get(i).getBounds()) || p1.getLeftBounds().overlaps(entity.get(i).getBounds()) || p1.getRightBounds().overlaps(entity.get(i).getBounds())) {
				System.out.println("Collision Detected!");
			}
		}


		p1.runBatch();

		for (int i = 0; i < flames_count; i++) {
			entity.get(i).runBatch();
		}
		
		camera2.update();

		stage.act(delta);

		batch.begin();
		batch.setProjectionMatrix(camera2.combined);

		stage.draw();
		batch.end();
		
		//timer.runBatch();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

		if (stage == null) {
			stage = new Stage(width, height, true);
		}

		stage.setCamera(camera2);
		stage.clear();

		Gdx.input.setInputProcessor(stage);

		TextButtonStyle back_btn = new TextButtonStyle();
		back_btn.up = skin.getDrawable("back_btn");
		back_btn.down = skin.getDrawable("back_btn");
		back_btn.font = black;

		button = new TextButton("", back_btn); // button1
		button.setWidth(25);
		button.setHeight(18);
		button.setX(-2);
		button.setY(180);

		button.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				stars.setNewScreen(new MainMenu(stars));
				return true;
			}

		});

		stage.addActor(button);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

		skin = new Skin();
		atlas = new TextureAtlas("new_designs/back_button.pack");
		skin.addRegions(atlas);
		black = new BitmapFont(Gdx.files.internal("new_designs/timer_font.fnt"), false);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		p1.dispose();
		bg.dispose();
		stage.dispose();

		for(Stars star: entity)
		{ star.dispose(); }

		batch.dispose();
		skin.dispose();
		atlas.dispose();
		black.dispose();
		//timer.dispose();
	}

}