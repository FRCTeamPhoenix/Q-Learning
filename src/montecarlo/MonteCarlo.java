package montecarlo;

import java.util.List;

import simulation.State;
import simulation.Simulation;

public class MonteCarlo {

	private State parentState;
	private final double c = 1;
	
	
	public  int findBestAction(Simulation sim, int n) {

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
				if (parentState.getCurrentPlayer() < sim.getRobotCount() / 2) score += current.getRedScore();
				if (parentState.getCurrentPlayer() >= sim.getRobotCount() / 2) score += current.getBlueScore();
			}
			possibleActionValue[i] = 1.0 * w / n + c * Math.sqrt(Math.log(possibleActions.size())) + 1.0 * score / n;

			//possibleActionValue[i] = 1.0 * w / n + c * Math.sqrt(Math.log(n * possibleActions.size()) / n);
		}
		double bestActionValue = possibleActionValue[0];
		int bestAction = possibleActions.get(0);
		for (int i = 1; i < possibleActions.size();i++) {
			if (possibleActionValue[i] > bestActionValue) {
				bestActionValue = possibleActionValue[i];
				bestAction = possibleActions.get(i);
			}
		}
		return bestAction;

	}

}
