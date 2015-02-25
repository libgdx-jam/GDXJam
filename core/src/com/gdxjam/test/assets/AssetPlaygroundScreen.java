package com.gdxjam.test.assets;

import com.badlogic.ashley.core.Engine;
import com.gdxjam.GameManager;
import com.gdxjam.screens.AbstractScreen;

public class AssetPlaygroundScreen extends AbstractScreen {
	Engine engine;

	@Override
	public void show() {
		super.show();
		engine = GameManager.initEngine();

	}

}
