package com.gdxgame.rockman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Demon implements Disposable {
	
	final int Frame_Width = 650, Frame_Height = 450;
	
	SpriteBatch batch;
	
	TextureAtlas right_atlas;
	TextureAtlas left_atlas;
	AssetManager manage;
	//OrthographicCamera camera;
	
	TextureRegion left_frames[];
	TextureRegion left_currentFrame;
	Animation left_anime;
	
	TextureRegion right_frames[];
	TextureRegion right_currentFrame;
	Animation right_anime;
	
	float left_stateTime;
	float right_stateTime;
	
	Vector2 position;
	enum FacingDirection {LEFT, RIGHT};
	FacingDirection dir;
	
	Vector2 size;
	Vector2 speed;
	
	Rectangle left_bounds, right_bounds;
	int hitCount;
	
	//ShapeRenderer Sr;
	
	
	public Demon(Vector2 position, Vector2 speed)
	{
		batch = new SpriteBatch();
		this.position = position;
		size = new Vector2(200f, 600f);
		
		this.speed = speed;
		hitCount = 0;
		
		if(speed.x < 0)
		{ dir = FacingDirection.LEFT; }
		
		else { dir = FacingDirection.RIGHT; }
		
		manage = new AssetManager();
		manage.load("rockman/right_demon.pack", TextureAtlas.class);
		manage.load("rockman/left_demon.pack", TextureAtlas.class);
		manage.finishLoading();
		
		left_atlas = manage.get("rockman/left_demon.pack");
		right_atlas = manage.get("rockman/right_demon.pack");
		
		left_frames = new TextureRegion[4];
		right_frames = new TextureRegion[4];
		
		left_frames[0] = left_atlas.findRegion("Areverse_demon");
		left_frames[1] = left_atlas.findRegion("Breverse_demon");
		left_frames[2] = left_atlas.findRegion("Creverse_demon");
		left_frames[3] = left_atlas.findRegion("Dreverse_demon");
		
		right_frames[0] = right_atlas.findRegion("A_demon");
		right_frames[1] = right_atlas.findRegion("B_demon");
		right_frames[2] = right_atlas.findRegion("C_demon");
		right_frames[3] = right_atlas.findRegion("D_demon");
		
		int length = left_frames.length;
		
		for(int i = 0; i < length; i++)
		{ left_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		  right_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); }
		
		left_anime = new Animation(0.09f, left_frames);
		right_anime = new Animation(0.09f, right_frames);
		
		left_stateTime = right_stateTime = 0f;
		
		/*camera = new OrthographicCamera(Frame_Width, Frame_Height);
		  camera.position.set(Frame_Width / 2, Frame_Height / 2, 0); */
		
		left_bounds = right_bounds = new Rectangle();
		//Sr = new ShapeRenderer();
		
	}
	
	public void update()
	{ right_stateTime += Gdx.graphics.getDeltaTime();
	  right_currentFrame = right_anime.getKeyFrame(right_stateTime, true);
	  
	  left_stateTime += Gdx.graphics.getDeltaTime();
	  left_currentFrame = left_anime.getKeyFrame(left_stateTime, true);
	  
	  if(speed.x < 0)
		{ dir = FacingDirection.LEFT; }
		
		else { dir = FacingDirection.RIGHT; }
	  
	  position.add(speed);  
	  
	  if(position.x < -400f || position.x > 2100f)
	  {  int x = MathUtils.random(1, 2) ;
	  
		  if(x == 1)
		 { position.set(-300f, -10f);
		   
		   if(speed.x < 0)
		   { speed.x = Math.abs(speed.x);
		     dir = FacingDirection.RIGHT; }     }
			  
		 else { position.set(2000f, -10f);
		 
		        if(speed.x > 0)
		        { speed.x = -speed.x;
		          dir = FacingDirection.LEFT; }   }  }
	 }
		
	public void runBatch()
	{   //camera.update();
		
		batch.begin();
		batch.setProjectionMatrix(Background.camera.combined);
		
		if(dir == FacingDirection.RIGHT)
		{ right_bounds.set(position.x + 45f, position.y + 50f, (size.x / 2) + 20f, (size.y / 2) + 50f);
		  batch.draw(left_currentFrame, position.x, position.y, size.x, size.y); }
			
		else { left_bounds.set(position.x + 37f, position.y + 50f, (size.x / 2) + 20f, (size.y / 2) + 50f);
			   batch.draw(right_currentFrame, position.x, position.y, size.x, size.y); }
		
		batch.end();
		
		/*Sr.setProjectionMatrix(Background.camera.combined);
		Sr.begin(ShapeType.Rectangle);
		Sr.setColor(Color.GREEN);
		Sr.rect(position.x + 37f, position.y + 50f, (size.x / 2) + 20f, (size.y / 2) + 50f);
		Sr.end(); */
	}
	
	@Override
	public void dispose()
	{   if(batch != null)
	   { batch.dispose(); }
		
	    manage.dispose();
		right_atlas.dispose();
		left_atlas.dispose();
		
		if(left_currentFrame != null)
		{ left_currentFrame.getTexture().dispose(); }
		
		if(right_currentFrame != null)
		{ right_currentFrame.getTexture().dispose(); }
		
		int size = left_frames.length;
		
		for(int i = 0; i < size; i++)
		{ left_frames[i].getTexture().dispose(); 
		  right_frames[i].getTexture().dispose(); }
		
	}
	
	public Rectangle getLeftBounds()
	{ return left_bounds; }
	
	public Rectangle getRightBounds()
	{ return right_bounds; }
	
	public void incrementHitCount()
	{ hitCount++; }
	
	public int getHitCount()
	{ return hitCount; }
	
	public void resetPositionSpeed()
	{ int x = MathUtils.random(1, 2);
	  float speed = MathUtils.random(3.5f, 4.5f);
	  
	  if(x == 1)
	  {  float pos = MathUtils.random(-300f * 1.5f, -300f * 2f);
		 position.x = pos;
		 this.speed.x = speed;  }
	  
	  else { float pos = MathUtils.random(2000f * 1.5f, 2000f * 2f);
		     position.x = pos;
		     this.speed.x = -speed; } 
	  
	  hitCount = 0;  	}
	
}
