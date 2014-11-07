package tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphScanner {

	/** Method which scans for the number of vertices specified in the .txt file given.
	 *  Please note that the file must be configured correctly.  See the supplemented notes
	 *  (if provided by the developer). 
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static int getVerticesNumber(File file) throws Exception {
		Scanner scanner = new Scanner(file);
		String scannedLine;
		int noOfVerts;
		
		while (scanner.hasNextLine()) {
			scannedLine = scanner.nextLine();
			if(scannedLine.contains("number_of_vertices=")) {
				int header = "number_of_vertices=".length();
				try{
					noOfVerts = Integer.parseInt(scannedLine.substring(header, scannedLine.length()));
				} catch (NumberFormatException nfe) {
					//Throw an exception as after "number_of_vertices=" is not a number
					throw new NumberFormatException("Input was incorrectly formatted, please follow the guidelines.");
				}
				scanner.close();
				return noOfVerts;
			}
		}
		throw new Exception("Did not find the number of vertices, please follow the guidelines on how to format the input .txt file.");
	}
	
	/** Method which extracts the adjacency matrix of the graph, in the specified .txt file.
	 *  Please note that the file must be configured as specified in the guidelines.
	 *  
	 * @param file
	 * @param numberOfVertices
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<ArrayList<Boolean>> getAdjacencyMatrix(File file, int numberOfVertices) throws Exception {
		Scanner scanner = new Scanner(file);
		String scannedLine;
		ArrayList<ArrayList<Boolean>> adjMatrix = new ArrayList<ArrayList<Boolean>>();
		int rowIndex = 0;
		
		while (scanner.hasNextLine()) {	//Find the line with "begin_matrix"
			scannedLine = scanner.nextLine();
			if (scannedLine.contains("begin_matrix"))
				break;
		}
		while (scanner.hasNextLine() && rowIndex < numberOfVertices) {	//Read in the adjacency matrix
			scannedLine = scanner.nextLine();
			if(scannedLine.isEmpty()||scannedLine.startsWith("%"))	//Ignore this line as empty or commented
				continue;
			String[] edges = scannedLine.split("\\s");
			if (edges.length != numberOfVertices)	//Check that the quantity of values match number of vertices
				throw new Exception("The number of vertices on the adjacency matrix, does not match the number of vertices specified in the .txt file.  Please ensure your input .txt file is correctly formatted.");
			for (int i=0;i<edges.length;i++){
				if (edges[i].isEmpty())
					continue;
				if (i==0) {
					adjMatrix.add(new ArrayList<Boolean>());
				}
				if(edges[i].equals("1")) {
					ArrayList<Boolean> row = adjMatrix.get(rowIndex);
					row.add(i, true);
					adjMatrix.set(rowIndex, row);
				} else if (edges[i].equals("0")){
					ArrayList<Boolean> row = adjMatrix.get(rowIndex);
					row.add(i,false);
					adjMatrix.set(rowIndex, row);
				} else
					throw new Exception("Please format the adjacency matrix properly, please refer to the guidelines.  Remember t=true, f=false.");
			}
			rowIndex++;
		}
		scanner.close();
		return adjMatrix;
	}
	
	/** Method which extracts the distribution of the vertices (what strategies each vertex is playing).
	 * 
	 * @param file
	 * @param numberOfVertices
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Integer> getVerticesDistribution(File file, int numberOfVertices) throws Exception {
		Scanner scanner = new Scanner(file);
		String scannedLine;
		ArrayList<Integer> vertices = new ArrayList<Integer>();
		int vertexIndex = 0;
		
		while (scanner.hasNextLine()) {	//Scan until "begin_distribution" is found
			scannedLine = scanner.nextLine();
			if (scannedLine.contains("begin_distribution"))
				break;
		}
		while (scanner.hasNextLine() && vertexIndex < numberOfVertices) {	//Get the strategy distribution in the file
			scannedLine = scanner.nextLine();
			if(scannedLine.isEmpty()||scannedLine.startsWith("%"))
				continue;
			String[] distribution = scannedLine.split("\\s");
			for (int i=0;i<distribution.length;i++) {
				if (vertexIndex<=numberOfVertices) {
					if (distribution[i].equals("1")) {
						vertices.add(1);
					} else if (distribution[i].equals("0")) {
						vertices.add(0);
					} else
						throw new Exception("Please format the vertices distribution, as detailed by the guidelines.");
				}
				vertexIndex++;
			}
		}
		scanner.close();
		return vertices;
	}
	
	/** Method which checks if the .txt file contains the keyword random.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static boolean isRandom(File file) throws Exception {
		boolean random = false;
		Scanner scanner = new Scanner(file);
		String scannedLine;
		
		while (scanner.hasNextLine()) {
			scannedLine = scanner.nextLine();
			if (scannedLine.contains("random_graph")) {
				random = true;
				break;
			}
		}
		return random;
	}
	
	/** Checks if the distribution is random.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static boolean isRandomDistribution(File file) throws Exception {
		boolean random = false;
		Scanner scanner = new Scanner(file);
		String scannedLine;
		
		while (scanner.hasNextLine()) {
			scannedLine = scanner.nextLine();
			if (scannedLine.contains("random_distribution")) {
				random = true;
				break;
			}
		}
		return random;
	}
	
	/** Method which extracts the payoff values specified by the .txt file given.
	 *  Payoff values must be specified in a specific format detailed in the guidelines.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static double[] getPayoffValues(File file) throws Exception {
		Scanner scanner = new Scanner(file);
		String scannedLine;
		String payoffValuesString = "";
		
		while (scanner.hasNextLine()) {	//Find "payoff_values=" then read the next values
			scannedLine = scanner.nextLine();
			if (scannedLine.contains("payoff_values=")){
				int index = scannedLine.indexOf("payoff_values=", 0);
				payoffValuesString = scannedLine.substring(index+"payoff_values=".length());
				break;
			}
		}
		
		String[] strPayoffValues = payoffValuesString.split(",");
		double[] payoffValues = new double[4];
		if (!(strPayoffValues.length==4))	//Make sure there is the correct amount of payoff values
			throw new Exception("Payoff values given was not a complete list, please refer to the guidelines.");
		for (int i=0;i<4;i++) {
			try {
				payoffValues[i] = Double.parseDouble(strPayoffValues[i]);
			} catch (NumberFormatException nfe) {
				throw new NumberFormatException("Payoff values specified in an incorrect format, please refer to the guidelines.");
			}
		}
		return payoffValues;
	}
	
}
