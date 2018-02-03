package simulation;

import java.util.LinkedHashMap;
import java.util.Map;

public class State {
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
	private long index;
	private double q;
	private double p;
	
	
	public State(Robot[] robots, Map<Character, Integer> powerCubes, int time, int currentPlayer,int redScore,int blueScore,int redClimb, int blueClimb) {
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
		q = 0;
		p = 0;
		
		
		this.index = time * 6;
		index = (index + currentPlayer) * 500;
		index = (index + redScore) * 500;
		index = (index + blueScore) * 3;
		index = (index + redClimb) * 3;
		index = (index + blueClimb) * 8;
		index = (index + powerCubes.get('P')) * 8;
		index = (index + powerCubes.get('Q')) * 11;
		index = (index + powerCubes.get('Z')) * 7;
		index = (index + powerCubes.get('C')) * 31;
		index = (index + powerCubes.get('W')) * 31;
		index = (index + powerCubes.get('S')) * 31;
		index = (index + powerCubes.get('w')) * 8;
		index = (index + powerCubes.get('p')) * 8;
		index = (index + powerCubes.get('q')) * 11;
		index = (index + powerCubes.get('z')) * 7;
		index = (index + powerCubes.get('c')) * 31;
		index = (index + powerCubes.get('v')) * 31;
		index = (index + powerCubes.get('s')) * 31;
		index = (index + powerCubes.get('V')) * 4;
		index = (index + powerCubes.get('F')) * 4;
		index = (index + powerCubes.get('f')) * 4;
		index = (index + powerCubes.get('B')) * 4;
		index = (index + powerCubes.get('b')) * 4;
		index = (index + powerCubes.get('L')) * 4;
		index = (index + powerCubes.get('l')) * 57;
		index = (index + robots[0].getX()) * 26;
		index = (index + robots[0].getY()) * 2;
		index = (index + robots[0].getColor()) * 2;
		index = (index + robots[0].getCube()) * 4;
		index = (index + robots[0].getDirection()) * 56;
		index = (index + robots[1].getX()) * 26;
		index = (index + robots[1].getY()) * 2;
		index = (index + robots[1].getColor()) * 2;
		index = (index + robots[1].getCube()) * 4;
		index = (index + robots[1].getDirection()) * 56;
		index = (index + robots[2].getX()) * 26;
		index = (index + robots[2].getY()) * 2;
		index = (index + robots[2].getColor()) * 2;
		index = (index + robots[2].getCube()) * 4;
		index = (index + robots[2].getDirection()) * 56;
		index = (index + robots[3].getX()) * 26;
		index = (index + robots[3].getY()) * 2;
		index = (index + robots[3].getColor()) * 2;
		index = (index + robots[3].getCube()) * 4;
		index = (index + robots[3].getDirection()) * 56;
		index = (index + robots[4].getX()) * 26;
		index = (index + robots[4].getY()) * 2;
		index = (index + robots[4].getColor()) * 2;
		index = (index + robots[4].getCube()) * 4;
		index = (index + robots[4].getDirection()) * 56;
		index = (index + robots[5].getX()) * 26;
		index = (index + robots[5].getY()) * 2;
		index = (index + robots[5].getColor()) * 2;
		index = (index + robots[5].getCube()) * 4;
		index = (index + robots[5].getDirection());
		
		
		
		
	} 
	
	
	
	
	public State(State node) {
		this(node.robots,node.powerCubes,node.time,node.currentPlayer,node.redScore,node.blueScore,node.redClimb,node.blueClimb);
	}
	
	public boolean equals(Object o) {
		return o instanceof State && Robot.equalsRobotArray(((State) o).getRobots(), robots) && ((State) o).getPowerCubes().equals(powerCubes)
				&& ((State) o).getTime() == time && ((State) o).getCurrentPlayer() == currentPlayer && ((State) o).getRedScore() == redScore
				&& ((State) o).getBlueScore() == blueScore && ((State) o).getRedClimb() == redClimb && ((State) o).getBlueClimb() == blueClimb;
	}
	
	public boolean equals(long index) {
		return index == this.index;
	}
	
	public String toString() {
		String str = "";
		for (int i = 0;i < 6;i++) {
			str += robots[i] + " ";
		}
		String str2 = powerCubes.values().toString();
		str2 = str2.replace("[", "");
		str2 = str2.replace("]", "");

		str2 = str2.replaceAll(",", "");

		return str + str2 + " " + time
				+ " " + currentPlayer + " " + redScore + " " + blueScore+ " " + redClimb + " " + blueClimb;
	
	}
	public String toData() {
		String str = "";
		for (int i = 0;i < 6;i++) {
			str += robots[i].toData() + " ";
		}
		String str2 = powerCubes.values().toString();
		str2 = str2.replace("[", "");
		str2 = str2.replace("]", "");

		str2 = str2.replaceAll(",", "");

		return str + str2 + " " + (time / 5400.0)
				+ " " + currentPlayer + " " + (redScore /3600.0) + " " + (blueScore / 3600.0)+ " " + redClimb + " " + blueClimb;
	
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