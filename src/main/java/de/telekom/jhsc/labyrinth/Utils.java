
package de.telekom.jhsc.labyrinth;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class Utils {

	public static Drawable generateColoredDrawable(Color color) {
		Texture texture = generateColorTexture(1, 1, color);

		return new TextureRegionDrawable(new TextureRegion(texture));
	}

	public static Texture generateColorTexture(int width, int height, Color color) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGB888);
		pixmap.setColor(color);
		pixmap.fill();

		return new Texture(pixmap);
	}
}
