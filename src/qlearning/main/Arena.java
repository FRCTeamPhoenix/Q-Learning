package qlearning.main;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;

public class Arena {
	private String fileName;
	private int arenaHeight;
	private int arenaWidth;
	private char[][] layout;
	private int[][] states;
	private int size = 20;
	DrawingPanel panel;
	private Graphics g;

	public Arena(String fileName, int arenaHeight, int arenaWidth) {
		this.arenaHeight = arenaHeight;
		this.arenaWidth = arenaWidth;
		this.fileName = fileName;
		layout = new char[arenaHeight][arenaWidth];
		states = new int[arenaHeight][arenaWidth];
		panel = new DrawingPanel(arenaWidth * size, arenaHeight * size);
		g = panel.getGraphics();
		createArena();
	}

	public Arena(char[][] layout, int arenaHeight, int arenaWidth) {
		this.arenaHeight = arenaHeight;
		this.arenaWidth = arenaWidth;
		this.layout = layout;
		panel = new DrawingPanel(arenaWidth * size, arenaHeight * size);
		g = panel.getGraphics();
	}
	public void edit(int x,int y, char c) {
		layout[y][x] = c;
	}

	private void createArena() {
		try (FileInputStream fis = new FileInputStream(new File(fileName))) {

			int i = 0;
			int j = 0;

			int content;
			System.out.println("Start reading file");
			while ((content = fis.read()) != -1) {
				char c = (char) content;
				if (!Character.isLetterOrDigit(c)) {
					continue;
				}
				layout[i][j] = c;
				
				
				j++;
				if (j == arenaWidth) {
					j = 0;
					i++;
				}
			}
//			for (int r = 0;r < arenaHeight;r++) {
//				for (int c = 0;c < arenaWidth;c++) {
//					System.out.print(layout[r][c]);
//				}
//				System.out.println();
//			}
//			System.out.println("done reading arena");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int[][] getStates() {
		return states;
	}

	public void setStates(int[][] states) {
		this.states = states;
	}

	public void draw() {
		panel.clear();
		for (int i = 0; i < arenaHeight; i++) {
			for (int j = 0; j < arenaWidth; j++) {

				if (layout[i][j] == 'B' || layout[i][j] == 'Q' || layout[i][j] == 'P' || layout[i][j] == 'E' || layout[i][j] == 'A' || layout[i][j] == 'H') {
					g.setColor(Color.RED);
				} else if (layout[i][j] == 'b' ||layout[i][j] == 'q' || layout[i][j] == 'p' || layout[i][j] == 'e' || layout[i][j] == 'a' || layout[i][j] == 'h') {
					g.setColor(Color.BLUE);
				} else if (layout[i][j] == 'L' || layout[i][j] == 'l') {
					g.setColor(Color.BLACK);
				} else if (layout[i][j] == 'W' || layout[i][j] == 'w' || layout[i][j] == 'T' || layout[i][j] == 'S'
						|| layout[i][j] == 's' || layout[i][j] == 'V' || layout[i][j] == 'v') {
					g.setColor(Color.GRAY);
				} else if (layout[i][j] == 'Z' || layout[i][j] == 'z' || layout[i][j] == 'C' || layout[i][j] == 'c') {
					g.setColor(Color.ORANGE);
				} else if (layout[i][j] == 'N') {
					g.setColor(Color.YELLOW);
				}
				if (layout[i][j] != '0') {
					g.fillRect(j * size, i * size, size, size);
				}

			}
		}
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getArenaHeight() {
		return arenaHeight;
	}

	public void setArenaHeight(int arenaHeight) {
		this.arenaHeight = arenaHeight;
	}

	public int getArenaWidth() {
		return arenaWidth;
	}

	public void setArenaWidth(int arenaWidth) {
		this.arenaWidth = arenaWidth;
	}

	public char[][] getLayout() {
		return layout;
	}

	public void setLayout(char[][] layout) {
		this.layout = layout;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Graphics getG() {
		return g;
	}

	public void setG(Graphics g) {
		this.g = g;
	}

}
