package qlearning.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Game {

	private Arena arena;
	private Robot allyRobot;
	private Graphics g;
	private int gameDuration = 150;
	private int time;
	private int[] actions;
	public Game(Arena arena, Robot allyRobot) {
		this.arena = arena;
		this.allyRobot = allyRobot;
		this.g = arena.getG();

	}

	public void play(int[] actions) {
		this.actions = actions;
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		time = 0;
		draw();
		
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
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	private void draw() {
		arena.draw();
		allyRobot.draw(g, arena.getSize());
		g.setColor(Color.BLACK);
		g.drawString("time = " + time, 5, 25);
	}
	
	public void update() {
		
		arena.update();
		allyRobot.update(actions[time - 1]);
	}

}
