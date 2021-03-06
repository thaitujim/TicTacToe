package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Board;
import com.mygdx.game.MasterGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Board.CELL_DIMENSION_PIXEL * Board.MATRIX_DIMENSION;
		config.height = Board.CELL_DIMENSION_PIXEL * Board.MATRIX_DIMENSION;
		new LwjglApplication(new MasterGame(), config);
	}
}
