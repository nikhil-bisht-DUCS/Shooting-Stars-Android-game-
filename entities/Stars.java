package com.gdxgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdxgame.stars.ShootingStars;


public class Stars {
	
	private final int FRAME_COLS = 4;
	private final int FRAME_ROWS = 1;
	
	ShapeRenderer sr;
	
	Vector2 position, size, direction;
	Rectangle bounds;
	
	SpriteBatch batch;
	Texture fireSheet;
	TextureRegion fireFrames[]; 
	TextureRegion currentFrame;
	Animation fire_anime;
	
	OrthographicCamera camera2;
	
	float stateTime = 0;
	
	final float stars_width = 1000, stars_height = 700;
	
	
	public Stars(Vector2 position, Vector2 size, Vector2 direction)
	{	fireSheet = new Texture(Gdx.files.internal("fire_sprite.png"));
		
		TextureRegion tmp[][] = TextureRegion.split(fireSheet, fireSheet.getWidth() / FRAME_COLS, fireSheet.getHeight() / FRAME_ROWS);
		fireFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
		
		int index = 0;
		
		for(int i = 0; i < FRAME_ROWS; i++)
		for(int j = 0; j < FRAME_COLS; j++)
		{ fireFrames[index++] = tmp[i][j]; }
		
		int length = fireFrames.length;
		
		for(int i = 0; i < length; i++)
		{ fireFrames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); }
		
		fire_anime = new Animation(0.08f, fireFrames);
		
		batch = new SpriteBatch();
		
		stateTime = 0f;
		
		/*----------------------------------------------------------------------------------------------------------------------------*/
		
		this.position = position;
		this.size = size;
		this.direction = direction;
		
		bounds = new Rectangle(position.x, position.y, size.x, size.y);	
		
		sr = new ShapeRenderer();
		
		float aspectRatio = (float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
		  
		camera2 = new OrthographicCamera(stars_width, stars_height);
		camera2.position.set(stars_width / 2, stars_height / 2, 0);
	}
	
	
	public void update()
	{ stateTime += Gdx.graphics.getDeltaTime();
	  currentFrame = fire_anime.getKeyFrame(stateTime, true); 
	  
	   position.add(direction);
	  	
	 // bounds.set(position.x + 230, position.y + 170, size.x - 340, size.y - 100);
	  
	   if(position.x >= 368 && position.x < 400)
	   { bounds.set(position.x + 65, position.y + 40, size.x - 350, size.y - 95); }
	   
	   else if(position.x >= 400  && position.x < 460)
	   { bounds.set(position.x + 56, position.y + 40, size.x - 340, size.y - 95); }
	   
	   else if(position.x >= 460 && position.x < 550)
	   { bounds.set(position.x + 40, position.y + 40, size.x - 350, size.y - 95); }
	   
	   else if(position.x >= 550 && position.x < 580)
	   { bounds.set(position.x + 20, position.y + 40, size.x - 350, size.y - 95); }
	   
	   else if(position.x >= 580 && position.x < 630)
	   { bounds.set(position.x + 7, position.y + 40, size.x - 330, size.y - 95); }
	   
	   else if(position.x >= 630 && position.x < 695)
	   { bounds.set(position.x, position.y + 40, size.x - 340, size.y - 95); }
	   
	   else if(position.x >= 695 && position.x < 710)
	   { bounds.set(position.x - 11, position.y + 40, size.x - 350, size.y - 95); }
	   
	   else if(position.x >= 710 && position.x <= 770)
	   { bounds.set(position.x - 18, position.y + 40, size.x - 340, size.y - 95); }
	  
	   else if(position.x >= -80 && position.x < 0)
	   { bounds.set(position.x + 140, position.y + 40, size.x - 320, size.y - 95); }
	   
	   else if(position.x >= 0 && position.x <= 100)
	   { bounds.set(position.x + 125, position.y + 40, size.x - 320, size.y - 95); }
	   
	   else if(position.x >= 224 && position.x < 368)
	   { bounds.set(position.x + 70, position.y + 40, size.x - 320, size.y - 95); }
	   
	   else if(position.x >= 165 && position.x < 185)
	   { bounds.set(position.x + 113, position.y + 45, size.x - 345, size.y - 95); }
	  
	   else if(position.x >= 185 && position.x < 224)
	   { bounds.set(position.x + 101, position.y + 45, size.x - 333, size.y - 95); }
	   
	   else { bounds.set(position.x + 105, position.y + 40, size.x - 320, size.y - 95); }
	   // else { bounds.set((position.x + 130 / 2) + 30, position.y + 40, ((size.x - 235) / 7) - 50, size.y - 90); }
	    
	  
      //bounds.setX(position.x + 50);
	  //bounds.setWidth(-50);
	  
	  //bounds.setY(position.y + 20);
	  //bounds.setHeight(110);
      
     if(position.y <= -getFireFrame().getRegionHeight())
       { float x = MathUtils.random(-80, 770);
	     float y = MathUtils.random(700, 700 * 3);
	    position.set(x, y); }
}
	
	
	public void runBatch()
	{ camera2.update();
	    
	  batch.begin();
	  
	  batch.setProjectionMatrix(camera2.combined);
	  batch.draw(currentFrame, position.x, position.y, stars_width / 3 , stars_height / 3);
	  
	  batch.end(); 
	  
	  
	 // sr.begin(ShapeType.Rectangle);
	  //sr.rect(position.x + 130, position.y + 40, size.x - 235, size.y - 90);
	 // sr.end();
	}
	
	
	public void dispose()
	{ batch.dispose();
	  fireSheet.dispose();
	  sr.dispose();
	  currentFrame.getTexture().dispose();
	  
	  int size = fireFrames.length;
	  
	  for(int i = 0; i < size; i++)
	  { fireFrames[i].getTexture().dispose(); }
	}
	
	public Rectangle getBounds()
	{ return bounds; }
	
	public TextureRegion getFireFrame()
	{ return currentFrame; }
		
	public void setSpeed(float speed)
	{ direction.y = -speed; }

}
