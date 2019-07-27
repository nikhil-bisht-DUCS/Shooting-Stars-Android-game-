package com.gdxgame.rockman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class Player implements Disposable {
 
 final int Frame_Width = 128, Frame_Height = 128;
	
 SpriteBatch batch;	
 
 TextureAtlas atlas;
 TextureAtlas atlas_left;
 TextureAtlas atlas_appear;
 TextureAtlas atlas_stand;
 TextureAtlas atlas_leftStand;
 
 AssetManager manage;
 
 OrthographicCamera camera;
 
 TextureRegion right_frames[];
 TextureRegion right_currentFrame;
 Animation right_anime;
 float right_stateTime;
 
 TextureRegion left_frames[];
 TextureRegion left_currentFrame;
 Animation left_anime;
 float left_stateTime;
 
 TextureRegion appear_frames[];
 TextureRegion appear_currentFrame;
 Animation appear_anime;
 static float appear_stateTime;
 
 TextureRegion left_standing;
 TextureRegion right_standing;
 
 static Vector2 position;
 float animation_speed;
 enum FacingDirection{LEFT, RIGHT};
 
 Array<Projectile> projectiles;
 Pool<Projectile> projectilePool;
 
 float projectile_speed;
 
 FacingDirection dir;
 long lastFire;
 static Vector2 size;
 
 Rectangle leftStanding_bounds, rightStanding_bounds;
 Rectangle leftMoving_bounds, rightMoving_bounds;
 
 int hitCount;
 
 Vector3 touch_coordinates;
 static int touch_direction;
 
 //ShapeRenderer Sr;
 
 public Player()
 {   
	 batch = new SpriteBatch();
	 position = new Vector2((1000f * 3) - 750f, -10f);
	 size = new Vector2(600f, 1200f);
	 
	 dir = FacingDirection.RIGHT;
	 lastFire = 0;
	 
	 animation_speed = 0.05f;
	 projectile_speed = 21.5f;
	 hitCount = 0;
	 
	 projectiles = new Array<Projectile>();
	 
	 projectilePool = new Pool<Projectile>(){

		@Override
		protected Projectile newObject() {
			// TODO Auto-generated method stub
			return new Projectile();
		}
	 };
	 
	 manage = new AssetManager();
	 manage.load("rockman/mega_man.pack", TextureAtlas.class);
	 manage.load("rockman/left_run.pack", TextureAtlas.class);
	 manage.load("rockman/megaman_appear.pack", TextureAtlas.class);
	 manage.load("rockman/megaman_standing.pack", TextureAtlas.class);
	 manage.load("rockman/left_standing.pack", TextureAtlas.class);
	 manage.finishLoading();
	 
	 atlas = manage.get("rockman/mega_man.pack");
	 atlas_left = manage.get("rockman/left_run.pack");
	 atlas_appear = manage.get("rockman/megaman_appear.pack");
	 atlas_stand = manage.get("rockman/megaman_standing.pack");
	 atlas_leftStand = manage.get("rockman/left_standing.pack");
	 
	 
	 right_frames = new TextureRegion[10];
	 left_frames = new TextureRegion[10];
	 appear_frames = new TextureRegion[8];
	 left_standing = new TextureRegion();
	 right_standing = new TextureRegion();
	 
	 
	 left_standing = atlas_leftStand.findRegion("megaman_leftStanding");
	 right_standing = atlas_stand.findRegion("megaman_standing");
	 
	 appear_frames[0] = atlas_appear.findRegion("A_appear");
	 appear_frames[1] = atlas_appear.findRegion("B_appear");
	 appear_frames[2] = atlas_appear.findRegion("C_appear");
	 appear_frames[3] = atlas_appear.findRegion("D_appear");
	 appear_frames[4] = atlas_appear.findRegion("E_appear");
	 appear_frames[5] = atlas_appear.findRegion("F_appear");
	 appear_frames[6] = atlas_appear.findRegion("G_appear");
	 appear_frames[7] = atlas_appear.findRegion("H_appear");
	 
	 left_frames[0] = atlas_left.findRegion("Areverse_megaman");
	 left_frames[1] = atlas_left.findRegion("Breverse_megaman");
	 left_frames[2] = atlas_left.findRegion("Creverse_megaman");
	 left_frames[3] = atlas_left.findRegion("Dreverse_megaman");
	 left_frames[4] = atlas_left.findRegion("Ereverse_megaman");
	 left_frames[5] = atlas_left.findRegion("Freverse_megaman");
	 left_frames[6] = atlas_left.findRegion("Greverse_megaman");
	 left_frames[7] = atlas_left.findRegion("Hreverse_megaman");
	 left_frames[8] = atlas_left.findRegion("Ireverse_megaman");
	 left_frames[9] = atlas_left.findRegion("Jreverse_megaman");
	 
	 right_frames[0] = atlas.findRegion("A_megaman");
	 right_frames[1] = atlas.findRegion("B_megaman");
	 right_frames[2] = atlas.findRegion("C_megaman");
	 right_frames[3] = atlas.findRegion("D_megaman");
	 right_frames[4] = atlas.findRegion("E_megaman");
	 right_frames[5] = atlas.findRegion("F_megaman");
	 right_frames[6] = atlas.findRegion("G_megaman");
	 right_frames[7] = atlas.findRegion("H_megaman");
	 right_frames[8] = atlas.findRegion("I_megaman");
	 right_frames[9] = atlas.findRegion("J_megaman");
	 
	 int length = appear_frames.length;
	 
	 for(int i = 0; i < length; i++)
	 { appear_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); }
	 
	 length = left_frames.length;
	 
	 for(int i = 0; i < length; i++)
	 { left_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	   right_frames[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); }
	 
	 left_standing.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	 right_standing.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	 
	 right_anime = new Animation(animation_speed, right_frames);
	 left_anime = new Animation(animation_speed, left_frames);
	 appear_anime = new Animation(0.05f, appear_frames);
	 
	 left_stateTime = right_stateTime = appear_stateTime = 0f;
	 
	 camera = new OrthographicCamera(Frame_Width, Frame_Height);
	 camera.position.set(Frame_Width / 2, Frame_Height / 2, 0);
	 
	 leftStanding_bounds = rightStanding_bounds = leftMoving_bounds = rightMoving_bounds = new Rectangle();
	 touch_coordinates = new Vector3();
	 //Sr = new ShapeRenderer();
 }
 
 public void update()
 {   appear_stateTime += Gdx.graphics.getDeltaTime();
     appear_currentFrame = appear_anime.getKeyFrame(appear_stateTime, false); 
	
     right_stateTime += Gdx.graphics.getDeltaTime();
     right_currentFrame = right_anime.getKeyFrame(right_stateTime, true);
   
     left_stateTime += Gdx.graphics.getDeltaTime();
     left_currentFrame = left_anime.getKeyFrame(left_stateTime, true);
     
      
   if(appear_stateTime > 0.5f)
   {  touch_direction = 0;
	   
		  if(Gdx.input.isTouched())
	      { camera.unproject(touch_coordinates.set(Gdx.input.getX(), Gdx.input.getY(), 0));
	        
	        if(touch_coordinates.x >  135 / 2)
	         { touch_direction = 2; }
	        
	        else { touch_direction = 1; }
	      }
	   
	   
	  if((Gdx.input.isKeyPressed(Keys.A) || touch_direction == 1) && position.x > -70f)
    {  position.x -= 24.5f;
    
	   if(System.currentTimeMillis() - lastFire >= 170)
	   { Projectile fire_blast = projectilePool.obtain();
	     fire_blast.init((position.x / 3) - 55f, -projectile_speed);
		 projectiles.add(fire_blast);
	     lastFire = System.currentTimeMillis(); } 
     }
   
	 else if((Gdx.input.isKeyPressed(Keys.D) || touch_direction == 2) && position.x < 4740f)
   { position.x += 24.5f;   
     
	  if(System.currentTimeMillis() - lastFire >= 170)
	 { Projectile fire_blast = projectilePool.obtain();
	   fire_blast.init((position.x / 3) + 100f, projectile_speed);
	   projectiles.add(fire_blast);
	   lastFire = System.currentTimeMillis(); }
    }
   
  }
   
   for(Projectile fire: projectiles)
   { if(fire.position.x > 2500f || fire.position.x < -700f)
     { projectiles.removeValue(fire, false);
       projectilePool.free(fire); } } 
   
   for(Projectile fire: projectiles)
   { fire.update(); }
 
 }
 
 public void runBatch()
 { 
   camera.update();

   batch.begin();
   batch.setProjectionMatrix(Background.camera.combined);
   
  /* leftStanding_bounds.set((position.x / 3) + 43f, (position.y / 4) + 30f, (size.x / 4) - 120f, (size.y / 4) - 60f);
   rightStanding_bounds.set((position.x / 3) + 78f, (position.y / 4) + 30f, (size.x / 4) - 120f, (size.y / 4) - 60f);
   leftMoving_bounds.set((position.x / 3) + 60f, (position.y / 4) + 38f, (size.x / 4) - 130f, (size.y / 4) - 75f);
   rightMoving_bounds.set((position.x / 3) + 68f, (position.y / 4) + 38f, (size.x / 4) - 130f, (size.y / 4) - 75f); */
   
   if(appear_stateTime <= 0.5f)
   { batch.draw(appear_currentFrame, position.x / 3, position.y / 4, size.x / 4, size.y / 4); }
   
   else
   { if(isLeftMoving())
   {  leftMoving_bounds.set((position.x / 3) + 60f, (position.y / 4) + 38f, (size.x / 4) - 130f, (size.y / 4) - 75f);
	  batch.draw(left_currentFrame, position.x / 3, position.y / 4, size.x / 4, size.y / 4); 
      dir = FacingDirection.LEFT; } 
   
   else if(isRightMoving())
   { rightStanding_bounds.set((position.x / 3) + 78f, (position.y / 4) + 30f, (size.x / 4) - 120f, (size.y / 4) - 60f);
	 batch.draw(right_currentFrame, position.x / 3, position.y / 4, size.x / 4, size.y / 4); 
     dir = FacingDirection.RIGHT; }
 
   else 
   { if(dir == FacingDirection.LEFT)
	 { leftStanding_bounds.set((position.x / 3) + 43f, (position.y / 4) + 30f, (size.x / 4) - 120f, (size.y / 4) - 60f);
	   batch.draw(left_standing, position.x / 3, position.y / 4, size.x / 4, size.y / 4); }
	   
	 else { rightStanding_bounds.set((position.x / 3) + 78f, (position.y / 4) + 30f, (size.x / 4) - 120f, (size.y / 4) - 60f);
		    batch.draw(right_standing, position.x / 3, position.y / 4, size.x / 4, size.y / 4); }
   }
   
  /* Sr.setProjectionMatrix(Background.camera.combined);
   Sr.setColor(Color.RED);
   Sr.begin(ShapeType.Rectangle);
   Sr.rect((position.x / 3) + 68f, (position.y / 4) + 38f, (size.x / 4) - 130f, (size.y / 4) - 75f);
   Sr.end(); */
 }
   
   batch.end();
   
   for(Projectile fire: projectiles)
	 { fire.runBatch(); }
   
 }
	
 @Override
 public void dispose()
 { 
   if(batch != null)
   { batch.dispose(); }
 
   manage.dispose();
 
   atlas.dispose();
   atlas_left.dispose();
   atlas_appear.dispose();
   atlas_stand.dispose();
   atlas_leftStand.dispose();
   
   if(right_currentFrame != null)
   { right_currentFrame.getTexture().dispose(); }
   
   if(left_currentFrame != null)
   { left_currentFrame.getTexture().dispose(); }
   
   if(right_standing != null)
   { right_standing.getTexture().dispose(); }
   
   if(left_standing != null)
   { left_standing.getTexture().dispose(); }
   
   if(appear_currentFrame != null)
   { appear_currentFrame.getTexture().dispose(); }
   
   int size = left_frames.length;
   
   for(int i = 0; i < size; i++)
   { left_frames[i].getTexture().dispose(); 
     right_frames[i].getTexture().dispose(); }
   
   size = appear_frames.length;
   
   for(int i = 0; i < size; i++)
   { appear_frames[i].getTexture().dispose(); }
   
   for(Projectile fire: projectiles)
   { fire.dispose(); }
   
   projectilePool.clear();
   projectiles.clear();
   
 }
 
 
 public static boolean isLeftMoving()
 { if(Gdx.input.isKeyPressed(Keys.A) || touch_direction == 1) return true;
   else return false; }
 
 public static boolean isRightMoving()
 { if(Gdx.input.isKeyPressed(Keys.D) || touch_direction == 2) return true;
   else return false; }
 
 public float getPlayerPosition()
 { return position.x / 3; }
 
 
 public Rectangle getLeftStandingBounds()
 { return leftStanding_bounds; }
 
 public Rectangle getRightStandingBounds()
 { return rightStanding_bounds; }
 
 public Rectangle getLeftMovingBounds()
 { return leftMoving_bounds; }
 
 public Rectangle getRightMovingBounds()
 { return rightMoving_bounds; }
 
 public Array<Projectile> getProjectiles()
 { return projectiles; }
 
 public FacingDirection getFacingDirection()
 { return dir; }
 
 public int getHitCount()
 { return hitCount; }
 
 public void incrementHitCount()
 { hitCount++; }
 
}