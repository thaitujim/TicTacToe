package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {
	public static Texture o_sprite;
	public static Texture x_sprite;
	public static Texture cell_sprite;
	public static Texture cell_sprite_gray;
	public static Texture you_win;
	public static Texture you_lose;
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		o_sprite = loadTexture("o_sprite.png");
		x_sprite = loadTexture("x_sprite.png");
		cell_sprite = loadTexture("cell_sprite.png");
		cell_sprite_gray = loadTexture("cell_sprite_gray.png");
		you_win = loadTexture("you_win.png");
		you_lose = loadTexture("you_lose.png");
	}
}
