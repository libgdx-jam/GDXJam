package com.gdxjam.base.desktop;

import com.badlogic.gdx.Files.FileType;
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
		config.width = 1280;
		config.height = 720;
		config.addIcon("icon128.png", FileType.Internal);
		config.addIcon("icon32.png", FileType.Internal);
		config.addIcon("icon16.png", FileType.Internal);

		new LwjglApplication(new Main(), config);

	}
}
