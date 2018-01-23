package qlearning.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Test {

	public static void main(String args[]) throws InterruptedException, FileNotFoundException {

		/*
		 * Simulation ql = new Simulation(); Robot robot = new Robot(1,4,1,false);
		 * ql.init(); ql.calculateQ(); ql.drawArena(robot); ql.result(robot);
		 */

		Arena arena = new Arena("resources/arena.txt", 26, 56);

		Robot[] redRobot = new Robot[3];
		redRobot[0] = new Robot(1, 4, 1, 0, 1);
		redRobot[1] = new Robot(1, 15, 1, 0, 1);
		redRobot[2] = new Robot(1, 21, 1, 0, 1);

		Robot[] blueRobot = new Robot[3];
		blueRobot[0] = new Robot(54, 4, 1, 1, 3);
		blueRobot[1] = new Robot(54, 10, 1, 1, 3);
		blueRobot[2] = new Robot(54, 21, 1, 1, 3);
		Game game = new Game(arena, redRobot, blueRobot);
		List<State> stateHistory = readStateHistory();
//		
//		long startTime = System.currentTimeMillis();
//	for (int i = 1; i <= 100; i++) {
//			System.out.println("current Simulation = " + i);
//			//
//			//
//			//
//			stateHistory = game.runSim(stateHistory);
//			//
//			
//		}
//		long endTime   = System.currentTimeMillis();
//		long totalTime = endTime - startTime;
//		
//	
//		startTime = System.currentTimeMillis();
//		System.out.println("Start writing, don't interrupt");
//		writeStateHistory(stateHistory);
//		System.out.println("done writing");
//		 endTime   = System.currentTimeMillis();
//		long totalTime2 = endTime - startTime;
//		System.out.println("Total time of simulation: " + totalTime);
//		System.out.println("Total time of writing: " + totalTime2);
		Scanner console = new Scanner(System.in);
		System.out.print("Start game? ");
		console.nextLine();
		game.run(stateHistory);
		console.close();

	}
	

	public static void writeStateHistory(List<State> stateHistory) throws FileNotFoundException {
		PrintStream output = new PrintStream(new File("resources/data.txt"));
		for (int i = 0; i < stateHistory.size(); i++) {
			for (int j = 0; j < 3; j++) {
				output.print(stateHistory.get(i).getRedRobot()[j].toString());

			}
			for (int j = 0; j < 3; j++) {
				output.print(stateHistory.get(i).getBlueRobot()[j].toString());

			}
			output.print(stateHistory.get(i).getPowerCubes().get('P') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('Q') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('Z') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('C') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('W') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('S') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('w') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('p') + " ");

			output.print(stateHistory.get(i).getPowerCubes().get('q') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('z') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('c') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('v') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('s') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('V') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('F') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('f') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('B') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('b') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('L') + " ");
			output.print(stateHistory.get(i).getPowerCubes().get('l') + " ");
			output.print(stateHistory.get(i).getTime() + " ");
			output.print(stateHistory.get(i).getCurrentTeam() + " ");
			output.print(stateHistory.get(i).getW() + " ");
			output.println(stateHistory.get(i).getN() + " N");
			
		}
	}

	public static List<State> readStateHistory() throws FileNotFoundException {
		List<State> stateHistory = new ArrayList<State>();
		;
		File file = new File("resources/dat.txt");
		Scanner input = new Scanner(file);
		while (input.hasNext()) {
			Robot[] currentRedRobot = new Robot[3];
			for (int i = 0; i < 3; i++) {
				int x = input.nextInt();
				int y = input.nextInt();
				int cube = input.nextInt();
				int color = input.nextInt();
				int direction = input.nextInt();

				currentRedRobot[i] = new Robot(x, y, cube, color, direction);
			}
			Robot[] currentBlueRobot = new Robot[3];
			for (int i = 0; i < 3; i++) {
				currentBlueRobot[i] = new Robot(input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(),
						input.nextInt());
			}
			Map<Character, Integer> powerCubes = new HashMap<Character, Integer>();
			powerCubes.put('P', input.nextInt());
			powerCubes.put('Q', input.nextInt());
			powerCubes.put('Z', input.nextInt());
			powerCubes.put('C', input.nextInt());
			powerCubes.put('W', input.nextInt());
			powerCubes.put('S', input.nextInt());
			powerCubes.put('w', input.nextInt());
			powerCubes.put('p', input.nextInt());
			powerCubes.put('q', input.nextInt());
			powerCubes.put('z', input.nextInt());
			powerCubes.put('c', input.nextInt());
			powerCubes.put('v', input.nextInt());
			powerCubes.put('s', input.nextInt());
			powerCubes.put('V', input.nextInt());
			powerCubes.put('F', input.nextInt());
			powerCubes.put('f', input.nextInt());
			powerCubes.put('B', input.nextInt());
			powerCubes.put('b', input.nextInt());
			powerCubes.put('L', input.nextInt());
			powerCubes.put('l', input.nextInt());

			stateHistory.add(new State(currentRedRobot, currentBlueRobot, powerCubes, input.nextInt(), input.nextInt(),
					input.nextInt(), input.nextInt()));
        input.next();
		}
		input.close();
		return stateHistory;
	}
}
