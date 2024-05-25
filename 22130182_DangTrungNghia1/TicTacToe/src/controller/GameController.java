package controller;

import model.GameModelInterface;
import view.GameBoardViewInterface;

/**
 * A class for the controller of a TicTacToe game
 * 
 * @author Trung Nghia
 *
 */
public class GameController extends Controller {
//
	public GameController(GameModelInterface theModel, GameBoardViewInterface theView) {
		super(theModel, theView);
		theView.addTicTacToeListener(new TicTacToeListener(theModel, theView));
	}
}
