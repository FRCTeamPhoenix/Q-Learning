package qlearning.main;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;

public class Simulation {

	private final double alpha = 0.1; // Learning rate
	private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future

	private final int arenaWidth = 56;
	private final int arenaHeight = 26;
	private final int statesCount = arenaHeight * arenaWidth;
	private final int reward = 100;
	private final int penalty = -1000;
	private final int time = 1;
	private final int robotState = 2;

	private char[][] arena; // arena read from file
	private int[][][][] R; // Reward lookup
	private double[][][][] Q; // Q learning

	// GUI
	private int size = 20;
	DrawingPanel panel = new DrawingPanel(arenaWidth * size, arenaHeight * size);
	private Graphics g = panel.getGraphics();

	public void result(Robot rob) throws InterruptedException {

		drawArena(rob);
		Thread.sleep(3000);
		int crtTime = 0;

		while (crtTime < 250) {
			updateArena(crtTime, rob);
			drawArena(rob);
			Thread.sleep(250);
			crtTime++;
		}
	}

	public int currentState() {
		int index = 0;
		for (int i = 0; i < arenaHeight; i++) {
			for (int j = 0; j < arenaWidth; j++) {
				if (arena[i][j] == 'R') {
					index = i * arenaWidth + j;
				}
			}
		}
		return index;
	}

	public void updateArena(int t, Robot rob) {
		int crtState = rob.getY() * arenaWidth + rob.getX();
		int nxtState = getPolicyFromState(crtState, t, rob.getCube());
		if (arena[rob.getY()][rob.getX()] == 'W' || arena[rob.getY()][rob.getX()] == 'S') {
			rob.setCube(0);
		}
		if (arena[rob.getY()][rob.getX()] == 'Z' || arena[rob.getY()][rob.getX()] == 'P'
				|| arena[rob.getY()][rob.getX()] == 'C') {
			rob.setCube(1);
		}
		rob.setY(nxtState / arenaWidth);
		rob.setX(nxtState - nxtState / arenaWidth * arenaWidth);

	}

	public boolean win() {
		for (int i = 0; i < arenaHeight; i++) {
			for (int j = 0; j < arenaWidth; j++) {
				if (arena[i][j] == 'W') {
					return true;
				}
			}
		}
		return false;
	}

	public void drawArena(Robot rob) {
		for (int i = 0; i < arenaHeight; i++) {
			for (int j = 0; j < arenaWidth; j++) {

				if (arena[i][j] == 'P' || arena[i][j] == 'E' || arena[i][j] == 'A' || arena[i][j] == 'H') {
					g.setColor(Color.RED);
				} else if (arena[i][j] == 'p' || arena[i][j] == 'e' || arena[i][j] == 'a' || arena[i][j] == 'h') {
					g.setColor(Color.BLUE);
				} else if (arena[i][j] == 'L' || arena[i][j] == 'l') {
					g.setColor(Color.BLACK);
				} else if (arena[i][j] == 'W' || arena[i][j] == 'w' || arena[i][j] == 'T' || arena[i][j] == 'S') {
					g.setColor(Color.GRAY);
				} else if (arena[i][j] == 'Z' || arena[i][j] == 'z' || arena[i][j] == 'C' || arena[i][j] == 'c') {
					g.setColor(Color.ORANGE);
				} else if (arena[i][j] == 'N') {
					g.setColor(Color.YELLOW);
				}
				if (arena[i][j] != '0') {
					g.fillRect(j * size, i * size, size, size);
				}

			}
		}
		g.fillRect(rob.getX() * size, rob.getY() * size, size, size);
	}

	public void init() {
		File file = new File("resources/arena.txt");

		R = new int[statesCount][statesCount][time][robotState];
		Q = new double[statesCount][statesCount][time][robotState];
		arena = new char[arenaHeight][arenaWidth];

		try (FileInputStream fis = new FileInputStream(file)) {

			int i = 0;
			int j = 0;

			int content;
			System.out.println("Start reading file");
			// Read the arena from the input file
			while ((content = fis.read()) != -1) {
				char c = (char) content;
				if (c != 'C' && c != 'c' && c != 'A' && c != 'a' && c != 'H' && c != 'h' && c != 'L' && c != 'l'
						&& c != 'P' && c != 'p' && c != 'Z' && c != 'z' && c != 'W' && c != 'w' && c != 'E' && c != 'e'
						&& c != 'S' && c != 'T' && c != 'N' && c != '0') {
					continue;
				}
				arena[i][j] = c;
				j++;
				if (j == arenaWidth) {
					j = 0;
					i++;
				}
			}
			System.out.println("done reading arena");
			// Fill in the reward matrix with -1
			for (int t = 0; t < time; t++) {
				for (int k = 0; k < statesCount; k++) {
					for (int s = 0; s < statesCount; s++) {
						R[k][s][t][0] = -1;
						R[k][s][t][1] = -1;
					}
				}
			}
			System.out.println("done filling reward matrix with -1");
			// We will navigate through the reward matrix R using k index
			for (int t = 0; t < time; t++) {
				for (int k = 0; k < statesCount; k++) {

					// We will navigate with i and j through the arena, so we need
					// to translate k into i and j
					i = k / arenaWidth;
					j = k - i * arenaWidth;
					// If not in final state or a wall try moving in all directions in the arena
					for (int r = 0; r < robotState; r++) {
						// Try to move left in the arena
						int goLeft = j - 1;
						if (goLeft >= 0) {
							int target = i * arenaWidth + goLeft;
							if (arena[i][goLeft] == '0' || arena[i][goLeft] == 'H' || arena[i][goLeft] == 'N'
									|| arena[i][goLeft] == 'h' || arena[i][goLeft] == 'L' || arena[i][goLeft] == 'l') {
								R[k][target][t][r] = 0;
							} else if (arena[i][goLeft] == 'Z' || arena[i][goLeft] == 'C' || arena[i][goLeft] == 'P') {
								if (r == 0) {
									R[k][target][t][r] = reward;
								}
							} else if (arena[i][goLeft] == 'W' || arena[i][goLeft] == 'S') {
								if (r == 1) {
									R[k][target][t][r] = reward;
								}
							} else {
								R[k][target][t][r] = -10;
							}
						}
						int goRight = j + 1;
						if (goRight < arenaWidth) {
							int target = i * arenaWidth + goRight;
							if (arena[i][goRight] == '0' || arena[i][goRight] == 'H' || arena[i][goRight] == 'N'
									|| arena[i][goRight] == 'h' || arena[i][goRight] == 'L'
									|| arena[i][goRight] == 'l') {
								R[k][target][t][r] = 0;
							} else if (arena[i][goRight] == 'Z' || arena[i][goRight] == 'C'
									|| arena[i][goRight] == 'P') {
								if (r == 0) {
									R[k][target][t][r] = reward;
								}
							} else if (arena[i][goRight] == 'W' || arena[i][goRight] == 'S') {
								if (r == 1) {
									R[k][target][t][r] = reward;
								}
							} else {
								R[k][target][t][r] = -10;
							}

						}
						int goUp = i - 1;
						if (goUp >= 0) {
							int target = goUp * arenaWidth + j;
							if (arena[goUp][j] == '0' || arena[goUp][j] == 'H' || arena[goUp][j] == 'N'
									|| arena[goUp][j] == 'h' || arena[goUp][j] == 'L' || arena[goUp][j] == 'l') {
								R[k][target][t][r] = 0;
							} else if (arena[goUp][j] == 'Z' || arena[goUp][j] == 'C' || arena[goUp][j] == 'P') {
								if (r == 0) {
									R[k][target][t][r] = reward;
								}
							} else if (arena[goUp][j] == 'W' || arena[goUp][j] == 'S') {
								if (r == 1) {
									R[k][target][t][r] = reward;
								}
							} else {
								R[k][target][t][r] = -10;
							}

						}
						int goDown = i + 1;
						if (goDown < arenaHeight) {
							int target = goDown * arenaWidth + j;
							if (arena[goDown][j] == '0' || arena[goDown][j] == 'H' || arena[goDown][j] == 'N'
									|| arena[goDown][j] == 'h' || arena[goDown][j] == 'L' || arena[goDown][j] == 'l') {
								R[k][target][t][r] = 0;
							} else if (arena[goDown][j] == 'Z' || arena[goDown][j] == 'C' || arena[goDown][j] == 'P') {
								if (r == 0) {
									R[k][target][t][r] = reward;
								}
							} else if (arena[goDown][j] == 'W' || arena[goDown][j] == 'S') {
								if (r == 1) {
									R[k][target][t][r] = reward;
								}
							} else {
								R[k][target][t][r] = -10;
							} 

						}

					}
				}
			}
			System.out.println("done creating reward matrix");

			// printR(R);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public char[][] getarena() {
		return arena;
	}

	public void setarena(char[][] arena) {
		this.arena = arena;
	}

	public int[][][][] getR() {
		return R;
	}

	public void setR(int[][][][] r) {
		R = r;
	}

	public double[][][][] getQ() {
		return Q;
	}

	public void setQ(double[][][][] q) {
		Q = q;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getGamma() {
		return gamma;
	}

	public int getarenaWidth() {
		return arenaWidth;
	}

	public int getarenaHeight() {
		return arenaHeight;
	}

	public int getStatesCount() {
		return statesCount;
	}

	public int getReward() {
		return reward;
	}

	public int getPenalty() {
		return penalty;
	}

	// Used for debug
	void printR(int[][] matrix) {
		System.out.printf("%25s", "States: ");
		for (int i = 0; i <= 8; i++) {
			System.out.printf("%4s", i);
		}
		System.out.println();

		for (int i = 0; i < statesCount; i++) {
			System.out.print("Possible states from " + i + " :[");
			for (int j = 0; j < statesCount; j++) {
				System.out.printf("%4s", matrix[i][j]);
			}
			System.out.println("]");
		}
	}

	void calculateQ() {
		Random rand = new Random();
		for (int r = 0; r < 2; r++) {
			for (int i = 0; i < 1000; i++) { // Train cycles
				// Select random initial state
				int crtState = rand.nextInt(statesCount);
				int crtTime = 0;
				int t = 0;
				while (t < 250) {
					int[] actionsFromCurrentState = possibleActionsFromState(crtState, 0, r);

					// Pick a random action from the ones possible
					int index = rand.nextInt(actionsFromCurrentState.length);
					int nextState = actionsFromCurrentState[index];

					// Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma *
					// Max(next state, all actions) - Q(state,action))
					double q = Q[crtState][nextState][crtTime][r];
					double maxQ = maxQ(nextState, crtTime, r);
					int currentReward = R[crtState][nextState][crtTime][r];

					double value = q + alpha * (currentReward + gamma * maxQ - q);
					Q[crtState][nextState][crtTime][r] = value;

					crtState = nextState;
					t++;
				}
			}
		}
	}

	boolean isFinalState(int state) {
		int i = state / arenaWidth;
		int j = state - i * arenaWidth;

		return arena[i][j] == 'F';
	}

	int[] possibleActionsFromState(int state, int t, int r) {
		HashSet<Integer> result = new HashSet<>();
		if (state != 0)
			result.add(state - 1);
		if (state != statesCount - 1)
			result.add(state + 1);
		for (int i = 0; i < statesCount; i++) {
			if (R[state][i][0][r] != -1) {
				result.add(i);
			}
		}

		return result.stream().mapToInt(i -> i).toArray();
	}

	double maxQ(int nextState, int t, int r) {
		int[] actionsFromState = possibleActionsFromState(nextState, t, r);
		double maxValue = Double.MIN_VALUE;
		for (int nextAction : actionsFromState) {
			double value = Q[nextState][nextAction][0][r];

			if (value > maxValue)
				maxValue = value;
		}
		return maxValue;
	}

	void printPolicy() {
		System.out.println("\nPrint policy");
		for (int r = 0; r < robotState; r++) {
			for (int t = 0; t < time; t++) {
				for (int i = 0; i < statesCount; i++) {
					System.out.println("From state " + i + " goto state " + getPolicyFromState(i, t, r));
				}
			}
		}

	}

	int getPolicyFromState(int state, int t, int r) {
		int[] actionsFromState = possibleActionsFromState(state, t, r);

		double maxValue = Double.MIN_VALUE;
		int policyGotoState = state;

		// Pick to move to the state that has the maximum Q value
		for (int nextState : actionsFromState) {
			double value = Q[state][nextState][0][r];

			if (value > maxValue) {
				maxValue = value;
				policyGotoState = nextState;
			}
		}
		return policyGotoState;
	}
	void printQ() {
		System.out.println("Q matrix");
		for (int i = 0; i < Q.length; i++) {
			System.out.print("From state " + i + ":  ");
			for (int j = 0; j < Q[i].length; j++) {
				System.out.printf("%6.2f ", (Q[i][j]));
			}
			System.out.println();
		}
	}
}
