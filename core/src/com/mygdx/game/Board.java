package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;

public class Board {

	public enum TURN {
		COMPUTER_RACE, HUMAN_RACE,
		// com: x sprite;
		// human: o sprite;
	}

	public static final int MATRIX_DIMENSION = 4;
	public static final int CELL_DIMENSION_PIXEL = 32;
	public static final String TAG = Board.class.getName();
	public NodeSprite[][] matrix;
	public TURN turn;
	public NodeSprite computerRace;

	public Board() {
		buildMatrix();
	}

	// FUNCTIOn
	// --------------------------------------------------------------------
	public void buildMatrix() {
		Gdx.app.log(TAG, "--> buildMatrix");
		matrix = new NodeSprite[MATRIX_DIMENSION][MATRIX_DIMENSION];
		Random r = new Random();
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				NodeSprite nodeSprite;

				// int type = r.nextInt() % 3;
				int type = 0;
				if (type == 0) {
					nodeSprite = new NodeSprite(NodeSprite.BLANK_TYPE,
							CELL_DIMENSION_PIXEL, CELL_DIMENSION_PIXEL, i, j);
				} else if (type == 1) {
					nodeSprite = new NodeSprite(NodeSprite.X_TYPE,
							CELL_DIMENSION_PIXEL, CELL_DIMENSION_PIXEL, i, j);
				} else {
					nodeSprite = new NodeSprite(NodeSprite.O_TYPE,
							CELL_DIMENSION_PIXEL, CELL_DIMENSION_PIXEL, i, j);
				}

				matrix[i][j] = nodeSprite;
			}
		}
		NodeSprite xSprite = matrix[0][0];
		matrix[0][0] = xSprite.setType(NodeSprite.X_TYPE);

		NodeSprite oSprite = matrix[MATRIX_DIMENSION - 1][MATRIX_DIMENSION - 1];
		matrix[MATRIX_DIMENSION - 1][MATRIX_DIMENSION - 1] = oSprite
				.setType(NodeSprite.O_TYPE);

	}

	// --------------------------------------------------------------------
	public void setCellType(Point point, int type) {
		int x = point.getX();
		int y = point.getY();

		NodeSprite nodeSprite = matrix[x][y];
		nodeSprite.setType(NodeSprite.O_TYPE);
		matrix[x][y] = nodeSprite;
	}

	// --------------------------------------------------------------------
	public NodeSprite returnNextMove() {
		if (isGameOver()) {
			return null;
		}
		minimax(0, turn);
		return computerRace;
	}

	// --------------------------------------------------------------------
	public int minimax(int depth, TURN turn) {

		Gdx.app.log(TAG, "minimax --> depth: " + depth + " - turn: " + turn);

		if (isComputerWin())
			return +1;
		if (isHumanWin())
			return -1;

		ArrayList<NodeSprite> nodeAvailables = getAvailableMoves(turn);

		if (nodeAvailables.isEmpty())
			return 0;

		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

		for (int i = 0; i < nodeAvailables.size(); ++i) {
			NodeSprite nodeSprite = nodeAvailables.get(i);
			if (turn == TURN.COMPUTER_RACE) {
				NodeSprite computerMove = nodeSprite.setType(NodeSprite.X_TYPE);
				Point point = nodeSprite.getPoint();

				placeAMove(point, computerMove);

				int currentScore = minimax(depth + 1, TURN.HUMAN_RACE);
				max = Math.max(currentScore, max);

				if (depth == 0)
					System.out.println("Score for position " + (i + 1) + " = "
							+ currentScore);
				if (currentScore >= 0) {
					if (depth == 0)
						computerRace = computerMove;
				}
				if (currentScore == 1) {
					int x = point.getX();
					int y = point.getY();
					matrix[x][y] = nodeSprite.setType(NodeSprite.BLANK_TYPE);
					break;
				}
				if (i == nodeAvailables.size() - 1 && max < 0) {
					if (depth == 0)
						computerRace = computerMove;
				}
			} else if (turn == TURN.HUMAN_RACE) {
				NodeSprite humanMove = nodeSprite.setType(NodeSprite.O_TYPE);
				Point point = nodeSprite.getPoint();

				placeAMove(point, humanMove);

				int currentScore = minimax(depth + 1, TURN.COMPUTER_RACE);
				min = Math.min(currentScore, min);
				if (min == -1) {
					int x = point.getX();
					int y = point.getY();
					matrix[x][y] = nodeSprite.setType(NodeSprite.BLANK_TYPE);
					break;
				}
			}
			Point point = nodeSprite.getPoint();
			int x = point.getX();
			int y = point.getY();
			matrix[x][y] = nodeSprite.setType(NodeSprite.BLANK_TYPE);
			// Reset this point
		}
		return turn == TURN.COMPUTER_RACE ? max : min;
	}

	// --------------------------------------------------------------------
	public void placeAMove(Point point, NodeSprite nodeSprite) {
		int i = point.getX();
		int j = point.getY();
		matrix[i][j] = nodeSprite;
	}

	// --------------------------------------------------------------------
	public ArrayList<NodeSprite> canMove(NodeSprite nodeSprite) {
		Point point = nodeSprite.getPoint();
		int x = point.getX();
		int y = point.getY();
		NodeSprite nodeCheck;
		ArrayList<NodeSprite> listResult = new ArrayList<NodeSprite>();
		// check north-side
		for (int i = y + 1; i < Board.MATRIX_DIMENSION; i++) {
			nodeCheck = matrix[x][i];
			if (nodeCheck.getType() == NodeSprite.BLANK_TYPE)
				listResult.add(nodeCheck);
			else
				break;

		}
		// check north-west-side
		for (int i = x + 1, j = y + 1; i < Board.MATRIX_DIMENSION
				&& j < Board.MATRIX_DIMENSION; i++, j++) {
			nodeCheck = matrix[i][j];
			if (nodeCheck.getType() == NodeSprite.BLANK_TYPE)
				listResult.add(nodeCheck);
			else
				break;

		}
		// check west-side
		for (int i = x + 1; i < Board.MATRIX_DIMENSION; i++) {
			nodeCheck = matrix[i][y];
			if (nodeCheck.getType() == NodeSprite.BLANK_TYPE)
				listResult.add(nodeCheck);
			else
				break;

		}
		// check south-west-side
		for (int i = x + 1, j = y - 1; i < Board.MATRIX_DIMENSION && j > -1; i++, j--) {
			nodeCheck = matrix[i][j];
			if (nodeCheck.getType() == NodeSprite.BLANK_TYPE)
				listResult.add(nodeCheck);
			else
				break;

		}
		// check south-side
		for (int j = y - 1; j > -1; j--) {
			nodeCheck = matrix[x][j];
			if (nodeCheck.getType() == NodeSprite.BLANK_TYPE)
				listResult.add(nodeCheck);
			else
				break;

		}
		// check east-south-side
		for (int i = x - 1, j = y - 1; i > -1 && j > -1; i--, j--) {
			nodeCheck = matrix[i][j];
			if (nodeCheck.getType() == NodeSprite.BLANK_TYPE)
				listResult.add(nodeCheck);
			else
				break;

		}
		// check east-side
		for (int i = x - 1; i > -1; i--) {
			nodeCheck = matrix[i][y];
			if (nodeCheck.getType() == NodeSprite.BLANK_TYPE)
				listResult.add(nodeCheck);
			else
				break;

		}
		// check east-north-side
		for (int i = x - 1, j = y + 1; i > -1 && j < Board.MATRIX_DIMENSION; i--, j++) {
			nodeCheck = matrix[i][j];
			if (nodeCheck.getType() == NodeSprite.BLANK_TYPE)
				listResult.add(nodeCheck);
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
		if (turn == TURN.COMPUTER_RACE) {
			return false;
		}
		// human turn
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				NodeSprite nodeSprite = matrix[i][j];
				if (nodeSprite.getType() == NodeSprite.O_TYPE) {
					ArrayList<NodeSprite> moves = canMove(nodeSprite);
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
		if (turn == TURN.HUMAN_RACE) {
			return false;
		}

		// computer race
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				NodeSprite nodeSprite = matrix[i][j];
				if (nodeSprite.getType() == NodeSprite.X_TYPE) {
					ArrayList<NodeSprite> moves = canMove(nodeSprite);
					if (moves.size() > 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public ArrayList<NodeSprite> getAvailableMoves(TURN turn) {
		ArrayList<NodeSprite> moves = new ArrayList<NodeSprite>();
		for (int i = 0; i < MATRIX_DIMENSION; i++) {
			for (int j = 0; j < MATRIX_DIMENSION; j++) {
				if (turn == TURN.COMPUTER_RACE) {
					NodeSprite nodeSprite = matrix[i][j];
					if (nodeSprite.getType() == NodeSprite.X_TYPE) {
						ArrayList<NodeSprite> canMoves = canMove(nodeSprite);
						for (NodeSprite canMove : canMoves) {
							insertUnique(moves, canMove);
						}
					}

				} else if (turn == TURN.HUMAN_RACE) {
					NodeSprite nodeSprite = matrix[i][j];
					if (nodeSprite.getType() == NodeSprite.O_TYPE) {
						ArrayList<NodeSprite> canMoves = canMove(nodeSprite);
						for (NodeSprite canMove : canMoves) {
							insertUnique(moves, canMove);
						}
					}

				}
			}
		}
		return moves;
	}

	// --------------------------------------------------------------------
	public static void insertUnique(ArrayList<NodeSprite> arrayList,
			NodeSprite insertNode) {
		for (NodeSprite nodeSprite : arrayList) {
			if (nodeSprite.getPoint().isEqual(insertNode.getPoint())) {
				return;
			}
		}
		arrayList.add(insertNode);
	}

	// --------------------------------------------------------------------
	public static boolean exitsInList(ArrayList<NodeSprite> arrayList,
			NodeSprite insertNode) {
		for (NodeSprite nodeSprite : arrayList) {
			if (nodeSprite.getPoint().isEqual(insertNode.getPoint())) {
				return true;
			}
		}
		return false;
	}

}
