package com.gdxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.gdxgame.miscellaneous.ScoreTimer;
import com.gdxgame.stars.ShootingStars;


public class GameScreen implements Screen {
	
    Player p1;
    Background bg;
    Array<Stars> entity;
    ScoreTimer timer;
    
    int flames_count;
    
    Stage stage;
	TextureAtlas atlas;
	Skin skin;
	BitmapFont black;
	SpriteBatch batch;
	TextButton button;
	OrthographicCamera camera2;

 	final float width = 200, height = 200;
     
 	Music music, music2;
 	int music_time, music_sec;
 	int music_startTime; 
 	AssetManager assetManage;
 	
 	ShootingStars stars;
 	int flag;
 	
	public GameScreen(ShootingStars stars) {
		
		music_time = music_sec = -1;
		flames_count = 9;
		
		timer = new ScoreTimer();
		this.stars = stars;
		
		p1 = new Player();
		bg = new Background();
	    flag = 0;
		
		entity = new Array<Stars>();
		
		for(int i = 0; i < flames_count; i++)
		 { float x = MathUtils.random(-80, 770);
	       float y = MathUtils.random(700, 700 * 3);
		   float speed = MathUtils.random(7, 8);
		   entity.add(new Stars(new Vector2(x, y), new Vector2(300, 190), new Vector2(0, -speed)) ); }
		
		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getHeight();

		camera2 = new OrthographicCamera(height * aspectRatio, height);
		camera2.position.set(width / 2, height / 2, 0);
		
        assetManage = new AssetManager();
        assetManage.load("audio/god_hand_ost.mp3", Music.class);
        assetManage.load("audio/project_justice_ost.mp3", Music.class);
        assetManage.finishLoading();
        
        music = assetManage.get("audio/god_hand_ost.mp3", Music.class);
        music2 = assetManage.get("audio/project_justice_ost.mp3", Music.class);
          
        music.play();
        music.setLooping(true); 
        music2.setLooping(true); 
        
        music_startTime = MathUtils.random(5, 35);
	} 

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		timer.update();
		
		if(timer.getMinutes() == music_time && timer.getSeconds() == music_sec + 2)
		{ music.stop(); }
		
		if(timer.getMinutes() == music_time + 1 && timer.getSeconds() == music_sec - 4)
		{ music.play(); }
		
		if(timer.getMinutes() == music_time + 1 && timer.getSeconds() == music_sec - 1)
		{ music2.stop();
		  
		  for(int i = 0; i < flames_count; i++)
		  { int speed = MathUtils.random(8, 9);
		    entity.get(i).setSpeed(speed); }
		   
		  music_startTime = MathUtils.random(5, 35); }
		
		if(timer.getMinutes() % 2 != 0 && timer.getSeconds() == music_startTime)
		{ music_time = timer.getMinutes();
		  music_sec = timer.getSeconds();
		  music2.play();
		  
		  for(int i = 0; i < flames_count; i++)
		  { int speed = MathUtils.random(11, 12);
			entity.get(i).setSpeed(speed); }
		}
		
		p1.update();
		
		for(int i = 0; i < flames_count; i++)
		{ entity.get(i).update(); } 
		
		bg.runBatch();
		
		p1.runBatch();
		
		for(int i = 0; i < flames_count; i++)
		{ entity.get(i).runBatch(); }
		
		stage.act(delta);

		batch.begin();
		batch.setProjectionMatrix(camera2.combined);

		stage.draw();
		batch.end();
		
		timer.runBatch();
		
		 for(int i = 0; i < flames_count; i++)
		{   if(p1.getStandingBounds().overlaps(entity.get(i).getBounds()) || p1.getLeftBounds().overlaps(entity.get(i).getBounds()) || p1.getRightBounds().overlaps(entity.get(i).getBounds()))
		  {  
			 if(music.isPlaying())  
		     { music.stop(); }
		     
			 if(music2.isPlaying())
		     { music2.stop(); }
		     
		     stars.setNewScreen(new GameOver(timer, stars)); }  }
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
				
				if(music.isPlaying())
				{ music.stop(); }
				
				if(music2.isPlaying())
				{ music2.stop(); }
				
				flag = 1;
				
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
		batch = new SpriteBatch();
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
		
		for(Stars fire: entity)
		{ fire.dispose(); }
		
		if(flag == 1)
		{ timer.dispose(); }
			
		batch.dispose();
		skin.dispose();
		atlas.dispose();
		black.dispose();
		assetManage.dispose();
		music.dispose();
		music2.dispose();
	}

	
}