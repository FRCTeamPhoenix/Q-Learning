package montecarlo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import simulation.Simulation;
import simulation.State;

public class MonteCarlo2 {

	private State parentState;
	private final double c = 1;

	public void generateRandomData(Simulation sim, int nOfGames, int n, File file) throws FileNotFoundException {
		
		PrintStream output = new PrintStream(file);
		for (int i = 0; i < nOfGames; i++) {
			State[] stateHistory = new State[5400];
			double[][] outputPI = new double[5400][10];
			double[][] outputZ = new double[5400][10];
			
			
			
			State currentState = new State(sim.getInitState());
			stateHistory[currentState.getTime() - 1] = currentState;
			//System.out.println(stateHistory);
			while (currentState.getTime() < 5400) {
				if (currentState.getTime() % 200 == 1) {
					System.out.println("game: " + i + " time: " + (currentState.getTime()-1) );
				}
				List<Integer> possibleActions = sim
						.possibleActions(currentState.getRobots()[currentState.getCurrentPlayer()]);

				for (int j = 0; j < 10; j++) {
					if (possibleActions.contains(j)) {
						for (int k = 0; k < n; k++) {
							State newState = new State(currentState);
							sim.updateState(newState,
									sim.createRandomAction(newState.getRobots()[newState.getCurrentPlayer()]));
							sim.setCurrentState(new State(newState));
							sim.playRandom();
							//System.out.println(sim.getCurrentState());
							if (sim.getCurrentState().getBlueScore() > sim.getCurrentState().getRedScore() && currentState.getCurrentPlayer() >= 3) {
								outputZ[currentState.getTime() - 1][j] += 1.0 / 3 / n;
							}
							else if (sim.getCurrentState().getBlueScore() < sim.getCurrentState().getRedScore()&& currentState.getCurrentPlayer() < 3) {
								outputZ[currentState.getTime() - 1][j] += 1.0 / 3 / n;
							}
							else if (sim.getCurrentState().getBlueScore() == sim.getCurrentState().getRedScore()) {
								outputZ[currentState.getTime() - 1][j] = 0;
							}
							else {
								outputZ[currentState.getTime() - 1][j] += -1.0 / 3 / n;
							}
							
						}
						outputPI[currentState.getTime() - 1][j] = 1.0 / possibleActions.size();
					}
				}
				sim.updateState(currentState,
						sim.createRandomAction(currentState.getRobots()[currentState.getCurrentPlayer()]));
				stateHistory[currentState.getTime() - 1] = new State(currentState);
			}
			
			for (int j = 0;j < 5400;j++) {
				output.print(stateHistory[j].toData() + ";");
				output.print(outputZ[j][0]);

				for (int k = 1;k < 10;k++) {
					output.print(" " + outputZ[j][k]) ;
				}
				for (int k = 0;k < 10;k++) {
					output.print(" " + outputPI[j][k]) ;
				}
				output.println();
			}
			
			
		}
		output.close();
	}

	public int findBestAction(Simulation sim, int n) {

		parentState = new State(sim.getCurrentState());
		State current;
		List<Integer> possibleActions = sim.possibleActions(parentState.getRobots()[parentState.getCurrentPlayer()]);
		double[] possibleActionValue = new double[possibleActions.size()];
		for (int i = 0; i < possibleActions.size(); i++) {
			int w = 0;
			int score = 0;
			for (int j = 0; j < n; j++) {
				current = new State(parentState);

				sim.updateState(current, possibleActions.get(i));
				while (current.getTime() <= sim.getGameDuration()) {
					sim.updateState(current, sim.createRandomAction(current.getRobots()[current.getCurrentPlayer()]));
				}
				if (parentState.getCurrentPlayer() < sim.getRobotCount() / 2
						&& current.getRedScore() > current.getBlueScore()) {
					w++;

				} else if (parentState.getCurrentPlayer() >= sim.getRobotCount() / 2
						&& current.getBlueScore() > current.getRedScore()) {
					w++;

				}
				if (parentState.getCurrentPlayer() < sim.getRobotCount() / 2)
					score += current.getRedScore();
				if (parentState.getCurrentPlayer() >= sim.getRobotCount() / 2)
					score += current.getBlueScore();
			}
			possibleActionValue[i] = 1.0 * w / n + c * Math.sqrt(Math.log(possibleActions.size())) + 1.0 * score / n;

			// possibleActionValue[i] = 1.0 * w / n + c * Math.sqrt(Math.log(n *
			// possibleActions.size()) / n);
		}
		double bestActionValue = possibleActionValue[0];
		int bestAction = possibleActions.get(0);
		for (int i = 1; i < possibleActions.size(); i++) {
			if (possibleActionValue[i] > bestActionValue) {
				bestActionValue = possibleActionValue[i];
				bestAction = possibleActions.get(i);
			}
		}
		return bestAction;

	}

}
