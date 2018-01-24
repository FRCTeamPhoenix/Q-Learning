package simulation;

import java.awt.Color;
import java.awt.Graphics;

public class Robot {

	private int x;
	private int y;
	private int direction;
	private int cube;
	private int color;
	private boolean climb;

	public Robot(int x, int y, int cube, int color, int direction) {
		this.x = x;
		this.y = y;
		this.cube = cube;
		this.direction = direction;
		this.color = color;
		this.climb = false;
	}

	public void draw(Graphics g, int size) {

		if (cube == 1) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.CYAN);
		}

		g.fillRect(x * size, y * size, size, size);
		if (color == 0) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLUE);
		}
		g.fillOval(x * size + 2, y * size + 2, size * 4 / 5, size * 4 / 5);
		char c = 0;
		if (direction == 0)
			c = (char) 9650;
		if (direction == 1)
			c = (char) 9658;
		if (direction == 2)
			c = (char) 9660;
		if (direction == 3)
			c = (char) 9668;

		g.setColor(Color.BLACK);
		g.drawString("" + c, x * size + 3, (y + 1) * size - 3);
	}
	public Robot clone() {
		return new Robot(x,y,cube,color,direction);
	}
	public static Robot[] cloneRobotArray(Robot[] robots) {
		Robot[] newRobot = new Robot[robots.length];
		for (int i = 0;i < robots.length;i++) {
			newRobot[i] = robots[i].clone();
		}
		return newRobot;
	}
	public boolean equals(Object object) {
		if (object instanceof Robot && ((Robot) object).getX() == this.x
				&& ((Robot) object).getY() == this.y && ((Robot) object).getCube() == this.cube
				&& ((Robot) object).getColor() == this.color
				&& ((Robot) object).getDirection() == this.direction
				&& ((Robot) object).isClimb() == this.climb) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean equalsRobotArray(Robot[] r1, Robot[] r2) {
		for (int i = 0;i < 3;i++) {
			if (!r1[i].equals(r2[i])) {
				return false;
			}
		}
		return true;
	}
	public void update(int action) {
		switch (action) {
		case 1:
			direction--;
			if (direction == -1)
				direction = 3;
			break;
		case 2:
			direction++;
			if (direction == 4)
				direction = 0;
			break;
		case 3:
			if (direction == 0) {

				y--;
			} else if (direction == 1) {

				x++;
			} else if (direction == 2) { 

				y++;
			} else {

				x--;
			}
			break;
		case 4:
			cube = 0;
			break;
		case 5:
			cube = 1;
			break;
		case 6:
			cube = 0;
			break;
		case 7:
			cube = 0;
			break;
		case 8:
			cube = 0;
			break;
		case 9:
			climb = true;
			break;
		default:
			break;
		}
	}

	public String toString() {
		return x + " " + y + " " + cube + " "  + color + " "+ direction;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
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
