package neuralnetwork;

import java.io.File;
import java.io.FileNotFoundException;

import montecarlo.MonteCarlo2;
import simulation.Arena;
import simulation.Robot;
import simulation.Simulation;

public class Main {

	public static void main(String[] args) throws FileNotFoundException{
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

		
		//long startTime = System.currentTimeMillis();

		Simulation sim = new Simulation(arena, robots);
		MonteCarlo2 monteCarlo = new MonteCarlo2();
		monteCarlo.generateRandomData(sim, 10, 10, new File("resources/data.txt"));
		System.out.println("done");
		Network net = new Network(56,28,20,Network.Cost.COST_QUADRATIC);
		TrainingDataLoader.loadTrainingData("resources/data.txt");
		net.sgd(TrainingDataLoader.training_data, 1000, 100, 0.1);
		String str = "1 4 1 0 2 1 15 1 0 1 1 21 1 0 1 54 4 1 1 3 54 10 1 1 3 54 21 1 1 3 7 7 10 6 0 0 0 7 7 10 6 0 0 0 0 0 0 0 0 0 2 1 0 0 0 0";
		String[] inputs = str.split(" ");
		Matrix inputMatrix = new Matrix(inputs.length,1);
		for(int i=0;i<inputs.length;i++)
			inputMatrix.put(i, 0, Double.parseDouble(inputs[i]));
		
		System.out.println("output: " + net.feedforward(inputMatrix).toString());
	};
	
}
