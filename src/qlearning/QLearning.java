package qlearning;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;

public class QLearning {

    private final double alpha = 0.1; // Learning rate
    private final double gamma = 0.999; // Eagerness - 0 looks in the near future, 1 looks in the distant future

    private final int arenaWidth = 10;
    private final int arenaHeight = 10;
    private final int statesCount = arenaHeight * arenaWidth;
    private final int reward = 100;
    private final int penalty = -10;

    private char[][] arena;  // arena read from file
    private int[][] R;       // Reward lookup
    private double[][] Q;    // Q learning
    
    //GUI
    private int size = 50;

    public static void main(String args[]) throws InterruptedException {
        QLearning ql = new QLearning();
        DrawingPanel panel = new DrawingPanel(ql.getarenaWidth() * ql.getSize(),ql.getarenaHeight() * ql.getSize());
        Graphics g = panel.getGraphics();
        
        
        

        for (int i = 0;i < 100;i++) {
        ql.init();
        ql.calculateQ();
        ql.printQ();
        ql.printPolicy();
        ql.result(g);
        }
        
    }
    public char[][] generateMap() {
    	Random rand = new Random();
    	char[][] c = new char[arenaHeight][arenaWidth];
    	for (int i = 0;i < arenaHeight;i++) {
    		for (int j = 0;j < arenaWidth;j++) {
    			if (3 <= rand.nextInt(10)) {
    				c[i][j] = '0';
    			} else {
    				c[i][j] = 'X';
    			}
    				
    				
    		}
    	}
    	c[0][0] = 'R';

    	c[arenaHeight - 1][arenaWidth-1] = 'F';
    	return c;
    }
    public void result(Graphics g) throws InterruptedException {
    	drawarena(g);
    	Thread.sleep(3000);

    	while(!win()) {
    		updatearena();
    		drawarena(g);
    		Thread.sleep(250);
    	}
    }
    
    public int currentState() {
    	int index = 0;
    	for (int i = 0;i < arenaHeight;i++) {
    		for (int j = 0;j < arenaWidth;j++) {
    			if (arena[i][j] == 'R') {
    				index =  i * arenaWidth + j;
    			}
    		}
    	}
		return index;
    }
    public void updatearena() {
    	int crtState = currentState();
    	int nxtState = getPolicyFromState(currentState());
    	
    	arena[crtState / arenaWidth][crtState - crtState / arenaWidth * arenaWidth] = '0';
    	if (arena[nxtState / arenaWidth][nxtState - nxtState / arenaWidth * arenaWidth] == 'F') {
    		arena[nxtState / arenaWidth][nxtState - nxtState / arenaWidth * arenaWidth] = 'W';
    	} else {
    		arena[nxtState / arenaWidth][nxtState - nxtState / arenaWidth * arenaWidth] = 'R';
    	}
    }
    
    
    public boolean win() {
    	for (int i = 0;i < arenaHeight;i++) {
    		for (int j = 0;j < arenaWidth;j++) {
    			if (arena[i][j] == 'W') {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    public void drawarena(Graphics g) {
    	for (int i = 0;i < arenaHeight;i++) {
    		for (int j = 0;j < arenaWidth;j++) {
    				switch(arena[i][j]) {
    			case '0': g.setColor(Color.WHITE);
    						break;
    			case 'F': g.setColor(Color.GREEN);
    						break;
    			case 'X': g.setColor(Color.GRAY);
    						break;			
    			case 'R': g.setColor(Color.BLUE);
    						break;
    			case 'W': g.setColor(Color.RED);
    			default: break;
    			}
    			g.fillRect(j * size, i * size, size, size);
    		}
    	}
    }

    public void init() {
        File file = new File("resources/map.txt");

        R = new int[statesCount][statesCount];
        Q = new double[statesCount][statesCount];
        arena = new char[arenaHeight][arenaWidth];


        try (FileInputStream fis = new FileInputStream(file)) {

            int i = 0;
            int j = 0;

            int content;

            // Read the arena from the input file
            while ((content = fis.read()) != -1) {
                char c = (char) content;
                if (c != '0' && c != 'F' && c != 'X' && c != 'R') {
                    continue;
                }
                arena[i][j] = c;
                j++;
                if (j == arenaWidth) {
                    j = 0;
                    i++;
                }
            }
            arena = generateMap();

            // We will navigate through the reward matrix R using k index
            for (int k = 0; k < statesCount; k++) {

                // We will navigate with i and j through the arena, so we need
                // to translate k into i and j
                i = k / arenaWidth;
                j = k - i * arenaWidth;

                // Fill in the reward matrix with -1
                for (int s = 0; s < statesCount; s++) {
                    R[k][s] = -1;
                }

                // If not in final state or a wall try moving in all directions in the arena
                if (arena[i][j] != 'F') {

                    // Try to move left in the arena
                    int goLeft = j - 1;
                    if (goLeft >= 0) {
                        int target = i * arenaWidth + goLeft;
                        if (arena[i][goLeft] == '0' || arena[i][goLeft] == 'R') {
                            R[k][target] = 0;
                        } else if (arena[i][goLeft] == 'F') {
                            R[k][target] = reward;
                        } else  {
                            R[k][target] = penalty;
                        }
                    }

                    // Try to move right in the arena
                    int goRight = j + 1;
                    if (goRight < arenaWidth) {
                        int target = i * arenaWidth + goRight;
                        if (arena[i][goRight] == '0') {
                            R[k][target] = 0;
                        } else if (arena[i][goRight] == 'F') {
                            R[k][target] = reward;
                        } else {
                            R[k][target] = penalty;
                        }
                    }

                    // Try to move up in the arena
                    int goUp = i - 1;
                    if (goUp >= 0) {
                        int target = goUp * arenaWidth + j;
                        if (arena[goUp][j] == '0') {
                            R[k][target] = 0;
                        } else if (arena[goUp][j] == 'F') {
                            R[k][target] = reward;
                        } else {
                            R[k][target] = penalty;
                        }
                    }

                    // Try to move down in the arena
                    int goDown = i + 1;
                    if (goDown < arenaHeight) {
                        int target = goDown * arenaWidth + j;
                        if (arena[goDown][j] == '0') {
                            R[k][target] = 0;
                        } else if (arena[goDown][j] == 'F') {
                            R[k][target] = reward;
                        } else {
                            R[k][target] = penalty;
                        }
                    }
                }
            }
            printR(R);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public char[][] getarena() {
		return arena;
	}

 
	public void setarena(char[][] arena) {
		this.arena = arena;
	}


	public int[][] getR() {
		return R;
	}


	public void setR(int[][] r) {
		R = r;
	}


	public double[][] getQ() {
		return Q;
	}


	public void setQ(double[][] q) {
		Q = q;
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}


	public double getAlpha() {
		return alpha;
	}


	public double getGamma() {
		return gamma;
	}


	public int getarenaWidth() {
		return arenaWidth;
	}


	public int getarenaHeight() {
		return arenaHeight;
	}


	public int getStatesCount() {
		return statesCount;
	}


	public int getReward() {
		return reward;
	}


	public int getPenalty() {
		return penalty;
	}


	// Used for debug
    void printR(int[][] matrix) {
        System.out.printf("%25s", "States: ");
        for (int i = 0; i <= 8; i++) {
            System.out.printf("%4s", i);
        }
        System.out.println();

        for (int i = 0; i < statesCount; i++) {
            System.out.print("Possible states from " + i + " :[");
            for (int j = 0; j < statesCount; j++) {
                System.out.printf("%4s", matrix[i][j]);
            }
            System.out.println("]");
        }
    }

    void calculateQ() {
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) { // Train cycles
            // Select random initial state
            int crtState = rand.nextInt(statesCount);

            while (!isFinalState(crtState)) {
                int[] actionsFromCurrentState = possibleActionsFromState(crtState);

                // Pick a random action from the ones possible
                int index = rand.nextInt(actionsFromCurrentState.length);
                int nextState = actionsFromCurrentState[index];

                // Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
                double q = Q[crtState][nextState];
                double maxQ = maxQ(nextState);
                int r = R[crtState][nextState];

                double value = q + alpha * (r + gamma * maxQ - q);
                Q[crtState][nextState] = value;

                crtState = nextState;
            }
        }
    }

    boolean isFinalState(int state) {
        int i = state / arenaWidth;
        int j = state - i * arenaWidth;

        return arena[i][j] == 'F';
    }

    int[] possibleActionsFromState(int state) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < statesCount; i++) {
            if (R[state][i] != -1) {
                result.add(i);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    double maxQ(int nextState) {
        int[] actionsFromState = possibleActionsFromState(nextState);
        double maxValue = Double.MIN_VALUE;
        for (int nextAction : actionsFromState) {
            double value = Q[nextState][nextAction];

            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    void printPolicy() {
        System.out.println("\nPrint policy");
        for (int i = 0; i < statesCount; i++) {
            System.out.println("From state " + i + " goto state " + getPolicyFromState(i));
        }
    }

    int getPolicyFromState(int state) {        int[] actionsFromState = possibleActionsFromState(state);

        double maxValue = Double.MIN_VALUE;
        int policyGotoState = state;

        // Pick to move to the state that has the maximum Q value
        for (int nextState : actionsFromState) {
            double value = Q[state][nextState];

            if (value > maxValue) {
                maxValue = value;
                policyGotoState = nextState;
            }
        }
        return policyGotoState;
    }

    void printQ() {
        System.out.println("Q matrix");
        for (int i = 0; i < Q.length; i++) {
            System.out.print("From state " + i + ":  ");
            for (int j = 0; j < Q[i].length; j++) {
                System.out.printf("%6.2f ", (Q[i][j]));
            }
            System.out.println();
        }
    }
}
