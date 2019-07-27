package com.gdxgame.miscellaneous;

import com.badlogic.gdx.graphics.g2d.Sprite;
import aurelienribon.tweenengine.TweenAccessor;


public class SpriteTween implements TweenAccessor<Sprite> {

	public static final int ALPHA = 1;
	
	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		// TODO Auto-generated method stub
		
		switch(tweenType)
		{ case ALPHA: returnValues[0] = target.getColor().a;
		              return 1;
		  
		              
		  default: return 0;  }
		
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		// TODO Auto-generated method stub
		
		switch(tweenType)
		{ case ALPHA: target.setColor(1, 1, 1, newValues[0]);
		              break;
		}
		
	}
	
	
}