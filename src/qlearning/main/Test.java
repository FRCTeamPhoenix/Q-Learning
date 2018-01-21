package qlearning.main;

import java.util.Random;

public class Test {

	public static void main(String args[]) throws InterruptedException {

		/*
		 * Simulation ql = new Simulation(); Robot robot = new Robot(1,4,1,false);
		 * ql.init(); ql.calculateQ(); ql.drawArena(robot); ql.result(robot);
		 */

		Arena arena = new Arena("resources/arena.txt", 26, 56);

		Robot[] redRobot = new Robot[3];
		redRobot[0] = new Robot(1,4,1,0,1);
		 redRobot[1] = new Robot(1, 15, 1, 0,1);
		 redRobot[2] = new Robot(1, 21, 1, 0,1);

		Robot[] blueRobot = new Robot[3];
		blueRobot[0] = new Robot(54,4,1,1,3);
		blueRobot[1] = new Robot(54, 10, 1, 1, 3);
		 blueRobot[2] = new Robot(54, 21, 1, 1, 3);

		Game game = new Game(arena, redRobot, blueRobot);
		game.play();

	}
}
