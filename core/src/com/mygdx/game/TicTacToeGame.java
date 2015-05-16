package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TicTacToeGame extends ApplicationAdapter {
	public SpriteBatch batch;
	public static final int MATRIX_DIMENSION = 8;
	public static final int CELL_DIMENSION = 32;
	public NodeSprite[][] matrix;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ResourceManager.load();
		buildMatrix();
	}

	private void buildMatrix() {
		matrix = new NodeSprite[MATRIX_DIMENSION][MATRIX_DIMENSION];
		
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				NodeSprite nodeSprite = new NodeSprite(NodeSprite.BLANK_TYPE, 32, 32, i, j);
				matrix[i][j] = nodeSprite;
			}
		}
	}

	@Override
	public void render () {
		//clear white
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				NodeSprite nodeSprite = matrix[i][j];
				nodeSprite.draw(batch);
			}
		}
		
		batch.end();
	}
}
