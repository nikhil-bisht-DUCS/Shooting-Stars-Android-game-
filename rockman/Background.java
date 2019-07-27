package com.gdxgame.rockman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.gdxgame.rockman.GameScreen.PlayerState;


public class Background implements Disposable {	
	
	SpriteBatch batch;
	Sprite sprite; 
	static OrthographicCamera camera;
    
	float bg_width, bg_height; 
	int x;
	double y;
	
	float camera_position;
	int flag;
	
	public Background()
	{ bg_width = 1700;
	  bg_height = 1000;
		
	  batch = new SpriteBatch();
	  
	  sprite = new Sprite(new Texture(Gdx.files.internal("rockman/megaman_stage.png"))); 
	  sprite.setSize(bg_width, bg_height);
	  sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	
	  float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	  
	  camera = new OrthographicCamera(bg_height * aspectRatio, bg_height);
	  camera.position.set(bg_width / 2, bg_height / 2, 0); 
	  
	  camera_position = camera.position.x;
	  flag = 0;
	  //camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);
	}
	
	
		
	public void runBatch()
	{ camera.update();
		
	if(Player.appear_stateTime > 0.5f)
	{ if(Player.isLeftMoving() && GameScreen.megaman_state == PlayerState.NEUTRAL)
	  { if(camera.position.x >= 505f)
	    camera.translate(-4.7f, 0); }  
		
	  else if(Player.isRightMoving() && GameScreen.megaman_state == PlayerState.NEUTRAL)
	  { if(camera.position.x <= 1195f)
		 camera.translate(4.7f, 0); }   }
		
	  batch.begin();
	  batch.setProjectionMatrix(camera.combined);
	  sprite.draw(batch);
	  batch.end();
	}
	
	@Override
	public void dispose()
	{ if(batch != null)
	  { batch.dispose(); }
	  
	  sprite.getTexture().dispose(); }
	
	public OrthographicCamera getCamera()
	{ return camera; }
	
	public void startBackground()
	{ while(camera.position.x > 505f)
	  { camera.translate(-4.5f, 0); }	} 
	
	public void traverseBackground()
	{  
	 if(camera.position.x > 1195)
	 { flag = 1; }
		 
	 if(camera.position.x <= 1195f && flag == 0)
	  { camera.translate(4.0f, 0); }
	
	 else if(camera.position.x >= camera_position)
	  { camera.translate(-4.0f, 0); }
	}
}
