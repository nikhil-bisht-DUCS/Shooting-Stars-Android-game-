package com.gdxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.gdxgame.stars.ShootingStars;
import com.gdxgame.entities.Background;
import com.gdxgame.rockman.GameScreen;


public class MainMenu implements Screen {
	
	OrthographicCamera camera2;
	
	Stage stage;
	TextureAtlas atlas;
	Skin skin;
	
	BitmapFont black;
	
	SpriteBatch batch;
	
	TextButton button;
	TextButton button2;
	TextButton button3;
	
	Background bg;
	AssetManager manage;
	ShootingStars stars;
	
	final float width = 500, height = 200;
	
	public MainMenu(ShootingStars stars) {
		
		this.stars = stars;
		manage = new AssetManager();
		manage.load("new_designs/menu_buttons.pack", TextureAtlas.class);
		manage.load("blackFont.fnt", BitmapFont.class);
		manage.finishLoading();
		
		bg = new Background(1.5);
		
		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getHeight();

		camera2 = new OrthographicCamera(height * aspectRatio, height);
		camera2.position.set(width / 2, height / 2, 0);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera2.update();
		
		stage.act(delta);
		
		bg.runBatch();
		
		batch.begin();
		batch.setProjectionMatrix(camera2.combined);
		
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
		if(stage == null)
		{ stage = new Stage(width, height, true); }
		
		stage.setCamera(camera2);
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		TextButtonStyle play = new TextButtonStyle();
		TextButtonStyle demo = new TextButtonStyle();
		TextButtonStyle exit = new TextButtonStyle(); // button1
		
		play.up = skin.getDrawable("Play_btn");
		play.down = skin.getDrawable("Pressed_Play_btn");
		play.font = black;
	    
	    demo.up = skin.getDrawable("Demo_btn");
		demo.down = skin.getDrawable("Pressed_Demo_btn");
	    demo.font = black;
	    
	    exit.up = skin.getDrawable("Quit");
		exit.down = skin.getDrawable("Pressed_Quit");
	    exit.font = black;
	    
		button = new TextButton("", play); // button1
		button.setWidth(53);
		button.setHeight(25);
		button.setX(220);
		button.setY(73);

		button2 = new TextButton("", demo); // button2
		button2.setWidth(50);
		button2.setHeight(20);
		button2.setX(225);
		button2.setY(53);

		button3 = new TextButton("", exit); // button3
		button3.setWidth(50);
		button3.setHeight(20);
		button3.setX(225);
		button3.setY(32);
		
		
		button.addListener(new InputListener() {				

		   boolean pressed = false;
			
			@Override
			public void exit(InputEvent event, float x, float y, int pointer,
					Actor toActor) {
				// TODO Auto-generated method stub
				super.exit(event, x, y, pointer, toActor);
				pressed = false;
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				pressed = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
			  super.touchUp(event, x, y, pointer, button);
			  
			  if(pressed)
			  { stars.setNewScreen(new com.gdxgame.rockman.GameScreen(stars)); }
			}
			
			
		});
		
		button2.addListener(new InputListener() {				
			
			boolean pressed = false;

			@Override
			public void exit(InputEvent event, float x, float y, int pointer,
					Actor toActor) {
				// TODO Auto-generated method stub
				super.exit(event, x, y, pointer, toActor);
				pressed = false;
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				pressed = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				super.touchUp(event, x, y, pointer, button);
				
				if(pressed)
				{ stars.setNewScreen(new com.gdxgame.screen.GameScreen(stars)); }
			}
			
			 
		});
		
		button3.addListener(new InputListener() {				
			
			boolean pressed = false;
			
			@Override
			public void exit(InputEvent event, float x, float y, int pointer,
					Actor toActor) {
				// TODO Auto-generated method stub
				super.exit(event, x, y, pointer, toActor);
				pressed = false;
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				pressed = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				super.touchUp(event, x, y, pointer, button);
				
				if(pressed)
				{ Gdx.app.exit(); }
			}
			
			 
		}); 
		
		stage.addActor(button);
		stage.addActor(button2);
		stage.addActor(button3);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		batch = new SpriteBatch();
		skin = new Skin();
		atlas = manage.get("new_designs/menu_buttons.pack");
		skin.addRegions(atlas);
		black = manage.get("blackFont.fnt");
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
		
		batch.dispose();
		manage.dispose();
		skin.dispose();
		atlas.dispose();
		black.dispose();
		stage.dispose(); 
		bg.dispose();
		
	}
	

}
