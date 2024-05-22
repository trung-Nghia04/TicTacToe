package model;

public interface GameModelInterface {
	boolean doMove(int row, int col);

	char findWinner();

	boolean isTiedGame();

	char getTurn();

	void resetGame();

	boolean isEmptyCell(int row, int col);

	public void undoMove(int row, int col);
}
