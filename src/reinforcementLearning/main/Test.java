package reinforcementLearning.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import montecarlo.MonteCarlo;
import simulation.Arena;
import simulation.State;
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
		long startTime = System.currentTimeMillis();

		Simulation sim = new Simulation(arena, robots);
		MonteCarlo monteCarlo = new MonteCarlo();
		sim.setCurrentState(new State(sim.getInitState()));
		int[] actions = new int[5400];
		Scanner input = new Scanner(new File("resources/actions.txt"));
		for (int i = 0;i < actions.length;i++) {
			actions[i] = input.nextInt();
		}
		input.close();
		sim.drawState(sim.getCurrentState());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (sim.getCurrentState().getTime() <= sim.getGameDuration()) {
			
			if (sim.getCurrentState().getTime() % (sim.getRobotCount()) == 1) {
				sim.drawState(sim.getCurrentState());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			sim.updateState(sim.getCurrentState(), actions[sim.getCurrentState().getTime() - 1] );
			
		}
		sim.drawState(sim.getCurrentState());
		System.out.println(sim.getCurrentState());
		
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total time of simulation: " + totalTime / 1000);

	}

}
