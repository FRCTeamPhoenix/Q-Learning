package qlearning.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Game {

	private Arena arena;
	private Robot[] redRobot;
	private Graphics g;
	private int gameDuration = 900;
	private int time;
	private int[] actions;
	private Random random = new Random();
	private int redScore;
	private int blueScore;
	private Map<Character, Integer> powerCubes;
	private char[][] layout;
	private Robot[] blueRobot;
	private int redClimb;
	private int blueClimb;
	private int robotCount;

	public Game(Arena arena, Robot[] redRobot, Robot[] blueRobot) {
		this.blueRobot = blueRobot;
		this.arena = arena;
		this.redRobot = redRobot;
		this.g = arena.getG();
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));

	}

	public void play() {
		actions = new int[gameDuration];
		redClimb = 0;
		blueClimb = 0;
		redScore = 0;
		blueScore = 0;
		robotCount = 3;
		time = 0;
		draw();
		powerCubes = new HashMap<Character, Integer>();
		initPowerCubes();
		layout = arena.getLayout();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		time++;
		while (time <= gameDuration) {

			update();
			draw();
			time++;
			try {
				Thread.sleep(1000 / 6 / 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
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

	private void draw() {
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

	private void update() {
		for (int i = 0; i < robotCount; i++) {
			int currentAction = createRandomAction(redRobot[i]);
			redScore = updatePowerCubes(currentAction, redRobot[i], redScore);
			redRobot[i].update(currentAction);
			currentAction = createRandomAction(blueRobot[i]);
			blueRobot[i].update(currentAction);
			blueScore = updatePowerCubes(currentAction, blueRobot[i], blueScore);

		}
		arena.update();
		updateScore();
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

}
