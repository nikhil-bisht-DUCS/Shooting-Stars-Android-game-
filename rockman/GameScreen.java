package com.gdxgame.rockman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.gdxgame.miscellaneous.LifeCount;
import com.gdxgame.miscellaneous.ScoreKill;
import com.gdxgame.rockman.Player.FacingDirection;
import com.gdxgame.screen.GameOver;
import com.gdxgame.stars.ShootingStars;


public class GameScreen implements Screen {

	Player megaman;
	Background bg;
	Stage stage;
	
	enum PlayerState {NEUTRAL, FAINT, DEAD, GAME_OVER};
	static PlayerState megaman_state = PlayerState.NEUTRAL;
	
	float miliCount, miliCount2;
	int secCount;
	
	Array<FireBall> fireballs;
	Array<Demon> demons;

	Music bg_music;
	AssetManager manage;
	
	Faint faint;
	Dead dead;
	
	ScoreKill player_score;
	LifeCount life_count;
	
	int demon_pts, fireball_pts;
	int endGameSec;
	
	ShootingStars stars;
	
	public GameScreen(ShootingStars stars)
	{   miliCount = miliCount2 = 0;
	    secCount = 0;
	    
	    this.stars = stars;
		
	    demon_pts = 200; 
	    fireball_pts = 50;
	    
		megaman_state = PlayerState.NEUTRAL;
		megaman = new Player();
		bg = new Background();
	
		fireballs = new Array<FireBall>();
		demons = new Array<Demon>();
		
		for(int i = 0; i < 23; i++)
		{ float x = MathUtils.random(0f, 6300f);
		  float y = MathUtils.random(4000f, 4000f * 3);
		  float speed = MathUtils.random(30f, 35f);	
		  fireballs.add(new FireBall(new Vector2(x, y), new Vector2(0, -speed))); }
		
		
		for(int i = 0; i < 4; i++)
		{ int x = MathUtils.random(1, 2);
		  float speed = MathUtils.random(3.5f, 4.5f);
		  
		  if(x == 1)
		  {  float pos = MathUtils.random(-300f * 1.5f, -300f * 2f);
			 demons.add(new Demon(new Vector2(pos, -10f), new Vector2(speed, 0))); }
		  
		  else { float pos = MathUtils.random(2000f * 1.5f, 2000f * 2f);
			     demons.add(new Demon(new Vector2(pos, -10f), new Vector2(-speed, 0))); }  }
	
		manage = new AssetManager();
		manage.load("audio/resident evil mercenaries.mp3", Music.class);
		manage.finishLoading();
		
		bg_music = manage.get("audio/resident evil mercenaries.mp3");
		
		bg_music.play();
		bg_music.setLooping(true);
		
		faint = new Faint();
		dead = new Dead();
		
		player_score = new ScoreKill();
		life_count = new LifeCount();
		
		bg.startBackground();
	}
	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		miliCount += delta; // handles overall
		miliCount2 += delta; // handles secCount
		
		if(miliCount2 >= 1)
		{ miliCount2 = 0;
		  secCount++; }
		
		  if(miliCount < 4.7f)
		  {  bg.traverseBackground();
			 bg.runBatch(); }
		  
		  else if(miliCount >= 4.7f && secCount  < 10)
		  { megaman.update();
		    bg.runBatch();
		    megaman.runBatch();
		    player_score.runBatch();
		    life_count.runBatch();   }
		  
		  else if(megaman_state == PlayerState.FAINT)
		  { 
			for(Projectile beam: megaman.projectiles)
		    { beam.update(); }
			  
			 if(secCount > 30)
			{ for(Demon demon: demons)
			 { demon.update(); 	 }  }
		  
		    for(FireBall fire: fireballs)
			{ fire.update(); }
			  
			bg.runBatch();
			
			for(Projectile beam: megaman.projectiles)
			{ beam.runBatch(); }
		    
		    if(megaman.dir == FacingDirection.LEFT)
		    { faint.runLeftBatch();
		    	
		       if(faint.left_anime.isAnimationFinished(faint.left_stateTime) && faint.left_stateTime > 0.5f)
		      { megaman_state = PlayerState.NEUTRAL;
		        faint.left_stateTime = 0f; } }
		    
		    else { faint.runRightBatch();
		    	
		    	    if(faint.right_anime.isAnimationFinished(faint.right_stateTime) && faint.right_stateTime > 0.5f)
		          { megaman_state = PlayerState.NEUTRAL;
		            faint.right_stateTime = 0f; } }
		    
			for(FireBall fire: fireballs)
			{ fire.runBatch();
			  
			for(Projectile beam: megaman.projectiles)
			  { if(beam.left_bounds.overlaps(fire.fireball_bounds) || beam.right_bounds.overlaps(fire.fireball_bounds))
			    { megaman.projectiles.removeValue(beam, false);
			      megaman.projectilePool.free(beam);
			      player_score.incrementKillCount(fireball_pts);
			      fire.resetPositionSpeed();  }   }
			
			}
			
			if(secCount > 30)
			{ for(Demon demon: demons)
			 { demon.runBatch();
			   
			  for(Projectile beam: megaman.projectiles)
			  { if(beam.left_bounds.overlaps(demon.left_bounds) || beam.right_bounds.overlaps(demon.left_bounds) || beam.left_bounds.overlaps(demon.right_bounds) || beam.right_bounds.overlaps(demon.right_bounds) )
			    { demon.hitCount++;
				  megaman.projectiles.removeValue(beam, false);
				  megaman.projectilePool.free(beam);
				
			    
			     if(demon.hitCount >= 2)
			     { player_score.incrementKillCount(demon_pts);
			       demon.resetPositionSpeed(); } 
			    } }
			  } }
			
			player_score.runBatch();
			life_count.runBatch(); 
	   }
		  
		  
		  else if(megaman_state == PlayerState.DEAD)
		  { endGameSec = secCount;
		  
			for(Projectile beam: megaman.projectiles)
			  { beam.update(); }
			  
			  if(secCount > 30)
			{ for(Demon demon: demons)
			 { demon.update(); } }
		  
		    for(FireBall fire: fireballs)
			{ fire.update(); }
			  
			bg.runBatch();
		    
			for(Projectile beam: megaman.projectiles)
			 { beam.runBatch(); }
			
		    if(megaman.dir == FacingDirection.LEFT)
		    { dead.runLeftBatch();
		    	
		       if(dead.left_anime.isAnimationFinished(dead.left_stateTime) && dead.left_stateTime > 0.5f)
		      { megaman_state = PlayerState.GAME_OVER;
		        dead.left_stateTime = 0f; } }
		    
		    else { dead.runRightBatch();
		    	
		    	    if(dead.right_anime.isAnimationFinished(dead.right_stateTime) && dead.right_stateTime > 0.5f)
		          { megaman_state = PlayerState.GAME_OVER;
		            dead.right_stateTime = 0f; } }
		    
			for(FireBall fire: fireballs)
			{ fire.runBatch();
			
			for(Projectile beam: megaman.projectiles)
			  { if(beam.left_bounds.overlaps(fire.fireball_bounds) || beam.right_bounds.overlaps(fire.fireball_bounds))
			    { megaman.projectiles.removeValue(beam, false);
			      megaman.projectilePool.free(beam);
			      player_score.incrementKillCount(fireball_pts);
			      fire.resetPositionSpeed();  }   }  }
			
			if(secCount > 30)
			{ for(Demon demon: demons)
			 { demon.runBatch();
			   
			  for(Projectile beam: megaman.projectiles)
			  { if(beam.left_bounds.overlaps(demon.left_bounds) || beam.right_bounds.overlaps(demon.left_bounds) || beam.left_bounds.overlaps(demon.right_bounds) || beam.right_bounds.overlaps(demon.right_bounds) )
			    { demon.hitCount++;
				  megaman.projectiles.removeValue(beam, false);
				  megaman.projectilePool.free(beam);
				
			    
			     if(demon.hitCount >= 2)
			     { player_score.incrementKillCount(demon_pts);
			       demon.resetPositionSpeed(); } 
			    }  }
			 } }
			
			player_score.runBatch();
			life_count.runBatch(); 
	   }
		  
		  else if(megaman_state == PlayerState.NEUTRAL) 
	   { megaman.update();
		
	    if(secCount > 30)
		{ for(Demon demon: demons)
		 { demon.update(); } }
		
		for(FireBall fire: fireballs)
		{ fire.update(); }
		
		bg.runBatch();
		
		 megaman.runBatch();
		
		for(FireBall fire: fireballs)
		{ fire.runBatch();
		    
		  if(megaman.leftStanding_bounds.overlaps(fire.fireball_bounds) || megaman.rightStanding_bounds.overlaps(fire.fireball_bounds) || megaman.leftMoving_bounds.overlaps(fire.fireball_bounds) || megaman.rightMoving_bounds.overlaps(fire.fireball_bounds))
		  {  megaman.hitCount++;
		     life_count.life_counter--;
			  
		     if(megaman.hitCount >= 3 || life_count.life_counter <= 0)   
		     { megaman_state = PlayerState.DEAD; }
		    	 
		     else { megaman_state = PlayerState.FAINT; }
			 
		     fire.resetPositionSpeed();
          }
		  
		  for(Projectile beam: megaman.projectiles)
		  { if(beam.left_bounds.overlaps(fire.fireball_bounds) || beam.right_bounds.overlaps(fire.fireball_bounds))
		    { megaman.projectiles.removeValue(beam, false);
		      megaman.projectilePool.free(beam);
		      player_score.incrementKillCount(fireball_pts);
		      fire.resetPositionSpeed();  }   } 
		}
		
		
		if(secCount > 30)
		{ for(Demon demon: demons)
		{ demon.runBatch();
		  
		  if(megaman.leftStanding_bounds.overlaps(demon.left_bounds) || megaman.leftMoving_bounds.overlaps(demon.left_bounds) || megaman.rightStanding_bounds.overlaps(demon.left_bounds) || megaman.rightMoving_bounds.overlaps(demon.left_bounds) || megaman.leftStanding_bounds.overlaps(demon.right_bounds) || megaman.leftMoving_bounds.overlaps(demon.right_bounds) || megaman.rightStanding_bounds.overlaps(demon.right_bounds) || megaman.rightMoving_bounds.overlaps(demon.right_bounds))
		  {  megaman.hitCount++;
		     life_count.life_counter--;
			  
		     if(megaman.hitCount >= 3 || life_count.life_counter <= 0)
		     { megaman_state = PlayerState.DEAD; }
		  
		     else { megaman_state = PlayerState.FAINT; }
		  
             demon.resetPositionSpeed();    }
				
		  for(Projectile beam: megaman.projectiles)
		  { if(beam.left_bounds.overlaps(demon.left_bounds) || beam.right_bounds.overlaps(demon.left_bounds) || beam.left_bounds.overlaps(demon.right_bounds) || beam.right_bounds.overlaps(demon.right_bounds))
		    { demon.hitCount++;
			  megaman.projectiles.removeValue(beam, false);
			  megaman.projectilePool.free(beam);
			
		    
		     if(demon.hitCount >= 2)
		     { player_score.incrementKillCount(demon_pts);
		       demon.resetPositionSpeed(); } 
		    }  
		  }
		 } 
	 }
	  
		player_score.runBatch();
		life_count.runBatch(); 
   }
		  
		  else if(megaman_state == PlayerState.GAME_OVER)
		  { 
			for(Projectile beam: megaman.projectiles)
			 { beam.update(); }
				 
			 if(secCount > 30)
			{ for(Demon demon: demons)
			 { demon.update(); } }
		  
		    for(FireBall fire: fireballs)
			{ fire.update(); }
			  
			bg.runBatch();
			
			for(Projectile beam: megaman.projectiles)
			{ beam.runBatch(); }
			
			for(FireBall fire: fireballs)
			{ fire.runBatch();
			  
			for(Projectile beam: megaman.projectiles)
			  { if(beam.left_bounds.overlaps(fire.fireball_bounds) || beam.right_bounds.overlaps(fire.fireball_bounds))
			    { megaman.projectiles.removeValue(beam, false);
			      megaman.projectilePool.free(beam);
			      player_score.incrementKillCount(fireball_pts);
			      fire.resetPositionSpeed();  }   }   }
			
			if(secCount > 30)
			{ for(Demon demon: demons)
			 { demon.runBatch(); 
			 
			  for(Projectile beam: megaman.projectiles)
			  { if(beam.left_bounds.overlaps(demon.left_bounds) || beam.right_bounds.overlaps(demon.left_bounds) || beam.left_bounds.overlaps(demon.right_bounds) || beam.right_bounds.overlaps(demon.right_bounds) )
			    { demon.hitCount++;
				  megaman.projectiles.removeValue(beam, false);
				  megaman.projectilePool.free(beam);
				
			    
			     if(demon.hitCount >= 2)
			     { player_score.incrementKillCount(demon_pts);
			       demon.resetPositionSpeed(); } 
			    }  }
			 }   
			}
		  
			if(secCount == endGameSec + 3)
			{ bg_music.stop();
			  stars.setNewScreen(new GameOver(player_score, stars)); }
		  }
		 
	}
		
	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
		if(stage == null)
		{ stage = new Stage(width, height, true); }
		
		stage.setCamera(Background.camera);
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
		bg.dispose();
		megaman.dispose();
		
		for(FireBall fire: fireballs)
		{ fire.dispose(); }
		
		for(Demon demon: demons)
		{ demon.dispose(); }
		
		fireballs.clear();
		demons.clear();
		
		manage.dispose();
		bg_music.dispose();
		
		faint.dispose();
		dead.dispose();
	    
		if(life_count != null)
	    { life_count.dispose(); }
	    
	    stage.dispose();	    
	}
	
	

}
