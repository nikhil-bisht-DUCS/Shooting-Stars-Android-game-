package com.gdxgame.rockman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.gdxgame.rockman.Player.FacingDirection;

public class Faint implements Disposable {

	SpriteBatch batch;
	
	TextureAtlas left_atlas;
	TextureAtlas right_atlas;
	AssetManager manage;
	
	TextureRegion left_frames[];
	TextureRegion left_currentFrame;
	Animation left_anime;
	float left_stateTime;
	
	TextureRegion right_frames[];
	TextureRegion right_currentFrame;
	Animation right_anime; 
	float right_stateTime;
	
	public Faint()
	{
		batch = new SpriteBatch();
		
		manage = new AssetManager();
		manage.load("rockman/left_faint.pack", TextureAtlas.class);
		manage.load("rockman/right_faint.pack", TextureAtlas.class);
		manage.finishLoading();
		
		left_atlas = manage.get("rockman/left_faint.pack");
		right_atlas = manage.get("rockman/right_faint.pack");
		
		left_frames = new TextureRegion[4];
		right_frames = new TextureRegion[4];
		
		left_frames[0] = left_atlas.findRegion("Areverse_faint");
		left_frames[1] = left_atlas.findRegion("Breverse_faint");
		left_frames[2] = left_atlas.findRegion("Creverse_faint");
		left_frames[3] = left_atlas.findRegion("Dreverse_faint");
		
		right_frames[0] = right_atlas.findRegion("A_faint");
		right_frames[1] = right_atlas.findRegion("B_faint");
		right_frames[2] = right_atlas.findRegion("C_faint");
		right_frames[3] = right_atlas.findRegion("D_faint");
		
		int length = left_frames.length;
		
		for(int i = 0; i < length; i++)
		{ left_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		  right_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); }
		
		left_anime = new Animation(0.05f, left_frames);
        right_anime = new Animation(0.05f, right_frames);
        
        left_stateTime = right_stateTime = 0f;
        
	}
	
	public void runLeftBatch()
	{ left_stateTime += Gdx.graphics.getDeltaTime();
	  left_currentFrame = left_anime.getKeyFrame(left_stateTime, false);
	  
	  batch.begin();
	  batch.setProjectionMatrix(Background.camera.combined);
	  
	  batch.draw(left_currentFrame, Player.position.x / 3, Player.position.y / 4, Player.size.x / 4, Player.size.y / 4);
	  
	  batch.end();
	}
	
	public void runRightBatch()
	{ right_stateTime += Gdx.graphics.getDeltaTime();
	  right_currentFrame = right_anime.getKeyFrame(right_stateTime, false);
	  
	  batch.begin();
	  batch.setProjectionMatrix(Background.camera.combined);
	  
	  batch.draw(right_currentFrame, Player.position.x / 3, Player.position.y / 4, Player.size.x / 4, Player.size.y / 4);
	  
	  batch.end();
	}
	
	@Override
	public void dispose()
	{   
		if(batch != null)
		{ batch.dispose(); }
		
		manage.dispose();
		left_atlas.dispose();
		right_atlas.dispose();
		
		if(left_currentFrame != null)
		{ left_currentFrame.getTexture().dispose(); }
		
		if(right_currentFrame != null)
		{ right_currentFrame.getTexture().dispose(); }
		
        int size = left_frames.length;
        
        for(int i = 0; i < size; i++)
        { left_frames[i].getTexture().dispose(); 
          right_frames[i].getTexture().dispose(); }
	}
}
