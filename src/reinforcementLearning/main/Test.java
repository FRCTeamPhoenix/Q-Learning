package reinforcementLearning.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import monteCarloAlgorithm.MonteCarlo;
import simulation.Arena;
import simulation.Robot;
import simulation.Simulation;

public class Test {

	public static void main(String args[]) throws InterruptedException, FileNotFoundException {

		Arena arena = new Arena("resources/arena.txt", 26, 56);
		Robot[] robots = new Robot[6];
		// Red robot starting state
		robots[0] = new Robot(1, 4, 1, 0, 1);
		robots[1] = new Robot(1, 15, 1, 0, 1);
		robots[2] = new Robot(1, 21, 1, 0, 1);
		// Blue robot starting state
		robots[3] = new Robot(54, 4, 1, 1, 3);
		robots[4] = new Robot(54, 10, 1, 1, 3);
		robots[5] = new Robot(54, 21, 1, 1, 3);

		//
		Simulation sim = new Simulation(arena, robots);
		MonteCarlo monteCarlo = new MonteCarlo();
		sim.drawNode(sim.getCurrentNode());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (sim.getCurrentNode().getTime() <= sim.getGameDuration()) {
			if (sim.getCurrentNode().getTime() % (sim.getRobotCount()) == 1) {
				sim.drawNode(sim.getCurrentNode());
				
			}

			if (sim.getCurrentNode().getCurrentPlayer() < 3) sim.updateNode(sim.getCurrentNode(), monteCarlo.findBestAction(sim, 10));
			else sim.updateNode(sim.getCurrentNode(), sim.createRandomAction(sim.getCurrentNode().getRobots()[sim.getCurrentNode().getCurrentPlayer()]));
		}

	}

}
