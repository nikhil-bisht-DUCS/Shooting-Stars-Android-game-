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

public class FireBall implements Disposable {
	
	final int Frame_Width = 700, Frame_Height = 380;
	
	SpriteBatch batch;
	
	AssetManager manage;
	TextureAtlas atlas;
	OrthographicCamera camera;
	
	TextureRegion fire_frames[];
    TextureRegion fire_currentFrame;
    Animation fire_anime;
    
    Vector2 position;
    Vector2 speed;
    Vector2 size;
    
    float fire_stateTime;
    Rectangle fireball_bounds;
    
    //ShapeRenderer Sr;
    
    public FireBall(Vector2 position, Vector2 speed)
    {
    	batch = new SpriteBatch();
    	this.position = position;
    	this.speed = speed;
    	
    	manage = new AssetManager();
    	manage.load("rockman/fire_ball.pack", TextureAtlas.class);
    	manage.finishLoading();
    	
    	atlas = manage.get("rockman/fire_ball.pack");
    	
    	fire_frames = new TextureRegion[3];
    	
    	fire_frames[0] = atlas.findRegion("A_fireball");
    	fire_frames[1] = atlas.findRegion("B_fireball");
    	fire_frames[2] = atlas.findRegion("C_fireball");
    	
    	int length = fire_frames.length;
    	
    	for(int i = 0; i < length; i++)
    	{ fire_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); }
    	
    	fire_anime = new Animation(0.08f, fire_frames);
    	fire_stateTime = 0f;
    	
    	camera = new OrthographicCamera(Frame_Width, Frame_Height);
    	camera.position.set(Frame_Width / 2, Frame_Height / 2, 0);
    	
    	size = new Vector2(130f, 340f);
    	fireball_bounds = new Rectangle();
    	
    	//Sr = new ShapeRenderer();
    }
    
    public void update()
    { fire_stateTime += Gdx.graphics.getDeltaTime();
      fire_currentFrame = fire_anime.getKeyFrame(fire_stateTime, true);
      
      position.add(speed);
      
      if(position.y < -1600f)
      { float x = MathUtils.random(0f, 6300f);
        float y = MathUtils.random(4000f, 4000f * 3);
        position.set(x, y); }
    }
    
    public void runBatch()
    {   camera.update();
    	
    	batch.begin();
    	batch.setProjectionMatrix(Background.camera.combined);
    	
    	fireball_bounds.set((position.x / 4) + 30f, (position.y / 4) + 40f, (size.x / 2) + 7f, size.y / 2);
    	
    	batch.draw(fire_currentFrame, position.x / 4, position.y / 4, size.x, size.y);
    	batch.end();
    	
    	/*Sr.setProjectionMatrix(Background.camera.combined);
    	Sr.setColor(Color.RED);
    	Sr.begin(ShapeType.Rectangle);
    	Sr.rect((position.x / 4) + 30f, (position.y / 4) + 40f, (size.x / 2) + 7f, size.y / 2);
    	Sr.end(); */
    }
    
    @Override
    public void dispose()
    {   
    	if(batch != null)
    	{ batch.dispose(); }
    	
    	manage.dispose();
    	atlas.dispose();
    	
    	if(fire_currentFrame != null)
    	{ fire_currentFrame.getTexture().dispose(); }
    	
    	int size = fire_frames.length;
    	
    	for(int i = 0; i < size; i++)
    	{ fire_frames[i].getTexture().dispose(); }
    }
    
    public Rectangle getBounds()
    { return fireball_bounds; }
    
    public void resetPositionSpeed()
    { float x = MathUtils.random(0f, 6300f);
	  float y = MathUtils.random(4000f, 4000f* 3);
	  float speed = MathUtils.random(35f, 50f);	
	  
      position.x = x;
      position.y = y;
      
      this.speed.y = -speed; }
}
