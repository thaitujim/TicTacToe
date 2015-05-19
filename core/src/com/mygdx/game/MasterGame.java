package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

public class MasterGame extends ApplicationAdapter {

	public static final String TAG = MasterGame.class.getName();
	public static final boolean DEBUG_DRAW = false;
	public static final boolean DEBUG_UPDATE = false;

	public SpriteBatch batch;
	public Board board;
	public State state;
	public float winning_counter;
	public float ready_counter;

	public enum State {
		READY, PLAYING, COM_WIN, HUMAN_WIN,
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		ResourceManager.load();
		board = new Board();
		board.turn = Board.MATRIX_X_TYPE;
		state = State.READY;
		winning_counter = 0;
		ready_counter = 0;
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

		drawMatrix();
		if (state == State.READY) {
			ready_counter = ready_counter + 1;
			if (ready_counter > 3 * 60) {
				state = State.PLAYING;
			}
		} else if (state == State.PLAYING) {
			// drawMatrix();
		} else if (state == State.COM_WIN) {
			if (winning_counter < 3 * 60) {
				winning_counter = winning_counter + 1;
			} else {
				winning_counter = winning_counter + 1;
				if (winning_counter > 3 * 60 * 2) {
					winning_counter = 0;
				}
				batch.draw(ResourceManager.you_lose, 0, 0,
						Board.CELL_DIMENSION_PIXEL * Board.MATRIX_DIMENSION,
						Board.CELL_DIMENSION_PIXEL * Board.MATRIX_DIMENSION);

			}
		} else {

			if (winning_counter < 3 * 60) {
				winning_counter = winning_counter + 1;
			} else {
				winning_counter = winning_counter + 1;
				if (winning_counter > 3 * 60 * 2) {
					winning_counter = 0;
				}
				batch.draw(ResourceManager.you_win, 0, 0,
						Board.CELL_DIMENSION_PIXEL * Board.MATRIX_DIMENSION,
						Board.CELL_DIMENSION_PIXEL * Board.MATRIX_DIMENSION);

			}
		}

		batch.end();
	}

	private void drawMatrix() {
		String[][] matrix = board.matrix;
		int cell_width = Board.CELL_DIMENSION_PIXEL;
		int cell_height = Board.CELL_DIMENSION_PIXEL;
		for (int i = 0; i < Board.MATRIX_DIMENSION; i++) {
			for (int j = 0; j < Board.MATRIX_DIMENSION; j++) {
				String type = matrix[i][j];
				if (type == Board.MATRIX_BLANK_TYPE) {
					batch.draw(ResourceManager.cell_sprite, i * cell_width, j
							* cell_height, cell_width, cell_height);
				} else if (type == Board.MATRIX_X_TYPE) {
					batch.draw(ResourceManager.x_sprite, i * cell_width, j
							* cell_height, cell_width, cell_height);
				} else if (type == Board.MATRIX_O_TYPE) {
					batch.draw(ResourceManager.o_sprite, i * cell_width, j
							* cell_height, cell_width, cell_height);
				} else if (type == Board.MATRIX_STAR_TYPE) {
					batch.draw(ResourceManager.cell_sprite_gray,
							i * cell_width, j * cell_height, cell_width,
							cell_height);
				}

			}
		}
	}

	// --------------------------------------------------------------------
	public void update() {
		if (DEBUG_UPDATE) {
			Gdx.app.log(TAG, "--> update");
		}
		if (state != State.PLAYING) {
			return;
		}

		if (board.turn == Board.MATRIX_O_TYPE) {

			if (board.isComputerWin()) {
				Gdx.app.log(TAG, "game over --> computer win");
				state = State.COM_WIN;
			} else if (board.isHumanWin()) {
				Gdx.app.log(TAG, "game over --> human win");
				state = State.HUMAN_WIN;
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

					// NodeSprite cell = board.matrix[cellX][cellY];
					Point currentPoint = board.getCurrentNode(board.turn);
					Point nextPoint = new Point(cellX, cellY);

					ArrayList<Point> nodeAvailables = board
							.canMove(currentPoint);
					if (Board.exitsInList(nodeAvailables, nextPoint)) {
						board.placeAMove(currentPoint.getX(),
								currentPoint.getY(), nextPoint.getX(),
								nextPoint.getY());
						board.turn = Board.MATRIX_X_TYPE;
					}
					//
					Gdx.app.log(TAG, "--> node x-y: " + cellX + "-" + cellY
							+ " updated");
				}
			}
		} else if (board.turn == Board.MATRIX_X_TYPE) {
			Point next = board.returnNextMove();
			if (next != null) {
				board.printMatrix();
				int i = next.getX();
				int j = next.getY();
				board.matrix[i][j] = Board.MATRIX_X_TYPE;
				board.turn = Board.MATRIX_O_TYPE;
			} else {
				if (board.isComputerWin()) {
					Gdx.app.log(TAG, "game over --> computer win");
					state = State.COM_WIN;
				} else {
					Gdx.app.log(TAG, "game over --> human win");
					state = State.HUMAN_WIN;
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
