package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameBoardView extends JFrame implements GameBoardViewInterface {

	private BoardPanel boardPanel;
	private JLabel messageLabel; // Thêm label để hiển thị thông báo

	public GameBoardView() {
//		int size = getSizeToCreateBoard();
		boardPanel = new BoardPanel(3);
		setTitle("Tic Tac Toe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		messageLabel = new JLabel(); // Khởi tạo messageLabel
		add(messageLabel, BorderLayout.SOUTH); // Thêm messageLabel vào JFrame

		// Lấy kích thước bàn cờ từ người dùng và tạo bàn cờ
		add(boardPanel, BorderLayout.CENTER); // Thêm boardPanel vào JFrame

		pack();
		centerWindow();
		setVisible(true);
	}

	private void centerWindow() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
	}

	@Override
	public void createBoard(int size) {
		boardPanel.createBoard(size);
	}

	@Override
	public void addTicTacToeListener(ActionListener listener) {
		boardPanel.addTicTacToeListener(listener);
	}

	@Override
	public void setButtonText(char c, int row, int col) {
		boardPanel.setButtonText(c, row, col);
	}

	@Override
	public void displayMessage(String message) {
		messageLabel.setText(message);
		JOptionPane.showMessageDialog(this, message);
	}

	@Override
	public void resetBoard() {
		boardPanel.resetBoard();
	}

//	public int getSizeToCreateBoard() {
//		int size = 0; // Khởi tạo size
//		do {
//			try {
//				String inputSize = JOptionPane
//						.showInputDialog("Vui lòng nhập kích thước của bảng Tic Tac Toe (lớn hơn 2):");
//				size = Integer.parseInt(inputSize);
//				if (size <= 2) {
//					JOptionPane.showMessageDialog(this, "Kích thước phải lớn hơn 2.");
//				}
//			} catch (NumberFormatException e) {
//				JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên.");
//			}
//		} while (size <= 2); // Tiếp tục yêu cầu nhập liệu nếu kích thước không hợp lệ
//
//		return size;
//	}

}
