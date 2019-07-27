package com.gdxgame.stars;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
//import com.gdxgame.rockman.GameScreen;
import com.gdxgame.screen.Splash;


public class ShootingStars extends Game {
	
   public static int WIDTH = 1000;
   public static int HEIGHT = 700;
   
   //public static ShootingStars stars;
   public Screen currentScreen;
	
	@Override
	public void create() {
		
		//stars = this;
		setNewScreen(new Splash(this));
 
	}

	@Override
	public void dispose() {
		super.dispose();
		currentScreen.dispose();
		//currentScreen.dispose();
	}

	@Override
	public void render() {		
		super.render();
		
	}
	

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	 public void setNewScreen(Screen screen)
	{ if(currentScreen != null)
	  { currentScreen.dispose(); }
		
	  currentScreen = screen;
	  setScreen(currentScreen); }
	
}
