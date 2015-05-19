package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import sun.net.www.protocol.http.HttpURLConnection.TunnelState;

import com.badlogic.gdx.Gdx;

public class Board {

	public static final int MATRIX_DIMENSION = 8;
	public static final int CELL_DIMENSION_PIXEL = 64;
	public static final String MATRIX_X_TYPE = "x";
	public static final String MATRIX_O_TYPE = "o";
	public static final String MATRIX_STAR_TYPE = "*";
	public static final String MATRIX_BLANK_TYPE = "\' \'";

	public static final String TAG = Board.class.getName();
	public String[][] matrix;
	public String turn;
	public Point computerRace;

	public Board() {
		buildMatrix();
	}

	// FUNCTIOn
	// --------------------------------------------------------------------
	public void buildMatrix() {
		Gdx.app.log(TAG, "--> buildMatrix");
		matrix = new String[MATRIX_DIMENSION][MATRIX_DIMENSION];
		Random r = new Random();
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				// int type = r.nextInt() % 3;
				int type = 2;
				if (type == 0) {
					matrix[i][j] = MATRIX_O_TYPE;
				} else if (type == 1) {
					matrix[i][j] = MATRIX_X_TYPE;
				} else {
					matrix[i][j] = MATRIX_BLANK_TYPE;
				}
			}
		}
		// set default position
		matrix[0][MATRIX_DIMENSION - 1] = MATRIX_X_TYPE;
		matrix[MATRIX_DIMENSION - 1][0] = MATRIX_O_TYPE;

	}

	// --------------------------------------------------------------------
	public Point returnNextMove() {
		if (isGameOver()) {
			return null;
		}
		minimax(0, turn);
		return computerRace;
	}

	// --------------------------------------------------------------------

	public int minimax(int depth, String turn) {
		if (isComputerWin())
			return +1;
		if (isHumanWin())
			return -1;

		Point currentNode = getCurrentNode(turn);

		ArrayList<Point> pointsAvailable = canMove(currentNode);
		if (pointsAvailable.isEmpty())
			return 0;

		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

		for (int i = 0; i < pointsAvailable.size(); ++i) {
			Point point = pointsAvailable.get(i);
			String currentValue = matrix[currentNode.getX()][currentNode.getY()];
			String nextValue = matrix[point.getX()][point.getY()];
			if (turn == MATRIX_X_TYPE) {
				placeAMove(currentNode.getX(), currentNode.getY(),
						point.getX(), point.getY());
				int currentScore = minimax(depth + 1, MATRIX_O_TYPE);
				max = Math.max(currentScore, max);

				if (depth == 0)
					System.out.println("Score for position " + (i + 1) + " = "
							+ currentScore);
				if (currentScore >= 0) {
					if (depth == 0)
						computerRace = point;
				}
				if (currentScore == 1) {
					matrix[currentNode.getX()][currentNode.getY()] = currentValue;
					matrix[point.getX()][point.getY()] = nextValue;
					break;
				}
				if (i == pointsAvailable.size() - 1 && max < 0) {
					if (depth == 0)
						computerRace = point;
				}
			} else if (turn == MATRIX_O_TYPE) {
				placeAMove(currentNode.getX(), currentNode.getY(),
						point.getX(), point.getY());
				int currentScore = minimax(depth + 1, MATRIX_X_TYPE);
				min = Math.min(currentScore, min);
				if (min == -1) {
					matrix[currentNode.getX()][currentNode.getY()] = currentValue;
					matrix[point.getX()][point.getY()] = nextValue;
					break;
				}
			}
			matrix[point.getX()][point.getY()] = nextValue;
			// Reset this point
		}
		return turn == MATRIX_X_TYPE ? max : min;
	}

	// --------------------------------------------------------------------
	public void placeAMove(int prev_x, int prev_y, int next_x, int next_y) {
		String currentCell = matrix[prev_x][prev_y];
		matrix[prev_x][prev_y] = MATRIX_STAR_TYPE;
		matrix[next_x][next_y] = currentCell;
	}

	// --------------------------------------------------------------------
	public ArrayList<Point> canMove(Point point) {
		int x = point.getX();
		int y = point.getY();
		String Check;
		ArrayList<Point> listResult = new ArrayList<Point>();
		// check north-side
		for (int i = y + 1; i < Board.MATRIX_DIMENSION; i++) {
			if (matrix[x][i] == MATRIX_BLANK_TYPE)
				listResult.add(new Point(x, i));
			else
				break;

		}
		// check north-west-side
		for (int i = x + 1, j = y + 1; i < Board.MATRIX_DIMENSION
				&& j < Board.MATRIX_DIMENSION; i++, j++) {
			if (matrix[i][j] == MATRIX_BLANK_TYPE)
				listResult.add(new Point(i, j));
			else
				break;

		}
		// check west-side
		for (int i = x + 1; i < Board.MATRIX_DIMENSION; i++) {
			if (matrix[i][y] == MATRIX_BLANK_TYPE)
				listResult.add(new Point(i, y));
			else
				break;

		}
		// check south-west-side
		for (int i = x + 1, j = y - 1; i < Board.MATRIX_DIMENSION && j > -1; i++, j--) {
			if (matrix[i][j] == MATRIX_BLANK_TYPE)
				listResult.add(new Point(i, j));
			else
				break;

		}
		// check south-side
		for (int j = y - 1; j > -1; j--) {
			if (matrix[x][j] == MATRIX_BLANK_TYPE)
				listResult.add(new Point(x, j));
			else
				break;

		}
		// check east-south-side
		for (int i = x - 1, j = y - 1; i > -1 && j > -1; i--, j--) {
			if (matrix[i][j] == MATRIX_BLANK_TYPE)
				listResult.add(new Point(i, j));
			else
				break;

		}
		// check east-side
		for (int i = x - 1; i > -1; i--) {
			if (matrix[i][y] == MATRIX_BLANK_TYPE)
				listResult.add(new Point(i, y));
			else
				break;

		}
		// check east-north-side
		for (int i = x - 1, j = y + 1; i > -1 && j < Board.MATRIX_DIMENSION; i--, j++) {
			if (matrix[i][j] == MATRIX_BLANK_TYPE)
				listResult.add(new Point(i, j));
			else
				break;

		}

		return listResult;
	}

	// --------------------------------------------------------------------
	public boolean isGameOver() {
		if (isComputerWin() || isHumanWin()) {
			return true;
		}
		return false;
	}

	// --------------------------------------------------------------------
	public boolean isComputerWin() {
		if (turn == MATRIX_X_TYPE) {
			return false;
		}
		// human turn
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				if (matrix[i][j] == MATRIX_O_TYPE) {
					ArrayList<Point> moves = canMove(new Point(i, j));
					if (moves.size() > 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// --------------------------------------------------------------------
	public boolean isHumanWin() {
		if (turn == MATRIX_O_TYPE) {
			return false;
		}

		// computer race
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				if (matrix[i][j] == MATRIX_X_TYPE) {
					ArrayList<Point> moves = canMove(new Point(i, j));
					if (moves.size() > 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// --------------------------------------------------------------------
	public Point getCurrentNode(String turn) {
		Point point = null;
		outerloop: for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				if (matrix[i][j] == turn) {
					point = new Point(i, j);
					break outerloop;
				}
			}
		}
		return point;
	}

	// --------------------------------------------------------------------
	public static boolean exitsInList(ArrayList<Point> listPoint, Point point) {
		for (Point _point : listPoint) {
			if (_point.isEqual(point)) {
				return true;
			}
		}
		return false;
	}
	// --------------------------------------------------------------------
	public void printMatrix()
	{
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j]);
			}
		System.out.print("\n");
		}
	}

}
