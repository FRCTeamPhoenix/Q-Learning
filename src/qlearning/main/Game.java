package qlearning.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

	private Arena arena;
	private Robot[] redRobot;
	private Graphics g;
	private int gameDuration = 900;
	private int time;
	private Random random = new Random();
	private int redScore;
	private int blueScore;
	private Map<Character, Integer> powerCubes;
	private char[][] layout;
	private Robot[] blueRobot;
	private int redClimb;
	private int blueClimb;
	private final double c = Math.sqrt(2);
	private int N = 100;
	private final int robotCount = 3;
	private Robot[] initBlueRobot;
	private Robot[] initRedRobot;
	private State parentState;

	public Game(Arena arena, Robot[] redRobot, Robot[] blueRobot) {
		initRedRobot = Robot.cloneRobotArray(redRobot);
		initBlueRobot = Robot.cloneRobotArray(blueRobot);
		this.blueRobot = blueRobot;
		this.redRobot = redRobot;
		this.arena = arena;
		this.g = arena.getG();
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));

	}

	public void init() {

		redRobot = Robot.cloneRobotArray(initRedRobot);
		blueRobot = Robot.cloneRobotArray(initBlueRobot);
		redClimb = 0;
		blueClimb = 0;
		redScore = 0;
		blueScore = 0;
		time = 1;
		powerCubes = new LinkedHashMap<Character, Integer>();

		initPowerCubes();
		layout = arena.getLayout();
		LinkedHashMap<Character, Integer> newPowerCubes = new LinkedHashMap<Character, Integer>(powerCubes);

		parentState = new State(Robot.cloneRobotArray(redRobot), Robot.cloneRobotArray(blueRobot), newPowerCubes, 1, 0);

	}

	private void initPowerCubes() {
		powerCubes.put('P', 7);
		powerCubes.put('Q', 7);
		powerCubes.put('Z', 10);
		powerCubes.put('C', 6);
		powerCubes.put('W', 0);
		powerCubes.put('S', 0);
		powerCubes.put('w', 0);
		powerCubes.put('p', 7);
		powerCubes.put('q', 7);
		powerCubes.put('z', 10);
		powerCubes.put('c', 6);
		powerCubes.put('v', 0);
		powerCubes.put('s', 0);
		powerCubes.put('V', 0);
		powerCubes.put('F', 0);
		powerCubes.put('f', 0);
		powerCubes.put('B', 0);
		powerCubes.put('b', 0);
		powerCubes.put('L', 0);
		powerCubes.put('l', 0);

	}

	public List<State> runSim(List<State> stateHistory) {
		System.out.println("Start Sim");
		init();
		System.out.println("init: " + new State(redRobot, blueRobot, powerCubes, time, 0));
		List<State> currentHistory = new ArrayList<State>();
		State current;
		int currentAction;
		while (time <= gameDuration) {
			for (int j = 0; j < robotCount; j++) {
				current = new State(redRobot, blueRobot, powerCubes, time, j);
				if (stateHistory.contains(current)) {
					currentHistory.add(stateHistory.get(stateHistory.indexOf(current)));
				} else {
					stateHistory.add(current.clone());
					currentHistory.add(current.clone());
				}
				currentHistory.add(current.clone());

				currentAction = this.createRandomAction(redRobot[j]);

				redScore = updatePowerCubes(currentAction, redRobot[j], redScore);
				redRobot[j].update(currentAction);

				current.setCurrentTeam(j + 3);
				if (stateHistory.contains(current)) {
					currentHistory.add(stateHistory.get(stateHistory.indexOf(current)));
				} else {
					stateHistory.add(current.clone());
					currentHistory.add(current.clone());
				}

				currentAction = this.createRandomAction(blueRobot[j]);

				blueScore = updatePowerCubes(currentAction, blueRobot[j], blueScore);
				blueRobot[j].update(currentAction);
			}
			updateScore();
			time++;
		}
		int win = -1;
		if (redScore > blueScore) {
			win = 0;
		} else if (blueScore > redScore) {
			win = 1;
		}
		for (State s : currentHistory) {
			s.update(win);
		}
		stateHistory.removeAll(currentHistory);
		stateHistory.addAll(currentHistory);
		
		return stateHistory;

	}

	public void run(List<State> stateHistory) {
		//1 4 1 0 1 1 15 1 0 1 1 21 1 0 1 54 4 1 1 3 54 10 1 1 3 54 21 1 1 3 7 7 10 6 0 0 0 7 7 10 6 0 0 0 0 0 0 0 0 0 1
		init();
		N = stateHistory.get(stateHistory.indexOf(parentState)).getN();
		System.out.println(initRedRobot[1].equals(redRobot[1]));
		System.out.println("Total Simulations: " + N  + "\nStart Game");
		draw();
		int currentAction;
		State current;
		int currentN = 0;
		int currentW = 0;
		State next;
		double currentBestActionValue = 0;
		while ( time <= gameDuration) {
			for (int j = 0;j < robotCount;j++) {
				current = new State(redRobot,blueRobot,powerCubes,time,j);
				ArrayList<Integer> possibleActions = possibleActions(redRobot[j]);
				currentAction = this.createRandomAction(redRobot[j]);
				for (int i = 0;i < possibleActions.size();i++) {
					next = nextState(current.clone(),possibleActions.get(i));
					if (stateHistory.contains(next)) {
						currentN = stateHistory.get(stateHistory.indexOf(next)).getN();
						currentW = stateHistory.get(stateHistory.indexOf(next)).getW();
						if (currentN != 0) {
							double nextBestActionValue = 1.0 *  currentW / currentN + c * Math.sqrt(Math.log(N)/ currentN);
							if (currentBestActionValue < nextBestActionValue) {
								currentAction = possibleActions.get(i);
								currentBestActionValue = nextBestActionValue;
								System.out.println(time + ": " + j + ": " + currentAction + ": " + nextBestActionValue);
							}
						}
					}
					
				}
				redScore = updatePowerCubes(currentAction, redRobot[j], redScore);
				redRobot[j].update(currentAction);
				
				
				currentAction = this.createRandomAction(blueRobot[j]);

				blueScore = updatePowerCubes(currentAction, blueRobot[j], blueScore);
				blueRobot[j].update(currentAction);
				
				}
			updateScore();
			time++;
			draw();
			}
		
		}
		
	
	public State nextState(State state, int action) {

		State nextState = state;
		Robot r;
		if (nextState.getCurrentTeam() < 3) {
			r = nextState.getRedRobot()[nextState.getCurrentTeam()];
		} else {
			r = nextState.getBlueRobot()[nextState.getCurrentTeam() - 3];
		}

		int temp;
		if (action == 4) {
			temp = nextState.getPowerCubes().get(getFront(r));
			nextState.getPowerCubes().replace(getFront(r), temp + 1);
		}
		if (action == 5) {
			temp = nextState.getPowerCubes().get(getFront(r));
			nextState.getPowerCubes().replace(getFront(r), temp - 1);
		}
		if (action == 6) {
			if (r.getColor() == 0) {
				temp = nextState.getPowerCubes().get('F');
				nextState.getPowerCubes().replace('F', temp + 1);
			} else {
				temp = powerCubes.get('f');
				nextState.getPowerCubes().replace('f', temp + 1);
			}
		}
		if (action == 7) {
			if (r.getColor() == 0) {
				temp = nextState.getPowerCubes().get('B');
				nextState.getPowerCubes().replace('B', temp + 1);
			} else {
				temp = nextState.getPowerCubes().get('b');
				nextState.getPowerCubes().replace('b', temp + 1);
			}
		}
		if (action == 8) {
			if (r.getColor() == 0) {
				temp = powerCubes.get('L');
				powerCubes.replace('L', temp + 1);
			} else {
				temp = powerCubes.get('l');
				powerCubes.replace('l', temp + 1);
			}
		}
		if (action == 9) {
			if (r.getColor() == 0) {
				r.setClimb(true);
				redClimb++;
			} else {
				r.setClimb(true);
				blueClimb++;
			}
		}
		r.update(action);
		return nextState;

	}

	public void play(List<State> stateHistory) {

		init();
		N = stateHistory.get(0).getN();
		System.out.println("Total simulations: " + N);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		time++;
		while (time <= gameDuration) {

			update(stateHistory);
			draw();
			time++;
			try {
				Thread.sleep(1000 / 6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private ArrayList<Integer> possibleActions(Robot r) {
		ArrayList<Integer> possibleActions = new ArrayList<Integer>();
		possibleActions.add(0);
		if (r.isClimb()) {
			return possibleActions;
		}
		possibleActions.add(1);
		possibleActions.add(2);
		if (canMove(getFront(r)) && !isFrontRobot(r)) {
			possibleActions.add(3);
			possibleActions.add(3);
		}

		if (nextToScaleOrSwitch(getFront(r), r) && r.getCube() == 1) {
			possibleActions.add(4);
			possibleActions.add(4);
			possibleActions.add(4);
		}
		if (nextToCube(getFront(r), r) && notEmpty(getFront(r)) && r.getCube() == 0) {
			possibleActions.add(5);
			possibleActions.add(5);
			possibleActions.add(5);
		}
		if (r.getCube() == 1 && time / 6 > 15) {
			if (getFront(r) == 'B' && r.getColor() == 0) {
				if (powerCubes.get('F') < 3) {
					possibleActions.add(6);
				}
				if (powerCubes.get('B') < 3) {
					possibleActions.add(7);
				}
				if (powerCubes.get('L') < 3) {
					possibleActions.add(8);
				}
			}
		}
		if (r.getCube() == 1 && time / 6 > 15) {
			if (getFront(r) == 'b' && r.getColor() == 1) {
				if (powerCubes.get('f') < 3) {
					possibleActions.add(6);
				}
				if (powerCubes.get('b') < 3) {
					possibleActions.add(7);
				}
				if (powerCubes.get('l') < 3) {
					possibleActions.add(8);
				}
			}
		}
		if (time / 6 > 120 && layout[r.getY()][r.getX()] == 'H' && getFront(r) == 'T' && redClimb < 2
				&& r.getColor() == 0) {
			possibleActions.add(9);
		}
		if (time / 6 > 120 && layout[r.getY()][r.getX()] == 'h' && getFront(r) == 'T' && blueClimb < 2
				&& r.getColor() == 1) {
			possibleActions.add(9);
		}

		return possibleActions;
	}

	public boolean notEmpty(char c) {
		return (powerCubes.get(c) > 0);
	}

	public char getFront(Robot r) {
		if (r.getDirection() == 0) {
			if (r.getY() - 1 < 0)
				return '\0';
			return layout[r.getY() - 1][r.getX()];
		}
		if (r.getDirection() == 1) {
			if (r.getX() + 1 >= arena.getArenaWidth())
				return '\0';
			return layout[r.getY()][r.getX() + 1];
		}
		if (r.getDirection() == 2) {
			if (r.getY() + 1 >= arena.getArenaHeight())
				return '\0';
			return layout[r.getY() + 1][r.getX()];
		}
		if (r.getDirection() == 3) {
			if (r.getX() - 1 < 0)
				return '\0';
			return layout[r.getY()][r.getX() - 1];
		}

		return '\0';
	}

	public boolean isFrontRobot(Robot currentRobot) {

		for (Robot robot : redRobot) {
			if (currentRobot.getDirection() == 0) {

				if ((currentRobot.getY() - 1 >= 0)
						&& (currentRobot.getY() - 1 == robot.getY() && currentRobot.getX() == robot.getX()))
					return true;
			}

			if (currentRobot.getDirection() == 1) {
				if ((currentRobot.getX() + 1 <= arena.getArenaWidth())
						&& (currentRobot.getY() - 1 == robot.getY() && currentRobot.getX() == robot.getX()))
					return true;
			}
			if (currentRobot.getDirection() == 2) {
				if ((currentRobot.getY() + 1 > arena.getArenaHeight())
						&& (currentRobot.getY() - 1 == robot.getY() && currentRobot.getX() == robot.getX()))
					return true;
			}
			if (currentRobot.getDirection() == 3) {
				if ((currentRobot.getX() - 1 < 0)
						&& (currentRobot.getY() - 1 == robot.getY() && currentRobot.getX() == robot.getX()))
					return true;
			}

		}
		for (Robot robot : blueRobot) {
			if (currentRobot.getDirection() == 0) {

				if ((currentRobot.getY() - 1 >= 0)
						&& (currentRobot.getY() - 1 == robot.getY() && currentRobot.getX() == robot.getX()))
					return true;
			}

			if (currentRobot.getDirection() == 1) {
				if ((currentRobot.getX() + 1 <= arena.getArenaWidth())
						&& (currentRobot.getY() - 1 == robot.getY() && currentRobot.getX() == robot.getX()))
					return true;
			}
			if (currentRobot.getDirection() == 2) {
				if ((currentRobot.getY() + 1 > arena.getArenaHeight())
						&& (currentRobot.getY() - 1 == robot.getY() && currentRobot.getX() == robot.getX()))
					return true;
			}
			if (currentRobot.getDirection() == 3) {
				if ((currentRobot.getX() - 1 < 0)
						&& (currentRobot.getY() - 1 == robot.getY() && currentRobot.getX() == robot.getX()))
					return true;
			}

		}

		return false;
	}

	public boolean canMove(char c) {
		return (c == '0' || c == 'N' || c == 'L' || c == 'l' || c == 'h' || c == 'H' || c == 'E' || c == 'e');

	}

	public boolean nextToScaleOrSwitch(char c, Robot r) {
		if (r.getColor() == 0) {
			return (c == 'W' || c == 'S' || c == 'w');
		}
		return (c == 'V' || c == 's' || c == 'v');
	}

	public boolean nextToCube(char c, Robot r) {
		if (r.getColor() == 0) {
			return (c == 'Z' || c == 'P' || c == 'C' || c == 'Q');
		}
		return (c == 'z' || c == 'p' || c == 'c' || c == 'q');
	}

	private int createRandomAction(Robot r) {
		ArrayList<Integer> possibleActions = possibleActions(r);
		int randomAction = random.nextInt(possibleActions.size());

		return possibleActions.get(randomAction);
	}

	public void draw() {
		arena.draw();

		for (Robot r : redRobot) {
			r.draw(g, arena.getSize());
		}
		for (Robot r : blueRobot) {
			r.draw(g, arena.getSize());
		}
		g.setColor(Color.BLACK);
		g.drawString("time = " + time / 6, 5, 25);
		g.drawString("score = " + (blueScore / 6), arena.getArenaWidth() * arena.getSize() - 70,
				arena.getArenaHeight() * arena.getSize() - 25);
		g.drawString("score = " + (redScore / 6), 5, arena.getArenaHeight() * arena.getSize() - 25);
	}

	private void update(List<State> stateHistory) {
		for (int i = 0; i < robotCount; i++) {

			int currentAction = bestMove(stateHistory, i);

			redScore = updatePowerCubes(currentAction, redRobot[i], redScore);
			redRobot[i].update(currentAction);

			currentAction = createRandomAction(blueRobot[i]);
			blueRobot[i].update(currentAction);
			blueScore = updatePowerCubes(currentAction, blueRobot[i], blueScore);

		}
		updateScore();
	}

	private int bestMove(List<State> StateHistory, int currentPlayer) {
		State currentState = new State(redRobot, blueRobot, powerCubes, time, currentPlayer);

		Robot r;
		if (currentState.getCurrentTeam() < 3) {
			r = currentState.getRedRobot()[currentState.getCurrentTeam()];
		} else {
			r = currentState.getBlueRobot()[currentState.getCurrentTeam() - 3];
		}
		ArrayList<Integer> possibleActions = possibleActions(r);
		double bestActionValue = -1;
		int bestAction = this.createRandomAction(r);
		for (int i = 0; i < possibleActions.size(); i++) {
			State nextState = nextState(currentState, possibleActions.get(i));
			double nextStateValue = 0;

			if (nextState.getN() != 0)
				nextStateValue = nextState.getW() / nextState.getN() + c * Math.sqrt(Math.log(N) / nextState.getN());
			// System.out.println("Action " + possibleActions.get(i) + " : " +
			// nextStateValue);
			if (nextStateValue > bestActionValue) {
				bestActionValue = nextStateValue;
				bestAction = i;
			}
		}
		return bestAction;
	}

	private int updatePowerCubes(int action, Robot r, int score) {
		int temp;
		if (action == 4) {
			score += 6;
			temp = powerCubes.get(getFront(r));
			powerCubes.replace(getFront(r), temp + 1);
		}
		if (action == 5) {
			temp = powerCubes.get(getFront(r));
			powerCubes.replace(getFront(r), temp - 1);
		}
		if (action == 6) {
			score += 30;
			if (r.getColor() == 0) {
				temp = powerCubes.get('F');
				powerCubes.replace('F', temp + 1);
			} else {
				temp = powerCubes.get('f');
				powerCubes.replace('f', temp + 1);
			}
		}
		if (action == 7) {
			score += 30;
			if (r.getColor() == 0) {
				temp = powerCubes.get('B');
				powerCubes.replace('B', temp + 1);
			} else {
				temp = powerCubes.get('b');
				powerCubes.replace('b', temp + 1);
			}
		}
		if (action == 8) {
			score += 30;
			if (r.getColor() == 0) {
				temp = powerCubes.get('L');
				powerCubes.replace('L', temp + 1);
			} else {
				temp = powerCubes.get('l');
				powerCubes.replace('l', temp + 1);
			}
		}
		if (action == 9) {
			score += 30 * 6;
			if (r.getColor() == 0) {
				r.setClimb(true);
				redClimb++;
			} else {
				r.setClimb(true);
				blueClimb++;
			}
		}

		return score;

	}

	private void updateScore() {
		if (powerCubes.get('W') > powerCubes.get('V'))
			redScore++;
		if (powerCubes.get('S') > powerCubes.get('s'))
			redScore++;
		else if (powerCubes.get('S') < powerCubes.get('s'))
			blueScore++;
		if (powerCubes.get('v') > powerCubes.get('w'))
			blueScore++;

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public Robot[] getRedRobot() {
		return redRobot;
	}

	public void setRedRobot(Robot[] redRobot) {
		this.redRobot = redRobot;
	}

	public Graphics getG() {
		return g;
	}

	public void setG(Graphics g) {
		this.g = g;
	}

	public int getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(int gameDuration) {
		this.gameDuration = gameDuration;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public int getRedScore() {
		return redScore;
	}

	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}

	public int getBlueScore() {
		return blueScore;
	}

	public void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}

	public Map<Character, Integer> getPowerCubes() {
		return powerCubes;
	}

	public void setPowerCubes(Map<Character, Integer> powerCubes) {
		this.powerCubes = powerCubes;
	}

	public char[][] getLayout() {
		return layout;
	}

	public void setLayout(char[][] layout) {
		this.layout = layout;
	}

	public Robot[] getBlueRobot() {
		return blueRobot;
	}

	public void setBlueRobot(Robot[] blueRobot) {
		this.blueRobot = blueRobot;
	}

	public int getRedClimb() {
		return redClimb;
	}

	public void setRedClimb(int redClimb) {
		this.redClimb = redClimb;
	}

	public int getBlueClimb() {
		return blueClimb;
	}

	public void setBlueClimb(int blueClimb) {
		this.blueClimb = blueClimb;
	}

	public Robot[] getInitBlueRobot() {
		return initBlueRobot;
	}

	public void setInitBlueRobot(Robot[] initBlueRobot) {
		this.initBlueRobot = initBlueRobot;
	}

	public Robot[] getInitRedRobot() {
		return initRedRobot;
	}

	public void setInitRedRobot(Robot[] initRedRobot) {
		this.initRedRobot = initRedRobot;
	}

	public double getC() {
		return c;
	}

	public int getN() {
		return N;
	}

}
