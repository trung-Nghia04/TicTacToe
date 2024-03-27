package windows;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.BoardGame;
import game.Cell;
import game.EndGameListener;

public class TicTacToeWindow extends JFrame {
	private static int sec = 0;
	private static Timer timer = new Timer();
	private static JLabel lblTime;
	private static JButton btnStart;
	private static BoardGame board;
	private JPanel jPanel;
	private BoxLayout boxLayout;
	private Dimension dimension;
	private JPanel bottomPanel;

	public TicTacToeWindow() {
		init();
	}

	public void init() {
		board = new BoardGame();
		board.setEndGameListener(new EndGameListener() {
			@Override
			public void end(String player, int st) {
				if (st == BoardGame.ST_WIN) {
					JOptionPane.showMessageDialog(null, "Người chơi " + player + " thắng");
					stopGame();
				} else if (st == BoardGame.ST_DRAW) {
					JOptionPane.showMessageDialog(null, "Hòa rồi");
					stopGame();
				}
			}
		});

		jPanel = new JPanel();
		boxLayout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
		jPanel.setLayout(boxLayout);
		board.setPreferredSize(new Dimension(300, 300));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);
		bottomPanel = new JPanel();
		bottomPanel.setLayout(flowLayout);
		btnStart = new JButton("Start");
		// Timer và TimerTask
		lblTime = new JLabel("0:0");
		bottomPanel.add(lblTime);
		bottomPanel.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnStart.getText().equals("Start")) {
					startGame();
				} else {
					stopGame();
				}
			}
		});

		jPanel.add(board);
		jPanel.add(bottomPanel);
		dimension = Toolkit.getDefaultToolkit().getScreenSize();

		this.setTitle("TicTacToe Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.add(jPanel);
		this.setSize(300, 300);
		this.setLocationRelativeTo(null);
		this.pack();
		URL urlIcon = TicTacToeWindow.class.getResource("/data/iconTicTacToe.jpg");
		Image img = Toolkit.getDefaultToolkit().createImage(urlIcon);
		this.setIconImage(img);
		// show ra frame
		this.setVisible(true);
	}

	private static void startGame() {
		// Hỏi ai đi trước
		int choice = JOptionPane.showConfirmDialog(null, "Người chơi O đi trước đúng không?", "Ai là người đi trước?",
				JOptionPane.YES_NO_OPTION);
		board.reset();
		String currentPlayer = (choice == 0) ? Cell.O_VALUE : Cell.X_VALUE;
		board.setCurrentPlayer(currentPlayer);

		// Time
		sec = 0;
		lblTime.setText("0:0");
		timer.cancel();
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sec++;
				String value = sec / 60 + " : " + sec % 60;
				lblTime.setText(value);
			}
		}, 1000, 1000);

		btnStart.setText("Stop");
	}

	private static void stopGame() {
		btnStart.setText("Start");

		sec = 0;
		lblTime.setText("0:0");

		timer.cancel();
		timer = new Timer();

		board.reset();
	}

}
