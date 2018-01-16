package qlearning.main;

import java.util.Random;

public class Test {

	public static void main(String args[]) throws InterruptedException {
		
		
		/*Simulation ql = new Simulation();
		Robot robot = new Robot(1,4,1,false);
		ql.init();
		ql.calculateQ();
		ql.drawArena(robot);
		ql.result(robot);*/
		
		Arena arena = new Arena("resources/arena.txt", 26, 56);
		Robot robot = new Robot(26, 13, 1, false);
		int[] actions = new int[150];
		Random rand = new Random();
		for (int i = 0;i < 150;i++) { 
			actions[i] = rand.nextInt(4);
		}
		
		Game game = new Game(arena, robot);
		game.play(actions);

	}
}
