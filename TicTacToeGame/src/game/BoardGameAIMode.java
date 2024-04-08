package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

public class BoardGameAIMode extends JPanel {
	private static final int N = 3;
	private static final int M = 3;

	public static final int ST_DRAW = 0;
	public static final int ST_WIN = 1;
	public static final int ST_NORMAL = 2;

	// 0: Hòa (hết nước không ai thắng cả), 1: Player hiện tại thắng, 2: Player hiện
	// tại chưa thắng (còn nước đánh tiếp)

	private EndGameListener endGameListener;
	private Image imgX;
	private Image imgO;
	private Cell[][] matrix = new Cell[N][M];
	private String currentPlayer = Cell.EMPTY_VALUE;
	private boolean isHumanTurn = true;// Biến để xác định lượt của người chơi hoặc máy

	public BoardGameAIMode(String player) {
		this();
		this.currentPlayer = player;
	}

	public BoardGameAIMode() {
		this.initMatrix();

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!isHumanTurn || currentPlayer.equals(Cell.EMPTY_VALUE)) {
					return;
				}
				int x = e.getX();
				int y = e.getY();

				// phát ra âm thanh
				soundClick();

				// Xử lý đánh của người chơi
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < M; j++) {
						Cell cell = matrix[i][j];
						int cXStart = cell.getX();
						int cYStart = cell.getY();
						int cXEnd = cXStart + cell.getW();
						int cYEnd = cYStart + cell.getH();
						if (x >= cXStart && x <= cXEnd && y >= cYStart && y <= cYEnd) {
							if (cell.getValue().equals(Cell.EMPTY_VALUE)) {
								cell.setValue(currentPlayer);
								repaint();
								int result = checkWin(currentPlayer);
								if (endGameListener != null) {
									endGameListener.end(currentPlayer, result);
								}

								if (result == ST_NORMAL) {
									currentPlayer = currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;
									isHumanTurn = false; // Sau nước đi của người chơi, đến lượt của máy
									computerMove(); // Máy thực hiện nước đi của mình
								}
							}
						}
					}
				}
			}
		});

		try {
			imgX = ImageIO.read(getClass().getResource("/data/x.png"));
			imgO = ImageIO.read(getClass().getResource("/data/o.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void computerMove() {
		// Tìm nước đi tối ưu cho máy bằng thuật toán MiniMax
		Move bestMove = minimax(matrix, currentPlayer);

		// Đánh vào ô tối ưu
		matrix[bestMove.row][bestMove.col].setValue(currentPlayer);
		repaint();
		int result = checkWin(currentPlayer);
		if (endGameListener != null) {
			endGameListener.end(currentPlayer, result);
		}

		if (result == ST_NORMAL) {
			currentPlayer = currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;
			isHumanTurn = true; // Sau nước đi của máy, đến lượt của người chơi
		}
	}

	private Move minimax(Cell[][] board, String player) {
		int bestScore = Integer.MIN_VALUE;
		Move bestMove = new Move(-1, -1);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (board[i][j].getValue().equals(Cell.EMPTY_VALUE)) {
					board[i][j].setValue(player);
					int score = minimaxHelper(board, 0, false, player);
					board[i][j].setValue(Cell.EMPTY_VALUE);

					if (score > bestScore) {
						bestScore = score;
						bestMove.row = i;
						bestMove.col = j;
					}
				}
			}
		}

		return bestMove;
	}

	private int minimaxHelper(Cell[][] board, int depth, boolean isMaximizingPlayer, String player) {
		int result = checkWin(player);
		if (result == ST_WIN) {
			return 10;
		} else if (result == ST_DRAW) {
			return 0;
		}

		if (isMaximizingPlayer) {
			int bestScore = Integer.MIN_VALUE;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					if (board[i][j].getValue().equals(Cell.EMPTY_VALUE)) {
						board[i][j].setValue(player);
						int score = minimaxHelper(board, depth + 1, false, player);
						board[i][j].setValue(Cell.EMPTY_VALUE);
						bestScore = Math.max(score, bestScore);
					}
				}
			}
			return bestScore;
		} else {
			int bestScore = Integer.MAX_VALUE;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					if (board[i][j].getValue().equals(Cell.EMPTY_VALUE)) {
						String opponent = player.equals(Cell.X_VALUE) ? Cell.O_VALUE : Cell.X_VALUE;
						board[i][j].setValue(opponent);
						int score = minimaxHelper(board, depth + 1, true, player);
						board[i][j].setValue(Cell.EMPTY_VALUE);
						bestScore = Math.min(score, bestScore);
					}
				}
			}
			return bestScore;
		}
	}

	private synchronized void soundClick() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream audioInputStream = AudioSystem
							.getAudioInputStream(getClass().getResource("/data/âm Thanh.wav"));
					clip.open(audioInputStream);
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private void initMatrix() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				Cell cell = new Cell();
				matrix[i][j] = cell;
			}
		}
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void setEndGameListener(EndGameListener endGameListener) {
		this.endGameListener = endGameListener;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void reset() {
		this.initMatrix();
		this.setCurrentPlayer(Cell.EMPTY_VALUE);
		repaint();
	}

	// 0: Hòa (hết nước không ai thắng cả), 1: Player hiện tại thắng, 2: Player hiện
	// tại chưa thắng (còn nước đánh tiếp)
	public int checkWin(String player) {
		// Đường chéo thứ nhất
		if (this.matrix[0][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player)
				&& this.matrix[2][2].getValue().equals(player)) {
			return ST_WIN;
		}

		// Đường chéo thứ hai
		if (this.matrix[0][2].getValue().equals(player) && this.matrix[1][1].getValue().equals(player)
				&& this.matrix[2][0].getValue().equals(player)) {
			return ST_WIN;
		}

		// Dòng thứ 1
		if (this.matrix[0][0].getValue().equals(player) && this.matrix[0][1].getValue().equals(player)
				&& this.matrix[0][2].getValue().equals(player)) {
			return ST_WIN;
		}

		// Dòng thứ 2
		if (this.matrix[1][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player)
				&& this.matrix[1][2].getValue().equals(player)) {
			return ST_WIN;
		}

		// Dòng thứ 3
		if (this.matrix[2][0].getValue().equals(player) && this.matrix[2][1].getValue().equals(player)
				&& this.matrix[2][2].getValue().equals(player)) {
			return ST_WIN;
		}

		// Cột thứ 1
		if (this.matrix[0][0].getValue().equals(player) && this.matrix[1][0].getValue().equals(player)
				&& this.matrix[2][0].getValue().equals(player)) {
			return ST_WIN;
		}

		// Cột thứ 2
		if (this.matrix[0][1].getValue().equals(player) && this.matrix[1][1].getValue().equals(player)
				&& this.matrix[2][1].getValue().equals(player)) {
			return ST_WIN;
		}

		// Cột thứ 2
		if (this.matrix[0][2].getValue().equals(player) && this.matrix[1][2].getValue().equals(player)
				&& this.matrix[2][2].getValue().equals(player)) {
			return 1;
		}

		if (this.isFull()) {
			return ST_DRAW;
		}

		return ST_NORMAL;
	}

	private boolean isFull() {
		int number = N * M;

		int k = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				Cell cell = matrix[i][j];
				if (!cell.getValue().equals(Cell.EMPTY_VALUE)) {
					k++;
				}
			}
		}

		return k == number;
	}

	@Override
	public void paint(Graphics g) {
		int w = getWidth() / 3;
		int h = getHeight() / 3;

		Graphics2D graphic2d = (Graphics2D) g;
		graphic2d.setColor(new Color(33, 70, 94));
		graphic2d.fillRect(0, 0, getWidth(), getHeight());

		// Tăng độ dày cho viền
		Stroke oldStroke = graphic2d.getStroke();
		graphic2d.setStroke(new BasicStroke(3)); // Độ dày 3 pixel

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				int x = j * w;
				int y = i * h;

				// Cập nhật lại ma trận
				Cell cell = matrix[i][j];
				cell.setX(x);
				cell.setY(y);
				cell.setW(w);
				cell.setH(h);

				// Vẽ viền
				graphic2d.setColor(new Color(100, 139, 163));
				graphic2d.drawRect(x, y, w, h);

				if (cell.getValue().equals(Cell.X_VALUE)) {
					Image img = imgX;
					graphic2d.drawImage(img, x, y, w, h, this);
				} else if (cell.getValue().equals(Cell.O_VALUE)) {
					Image img = imgO;
					graphic2d.drawImage(img, x, y, w, h, this);
				}
			}
		}

		// Khôi phục lại độ dày ban đầu của viền
		graphic2d.setStroke(oldStroke);
	}

}
