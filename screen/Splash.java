package com.gdxgame.screen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdxgame.miscellaneous.SpriteTween;
import com.gdxgame.stars.ShootingStars;


public class Splash implements Screen {
	
	Texture texture;
	Sprite sprite;
	SpriteBatch batch;
	ShootingStars stars;
	
	OrthographicCamera camera2;
	
	TweenManager manager;
	
	
	final float width = 1024, height = 512;
	
	public Splash(ShootingStars stars)
	{ this.stars = stars;
	  float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getHeight();
	
	  camera2 = new OrthographicCamera(height * aspectRatio, height);
	  camera2.position.set(width / 2, height / 2, 0); }
	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera2.update();
		
		batch.begin();
		batch.setProjectionMatrix(camera2.combined);
		
		sprite.draw(batch);
		
		batch.end();
		
		manager.update(delta);
	}

	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		texture = new Texture(Gdx.files.internal("new_designs/xhdpi_intro.png"));
		
		sprite = new Sprite(texture);
		sprite.setColor(1, 1, 1, 0);
		
		sprite.setSize((width / 1.5f) + 30, height);
		sprite.setPosition(160, 16);
		
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		batch = new SpriteBatch();		
		
	    Tween.registerAccessor(Sprite.class, new SpriteTween());
	    
	    manager = new TweenManager();
	    
	    TweenCallback callBack = new TweenCallback() {

			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				// TODO Auto-generated method stub
				tweenComplete();
			}
	    	
	    };
	    
	    Tween.to(sprite, SpriteTween.ALPHA, 3f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, 2f).setCallback(callBack).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
	}

	
	private void tweenComplete()
	{ stars.setNewScreen(new MainMenu(stars)); }
	
	
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
		batch.dispose();
		texture.dispose();
		sprite.getTexture().dispose();
		
	}

	
}
