package neuralnetwork;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Network {

	enum Cost {
		COST_QUADRATIC,
		COST_CROSSENTROPY
	}
	
	private Matrix hidden_weights;
	private Matrix output_weights;
	private Matrix hidden_bias;
	private Matrix output_bias;
	
	private Cost cost;

	public static double sigmoid(double z) {
		return 1.0 / (1.0 + Math.exp(-z));
	}
	
	public static Matrix sigmoid(Matrix z) {
		Matrix out = z;
		for(int i=0;i<z.rows();i++)
			for(int j=0;j<z.columns();j++)
				out.put(i, j, sigmoid(out.get(i, j)));
		return out;
	}
	
	public static double sigmoid_prime(double z) {
		return sigmoid(z) * (1.0 - sigmoid(z));
	}
	
	public static Matrix sigmoid_prime(Matrix z) {
		Matrix out = z;
		for(int i=0;i<z.rows();i++)
			for(int j=0;j<z.columns();j++)
				out.put(i, j, sigmoid_prime(out.get(i, j)));
		return out;
	}
	
	public Network(int input_layer, int hidden_layer, int output_layer, Cost cost) {
		hidden_weights = new Matrix(hidden_layer, input_layer);
		hidden_weights.initRandom();
		output_weights = new Matrix(output_layer, hidden_layer);
		output_weights.initRandom();
		hidden_bias = new Matrix(hidden_layer, 1);
		hidden_bias.initRandom();
		output_bias = new Matrix(output_layer, 1);
		output_bias.initRandom();
		
		this.cost = cost;
	}
	
	public Network(Matrix hidden_weights, Matrix output_weights, Matrix hidden_bias, Matrix output_bias, Cost cost) {
		this.hidden_weights = hidden_weights;
		this.output_weights = output_weights;
		this.hidden_bias = hidden_bias;
		this.output_bias = output_bias;
		this.cost = cost;
	}

	public Matrix feedforward(Matrix input) {
		System.out.println(hidden_weights);
		System.out.println(hidden_bias);
		System.out.println(output_weights);
		System.out.println(output_bias);
		Matrix prop1 = hidden_weights.times(input).plus(hidden_bias);
		Matrix prop2 = output_weights.times(prop1).plus(output_bias);
		return prop2;
	}
	
	public void sgd(ArrayList<NetworkEntry> training_data, int epochs, int mb_size, double eta) {
		int length = training_data.size();
		
		for(int i=0;i<epochs;i++) {
			Collections.shuffle(training_data);
			
			for(int j=0;j<length;j+=mb_size) {
				List<NetworkEntry> mini_batch = training_data.subList(j, j + mb_size);
				update_mini_batch(mini_batch, eta, length);
			}
		}
	}
	
	private void update_mini_batch(List<NetworkEntry> mini_batch, double eta, int n) {
		Matrix hidden_weightsG = new Matrix(hidden_weights.rows(), hidden_weights.columns());
		Matrix hidden_biasG  = new Matrix(hidden_bias.rows(), 1);
		Matrix output_weightsG = new Matrix(output_weights.rows(), output_weights.columns());
		Matrix output_biasG = new Matrix(output_bias.rows(), 1);
		
		for(NetworkEntry entry : mini_batch) {
			Matrix[] dgrad = backpropagate(entry);
			
			hidden_weightsG = hidden_weightsG.plus(dgrad[0]);
			hidden_biasG = hidden_biasG.plus(dgrad[1]);
			output_weightsG = output_weightsG.plus(dgrad[2]);
			output_biasG = output_biasG.plus(dgrad[3]);
		}
		
		for(int i=0;i<hidden_weights.rows();i++) {
			for(int j=0;j<hidden_weights.columns();j++) {
				hidden_weights.put(i, j, hidden_weights.get(i, j) - eta * hidden_weightsG.get(i, j) / mini_batch.size());
			}
		}
		for(int i=0;i<hidden_bias.columns();i++) {
			hidden_bias.put(i, 0, hidden_bias.get(i, 0) - eta * hidden_biasG.get(i, 0) / mini_batch.size());
		}
		for(int i=0;i<output_weights.rows();i++) {
			for(int j=0;j<output_weights.columns();j++) {
				output_weights.put(i, j, output_weights.get(i, j) - eta * output_weightsG.get(i, j) / mini_batch.size());
			}
		}
		for(int i=0;i<output_bias.columns();i++) {
			output_bias.put(i, 0, output_bias.get(i, 0) - eta * output_biasG.get(i, 0) / mini_batch.size());
		}
	}
	
	private Matrix[] backpropagate(NetworkEntry entry) {
		Matrix[] out = new Matrix[4];
		
		Matrix a = new Matrix(entry.getInput());
		Matrix[] activations = new Matrix[3];
		Matrix[] zValues = new Matrix[2];
		activations[0] = a;
		Matrix z = hidden_weights.times(a).plus(hidden_bias);
		zValues[0] = z;
		a = sigmoid(z);
		activations[1] = a;
		
		z = output_weights.times(a).plus(output_bias);
		zValues[1] = z;
		a = sigmoid(z);
		activations[2] = a;
		
		Matrix delta = cost_error(activations[2], new Matrix(entry.getOutput()), zValues[1]);
		out[3] = new Matrix(delta);
		out[2] = delta.times(activations[1].transpose());
		
		z = new Matrix(zValues[0]);
		Matrix sp = sigmoid_prime(z);
		delta = output_weights.transpose().times(delta).hadamard(sp);
		out[1] = new Matrix(delta);
		out[0] = delta.times(activations[0].transpose());
		
		return out;
	}
	
	private Matrix cost_error(Matrix output_activations, Matrix y, Matrix z) {
		switch(cost) {
		case COST_QUADRATIC:
			return output_activations.minus(y).hadamard(sigmoid_prime(z));
		case COST_CROSSENTROPY:
			return output_activations.minus(y);
		default:
			return output_activations.minus(y).hadamard(sigmoid_prime(z));
		}
	}
}
