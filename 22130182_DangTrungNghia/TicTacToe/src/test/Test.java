package test;

import controller.Controller;
import controller.GameController;
import model.GameModel;
import model.GameModelInterface;
import view.GameBoardView;
import view.GameBoardViewInterface;

public class Test {

	private static GameBoardViewInterface theView;
	private static GameModelInterface theModel;
	private static Controller theController;

	public static void main(String args[]) {
		theView = new GameBoardView();
		theModel = new GameModel(3);
		theController = new GameController(theModel, theView);
	}
}
