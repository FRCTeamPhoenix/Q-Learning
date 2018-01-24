package monteCarloAlgorithm;

import java.util.List;

import simulation.Node;
import simulation.Simulation;

public class MonteCarlo {

	private Node parentNode;
	private final double c = 1;
	
	
	public  int findBestAction(Simulation sim, int n) {

		parentNode = new Node(sim.getCurrentNode());
		Node bestNode = new Node(parentNode);
		Node current;
		List<Integer> possibleActions = sim.possibleActions(parentNode.getRobots()[parentNode.getCurrentPlayer()]);
		double[] possibleActionValue = new double[possibleActions.size()];
		for (int i = 0; i < possibleActions.size(); i++) {
			int w = 0;
			for (int j = 0; j < n; j++) {
				current = new Node(parentNode);

				sim.updateNode(current, possibleActions.get(i));
				while (current.getTime() <= sim.getGameDuration()) {
					sim.updateNode(current, sim.createRandomAction(current.getRobots()[current.getCurrentPlayer()]));
				}
				if (parentNode.getCurrentPlayer() < sim.getRobotCount() / 2
						&& current.getRedScore() > current.getBlueScore()) {
					w++;

				} else if (parentNode.getCurrentPlayer() >= sim.getRobotCount() / 2
						&& current.getBlueScore() > current.getRedScore()) {
					w++;
				}
			}
			possibleActionValue[i] = 1.0 * w / n + c * Math.sqrt(Math.log(n * possibleActions.size()) / n);
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
