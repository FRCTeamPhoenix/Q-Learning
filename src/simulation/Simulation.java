package simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Simulation {

	private Arena arena;
	private Robot[] robots;
	private Graphics g;
	private int gameDuration = 5400;
	private Map<Character, Integer> initPowerCubes;
	private char[][] layout;
	private final int robotCount = 6;
	private State initState;
	private final int speed = 6;
	private Random random = new Random();
	private State currentState;

	public Simulation(Arena arena, Robot[] robots) {
		this.robots = Robot.cloneRobotArray(robots);
		this.arena = arena;
		this.g = arena.getG();
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		initPowerCubes = new LinkedHashMap<Character, Integer>();
		initPowerCubes();
		layout = arena.getLayout();
		initState = new State(robots, initPowerCubes, 1, 0, 0, 0, 0, 0);
		currentState = new State(initState);
	}

	// Runs a simulation of the game with random actions
	public void playRandom() {
		initPowerCubes = new LinkedHashMap<Character, Integer>();
		initPowerCubes();
		layout = arena.getLayout();
		currentState = new State(initState);
		drawState(currentState);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (currentState.getTime() <= gameDuration) {
			if (currentState.getTime() % (robotCount) == 1) {
				drawState(currentState);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			updateState(currentState, createRandomAction(currentState.getRobots()[currentState.getCurrentPlayer()]));

		}
	}

	// Returns the possible actions of a robot at the current node
	public ArrayList<Integer> possibleActions(Robot r) {
		ArrayList<Integer> possibleActions = new ArrayList<Integer>();
		possibleActions.add(0);
		if (r.isClimb()) {
			return possibleActions;
		}
		possibleActions.add(1);
		possibleActions.add(2);
		if (canMove(getFront(r)) && !isFrontRobot(r)) {
			possibleActions.add(3);
		}

		if (nextToScaleOrSwitch(getFront(r), r) && r.getCube() == 1) {
			possibleActions.add(4);
		}
		if (nextToCube(getFront(r), r) && (currentState.getPowerCubes().get(getFront(r)) > 0) && r.getCube() == 0) {
			possibleActions.add(5);
;
		}
		if (r.getCube() == 1 && currentState.getTime() / speed / robotCount> 15) {
			if (getFront(r) == 'B' && r.getColor() == 0) {
				if (currentState.getPowerCubes().get('F') < 3) {
					possibleActions.add(6);
				}
				if (currentState.getPowerCubes().get('B') < 3) {
					possibleActions.add(7);
				}
				if (currentState.getPowerCubes().get('L') < 3) {
					possibleActions.add(8);
				}
			}
		}
		if (r.getCube() == 1 && currentState.getTime() / speed / robotCount > 15) {
			if (getFront(r) == 'b' && r.getColor() == 1) {
				if (currentState.getPowerCubes().get('f') < 3) {
					possibleActions.add(6);
				}
				if (currentState.getPowerCubes().get('b') < 3) {
					possibleActions.add(7);
				}
				if (currentState.getPowerCubes().get('l') < 3) {
					possibleActions.add(8);
				}
			}
		}
		if (currentState.getTime() / speed / robotCount > 120 && layout[r.getY()][r.getX()] == 'H' && getFront(r) == 'T'
				&& currentState.getRedClimb() < 2 && r.getColor() == 0) {
			possibleActions.add(9);
		}
		if (currentState.getTime() / speed / robotCount > 120 && layout[r.getY()][r.getX()] == 'h' && getFront(r) == 'T'
				&& currentState.getBlueClimb() < 2 && r.getColor() == 1) {
			possibleActions.add(9);
		}

		return possibleActions;
	}

	// updates the given node depending on the action
	public void updateState(State node, int action) {
		updatePowerCubes(node, action);
		updateScore(node);
		node.getRobots()[node.getCurrentPlayer()].update(action);
		node.setCurrentPlayer((node.getCurrentPlayer() + 1) % robotCount);
		node.setTime(node.getTime() + 1);

	}

	// Returns a random possible action of a robot at the current node
	public int createRandomAction(Robot r) {
		ArrayList<Integer> possibleActions = possibleActions(r);
		int randomAction = random.nextInt(possibleActions.size());

		return possibleActions.get(randomAction);
	}

	// Draws the given node
	public void drawState(State node) {
		arena.draw();

		for (Robot r : node.getRobots()) {
			r.draw(g, arena.getSize());
		}

		g.setColor(Color.BLACK);
		g.drawString("time = " + node.getTime() / speed / robotCount, 5, 25);
		g.drawString("score = " + (node.getBlueScore() / speed / robotCount),
				arena.getArenaWidth() * arena.getSize() - 70, arena.getArenaHeight() * arena.getSize() - 25);
		g.drawString("score = " + (node.getRedScore() / speed / robotCount), 5,
				arena.getArenaHeight() * arena.getSize() - 25);
	}

	private void initPowerCubes() {
		initPowerCubes.put('P', 7);
		initPowerCubes.put('Q', 7);
		initPowerCubes.put('Z', 10);
		initPowerCubes.put('C', 6);
		initPowerCubes.put('W', 0);
		initPowerCubes.put('S', 0);
		initPowerCubes.put('w', 0);
		initPowerCubes.put('p', 7);
		initPowerCubes.put('q', 7);
		initPowerCubes.put('z', 10);
		initPowerCubes.put('c', 6);
		initPowerCubes.put('v', 0);
		initPowerCubes.put('s', 0);
		initPowerCubes.put('V', 0);
		initPowerCubes.put('F', 0);
		initPowerCubes.put('f', 0);
		initPowerCubes.put('B', 0);
		initPowerCubes.put('b', 0);
		initPowerCubes.put('L', 0);
		initPowerCubes.put('l', 0);

	}

	private void updateScore(State node) {
		if (node.getPowerCubes().get('W') > node.getPowerCubes().get('V'))
			node.setRedScore(node.getRedScore() + 1);
		if (node.getPowerCubes().get('S') > node.getPowerCubes().get('s'))
			node.setRedScore(node.getRedScore() + 1);
		else if (node.getPowerCubes().get('S') < node.getPowerCubes().get('s'))
			node.setBlueScore(node.getBlueScore() + 1);
		if (node.getPowerCubes().get('v') > node.getPowerCubes().get('w'))
			node.setBlueScore(node.getBlueScore() + 1);

	}

	private void updatePowerCubes(State node,int action) {
		int temp;
		int score = 0;
		Robot r = node.getRobots()[node.getCurrentPlayer()];
		if (action == 4) {
			score += speed * robotCount;
			temp = node.getPowerCubes().get(getFront(r));
			node.getPowerCubes().replace(getFront(r), temp + 1);
		}
		if (action == 5) {
			temp = node.getPowerCubes().get(getFront(r));
			node.getPowerCubes().replace(getFront(r), temp - 1);
		}
		if (action == 6) {
			score += 5 * speed * robotCount;
			if (r.getColor() == 0) {
				temp = node.getPowerCubes().get('F');
				node.getPowerCubes().replace('F', temp + 1);
			} else {
				temp = node.getPowerCubes().get('f');
				node.getPowerCubes().replace('f', temp + 1);
			}
		}
		if (action == 7) {
			score += 5 * speed * robotCount;
			if (r.getColor() == 0) {
				temp = node.getPowerCubes().get('B');
				node.getPowerCubes().replace('B', temp + 1);
			} else {
				temp = node.getPowerCubes().get('b');
				node.getPowerCubes().replace('b', temp + 1);
			}
		}
		if (action == 8) {
			score += 5 * speed * robotCount;
			if (r.getColor() == 0) {
				temp = node.getPowerCubes().get('L');
				node.getPowerCubes().replace('L', temp + 1);
			} else {
				temp = node.getPowerCubes().get('l');
				node.getPowerCubes().replace('l', temp + 1);
			}
		}
		if (action == 9) {
			score += 30 * speed * robotCount;
			if (r.getColor() == 0) {
				r.setClimb(true);
				node.setRedClimb(node.getRedClimb() + 1);
			} else {
				r.setClimb(true);
				node.setBlueClimb(node.getBlueClimb() + 1);
			}
		}

		if (r.getColor() == 0) {
			node.setRedScore(node.getRedScore() + score);

		} else {
			node.setBlueScore(node.getBlueScore() + score);
		}
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

	public boolean isFrontRobot(Robot currentRobot) {

		for (Robot robot : robots) {
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

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public Robot[] getRobots() {
		return robots;
	}

	public void setRobots(Robot[] robots) {
		this.robots = robots;
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

	public Map<Character, Integer> getInitPowerCubes() {
		return initPowerCubes;
	}

	public void setInitPowerCubes(Map<Character, Integer> initPowerCubes) {
		this.initPowerCubes = initPowerCubes;
	}

	public char[][] getLayout() {
		return layout;
	}

	public void setLayout(char[][] layout) {
		this.layout = layout;
	}

	public State getInitState() {
		return initState;
	}

	public void setInitState(State initState) {
		this.initState = initState;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public int getRobotCount() {
		return robotCount;
	}

	public int getSpeed() {
		return speed;
	}

}
