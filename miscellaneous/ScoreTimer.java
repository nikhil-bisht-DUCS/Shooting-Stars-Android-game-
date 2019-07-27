package com.gdxgame.miscellaneous;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.assets.AssetManager;

public class ScoreTimer implements Disposable {

	BitmapFont timer_font;
	SpriteBatch batch;
	OrthographicCamera camera;
	
	final float width = 500, height = 500;
	
	int minCount = 0, secCount = 0;
	float miliCount = 0;
	String current_score;
	
	AssetManager manage;
	
	public ScoreTimer()
	{ batch = new SpriteBatch();
	  
	  manage = new AssetManager();
	  manage.load("new_designs/timer_font.fnt", BitmapFont.class);
	  manage.finishLoading();
	  
	  timer_font = manage.get("new_designs/timer_font.fnt");
	  
	  float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	  camera = new OrthographicCamera(height * aspectRatio, height);
	  camera.position.set(width / 2, height / 2, 0);
	 }
	
	public void update()
	{ miliCount += Gdx.graphics.getDeltaTime();
	
	  if(miliCount >= 1)
	  { miliCount = 0;
	    secCount++;
	    
	    if(secCount == 60)
	    { secCount = 0;
	      minCount++; }  }
	  
	  if(secCount < 10)
	  { current_score = Integer.toString(minCount) + "\' 0" + Integer.toString(secCount) + "\""; }
	  
	  else { current_score = Integer.toString(minCount) + "\' " + Integer.toString(secCount) + "\""; }
	}
	
	public void runBatch()
	{ batch.begin();
	  batch.setProjectionMatrix(camera.combined);
	  
	  timer_font.draw(batch, "Score Timer: " + current_score, 18, 240);
	  
	  batch.end();
	}
	
	public void runFinalScoreBatch()
	{ batch.begin();
	  batch.setProjectionMatrix(camera.combined);
	  
	  int x = -175, y = 210;
	  
	  if(minCount == 0)
	  { if(secCount == 1)
	   { timer_font.draw(batch, "You survived for " + secCount + " second", x, y); }
	  
	  else { timer_font.draw(batch, "You survived for " + secCount + " seconds", x, y); } }
	  
	  else
	  { if(secCount == 0)
	    { if(minCount == 1)
		   { timer_font.draw(batch, "You survived for " + minCount + " minute", x, y); }
	      
	    else { timer_font.draw(batch, "You survived for " + minCount + " minutes", x, y); } }
	  
	   else { timer_font.draw(batch, "You survived for " + minCount + " min. " + secCount + " sec.", x, y); }
	  }
	  
	  batch.end();
	}
	
	@Override
	public void dispose()
	{ batch.dispose();
	  manage.dispose();
	  timer_font.dispose(); }
	
	public int getMinutes()
	{ return minCount; }
	
	public int getSeconds()
	{ return secCount; }
	
}
