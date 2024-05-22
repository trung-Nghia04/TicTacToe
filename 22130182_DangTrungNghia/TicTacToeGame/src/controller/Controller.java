package controller;

import model.AI;
import model.GameModelInterface;
import view.GameBoardViewInterface;
import view.GameBoardViewInterface;

public abstract class Controller {
	protected GameModelInterface theModel;
	protected GameBoardViewInterface theView;
	protected AI aiPlayer;

	public Controller(GameModelInterface theModel, GameBoardViewInterface theView) {
		this.theModel = theModel;
		this.theView = theView;
	}
}
