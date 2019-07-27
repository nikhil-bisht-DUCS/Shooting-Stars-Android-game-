package com.gdxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.gdxgame.entities.Background;
import com.gdxgame.miscellaneous.ScoreKill;
import com.gdxgame.miscellaneous.ScoreTimer;
import com.gdxgame.stars.ShootingStars;
import com.gdxgame.rockman.GameScreen;



public class GameOver implements Screen {
	

	   OrthographicCamera camera;
	   Background bg;
	   
	   ScoreTimer timer;
	   
	   ScoreKill player_score;
	   
	   final float width = 256, height = 64;
	   int flag;
	   
	   
	   /*-----------------------------------------------------------------------------------------------------------------*/
	   
	    ShootingStars stars;
		
		Stage stage;
		TextureAtlas atlas;
		Skin skin;
		
		BitmapFont black;
		
		SpriteBatch batch2;
		
		TextButton button;
		TextButton button2;
	   	 
		AssetManager manage;
		
	   public GameOver(ScoreTimer timer, ShootingStars stars)
	   { this.timer = timer;
	     this.stars = stars;  
	     
	     flag = 1;
	     
		 bg = new Background(1);
	     
	     float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	     
	     camera = new OrthographicCamera(height * aspectRatio, height);
	     camera.position.set(width / 2, height / 2, 0);
	   }
	   
	   public GameOver(ScoreKill player_score, ShootingStars stars)
	   { this.player_score = player_score;
	     this.stars = stars;  
	     
	     flag = 2;
	     
		 bg = new Background(1);
	     
	     float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	     
	     camera = new OrthographicCamera(height * aspectRatio, height);
	     camera.position.set(width / 2, height / 2, 0);
	   }

	
   @Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	   
	   Gdx.gl.glClearColor(1, 1, 1, 1);
	   Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	   
	    bg.runBatch();
	   
	    camera.update();
	   
	    stage.act(delta);
		
		batch2.begin();
		batch2.setProjectionMatrix(camera.combined);
		stage.draw();
		batch2.end();
		
		if(flag == 2)
		{ player_score.runFinalScoreBatch(); }
		
		else if(flag == 1)
		{ timer.runFinalScoreBatch(); }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
		if(stage == null)
		{ stage = new Stage(width, height, true); }
		
		stage.setCamera(camera);
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		TextButtonStyle main_menu = new TextButtonStyle(); 
		main_menu.up = skin.getDrawable("Main_Menu_btn");
		main_menu.down = skin.getDrawable("Main_Menu_btn");
		main_menu.font = black;
		
		TextButtonStyle retry = new TextButtonStyle(); 
		retry.up = skin.getDrawable("Retry_btn");
		retry.down = skin.getDrawable("Retry_btn");
		retry.font = black;
	
	    button = new TextButton("", retry); // button1
	    button.setWidth(20);
	    button.setHeight(9);
	    button.setX(105);
	    button.setY(7);

		button2 = new TextButton("", main_menu); // button2
		button2.setWidth(20);
	    button2.setHeight(9);
	    button2.setX(105 + 25);
	    button2.setY(6.5f);
		
		
		button.addListener(new InputListener() {				

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				if(flag == 2)
				{ stars.setNewScreen(new com.gdxgame.rockman.GameScreen(stars)); }
				
				else if(flag == 1)
				{ stars.setNewScreen(new com.gdxgame.screen.GameScreen(stars)); }
					
				return true;
			}
			 
		});
		
		  button2.addListener(new InputListener() {				

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				stars.setNewScreen(new MainMenu(stars));
				return true;
			}
			 
		}); 
		
		
		stage.addActor(button);
		stage.addActor(button2);
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		manage = new AssetManager();
		manage.load("new_designs/new_buttons.pack", TextureAtlas.class);
		manage.load("blackFont.fnt", BitmapFont.class);
		manage.finishLoading();
		
		batch2 = new SpriteBatch();
		skin = new Skin();
		atlas = manage.get("new_designs/new_buttons.pack");
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
		bg.dispose();
		
		manage.dispose();
		batch2.dispose();
		skin.dispose();
		atlas.dispose();
		black.dispose();
		stage.dispose(); 
		
		if(flag == 1)
	    { timer.dispose(); }
		
		else if(flag == 2)
		{ player_score.dispose(); }
	}

}
