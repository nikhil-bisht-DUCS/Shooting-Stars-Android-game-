package com.gdxgame.miscellaneous;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class ScoreKill implements Disposable {
    
	AssetManager manage;
	
	BitmapFont timer_font;
	SpriteBatch batch;
	OrthographicCamera camera;
	
	final float width = 600, height = 600;
	
	long enemyKills;
	
	public ScoreKill()
	{ batch = new SpriteBatch();
	  
	  manage = new AssetManager();
	  manage.load("new_designs/timer_font.fnt", BitmapFont.class);
	  manage.finishLoading();
			  
	  timer_font = manage.get("new_designs/timer_font.fnt");
	  enemyKills = 0;
	  
	  float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	  camera = new OrthographicCamera(height * aspectRatio, height);
	  camera.position.set(width / 2, height / 2, 0);
	 }
	
	
	public void runBatch()
	{ batch.begin();
	  batch.setProjectionMatrix(camera.combined);
	  
	  timer_font.draw(batch, "Score: " + Long.toString(enemyKills), -290f, 290f);
	  
	  batch.end();
	}
	
	public void runFinalScoreBatch()
	{ batch.begin();
	  batch.setProjectionMatrix(camera.combined);
	  
	  timer_font.draw(batch, "Your final Score: " + Long.toString(enemyKills) + " pts.", -160f, 260f);
	  
	  batch.end();
	}
	
	public void incrementKillCount(int enemy_killed)
	{ enemyKills += enemy_killed; }
	
	@Override
	public void dispose()
	{ 
	  batch.dispose(); 
	  manage.dispose();
	  timer_font.dispose(); }
	
	
}
