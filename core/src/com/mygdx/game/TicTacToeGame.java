package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.mygdx.game.Board.TURN;

public class TicTacToeGame extends ApplicationAdapter {

	public static final String TAG = TicTacToeGame.class.getName();
	public static final boolean DEBUG_DRAW = false;
	public static final boolean DEBUG_UPDATE = false;

	public SpriteBatch batch;
	public Board board;

	@Override
	public void create() {
		batch = new SpriteBatch();
		ResourceManager.load();
		board = new Board();
		board.turn = TURN.HUMAN_RACE;
	}

	@Override
	public void render() {
		update();
		draw();
	}

	// FUNCTION
	// --------------------------------------------------------------------
	public void draw() {
		if (DEBUG_DRAW) {
			Gdx.app.log(TAG, "--> draw");
		}

		// clear white
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		NodeSprite[][] matrix = board.matrix;

		for (int i = 0; i < Board.MATRIX_DIMENSION; i++) {
			for (int j = 0; j < Board.MATRIX_DIMENSION; j++) {
				NodeSprite nodeSprite = matrix[i][j];
				nodeSprite.draw(batch);
			}
		}

		batch.end();
	}

	// --------------------------------------------------------------------
	public void update() {
		if (DEBUG_UPDATE) {
			Gdx.app.log(TAG, "--> update");
		}

		if (board.turn == TURN.HUMAN_RACE) {

			if (board.isComputerWin()) {
				Gdx.app.log(TAG, "game over --> computer win");
			} else if (board.isHumanWin()) {
				Gdx.app.log(TAG, "game over --> human win");
			}

			if (Gdx.input.justTouched()) {
				int touchX = Gdx.input.getX();
				int touchY = Gdx.input.getY();
				Gdx.app.log(TAG, "--> touch x-y: " + touchX + "-" + touchY);

				int cellX = convertToMatrixCol(touchX);
				int cellY = convertToMatrixRow(touchY);
				Gdx.app.log(TAG, "--> cell x-y: " + cellX + "-" + cellY);

				if (cellX > -1 && cellX < Board.MATRIX_DIMENSION && cellY > -1
						&& cellY < Board.MATRIX_DIMENSION) {
					//
					// board.setCellType(new Point(cellX, cellY),
					// NodeSprite.O_TYPE);
					NodeSprite cell = board.matrix[cellX][cellY];
					ArrayList<NodeSprite> nodeAvailables = board
							.getAvailableMoves(TURN.HUMAN_RACE);
					NodeSprite humanRace = new NodeSprite(NodeSprite.O_TYPE,
							Board.CELL_DIMENSION_PIXEL,
							Board.CELL_DIMENSION_PIXEL, cellX, cellY);
					if (Board.exitsInList(nodeAvailables, humanRace)) {
						board.matrix[cellX][cellY] = humanRace;
						board.turn = TURN.COMPUTER_RACE;
					}
					//
					Gdx.app.log(TAG, "--> node x-y: " + cellX + "-" + cellY
							+ " updated");
				}
			}
		} else if (board.turn == TURN.COMPUTER_RACE) {
			NodeSprite next = board.returnNextMove();
			if (next != null) {
				Point point = next.getPoint();
				int i = point.getX();
				int j = point.getY();
				board.matrix[i][j] = next.setType(NodeSprite.X_TYPE);
				board.turn = TURN.HUMAN_RACE;
			} else {
				if (board.isComputerWin()) {
					Gdx.app.log(TAG, "game over --> computer win");
				} else {
					Gdx.app.log(TAG, "game over --> human win");
				}
			}

		}

	}

	// --------------------------------------------------------------------
	public int convertToMatrixCol(int posX) {
		Gdx.app.log(TAG, "--> convertToMatrixCol");
		return Math.abs(posX) / Board.CELL_DIMENSION_PIXEL;
	}

	// --------------------------------------------------------------------
	public int convertToMatrixRow(int posY) {
		Gdx.app.log(TAG, "--> convertToMatrixRow");
		return Math.abs(posY - Board.CELL_DIMENSION_PIXEL
				* Board.MATRIX_DIMENSION)
				/ Board.CELL_DIMENSION_PIXEL;
	}
}
