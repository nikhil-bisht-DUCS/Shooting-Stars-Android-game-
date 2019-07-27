package com.gdxgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;



public class Player {
	
	private static final int FRAME_ROWS = 1;
	private static final int FRAME_COLS = 8;
	
	SpriteBatch batch;
	
	Texture standing;
	
	Texture walkSheet;
	TextureRegion walkFrames[], currentFrame;
	
	Animation anmLeft, anmRight;
	Vector2 position;
	
	Texture backSheet;
	TextureRegion backFrames[], backFrame;
	
	Rectangle standingBounds, rightBounds, leftBounds;
	
	float stateTime;
	float stateTime2;
	int dir;
	
	OrthographicCamera camera2;
	
	final float player_width = 128;
	final float player_height = 128;
	
	Vector3 coordinates;
	
	
	public Player()
	{   standing = new Texture(Gdx.files.internal("scott_standing.png"));
		walkSheet = new Texture(Gdx.files.internal("scott_spritesheet.png"));
		
		standing.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion tmp[][] = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
		walkFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
		
		int index = 0;	
		
		for(int i = 0; i < FRAME_ROWS; i++)
	    for(int j = 0; j < FRAME_COLS; j++)
	    { walkFrames[index++] = tmp[i][j]; }
		
		int length = walkFrames.length;
		
		for(int i = 0; i < length; i++)
		{ walkFrames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); }
		
		anmRight = new Animation(0.07f, walkFrames);
		
		/*--------------------------------------------------------------------------------------------------------------------------------------------*/
		
		backSheet = new Texture(Gdx.files.internal("scott_spritesheet2.png"));
		TextureRegion tmp2[][] = TextureRegion.split(backSheet, backSheet.getWidth() / FRAME_COLS, backSheet.getHeight() / FRAME_ROWS);
		backFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
		
		index = 7;
		
		for(int i = 0; i < FRAME_ROWS; i++)
		for(int j = 0; j < FRAME_COLS; j++)
		{ backFrames[index--] = tmp2[i][j]; }
		
		length = backFrames.length;
		
		for(int i = 0; i < length; i++)
		{ backFrames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); }
		
		anmLeft = new Animation(0.07f, backFrames);
		
		batch = new SpriteBatch();
		
		
		standingBounds = leftBounds = rightBounds = new Rectangle();
		
		stateTime = stateTime2 = 0f;
		
		coordinates = new Vector3();
		float aspectRatio = (float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
		  
		camera2 = new OrthographicCamera(player_width, player_height);
		camera2.position.set(player_width / 2, player_height / 2, 0);
		
		position = new Vector2(300, 0);
		
	}
	
	
	public void update()
	{ stateTime += Gdx.graphics.getDeltaTime();
	  currentFrame = anmRight.getKeyFrame(stateTime, true);
	  
	  stateTime2 += Gdx.graphics.getDeltaTime();
	  backFrame = anmLeft.getKeyFrame(stateTime2, true);
	  
	  //currentFrame.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	  //backFrame.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	  
	  leftBounds.set(position.x, position.y, backFrame.getRegionWidth(), backFrame.getRegionHeight());
	  rightBounds.set(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
	  standingBounds.set(position.x, position.y, standing.getWidth(), standing.getHeight());
	 
	  dir = 0;
	  
	  if(Gdx.input.isTouched())
      { camera2.unproject(coordinates.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        
        if(coordinates.x >  135 / 2)
         { dir = 2; }
        
        else { dir = 1; }
      }
	  
	  if(Gdx.input.isKeyPressed(Keys.D) && Gdx.input.isKeyPressed(Keys.A))
	  { position.x += 0f; }
	  
	  else if((Gdx.input.isKeyPressed(Keys.A) || dir == 1) && position.x > -20)
	  { position.x -= 9f; }
	  
	  else if((Gdx.input.isKeyPressed(Keys.D) || dir == 2) && position.x < 680)
	  { position.x += 9f; }
	  
	  else if(Gdx.input.isKeyPressed(Keys.W))
	 { position.y += 10f; }
	  
	  else if(Gdx.input.isKeyPressed(Keys.Z))
	  { position.y -= 10f; }
	  
	}
	
	
	public void runBatch()
	{ 
	  camera2.update();
	  
	  batch.begin();
	  
	  batch.setProjectionMatrix(camera2.combined);
	  
	  if(isRunLeft())
	  { batch.draw(backFrame, position.x / 6, position.y / 6, player_width / 6, player_height / 4); }
	  
	  else if(isRunRight())
	  { batch.draw(currentFrame, position.x / 6, position.y / 6, player_width / 6, player_height / 4); }
	  
	  else { batch.draw(standing, position.x / 6, position.y / 6, player_width / 6, player_height / 4); }
	 
	  batch.end(); }
	
	
	public boolean isRunLeft()
	{ if(Gdx.input.isKeyPressed(Keys.A) || dir == 1) { return true; }
	else { return false; }  }
	
	
	public boolean isRunRight()
	{ if(Gdx.input.isKeyPressed(Keys.D) || dir == 2) { return true; }
	else { return false; } }
	
	
	public void dispose()
	{ batch.dispose();
	  backSheet.dispose();
	  standing.dispose();
	  walkSheet.dispose();
	  currentFrame.getTexture().dispose();
	  backFrame.getTexture().dispose();
	  
	  int size = walkFrames.length;
	  
	  for(int i = 0; i < size; i++)
	  { walkFrames[i].getTexture().dispose(); }
	  
	  size = backFrames.length;
	  
	  for(int i = 0; i < size; i++)
	  { backFrames[i].getTexture().dispose(); }
	  
	}
	
	public Rectangle getStandingBounds()
	{ return standingBounds; }
	
	public Rectangle getLeftBounds()
	{ return leftBounds; }
	
	public Rectangle getRightBounds()
	{ return rightBounds; }
	
	
	
}
