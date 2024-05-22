package test;

import controller.Controller;
import controller.GameController;
import model.GameModel;
import model.GameModelInterface;
import view.GameBoardView;
import view.GameBoardViewInterface;

public class Test {
	public static void main(String args[]) {
		GameBoardViewInterface theView = new GameBoardView();
		GameModelInterface theModel = new GameModel(3);
		Controller theController = new GameController(theModel, theView);
	}
}
