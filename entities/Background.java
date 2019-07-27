package com.gdxgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Background {	
	
	SpriteBatch batch;
	Sprite sprite; 
	OrthographicCamera camera;
    
	final float bg_width = 1000, bg_height = 1000; 
	int x;
	double y;
	
	public Background()
	{ batch = new SpriteBatch();
	  sprite = new Sprite(new Texture(Gdx.files.internal("new_designs/Evening_BG_Final.png"))); 
	  sprite.setSize(bg_width, bg_height);
	  sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	
	  float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	  
	  camera = new OrthographicCamera(bg_height * aspectRatio, bg_height);
	  camera.position.set(bg_width / 2, bg_height / 2, 0);
	  
	  //camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);
	}
	
	public Background(int x)
	{ this.x = x;
	  batch = new SpriteBatch();
	  sprite = new Sprite(new Texture(Gdx.files.internal("new_designs/Game_Over_xhdpi.png"))); 
	  sprite.setSize(bg_width, bg_height);
	  sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	
	  float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	  
	  camera = new OrthographicCamera(bg_height * aspectRatio, bg_height);
	  camera.position.set(bg_width / 2, bg_height / 2, 0);
	  
	  //camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);
	}
	
	public Background(double y)
	{ this.y = y;
	  batch = new SpriteBatch();
	  sprite = new Sprite(new Texture(Gdx.files.internal("new_designs/Main-Menu.png"))); 
	  sprite.setSize(bg_width, bg_height);
	  sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	
	  float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	  
	  camera = new OrthographicCamera(bg_height * aspectRatio, bg_height);
	  camera.position.set(bg_width / 2, bg_height / 2, 0);
	  
	  //camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);
	}
		
	public void runBatch()
	{ camera.update();
		
	  batch.begin();
	  batch.setProjectionMatrix(camera.combined);
	  sprite.draw(batch);
	  batch.end();
	}
	
	
	public void dispose()
	{ batch.dispose();
	  sprite.getTexture().dispose(); }
	
	public OrthographicCamera getCamera()
	{ return camera; }
}
