package neuralnetwork;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TrainingDataLoader {

	public static ArrayList<NetworkEntry> training_data = new ArrayList<NetworkEntry>();
	
	public static boolean loadTrainingData(String path) {
		Scanner scanner = null;
		training_data.clear();
		try {
			scanner = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] data1 = line.split(";");
			String[] inputs = data1[0].split(" ");
			String[] outputs = data1[1].split(" ");
			Matrix inputMatrix = new Matrix(inputs.length,1);
			Matrix outputMatrix = new Matrix(outputs.length,1);
			for(int i=0;i<inputs.length;i++) {
				
				inputMatrix.put(i, 0, Double.parseDouble(inputs[i]));
			}
			for(int i=0;i<outputs.length;i++)
				outputMatrix.put(i, 0, Double.parseDouble(outputs[i]));
			training_data.add(new NetworkEntry(inputMatrix, outputMatrix));	
		}
		
		scanner.close();
		
		return false;
	}
	
}
