package neuralnetwork;
public class NetworkEntry {

	Matrix input;
	Matrix output;
	
	public NetworkEntry(Matrix input, Matrix output) {
		this.input = input;
		this.output = output;
	}

	public Matrix getInput() {
		return input;
	}

	public Matrix getOutput() {
		return output;
	}
	
}
