
package com.gdxjam.utils;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenshotFactory {
	
	private static int counter = 1;
	private static String directory = "screenshots/";

	public static void saveScreenshot () {
		String path = directory + "screenshot" + counter++ + ".png";
		
		while(Gdx.files.local(path).exists()){
			counter++;
			path = directory + "screenshot" + counter++ + ".png";
		}

			Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
			PixmapIO.writePNG(Gdx.files.local(path), pixmap);
			pixmap.dispose();
		} 

	private static Pixmap getScreenshot (int x, int y, int w, int h, boolean yDown) {
		final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

		if (yDown) {
			// Flip the pixmap upside down
			ByteBuffer pixels = pixmap.getPixels();
			int numBytes = w * h * 4;
			byte[] lines = new byte[numBytes];
			int numBytesPerLine = w * 4;
			for (int i = 0; i < h; i++) {
				pixels.position((h - i - 1) * numBytesPerLine);
				pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
			}
			pixels.clear();
			pixels.put(lines);
		}

		return pixmap;
	}

}
