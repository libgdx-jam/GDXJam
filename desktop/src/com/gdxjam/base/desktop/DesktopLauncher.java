package com.gdxjam.base.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.gdxjam.Assets;
import com.gdxjam.Main;

public class DesktopLauncher {



	public static void main(String[] arg) {

		if (Assets.rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 2048;
			settings.debug = Assets.drawDebugOutline;
			try {
				TexturePacker.process(settings, "assets-raw",
						"../android/assets", "assets");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1196;
		config.height = 720;

		new LwjglApplication(new Main(), config);

	}
}
