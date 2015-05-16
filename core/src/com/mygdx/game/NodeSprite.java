package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NodeSprite {

	public static final int BLANK_TYPE = 0;
	public static final int X_TYPE = 1;
	public static final int O_TYPE = 2;

	private int width;
	private int height;
	private int type;
	private int x;
	private int y;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public NodeSprite() {

	}

	public NodeSprite(int type, int width, int height) {
		this.width = width;
		this.height = height;
		this.type = type;
	}

	public NodeSprite(int type, int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch batch) {
		int posX = x * this.width;
		int posY = y * this.height;

		switch (this.type) {
		case BLANK_TYPE:
			batch.draw(ResourceManager.cell_sprite, posX, posY, width, height);

			break;
		case X_TYPE:
			batch.draw(ResourceManager.x_sprite, posX, posY, width, height);

			break;
		case O_TYPE:
			batch.draw(ResourceManager.o_sprite, posX, posY, width, height);

			break;

		default:
			break;
		}
	}
}
