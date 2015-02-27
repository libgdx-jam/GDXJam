package com.gdxjam.screens;

import com.gdxjam.Assets;
import com.gdxjam.GameManager;

public class SplashScreen extends AbstractScreen{
	
	@Override
	public void show () {
		super.show();
		Assets.load();
	}
	
	@Override
	public void render (float delta) {
		super.render(delta);
		
		if(Assets.getManager().update()){
			Assets.create();
			GameManager.setScreen(new SelectorScreen());
		}
	}

}
