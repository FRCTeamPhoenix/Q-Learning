package qlearning.main;

import java.awt.Color;
import java.awt.Graphics;

public class Robot {

	private int x;
	private int y;
	private int cube;
	private boolean climb;

	public Robot(int x, int y) {
		this.x = x;
		this.y = y;
		cube = 1;
		climb = false;
	}

	public Robot(int x, int y, int cube, boolean climb) {
		this(x, y);
		this.cube = cube;
		this.climb = climb;
	}

	public void draw(Graphics g, int size) {
		g.setColor(Color.GREEN);
		g.fillRect(x * size, y * size, size, size);
	}

	public void update(int action) {
		switch (action) {
		case 0:
			x++;
			break;
		case 1:
			x--;
			break;
		case 2:
			y--;
			break;
		case 3:
			y++;
			break;
		default:
			break;

		}

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCube() {
		return cube;
	}

	public void setCube(int cube) {
		this.cube = cube;
	}

	public boolean isClimb() {
		return climb;
	}

	public void setClimb(boolean climb) {
		this.climb = climb;
	}

}
