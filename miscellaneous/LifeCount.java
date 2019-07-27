package com.gdxgame.miscellaneous;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class LifeCount implements Disposable {

	BitmapFont timer_font;
	SpriteBatch batch;
	OrthographicCamera camera;
	
	AssetManager manage;
	TextureAtlas atlas;
	TextureRegion icon;
	
	final float width = 600, height = 600;
	
	Vector2 size;
	public int life_counter;
	
	
	public LifeCount()
	{ batch = new SpriteBatch();
	  
	  manage = new AssetManager();
	  manage.load("rockman/life_count.pack", TextureAtlas.class);
	  manage.load("new_designs/timer_font.fnt", BitmapFont.class);
	  manage.finishLoading();
	  
	  timer_font = manage.get("new_designs/timer_font.fnt");
	  atlas = manage.get("rockman/life_count.pack");
	  
	  icon = atlas.findRegion("megaman_icon");
	  icon.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	  
	  size = new Vector2(40f, 60f);
	  life_counter = 3;
	  
	  float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	  camera = new OrthographicCamera(height * aspectRatio, height);
	  camera.position.set(width / 2, height / 2, 0);
	 }
	
	
	public void runBatch()
	{ batch.begin();
	  batch.setProjectionMatrix(camera.combined);
	  
	  if(life_counter >= 0)
	  { timer_font.draw(batch, " x " + Integer.toString(life_counter), 235f, 270f);
	    batch.draw(icon, 200f, 240f, size.x, size.y); }
	  
	  else { timer_font.draw(batch, " x " + Integer.toString(0), 235f, 270f);
	         batch.draw(icon, 200f, 240f, size.x, size.y); }
	  
	  batch.end();
	}
	
	@Override
	public void dispose()
	{ if(batch != null)
	  { batch.dispose(); }
	  
	  manage.dispose();
	  atlas.dispose();
	  
	  if(icon != null)
	  { icon.getTexture().dispose(); }
	  
	  timer_font.dispose(); }
	
	
	public void decrementLifeCounter()
	{ life_counter--; }
	
	public int getLifeCounter()
	{ return life_counter; }
}
