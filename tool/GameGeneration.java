package tool;

import java.util.ArrayList;

/** Class which containing methods that generate the graph and the
 *  starting distribution of spatial games.
 * 
 * @author Jackie, s1035578
 *
 */
public class GameGeneration {
	
	/** Given the number of vertices for the graph, and an edge
	 *  probability, this method uses the Erdos-Renyi model to 
	 *  generate a random graph.  The variant of the Erdos-Renyi 
	 *  model used here is the method where each edge is given
	 *  a probability of being present in the graph. 
	 * 
	 * @param noOfVertices
	 * @param probability
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateERGraph(int noOfVertices, double probability) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		for (int i=0;i<noOfVertices;i++) {
			for (int j=0;j<i;j++) {
				double rand = Math.random();
				if (probability>rand) {
					adjMatrix.get(i).set(j, true);
					adjMatrix.get(j).set(i, true);
				}
			}
		}
		return adjMatrix;
	}
	
	/** Generates a connected a random graph, namely using the 
	 *  generateERGraph method, and then ensures the whole graph
	 *  is connected.
	 * 
	 * @param noOfVertices
	 * @param probability
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateConnectedERGraph(int noOfVertices, double probability) {
		ArrayList<ArrayList<Boolean>> adjMatrix = generateERGraph(noOfVertices, probability);
		
		while (!ToolMethods.completedConnectedGraph(adjMatrix)) {
			ArrayList<ArrayList<Integer>> subgraphs = ToolMethods.getListOfDisconnectedGraphs(adjMatrix);
			int randSubGph1 = (int) (Math.random()*subgraphs.size());
			int randSubGph2 = (int) (Math.random()*subgraphs.size());

			while (randSubGph1==randSubGph2) {
				if (subgraphs.size()==2) {
					randSubGph1=0;
					randSubGph2=1;
				} else {
				double rand = Math.random()*subgraphs.size();
				randSubGph2 = (int) rand;
				}
			}
			
			int randIndex1 = (int) (Math.random()*subgraphs.get(randSubGph1).size());
			int randIndex2 = (int) (Math.random()*subgraphs.get(randSubGph2).size());
			
			int randVertex1 = subgraphs.get(randSubGph1).get(randIndex1);
			int randVertex2 = subgraphs.get(randSubGph2).get(randIndex2);
			
			adjMatrix.get(randVertex1).set(randVertex2, true);
			adjMatrix.get(randVertex2).set(randVertex1, true);
		}
		return adjMatrix;
	}
	
	/** Generates a random tree graph, by adding each node to the graph
	 *  by an edge to a node that exists already in the graph.
	 * 
	 * @param noOfVertices
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateRandomTreeGraph(int noOfVertices) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		ArrayList<Boolean> isInTree = ToolMethods.createFalseArray(noOfVertices);
		ArrayList<Integer> inTree = new ArrayList<Integer>();
		
		while (!ToolMethods.allTrue(isInTree)) {
			double rand = Math.random()*noOfVertices;
			int vertex = (int) rand;
			
			while (isInTree.get(vertex)) {
				rand = Math.random()*noOfVertices;
				vertex = (int) rand;
			}
			
			if (inTree.size()==0) {
				inTree.add(vertex);	//essentially the root node of tree
				isInTree.set(vertex, true);
			} else {
				int randIndex = (int) (Math.random()*inTree.size());
				int motherVertex = inTree.get(randIndex);
				
				adjMatrix.get(motherVertex).set(vertex, true);
				adjMatrix.get(vertex).set(motherVertex, true);
				
				inTree.add(vertex);
				isInTree.set(vertex, true);
			}
		}
		return adjMatrix;
	}
	
	@Deprecated
	public static ArrayList<ArrayList<Boolean>> generateRandomRestrictedChildTree(int noOfVertices, int restriction) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		ArrayList<Boolean> isInTree = ToolMethods.createFalseArray(noOfVertices);
		int[] noOfChilds = new int[noOfVertices];
		ArrayList<Integer> inTree = new ArrayList<Integer>();
		
		while (!ToolMethods.allTrue(isInTree)) {
			double rand = Math.random()*noOfVertices;
			int vertex = (int) rand;
			
			while (isInTree.get(vertex)) {
				rand = Math.random()*noOfVertices;
				vertex = (int) rand;
			}
			
			if (inTree.size()==0) {
				inTree.add(vertex);	//essentially the root node of tree
				isInTree.set(vertex, true);
			} else {
				int randIndex = (int) (Math.random()*inTree.size());
				int motherVertex = inTree.get(randIndex);
				
				while(noOfChilds[motherVertex]==restriction) {
					randIndex = (int) (Math.random()*inTree.size());
					motherVertex = inTree.get(randIndex);
				}
				
				adjMatrix.get(motherVertex).set(vertex, true);
				adjMatrix.get(vertex).set(motherVertex, true);
				
				inTree.add(vertex);
				isInTree.set(vertex, true);
				noOfChilds[motherVertex]+=1;
			}
		}
		return adjMatrix;
	}
	
	/** Generates an complete/almost complete n-ary tree, given n
	 *  and number of vertices.
	 * 
	 * @param noOfVertices
	 * @param noOfChildren
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateNChildsTree(int noOfVertices, int noOfChildren) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		
		int assignedNoOfVertices = 1;
		int currentParentIndex = 0;
		int currentParentNoOfChilds = 0;
		
		while (assignedNoOfVertices < noOfVertices) {
			adjMatrix.get(currentParentIndex).set(assignedNoOfVertices, true);
			adjMatrix.get(assignedNoOfVertices).set(currentParentIndex, true);
			assignedNoOfVertices++;
			currentParentNoOfChilds++;
			if (currentParentNoOfChilds==noOfChildren) {
				currentParentIndex++;
				currentParentNoOfChilds = 0;
			}
		}
		
		return adjMatrix;
	}
	
	/** Generates a random n-ary tree, given n and number of vertices.
	 * 
	 * @param noOfVertices
	 * @param noOfChildren
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateRandomNChildsTree(int noOfVertices, int noOfChildren) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		int[] noOfChildrenInTree = new int[noOfVertices];
		
		int assignedNoOfVertices = 1;
		
		for (int i=1;i<noOfVertices;i++) {
			int parent = (int) (Math.random()*assignedNoOfVertices);
			while (noOfChildrenInTree[parent]==noOfChildren) {
				parent = (int) (Math.random()*assignedNoOfVertices);
			}
			adjMatrix.get(parent).set(i, true);
			adjMatrix.get(i).set(parent, true);
			noOfChildrenInTree[parent]++;
			assignedNoOfVertices++;
		}
		return adjMatrix;
	}
	
	/** Generates a grid graph, with the borders not connected, with
	 *  specified rows and columns.
	 * 
	 * @param columns
	 * @param rows
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateBoundedGridGraph(int columns, int rows) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(columns*rows);
		for (int i=0;i<columns;i++) {
			if (i==0) {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+1, true);
					} else {
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					}
				}
			} else if (i==(columns-1)) {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
					} else {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					}
				}
			} else {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+1, true);
					} else {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					}
				}
			}
		}
		return adjMatrix;
	}
	
	/** Generates a grid graph, with specified number of rows and columns.
	 * 
	 * @param columns
	 * @param rows
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateGridGraph(int columns, int rows) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(columns*rows);
		for (int i=0;i<columns;i++) {
			if (i==0) {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex+columns-1, true);
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex+columns-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns), true);
					} else {
						adjMatrix.get(vertex).set(vertex+columns-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					}
				}
			} else if (i==(columns-1)) {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns), true);
					} else {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					}
				}
			} else {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns), true);
					} else {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
					}
				}
			}
		}
		return adjMatrix;
	}
	
	/** Generates a grid that has diagonal connections, with given
	 *  rows and columns number.
	 * 
	 * @param columns
	 * @param rows
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateDiagGridGraph(int columns, int rows) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(columns*rows);
		for (int i=0;i<columns;i++) {
			if (i==0) {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex+((rows)*columns)-1, true);
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns)+1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
						adjMatrix.get(vertex).set(vertex+columns+(columns-1), true);
						adjMatrix.get(vertex).set(vertex+columns-1, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns)+1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex-((rows-2)*columns)-1, true);
						adjMatrix.get(vertex).set(vertex+columns-1, true);
					} else {
						adjMatrix.get(vertex).set(vertex-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
						adjMatrix.get(vertex).set(vertex+columns+columns-1, true);
						adjMatrix.get(vertex).set(vertex+columns-1, true);
					}
				}
			} else if (i==(columns-1)) {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns)-1, true);
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex+((rows-2)*columns)+1, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
						adjMatrix.get(vertex).set(vertex+columns-1, true);
						adjMatrix.get(vertex).set(vertex-1, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex-columns-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex-columns-columns+1, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns)-1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex-((rows)*columns)+1, true);
						adjMatrix.get(vertex).set(vertex-1, true);
					} else {
						adjMatrix.get(vertex).set(vertex-columns-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex-columns-columns+1, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
						adjMatrix.get(vertex).set(vertex+columns-1, true);
						adjMatrix.get(vertex).set(vertex-1, true);
					}
				}
			} else {
				for (int j=0;j<rows;j++) {
					int vertex = i+(j*columns);
					if (j==0) {
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns)-1, true);
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex+((rows-1)*columns)+1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
						adjMatrix.get(vertex).set(vertex+columns-1, true);
						adjMatrix.get(vertex).set(vertex-1, true);
					} else if (j==(rows-1)) {
						adjMatrix.get(vertex).set(vertex-columns-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns)+1, true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns), true);
						adjMatrix.get(vertex).set(vertex-((rows-1)*columns)-1, true);
						adjMatrix.get(vertex).set(vertex-1, true);
					} else {
						adjMatrix.get(vertex).set(vertex-columns-1, true);
						adjMatrix.get(vertex).set(vertex-columns, true);
						adjMatrix.get(vertex).set(vertex-columns+1, true);
						adjMatrix.get(vertex).set(vertex+1, true);
						adjMatrix.get(vertex).set(vertex+columns+1, true);
						adjMatrix.get(vertex).set(vertex+columns, true);
						adjMatrix.get(vertex).set(vertex+columns-1, true);
						adjMatrix.get(vertex).set(vertex-1, true);
					}
				}
			}
		}
		return adjMatrix;
	}
	
	/** Generates a cycle graph.
	 * 
	 * @param noOfVertices
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generatePolygonGraph(int noOfVertices) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		for (int i=0;i<noOfVertices;i++) {
			if (i==0) {
				adjMatrix.get(i).set(noOfVertices-1, true);
				adjMatrix.get(i).set(i+1, true);
			} else if (i==noOfVertices-1) {
				adjMatrix.get(i).set(0, true);
				adjMatrix.get(i).set(i-1, true);
			} else {
				adjMatrix.get(i).set(i-1, true);
				adjMatrix.get(i).set(i+1, true);
			}
		}
		return adjMatrix;
	}
	
	/** Generates a cluster graph, given the cluster size and number
	 *  of vertices.
	 * 
	 * @param noOfVertices
	 * @param clique
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateClusterGraph(int noOfVertices, int clique) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		int noOfClusters = (int) Math.ceil(((double)noOfVertices)/((double)clique));
		ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
		
		int noOfVertsIndex = 0;
		
		for (int i=0;i<noOfClusters;i++) {
			ArrayList<Integer> cluster = new ArrayList<Integer>();
			for (int j=0;j<clique;j++) {
				if (noOfVertsIndex < noOfVertices) {
					cluster.add(noOfVertsIndex);
					noOfVertsIndex++;
				}
			}
			for (int k=0;k<cluster.size();k++) {
				for (int l=0;l<cluster.size();l++) {
					if (k!=l) {
						adjMatrix.get(cluster.get(k)).set(cluster.get(l), true);
					}
				}
			}
			clusters.add(cluster);
		}
		
		while (!ToolMethods.completedConnectedGraph(adjMatrix)) {
			
			ArrayList<ArrayList<Integer>> dclusters = ToolMethods.getListOfDisconnectedGraphs(adjMatrix);
			
			int randCluster1 = (int) (Math.random()*dclusters.size());
			int randCluster2 = (int) (Math.random()*dclusters.size());
			
			while (randCluster1==randCluster2) {
				if (dclusters.size()==2) {
					randCluster1 = 0;
					randCluster2 = 1;
				} else {
					randCluster2 = (int) (Math.random()*dclusters.size());
				}
			}
			
			int randIndex1 = (int) (Math.random()*dclusters.get(randCluster1).size());
			int randIndex2 = (int) (Math.random()*dclusters.get(randCluster2).size());
			
			int index1 = dclusters.get(randCluster1).get(randIndex1);
			int index2 = dclusters.get(randCluster2).get(randIndex2);
			
			adjMatrix.get(index1).set(index2, true);
			adjMatrix.get(index2).set(index1, true);
		}
		
		return adjMatrix;
	}
	
	/** Generates a random bipartite graph, given the number of vertices
	 *  and the probability of an edge.
	 * 
	 * @param noOfVertices
	 * @param edgeProbability
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateRandomBipartiteGraph(int noOfVertices, double edgeProbability) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		ArrayList<Boolean> side1 = ToolMethods.createFalseArray(noOfVertices);
		
		int noOnSide1 = (int) (Math.random()*noOfVertices);
		
		while (noOnSide1==0)
			noOnSide1 = (int) (Math.random()*noOfVertices);
		
		for (int i=0;i<noOnSide1;i++) {
			side1.set(i, true);
		}
		
		ArrayList<Integer> side1Vertices = ToolMethods.getAllTrue(side1);
		ArrayList<Integer> side2Vertices = ToolMethods.getAllFalse(side1);
		
		for (int i=0;i<side1Vertices.size();i++) {
			int noOfEdges = 0;
			for (int j=0;j<side2Vertices.size();j++) {
				double rand = Math.random();
				if (rand<edgeProbability) {
					adjMatrix.get(side1Vertices.get(i)).set(side2Vertices.get(j), true);
					adjMatrix.get(side2Vertices.get(j)).set(side1Vertices.get(i), true);
					noOfEdges++;
				}
			}
			if (noOfEdges==0) {
				int rand = (int) (Math.random()*side2Vertices.size());
				int randVertex = side2Vertices.get(rand);
				
				adjMatrix.get(side1Vertices.get(i)).set(randVertex, true);
				adjMatrix.get(randVertex).set(side1Vertices.get(i), true);
			}
		}
		
		ToolMethods.connectGraph(adjMatrix);
		
		return adjMatrix;
	}
	
	/** Generates a proportional bipartite graph, where the proportion
	 *  determines how big the two disconnected sets are in comparison
	 *  to each other.
	 * 
	 * @param noOfVertices
	 * @param edgeProbability
	 * @param proportion
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generatePropBipartiteGraph(int noOfVertices, double edgeProbability, double proportion) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		ArrayList<Boolean> side1 = ToolMethods.createFalseArray(noOfVertices);
		
		int noOnSide1 = (int) (proportion*noOfVertices);
		
		for (int i=0;i<noOnSide1;i++) {
			side1.set(i, true);
		}
		
		ArrayList<Integer> side1Vertices = ToolMethods.getAllTrue(side1);
		ArrayList<Integer> side2Vertices = ToolMethods.getAllFalse(side1);
		
		for (int i=0;i<side1Vertices.size();i++) {
			int noOfEdges = 0;
			for (int j=0;j<side2Vertices.size();j++) {
				double rand = Math.random();
				if (rand<edgeProbability) {
					adjMatrix.get(side1Vertices.get(i)).set(side2Vertices.get(j), true);
					adjMatrix.get(side2Vertices.get(j)).set(side1Vertices.get(i), true);
					noOfEdges++;
				}
			}
			if (noOfEdges==0) {
				int rand = (int) (Math.random()*side2Vertices.size());
				int randVertex = side2Vertices.get(rand);
				
				adjMatrix.get(side1Vertices.get(i)).set(randVertex, true);
				adjMatrix.get(randVertex).set(side1Vertices.get(i), true);
			}
		}
		
		ToolMethods.connectGraph(adjMatrix);
		
		return adjMatrix;
	}
	
	/** Generates a bipartite graphs where the disconnect sets are
	 *  of equal size or the difference is 1.
	 * 
	 * @param noOfVertices
	 * @param edgeProbability
	 * @return
	 */
	public static ArrayList<ArrayList<Boolean>> generateEvenRandomBipartiteGraph(int noOfVertices, double edgeProbability) {
		ArrayList<ArrayList<Boolean>> adjMatrix = ToolMethods.createFalseAdjMatrix(noOfVertices);
		ArrayList<Boolean> side1 = ToolMethods.createFalseArray(noOfVertices);
		
		int noOnSide1 = (int) ((double) noOfVertices)/2;
		
		for (int i=0;i<noOnSide1;i++) {
			side1.set(i, true);
		}
		
		ArrayList<Integer> side1Vertices = ToolMethods.getAllTrue(side1);
		ArrayList<Integer> side2Vertices = ToolMethods.getAllFalse(side1);
		
		for (int i=0;i<side1Vertices.size();i++) {
			int noOfEdges = 0;
			for (int j=0;j<side2Vertices.size();j++) {
				double rand = Math.random();
				if (rand<edgeProbability) {
					adjMatrix.get(side1Vertices.get(i)).set(side2Vertices.get(j), true);
					adjMatrix.get(side2Vertices.get(j)).set(side1Vertices.get(i), true);
					noOfEdges++;
				}
			}
			if (noOfEdges==0) {
				int rand = (int) (Math.random()*side2Vertices.size());
				int randVertex = side2Vertices.get(rand);
				
				adjMatrix.get(side1Vertices.get(i)).set(randVertex, true);
				adjMatrix.get(randVertex).set(side1Vertices.get(i), true);
			}
		}
		
		ToolMethods.connectGraph(adjMatrix);
		
		return adjMatrix;
	}
	
	/** Generate a random distribution, given the number of vertices,
	 *  and the probability of starting with defection.
	 * 
	 * @param noOfVertices
	 * @param probability
	 * @return
	 */
	public static ArrayList<Integer> generateRandomDistribution(int noOfVertices, double probability) {
		ArrayList<Integer> vertexDistribution = new ArrayList<Integer>();
		for (int i=0;i<noOfVertices;i++) {
			double rand = Math.random();
			if (probability<rand)
				vertexDistribution.add(1);
			else
				vertexDistribution.add(0);
		}
		if (ToolMethods.allTrueInteger(vertexDistribution))
			vertexDistribution = generateR1DefectDistribution(noOfVertices);
		return vertexDistribution;
	}
	
	/** Generates a proportional distribution, where the proportion
	 *  is the ratio of vertices which start with defection.
	 * 
	 * @param noOfVertices
	 * @param proportion
	 * @return
	 */
	public static ArrayList<Integer> generateProportionalDistribution(int noOfVertices, double proportion) {
		ArrayList<Integer> vertexDistribution = ToolMethods.createOneArray(noOfVertices);
		
		int noOfDefectors = (int) (noOfVertices*proportion);
		int noOfDefectorsIndex = 0;
		
		while (noOfDefectorsIndex < noOfDefectors) {
			int rand = (int) (Math.random()*noOfVertices);
			
			while (vertexDistribution.get(rand)==0) {
				rand =  (int) (Math.random()*noOfVertices);
			}
			
			vertexDistribution.set(rand, 0);
			noOfDefectorsIndex++;
		}
		return vertexDistribution;
	}
	
	/** Generates a distribution with one randomly selected node
	 *  as a defector.
	 * 
	 * @param noOfVertices
	 * @return
	 */
	public static ArrayList<Integer> generateR1DefectDistribution(int noOfVertices) {
		ArrayList<Integer> vertexDistribution = new ArrayList<Integer>();
		double rand = Math.random()*noOfVertices;
		int randVertex = (int) rand;
		for (int i=0;i<noOfVertices;i++) {
			if (i==randVertex)
				vertexDistribution.add(0);
			else
				vertexDistribution.add(1);
		}
		return vertexDistribution;
	}
	
	@Deprecated	//Use generateProportionalDistribution instead
	public static ArrayList<Integer> generatePercentageDefectDistribution(int noOfVertices, double percentage) {
		ArrayList<Integer> vertexDistribution = ToolMethods.createOneArray(noOfVertices);
		int noOfDefectors = (int) (noOfVertices*percentage);
		for (int i=0;i<noOfDefectors;i++) {
			double rand = Math.random()*noOfVertices;
			int randVertex = (int) rand;
			
			while (!(vertexDistribution.get(randVertex)==0)) {
				rand = Math.random()*noOfVertices;
				randVertex = (int) rand;
			}
			
			vertexDistribution.set(randVertex, 0);
		}
		return vertexDistribution;
	}
	
	/** Generates a proportional distribution, with the root node
	 *  starting with cooperation.
	 * 
	 * @param noOfVertices
	 * @param proportion
	 * @return
	 */
	public static ArrayList<Integer> generateNoRootProportionalDistribution(int noOfVertices, double proportion) {
		ArrayList<Integer> vertexDistribution = ToolMethods.createOneArray(noOfVertices);
		
		int noOfDefectors = (int) (noOfVertices*proportion);
		int noOfDefectorsIndex = 0;
		
		while (noOfDefectorsIndex < noOfDefectors) {
			int rand = (int) (Math.random()*noOfVertices);
			
			while (vertexDistribution.get(rand)==0 || rand==0) {
				rand = (int) (Math.random()*noOfVertices);
			}
			
			vertexDistribution.set(rand, 0);
			noOfDefectorsIndex++;
		}
		return vertexDistribution;
	}
	
	/** Generates a proportional distribution, with the root node
	 *  starting with cooperation.
	 * 
	 * @param noOfVertices
	 * @param proportion
	 * @return
	 */
	public static ArrayList<Integer> generateNoRootR1DefectDistribution(int noOfVertices) {
		ArrayList<Integer> vertexDistribution = ToolMethods.createOneArray(noOfVertices);
		
		int defector = (int) (Math.random()*noOfVertices);
		
		while (defector==0) {
			defector = (int) (Math.random()*noOfVertices);
		}
		vertexDistribution.set(defector, 0);
		
		return vertexDistribution;
	}
	
	/** Generates an even distribution with the tit-for-tat strategy.
	 * 
	 * @param noOfVertices
	 * @return
	 */
	public static ArrayList<Integer> generateT4TEvenProporDistribution(int noOfVertices) {
		ArrayList<Integer> vertexDistribution = ToolMethods.createOneArray(noOfVertices);
		
		int noOfOtherStrats = (int) Math.floor(((double)noOfVertices)/3);
		int noOfDefe = 0;
		
		while (noOfDefe < noOfOtherStrats) {
			int drand = (int) (Math.random()*noOfVertices);
			int trand = (int) (Math.random()*noOfVertices);
			
			while (vertexDistribution.get(drand)==0 || vertexDistribution.get(drand)==2) {
				drand = (int) (Math.random()*noOfVertices);
			}
			
			while (vertexDistribution.get(trand)==0 || vertexDistribution.get(trand)==2) {
				trand = (int) (Math.random()*noOfVertices);
			}
			
			vertexDistribution.set(drand,0);
			vertexDistribution.set(trand, 2);
			noOfDefe++;
		}
		
		return vertexDistribution;
	}
	
	/** Generates a proportional distribution with tit-for-tat,
	 *  given the proportion of defectors and tit-for-tat players.
	 * 
	 * @param noOfVertices
	 * @param defeProp
	 * @param T4TProp
	 * @return
	 */
	public static ArrayList<Integer> generateT4TProporDistribution(int noOfVertices, double defeProp, double T4TProp) {
		ArrayList<Integer> vertexDistribution = ToolMethods.createOneArray(noOfVertices);
		
		int noOfDefeStrats = (int) (noOfVertices*defeProp);
		int noOfT4TStrats = (int) (noOfVertices*T4TProp);
		int noOfDefe = 0;
		int noOfT4T = 0;
		
		while (noOfDefe < noOfDefeStrats) {
			int drand = (int) (Math.random()*noOfVertices);
			
			while (vertexDistribution.get(drand)==0 || vertexDistribution.get(drand)==2) {
				drand = (int) (Math.random()*noOfVertices);
			}
			
			vertexDistribution.set(drand,0);
			noOfDefe++;
		}
		
		while (noOfT4T < noOfT4TStrats) {
			int trand = (int) (Math.random()*noOfVertices);
			
			while (vertexDistribution.get(trand)==0 || vertexDistribution.get(trand)==2) {
				trand = (int) (Math.random()*noOfVertices);
			}
			
			vertexDistribution.set(trand, 2);
			noOfT4T++;
		}
		
		return vertexDistribution;
	}
	
}
