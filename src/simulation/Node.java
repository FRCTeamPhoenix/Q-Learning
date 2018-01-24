package simulation;

import java.util.LinkedHashMap;
import java.util.Map;

public class Node {
	private Robot[] robots;
	private Map<Character, Integer> powerCubes;
	private int currentPlayer;
	private int time;
	private int w;
	private int n;
	private int redScore;
	private int blueScore;
	private int redClimb;
	private int blueClimb;

	public Node(Robot[] robots, Map<Character, Integer> powerCubes, int time, int currentPlayer,int redScore,int blueScore,int redClimb, int blueClimb) {
		this.robots = Robot.cloneRobotArray(robots);
		this.powerCubes = new LinkedHashMap<Character, Integer>(powerCubes);
		this.time = time;
		this.currentPlayer = currentPlayer;
		this.time = time;
		this.redScore = redScore;
		this.blueScore = blueScore;
		this.redClimb = redClimb;
		this.blueClimb = blueClimb;

		w = 0;
		n = 0;
	} 
	
	public Node(Node node) {
		this(node.robots,node.powerCubes,node.time,node.currentPlayer,node.redScore,node.blueScore,node.redClimb,node.blueClimb);
	}
	
	public boolean equals(Object o) {
		return o instanceof Node && Robot.equalsRobotArray(((Node) o).getRobots(), robots) && ((Node) o).getPowerCubes().equals(powerCubes)
				&& ((Node) o).getTime() == time && ((Node) o).getCurrentPlayer() == currentPlayer && ((Node) o).getRedScore() == redScore
				&& ((Node) o).getBlueScore() == blueScore && ((Node) o).getRedClimb() == redClimb && ((Node) o).getBlueClimb() == blueClimb;
	}
	
	public String toString() {
		String str = "";
		for (int i = 0;i < 6;i++) {
			str += robots[i] + " ";
		}
		return str + powerCubes.values().toString() + " " + time
				+ " " + currentPlayer + " " + redScore + " " + blueScore + " " + redClimb + " " + blueClimb + " " + w + " " + n;
	
	}

	public Robot[] getRobots() {
		return robots;
	}

	public void setRobots(Robot[] robots) {
		this.robots = robots;
	}

	public Map<Character, Integer> getPowerCubes() {
		return powerCubes;
	}

	public void setPowerCubes(Map<Character, Integer> powerCubes) {
		this.powerCubes = powerCubes;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
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
	

}