package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import model.AI;
import model.GameModelInterface;
import view.GameBoardViewInterface;

public class TicTacToeListener extends Controller implements ActionListener {
	private char humanPlayer = 'X'; // Người chơi là 'X'
	private char aiPlayerChar = 'O'; // AI là 'O'

	public TicTacToeListener(GameModelInterface theModel, GameBoardViewInterface theView) {
		super(theModel, theView);
		this.aiPlayer = new AI(theModel, aiPlayerChar, humanPlayer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		int row = (int) btn.getClientProperty("row");
		int col = (int) btn.getClientProperty("col");
		if (theModel.getTurn() == humanPlayer && theModel.doMove(row, col)) {
			theView.setButtonText(humanPlayer, row, col);
			checkGameOver();
			aiTurn(); // Gọi lượt của AI sau khi người chơi đánh
		}
	}

	private void aiTurn() {
		if (theModel.getTurn() == aiPlayerChar && !theModel.isBoardFull()) { // Kiểm tra lượt và bàn cờ chưa đầy
			int[] move = aiPlayer.makeMove();
			int row = move[0];
			int col = move[1];

			if (theModel.doMove(row, col)) {
				theView.setButtonText(aiPlayerChar, row, col);
				checkGameOver();
			} else {
				aiTurn(); // Gọi đệ quy nếu nước đi không hợp lệ (thử lại)
			}
		}
	}

	private void checkGameOver() {
		char winner = theModel.findWinner();
		if (winner != '\0') {
			theView.displayMessage("Player " + winner + " has won!");
			resetGame();
		} else if (theModel.isTiedGame()) {
			theView.displayMessage("Draw!");
			resetGame();
		}
	}

	private void resetGame() {
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				Thread.sleep(2000); // Delay 1 giây
				return null;
			}

			@Override
			protected void done() {
				theModel.resetGame();
				theView.resetBoard();
				// Thêm logic khác sau khi reset nếu cần (ví dụ: thông báo bắt đầu ván mới)
			}
		}.execute();
	}

}
