package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {
	public static Texture o_sprite;
	public static Texture x_sprite;
	public static Texture cell_sprite;
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		o_sprite = loadTexture("x_sprite.png");
		x_sprite = loadTexture("o_sprite.png");
		cell_sprite = loadTexture("cell_sprite.png");
	}
}
