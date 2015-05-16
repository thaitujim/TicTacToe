package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.TicTacToeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TicTacToeGame.CELL_DIMENSION* TicTacToeGame.MATRIX_DIMENSION;
		config.height = TicTacToeGame.CELL_DIMENSION* TicTacToeGame.MATRIX_DIMENSION;
		new LwjglApplication(new TicTacToeGame(), config);
	}
}
