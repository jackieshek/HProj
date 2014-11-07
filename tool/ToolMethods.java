package tool;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Component;
import java.io.*;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/** Class which contains an assortment of methods for ease of use
 *  in other classes.
 * 
 * @author Jackie, s1035578
 *
 */
public class ToolMethods {

	/** Tries to parse the given string into an integer.
	 *  If successful, it returns true.
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	/** Checks if a given integer, is in the given array list of integers.
	 * 
	 * @param num
	 * @param list
	 * @return
	 */
	public static boolean isIn(int num, ArrayList<Integer> list) {
		for (int i=0;i<list.size();i++) {
			if (num == list.get(i)) {
				return true;
			}
		}
		return false;
	}
	
	/** Checks if a given integer, is in the given array list of integers.
	 * 
	 * @param num
	 * @param list
	 * @return
	 */
	public static boolean isIn(Integer num, ArrayList<Integer> list) {
		for (int i=0;i<list.size();i++) {
			if (num == list.get(i)) {
				return true;
			}
		}
		return false;
	}
	
	/** Get's the list of vertices, by their index.
	 * 
	 * @param verticesDistribution
	 * @return
	 */
	public static ArrayList<Integer> getVerticesList(ArrayList<Integer> verticesDistribution) {
		ArrayList<Integer> verticesIndex = new ArrayList<Integer>();
		for (int i=0;i<verticesDistribution.size();i++) {
			if (verticesDistribution.get(i)!=null && (verticesDistribution.get(i)==0 || verticesDistribution.get(i)==1))
				verticesIndex.add(i);
		}
		return verticesIndex;
	}
	
	/** Mainly a debugging method, which displays a given adjMatrix. 
	 * 
	 * @param adjMatrix
	 */
	public static void adjMatrixDisplay(ArrayList<ArrayList<Boolean>> adjMatrix) {
		int numberOfVertices = adjMatrix.size();
		System.out.println("----ADJACENCY MATRIX----");
		for (int i=0;i<numberOfVertices;i++) {
			ArrayList<Boolean> row = adjMatrix.get(i);
			for (int j=0;j<numberOfVertices;j++) {
				boolean edge = row.get(j);
				System.out.print(edge + " ");
			}
			System.out.println();
		}
	}
	
	/** Mainly a debugging method, which displays a given vertex distribution.
	 * 
	 * @param vertDist
	 */
	public static void vertDistrDisplay(ArrayList<Boolean> vertDist) {
		int numberOfVertices = vertDist.size();
		System.out.println("--VERTEX DISTRIBUTION--");
		for (int i=0;i<numberOfVertices;i++) {
			if (vertDist.get(i)!=null) {
				boolean vertex = vertDist.get(i);
				System.out.print(vertex + " ");
			} else
				System.out.print("null ");
		}
		System.out.println();
	}
	
	/** Displays the payoff values, given the payoff values.
	 * 
	 * @param payoffs
	 */
	public static void payoffDisplay(int[] payoffs) {
		System.out.println("-----PAYOFF VALUES-----");
		System.out.println(" Defect-Coop: "+payoffs[0]);
		System.out.println(" Coop-Coop: "+payoffs[1]);
		System.out.println(" Defect-Defect: "+payoffs[2]);
		System.out.println(" Coop-Defect: "+payoffs[3]);
		System.out.println();
	}
	
	/** Given a list of subgraphs, displays them textually.
	 * 
	 * @param subgraphs
	 */
	public static void subgraphsDisplay(ArrayList<ArrayList<Integer>> subgraphs) {
		System.out.println("---SUBGRAPHS DISPLAY---");
		for (int i=0;i<subgraphs.size();i++) {
			System.out.println(" Subgraph " + i + ":");
			System.out.print("  Vertices: ");
			for (int j=0;j<subgraphs.get(i).size();j++) {
				System.out.print(" "+subgraphs.get(i).get(j));
			}
			System.out.println();
		}
	}
	
	/** Extracts the list of vertices, when given from user input as a string.
	 *  Returns null, if it is incorrectly formatted.
	 * @param stringList
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Integer> extractVertices(String stringList) throws Exception {
		String[] stringVertices = stringList.split(",");
		ArrayList<Integer> vertices = new ArrayList<Integer>();
		
		for (int i=0;i<stringVertices.length;i++) {
			if (isInteger(stringVertices[i]))
				vertices.add(Integer.parseInt(stringVertices[i]));
			else {
				vertices = null;
				break;
			}
		}
		
		return vertices;
	}
	
	/** Given a list of edges (as tuples) in the format of a string, returns two arrays of integers
	 *  as an array of array of integers, where int[0] = list of head vertices of the given edges; 
	 *  int[1] is the tail vertices.
	 *  
	 *  Example: Suppose given the string - "(0,1);(0,2);(1,3);(1,4)" then the return value is
	 *  int[0] = {0,0,1,1}; int[1] = {1,2,3,4}.
	 * 
	 * @param stringList
	 * @param comp
	 * @return
	 */
	public static Integer[][] extractEdges(String stringList) {
		String[] edgeTuples = stringList.split(";");
		Integer[] headVertices = new Integer[edgeTuples.length];
		Integer[] tailVertices = new Integer[edgeTuples.length];
		
		for (int i=0;i<edgeTuples.length;i++) {
			if (edgeTuples[i].matches("\\(\\d+\\,\\d+\\)")) {
				int comSplit = edgeTuples[i].indexOf(",");
				headVertices[i] = Integer.parseInt(edgeTuples[i].substring(1, comSplit));
				tailVertices[i] = Integer.parseInt(edgeTuples[i].substring(comSplit+1, edgeTuples[i].length()-1));
				
			} else {
				return null;
			}
			
		}
		Integer[][] edges = new Integer[][] {
				headVertices, tailVertices
		};
		return edges;
	}
	
	@Deprecated
	/** Checks if a given tuple, is in the form of an edge tuple.
	 * 
	 * @param edgeTuple
	 * @return
	 */
	public static boolean isEdgeTuple(String edgeTuple) {
		if (edgeTuple.matches("\\(\\d+\\,\\d+\\)"))
			return true;
		return false;
	}
	
	/** A method which given a game (and it's associated graph) checks if the graph is completely dense
	 *  i.e. an edge exists between all pairings of the vertices of the graph
	 * @param game
	 * @return
	 */
	public static boolean isDense(GameGraph game) {
		for (int i=0;i<game.getNumberOfVertices();i++) {
			for (int j=0; j<game.getNumberOfVertices();j++) {
				if (i!=j) {	//May remove this
					if (!game.getAdjMatrix().get(i).get(j)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/** Returns the integer and fractional parts of a number, as pair of ints.
	 *  The fractional part is multiplied by 100.
	 *  
	 * @param number
	 * @return
	 */
	public static int[] getIntegerAndFractionalParts(double number) {
		int integerPart = (int) Math.floor(number);
		double fractionalPart = number % 1;
		int uppedFractional = (int) (fractionalPart*100);
		
		return new int[] {integerPart,uppedFractional};		
	}
	
	/** Given an adjacency matrix, checks to see the graph it represents is connected.
	 * 
	 * @param adjMatrix
	 * @param vertex1
	 * @param vertex2
	 * @return
	 */
	public static boolean isConnected(ArrayList<ArrayList<Boolean>> adjMatrix, int vertex1, int vertex2) {
		ArrayList<ArrayList<Integer>> subgraphs = getListOfDisconnectedGraphs(adjMatrix);
		
		for (int i=0;i<subgraphs.size();i++) {
			if (isIn(vertex1, subgraphs.get(i)) && isIn(vertex2, subgraphs.get(i)))
				return true;
		}
		return false;
	}
	
	/** Given an adjacency matrix, returns a list of disconnected components of the graph.
	 * 
	 * @param adjMatrix
	 * @return
	 */
	public static ArrayList<ArrayList<Integer>> getListOfDisconnectedGraphs(ArrayList<ArrayList<Boolean>> adjMatrix) {
		ArrayList<ArrayList<Integer>> intListOfSubgraphs = new ArrayList<ArrayList<Integer>>();
		ArrayList<Boolean> assignedVertices = createFalseArray(adjMatrix.size());
		
		while (!allTrue(assignedVertices)) {
			ArrayList<Integer> subgraph = new ArrayList<Integer>();
			int visited = 0;
			boolean actualSubgraph = false;
			int index = getFirstFalse(assignedVertices);
			subgraph.add(index);
			assignedVertices.set(index, true);
			
			while (!actualSubgraph) {
				boolean changed = false;
				for (int i=0;i<adjMatrix.size();i++) {
					if (!assignedVertices.get(i)) {
						if (adjMatrix.get(index).get(i)) {
							if (!isIn(i,subgraph)) {
								subgraph.add(i);
								assignedVertices.set(i, true);
								changed = true;
							}
						}
					}
				}
				visited++;
				if (!changed && visited==subgraph.size())
					actualSubgraph = true;
				else
					index = subgraph.get(visited);
			}
			intListOfSubgraphs.add(subgraph);
		}
		return intListOfSubgraphs;
	}
	
	/** Given an adjacency matrix, it ensures the graph is connected,
	 *  i.e. it has no disconnected components.
	 * 
	 * @param adjMatrix
	 */
	public static void connectGraph(ArrayList<ArrayList<Boolean>> adjMatrix) {
		while (!completedConnectedGraph(adjMatrix)) {
			ArrayList<ArrayList<Integer>> graphList = getListOfDisconnectedGraphs(adjMatrix);
			
			int randSub1 = (int) (Math.random()*graphList.size());
			int randSub2 = (int) (Math.random()*graphList.size());
			
			while (randSub1==randSub2) {
				if (graphList.size()==2) {
					randSub1 = 0;
					randSub2 = 1;
				} else {
					randSub2 = (int) (Math.random()*graphList.size());
				}
			}
			
			int randSub1Index = (int) (Math.random()*graphList.get(randSub1).size());
			int randSub2Index = (int) (Math.random()*graphList.get(randSub2).size());
			
			int randSub1Vertex = graphList.get(randSub1).get(randSub1Index);
			int randSub2Vertex = graphList.get(randSub2).get(randSub2Index);
			
			adjMatrix.get(randSub1Vertex).set(randSub2Vertex, true);
			adjMatrix.get(randSub2Vertex).set(randSub1Vertex, true);
		}
	}
	
	/** Checks if a graph is connected.
	 * 
	 * @param adjMatrix
	 * @return
	 */
	public static boolean completedConnectedGraph(ArrayList<ArrayList<Boolean>> adjMatrix) {
		ArrayList<ArrayList<Integer>> graphList = getListOfDisconnectedGraphs(adjMatrix);
		if (graphList.size()>1)
			return false;
		return true;
	}
	
	/** Checks if all values of an array list are true;
	 * 
	 * @param booleanList
	 * @return
	 */
	public static boolean allTrue(ArrayList<Boolean> booleanList) {
		for (int i=0;i<booleanList.size();i++) {
			if (!booleanList.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	/** Checks if all values of an array are 1.
	 * 
	 * @param booleanList
	 * @return
	 */
	public static boolean allTrueInteger(ArrayList<Integer> booleanList) {
		for (int i=0;i<booleanList.size();i++) {
			if (booleanList.get(i)==0) {
				return false;
			}
		}
		return true;
	}
	
	/** Checks if all values of an array are true.
	 * 
	 * @param booleanList
	 * @return
	 */
	public static boolean allTrue(boolean[] booleanList) {
		for (int i=0;i<booleanList.length;i++) {
			if (!booleanList[i])
				return false;
		}
		return true;
	}
	
	/** Checks if all values of an array list are false.
	 * 
	 * @param booleanList
	 * @return
	 */
	public static boolean allFalse(ArrayList<Integer> booleanList) {
		for (int i=0;i<booleanList.size();i++) {
			if (booleanList.get(i)==1) {
				return false;
			}
		}
		return true;
	}
	
	/** Checks if all values of an array are false.
	 * 
	 * @param booleanList
	 * @return
	 */	
	public static boolean allFalse(int[] booleanList) {
		for (int i=0;i<booleanList.length;i++) {
			if (booleanList[i]==1)
				return false;
		}
		return true;
	}
	
	/** Sets all values of an array list to false and returns it.
	 * 
	 * @param booleanList
	 * @return
	 */
	public static ArrayList<Boolean> setAllFalse(ArrayList<Boolean> booleanList) {
		for (int i=0;i<booleanList.size();i++) {
			booleanList.set(i, false);
		}
		return booleanList;
	}
	
	/** Initiates an array list with all entries as false and returns it.
	 * 
	 * @param noOfVertices
	 * @return
	 */
	public static ArrayList<Boolean> createFalseArray(int noOfVertices) {
		ArrayList<Boolean> booleanList = new ArrayList<Boolean>();
		for (int i=0;i<noOfVertices;i++) {
			booleanList.add(false);
		}
		return booleanList;
	}
	
	/** Initiates an adjacency matrix with all entries as false and returns it.
	 * 
	 * @param noOfVertices
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> createFalseAdjMatrix(int noOfVertices) {
		ArrayList<ArrayList<Boolean>> adjMatrix = new ArrayList<ArrayList<Boolean>>(); 
		for (int i=0;i<noOfVertices;i++) {
			ArrayList<Boolean> falseArray = createFalseArray(noOfVertices);
			adjMatrix.add(falseArray);
		}
		return adjMatrix;
	}
	
	/** Returns the index of the first entry that contains false.
	 *  Returns -1 if no such entry exists.
	 * 
	 * @param booleanList
	 * @return
	 */
	public static int getFirstFalse(ArrayList<Boolean> booleanList) {
		for (int i=0;i<booleanList.size();i++) {
			if (!booleanList.get(i))
				return i;
		}
		return -1;
	}
	
	/** Returns the index of the first entry that contains false.
	 *  Returns -1 if no such entry exists.
	 * 
	 * @param booleanList
	 * @return
	 */
	public static int getFirstFalse(boolean[] booleanList) {
		for (int i=0;i<booleanList.length;i++) {
			if (!booleanList[i])
				return i;
		}
		return -1;
	}
	
	/** Returns an array list of all indices which entries are true.
	 * 
	 * @param booleanList
	 * @return
	 */
	public static ArrayList<Integer> getAllTrue(ArrayList<Boolean> booleanList) {
		ArrayList<Integer> allTrue = new ArrayList<Integer>();
		
		for (int i=0;i<booleanList.size();i++) {
			if (booleanList.get(i))
				allTrue.add(i);
		}
		return allTrue;
	}
	
	/** Returns an array list of all indices which entries are false.
	 * 
	 * @param booleanList
	 * @return
	 */
	public static ArrayList<Integer> getAllFalse(ArrayList<Boolean> booleanList) {
		ArrayList<Integer> allFalse = new ArrayList<Integer>();
		
		for (int i=0;i<booleanList.size();i++) {
			if (!booleanList.get(i))
				allFalse.add(i);
		}
		return allFalse;
	}
	
	/** Creates an array list containing all entries with the value 0.
	 * 
	 * @param noOfVertices
	 * @return
	 */
	public static ArrayList<Integer> createZeroArray(int noOfVertices) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (int i=0;i<noOfVertices;i++) {
			array.add(0);
		}
		return array;
	}
	
	/** Creates an array list containing all entries with the value 1.
	 * 
	 * @param noOfVertices
	 * @return
	 */
	public static ArrayList<Integer> createOneArray(int noOfVertices) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (int i=0;i<noOfVertices;i++) {
			array.add(1);
		}
		return array;
	}
}
