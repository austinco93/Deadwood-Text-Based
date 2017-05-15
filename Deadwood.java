import java.util.*;
import java.lang.*;
import java.io.*;
/* Deadwood.java
 * Authors: Austin Corotan and Allison Anderson
 * Description: This program executes the flow methods from the board class. It will loop,
 * allowing players to take turns, until the the specified full amount of days have passed.
 */

public class Deadwood {
	//controller
	private static Scanner term = new Scanner(System.in);
	private static Board gameBoard = new Board();

	public static void main(String[] args) {
		/* Initialize Board */
		if(args.length != 3){
			System.err.println("Usage: java Deadwood <roomSetup.txt> <adjacency.txt> <sceneSetup.txt>");
			System.exit(1);
		}
		gameBoard.setupBoard(args[0], args[1], args[2]);
		gameBoard.initGame();
		gameBoard.distributeScenes();

		/* loop turns until specified full amount of days have passed */
		System.out.println();
		System.out.println("Thank you. Type \"menu\" for the list of command options");
		while(gameBoard.getDayNum()!= gameBoard.getDayTotal()+1){
			gameBoard.takeTurn();
			if(gameBoard.endDayCheck()){
				gameBoard.dayReset();
			}
		}
		gameBoard.scorePlayers();
	}
}
