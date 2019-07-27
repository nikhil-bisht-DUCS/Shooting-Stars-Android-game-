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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Projectile implements Disposable, Poolable {
	
	//final static int Frame_Width = 500, Frame_Height = 450; 
	
	SpriteBatch batch;
	
	TextureAtlas atlas;
	TextureAtlas atlas_reverse;
	
	AssetManager manage;
	OrthographicCamera camera;
	
	TextureRegion shoot_frames[];
	TextureRegion shoot_currentFrame;
	Animation shoot_anime;
	
	TextureRegion reverse_frames[];
	TextureRegion reverse_currentFrame;
	Animation reverse_anime;
	
	float shoot_stateTime, reverse_stateTime;
	
	Vector2 position;
	Vector2 speed;
	
	enum FacingDirection {LEFT, RIGHT};
	FacingDirection dir;
	Vector2 size;
	;
	Vector2 reset_position, reset_speed;
	
	//ShapeRenderer Sr;
	Rectangle left_bounds, right_bounds;
	
	public Projectile()
	{ batch = new SpriteBatch();
	  size = new Vector2();
	  position = new Vector2();
	  speed = new Vector2();
	  manage = new AssetManager();
	  
	  reset_position = new Vector2();
	  reset_speed = new Vector2();
	  
	  shoot_frames = new TextureRegion[5];
	  reverse_frames = new TextureRegion[5];
	  left_bounds = right_bounds = new Rectangle();
	}
	
	public void init(float xPosition, float projectile_speed)
	{
		position.set(xPosition, 100f);
		speed.set(projectile_speed, 0f);
		size.set(450f, 500f); //450f 350f
		
		if(speed.x > 0)
		{ dir = FacingDirection.RIGHT; }
		
		else { dir = FacingDirection.LEFT; }
		
		manage.load("rockman/shoot_projectile.pack", TextureAtlas.class);
		manage.load("rockman/reverse_projectile.pack", TextureAtlas.class);
		manage.finishLoading();
		
		atlas = manage.get("rockman/shoot_projectile.pack");
		atlas_reverse = manage.get("rockman/reverse_projectile.pack");
		
		shoot_frames[0] = atlas.findRegion("A_projectile");
		shoot_frames[1] = atlas.findRegion("B_projectile");
		shoot_frames[2] = atlas.findRegion("C_projectile");
		shoot_frames[3] = atlas.findRegion("D_projectile");
		shoot_frames[4] = atlas.findRegion("E_projectile");
		
		reverse_frames[0] = atlas_reverse.findRegion("Areverse_projectile");
		reverse_frames[1] = atlas_reverse.findRegion("Breverse_projectile");
		reverse_frames[2] = atlas_reverse.findRegion("Creverse_projectile");
		reverse_frames[3] = atlas_reverse.findRegion("Dreverse_projectile");
		reverse_frames[4] = atlas_reverse.findRegion("Ereverse_projectile");
		
		int length = shoot_frames.length;
		
		for(int i = 0; i < length; i++)
		{ shoot_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		  reverse_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); } 
		 
		shoot_anime = new Animation(0.07f, shoot_frames);
		reverse_anime = new Animation(0.07f, reverse_frames);
		
		shoot_stateTime = reverse_stateTime = 0f;
		
		//camera = new OrthographicCamera(Frame_Width, Frame_Height);
		//camera.position.set(Frame_Width / 2, Frame_Height / 2, 0);
		
		//Sr = new ShapeRenderer();
	}
	
	public void update()
	{ shoot_stateTime += Gdx.graphics.getDeltaTime();
	  shoot_currentFrame = shoot_anime.getKeyFrame(shoot_stateTime, true);
	  
	  reverse_stateTime += Gdx.graphics.getDeltaTime();
	  reverse_currentFrame = reverse_anime.getKeyFrame(reverse_stateTime, true);
	  
	  position.add(speed);
	}
		
	public void runBatch()
	{   //camera.update();
		
		batch.begin();
		batch.setProjectionMatrix(Background.camera.combined);
		
		/*left_bounds.set(position.x + 10f, position.y + 35f, (size.x / 4) - 50f, (size.y / 4) - 70f);
		right_bounds.set(position.x + 38f, position.y + 35f, (size.x / 4) - 50f, (size.y / 4) - 70f); */
		
		if(dir == FacingDirection.RIGHT)
		{ right_bounds.set(position.x + 38f, position.y + 35f, (size.x / 4) - 50f, (size.y / 4) - 70f);
		  batch.draw(shoot_currentFrame, position.x, position.y, size.x / 4, size.y / 4); }
		
		else { left_bounds.set(position.x + 10f, position.y + 35f, (size.x / 4) - 50f, (size.y / 4) - 70f);
			   batch.draw(reverse_currentFrame, position.x, position.y, size.x / 4, size.y / 4); }
		
		batch.end();
		
		/*Sr.setProjectionMatrix(Background.camera.combined);
		Sr.begin(ShapeType.Rectangle);
		Sr.setColor(Color.BLACK);
		
		if(dir == FacingDirection.RIGHT)
		{ Sr.rect(position.x + 38f, position.y + 35f, (size.x / 4) - 50f, (size.y / 4) - 70f); }
		
		else { Sr.rect(position.x + 10f, position.y + 35f, (size.x / 4) - 50f, (size.y / 4) - 70f); }
		
		Sr.end(); */
	}
	
	@Override
	public void dispose()
	{   
		if(batch != null)
		{ batch.dispose(); }
		
	    manage.dispose();
		atlas.dispose();
		atlas_reverse.dispose();
		
		if(reverse_currentFrame != null)
		{ reverse_currentFrame.getTexture().dispose(); }
		
		if(shoot_currentFrame != null)
		{ shoot_currentFrame.getTexture().dispose(); }
		
		int size = shoot_frames.length;
		
		for(int i = 0; i < size; i++)
		{ shoot_frames[i].getTexture().dispose(); 
		  reverse_frames[i].getTexture().dispose(); }
	}
	
	
	public Rectangle getLeftBounds()
	{ return left_bounds; }
	
	public Rectangle getRightBounds()
	{ return right_bounds; }

	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		position = reset_position;
		speed = reset_speed;
	}
	
}