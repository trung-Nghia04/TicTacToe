package view;

import java.awt.event.ActionListener;

public interface GameBoardViewInterface {
	void createBoard(int size);

	void addTicTacToeListener(ActionListener listener);

	void setButtonText(char c, int row, int col);

	void displayMessage(String message);

	void resetBoard();

//	public int getSizeToCreateBoard();

}
