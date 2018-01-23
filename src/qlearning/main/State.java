package qlearning.main;

import java.util.LinkedHashMap;
import java.util.Map;

public class State {
	private static long count = 0;
	private long id;
	private Robot[] redRobot;
	private Robot[] blueRobot;
	private Map<Character, Integer> powerCubes;
	private int currentTeam;
	private int time;
	private int w;
	private int n;

	public State(Robot[] redRobot, Robot[] blueRobot, Map<Character, Integer> powerCubes, int time, int currentTeam) {
		this.redRobot = redRobot;
		this.blueRobot = blueRobot;
		this.powerCubes = powerCubes;
		this.time = time;
		count++;
		id = count;
		this.currentTeam = currentTeam;
		w = 0;
		n = 0;
	}

	public State(Robot[] redRobot, Robot[] blueRobot, Map<Character, Integer> powerCubes, int time, int currentTeam,
			int w, int n) {
		this(redRobot, blueRobot, powerCubes, time, currentTeam);
		this.w = w;
		this.n = n;
	}

	public State clone() {
		LinkedHashMap<Character,Integer> newPowerCubes = new LinkedHashMap<Character,Integer>(powerCubes);
		
		return new State(Robot.cloneRobotArray(redRobot), Robot.cloneRobotArray(blueRobot), newPowerCubes, time, currentTeam);
	}

	public State cloneAll() {
		LinkedHashMap<Character,Integer> newPowerCubes = new LinkedHashMap<Character,Integer>(powerCubes);
		return new State(Robot.cloneRobotArray(redRobot), Robot.cloneRobotArray(blueRobot), newPowerCubes, time, currentTeam, w, n);
	}

	public void update(int win) {
		n++;
		if (win == 0 && currentTeam < 3)
			w++;
		else if (win == 1 && currentTeam >= 3)
			w++;

	}

	public String toString() {
		String str = "";
		for (int i = 0;i < 3;i++) {
			str += redRobot[i] + " ";
		}
		for (int i = 0;i < 3;i++) {
			str += blueRobot[i] + " ";
		}
		return str + powerCubes.toString() + " time: " + time
				+ " " + currentTeam + " " + w + " " + n;
	}

	public boolean equals(Object object) {
		if (object instanceof State && ((State) object).getTime() == this.time
				&& Robot.equalsRobotArray(((State) object).getRedRobot(),this.redRobot) && Robot.equalsRobotArray(((State) object).getBlueRobot(),this.blueRobot)
				&& ((State) object).getPowerCubes().equals(this.powerCubes)
				&& ((State) object).getCurrentTeam() == this.currentTeam) {
			return true;
		} else {
			return false;
		}
	}

	public int getCurrentTeam() {
		return currentTeam;
	}

	public void setCurrentTeam(int currentTeam) {
		this.currentTeam = currentTeam;
	}

	public static long getCount() {
		return count;
	}

	public static void setCount(long count) {
		State.count = count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Robot[] getRedRobot() {
		return redRobot;
	}

	public void setRedRobot(Robot[] redRobot) {
		this.redRobot = redRobot;
	}

	public Robot[] getBlueRobot() {
		return blueRobot;
	}

	public void setBlueRobot(Robot[] blueRobot) {
		this.blueRobot = blueRobot;
	}

	public Map<Character, Integer> getPowerCubes() {
		return powerCubes;
	}

	public void setPowerCubes(Map<Character, Integer> powerCubes) {
		this.powerCubes = powerCubes;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

}
