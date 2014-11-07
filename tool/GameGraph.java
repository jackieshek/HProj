package tool;

import java.util.ArrayList;

/** Class to create essentially the graph, for any spatial game to "play" in.
 *  Abstracted away from external visual libraries, so that it is much easier to maintain,
 *  and change to use any other visual library, without too much fuss.
 * 
 * @author Jackie, s1035578
 *
 */

public class GameGraph {

	/** Take note of the starting graph, for visualisation.
	 *  Also take note of previous states of the graph, so the user may
	 *  flick back and forth to see changes to the spatial game.
	 *  (Only keep previous state of the graph).
	 *  Record the current state.
	 */
	
	/** The arraylist of booleans for vertices can be decyphered as:
	 *  0  		= defection;
	 *  1 		= cooperation;
	 *  2		= ...
	 *  null  	= deleted.
	 *  
	 *  The arraylist of arraylists of booleans, represents the adjacency
	 *  matrix of the graph, where:
	 *  true  = theres an edge between vertices i and j;
	 *  false = no edge;
	 *  null  = either i or j (or both) vertex has been deleted;
	 */
	private final ArrayList<ArrayList<Boolean>> startAdjMatrix;
	private final ArrayList<Integer> startVertices;
	
	private ArrayList<ArrayList<Boolean>> prevAdjMatrix;
	private ArrayList<Integer> prevVertices;
		
	private ArrayList<ArrayList<Boolean>> curAdjMatrix;
	private ArrayList<Integer> curVertices;
	
	private int numOfAddedVertices;
	private int numOfAddedEdges;
	
	private boolean selfNeighbour = false;
	private boolean showEdges = true;
	
	/** Constructor for the game.
	 * 
	 * @param matrix
	 * @param vertices
	 */
	public GameGraph(ArrayList<ArrayList<Boolean>> matrix, ArrayList<Integer> vertices) {
		this.startAdjMatrix = new ArrayList<ArrayList<Boolean>>(matrix);
		for (int i=0;i<startAdjMatrix.size();i++) {
			startAdjMatrix.set(i, new ArrayList<Boolean>(matrix.get(i)));
		}
		this.startVertices = new ArrayList<Integer>(vertices);
		this.curAdjMatrix = new ArrayList<ArrayList<Boolean>>(matrix);
		for (int i=0;i<curAdjMatrix.size();i++) {
			curAdjMatrix.set(i, new ArrayList<Boolean>(matrix.get(i)));
		}
		this.curVertices = new ArrayList<Integer>(vertices);
		this.numOfAddedVertices = 0;
		this.numOfAddedEdges = 0;
	}
	
	/** Adds a vertex to the graph of the game, given it's strategy and it's neighbours
	 * 	
	 * @param strat
	 * @param neighbours
	 */
	public void addVertex(Integer strat, ArrayList<Integer> neighbours) {
		numOfAddedVertices++;
		curVertices.add(strat);
		curAdjMatrix.add(new ArrayList<Boolean>());
		ArrayList<Boolean> newRow = new ArrayList<Boolean>();
		for (int i=0;i<curVertices.size();i++) {
			if (ToolMethods.isIn(i, neighbours) && curVertices.get(i)!=null) {
				newRow.add(true);
				curAdjMatrix.get(i).add(true);
			}
			else if (curVertices.get(i)!=null) {
				newRow.add(false);
				curAdjMatrix.get(i).add(false);
			}
			else
				newRow.add(null);
		}
		curAdjMatrix.set(curVertices.size()-1, newRow);
	}
	
	/** Removes a vertex and sets its value in the distribution to null.
	 *  Furthermore it sets all the it's row and column entries in the adjacency
	 *  matrix all to null.
	 * 
	 * @param vertices
	 */
	public void removeVertices(ArrayList<Integer> vertices) {
		for (int i=0;i<curVertices.size();i++) {
			if (ToolMethods.isIn(i, vertices)) {
				curVertices.set(i, null);
				curAdjMatrix.set(i, null);
				for (int j=0;j<curVertices.size();j++) {
					if (j!=i && curVertices.get(j)!=null) {
						curAdjMatrix.get(j).set(i, null);
					}
				}
			}
		}
	}
	
	/** Adds a single edge between the vertices given.
	 * 
	 * @param vertex1
	 * @param vertex2
	 */
	public void addEdge(int vertex1, int vertex2) {
		numOfAddedEdges++;
		ArrayList<Boolean> v1Row = curAdjMatrix.get(vertex1);
		v1Row.set(vertex2, true);
		curAdjMatrix.set(vertex1, v1Row);
		
		ArrayList<Boolean> v2Row = curAdjMatrix.get(vertex2);
		v2Row.set(vertex1, true);
		curAdjMatrix.set(vertex2, v2Row);
	}
	
	/** Removes a single edge between the vertices given.
	 * 
	 * @param vertex1
	 * @param vertex2
	 */
	public void removeEdge(int vertex1, int vertex2) {
		ArrayList<Boolean> v1Row = curAdjMatrix.get(vertex1);
		v1Row.set(vertex2, false);
		curAdjMatrix.set(vertex1, v1Row);
		
		ArrayList<Boolean> v2Row = curAdjMatrix.get(vertex2);
		v2Row.set(vertex1, false);
		curAdjMatrix.set(vertex2, v2Row);
	}
	
	/** Sets previous adjacency matrix and vertices, to the matrix and array given.
	 * 
	 * @param edgeMatrix
	 * @param vertices
	 */
	protected void setPrev(ArrayList<ArrayList<Boolean>> edgeMatrix, ArrayList<Integer> vertices) {
		this.prevAdjMatrix = edgeMatrix;
		this.prevVertices = vertices;
	}
	
	/** Returns the current adjacency matrix.
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<Boolean>> getAdjMatrix() {
		return this.curAdjMatrix;
	}
	
	/** Returns the current vertices distribution.
	 * 
	 * @return
	 */
	public ArrayList<Integer> getVertices() {
		return this.curVertices;
	}
	
	/** Returns the previous round's vertices distribution.
	 * 
	 * @return
	 */
	protected ArrayList<Integer> getPrevVertices() {
		return this.prevVertices;
	}
	
	/** Returns the list of all vertices which exists (i.e. not null) in the
	 *  current game.
	 *  
	 * @return
	 */
	public ArrayList<Integer> getExistVerticesIndex() {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for (int i=0;i<curVertices.size();i++) {
			if (curVertices.get(i)!=null)
				indexes.add(i);
		}
		return indexes;
	}
	
	/** Sets the current vertices distribution to the array specified.
	 * 
	 * @param newVertices
	 */
	protected void setVertices(ArrayList<Integer> newVertices) {
		setPrev(curAdjMatrix, curVertices);
		this.curVertices = newVertices;
	}
	
	/** Changes a vertex's strategy to the one given.
	 * 
	 * @param vertex
	 * @param strategy
	 */
	public void changeStrategy(int vertex, int strategy) {
		curVertices.set(vertex, strategy);
	}
	
	/** Returns an array list of all the cooperating vertices
	 * 
	 * @return
	 */
	public ArrayList<Integer> getCoopVertices() {
		ArrayList<Integer> coopList = new ArrayList<Integer>();
		for (int i=0;i<curVertices.size();i++) {
			if (curVertices.get(i)!=null) {
				if (curVertices.get(i)==1)
					coopList.add(i);
			}
		}
		return coopList;
	}
	
	/** Returns an array list of all the defecting vertices.
	 * 
	 * @return
	 */
	public ArrayList<Integer> getDefectVertices() {
		ArrayList<Integer> defectList = new ArrayList<Integer>();
		for (int i=0;i<curVertices.size();i++) {
			if (curVertices.get(i)!=null) {
				if (curVertices.get(i)==0)
					defectList.add(i);
			}
		}
		return defectList;
	}
	
	/** Returns the number of vertices in the game.
	 * 
	 * @return
	 */
	public int getNumberOfVertices() {
		int numberOfVertices = 0;
		for (int i=0;i<curVertices.size();i++) {
			if (curVertices.get(i)!=null)
				numberOfVertices++;
		}
		return numberOfVertices;
	}
	
	/** Returns the number of edges in the game.
	 * 
	 * @return
	 */
	public int getNumberOfEdges() {
		int numberOfEdges=0;
		for (int i=0;i<curAdjMatrix.size();i++) {
			ArrayList<Boolean> row = curAdjMatrix.get(i);
			if (curAdjMatrix.get(i)!=null) {
				for (int j=0;j<i;j++) {
					if (row.get(j)!=null) {
						if (row.get(j)==true)
							numberOfEdges++;
					}
				}
			}
		}
		return numberOfEdges;
	}
	
	/** Returns an array, consisting of two array slots;
	 *  slot 0: number of cooperating vertices
	 *  slot 1: number of defecting vertices
	 *  based on the current set of vertices
	 *  
	 * @return
	 */
	public int[] getDistribution() {
		int[] distribution = new int[]
				{getCoopVertices().size(),
				getDefectVertices().size(),
				getNumberOfVertices()-getCoopVertices().size()-getDefectVertices().size()};
		
		return distribution;
	}
	
	/** Returns the number of vertices that have been added to the
	 *  original graph.
	 * 
	 * @return
	 */
	public int getNumberOfAddedVertices() {
		return this.numOfAddedVertices;
	}
	
	/** Returns the number of edges that have been added to the
	 *  original graph.
	 * 
	 * @return
	 */
	public int getNumberOfAddedEdges() {
		return this.numOfAddedEdges;
	}
	
	/** Resets the game to the original spatial game
	 * 
	 */
	public void reset() {
		this.numOfAddedVertices=0;
		this.numOfAddedEdges=0;
		this.curAdjMatrix = new ArrayList<ArrayList<Boolean>>(startAdjMatrix);
		this.curVertices = new ArrayList<Integer>(startVertices);
		System.out.println(curAdjMatrix.size());
		for (int i=0; i<curAdjMatrix.size();i++) {
			curAdjMatrix.set(i, new ArrayList<Boolean>(startAdjMatrix.get(i)));
			System.out.println("Row " + i + " is size "+curAdjMatrix.get(i).size());
		}
		System.out.println(curVertices.size());
		if (selfNeighbour)
			selfNeighbour();
	}
	
	/** Adds the requirement that each vertex also plays the game with itself.
	 * 
	 */
	public void selfNeighbour() {
		for (int i=0;i<curAdjMatrix.size();i++) {
			curAdjMatrix.get(i).set(i, true);
		}
		this.selfNeighbour = true;
	}
	
	public boolean isSelfInteraction() {
		return selfNeighbour;
	}
	
	/** Checks if the game is in a stable point/equilibrium.
	 * 
	 * @return
	 */
	public boolean atStablePoint() {
		if (!(this.prevAdjMatrix==null)) {
			if (this.prevAdjMatrix.equals(this.curAdjMatrix) &&
					this.prevVertices.equals(this.curVertices))
				return true;
		}
		return false;
	}
	
	/** Returns the highest density, i.e. the max density of a vertex
	 *  in a graph. 
	 * 
	 * @return
	 */
	public int getHighestVertexDensity() {
		int highestVertexDensity = 0;
		for (int i=0;i<this.curAdjMatrix.size();i++) {
			int vertexDensity = 0;
			ArrayList<Boolean> row = this.curAdjMatrix.get(i);
			for (int j=0;j<this.curAdjMatrix.size();j++) {
				if(row.get(j))
					vertexDensity++;
			}
			if (vertexDensity > highestVertexDensity) {
				highestVertexDensity = vertexDensity;
			}
		}
		return highestVertexDensity;
	}
	
	/** Returns the highest density belonging to a cooperator.
	 * 
	 * @return
	 */
	public int getHighestCoopVertexDensity() {
		int highestVertexDensity = 0;
		for (int i=0;i<this.curAdjMatrix.size();i++) {
			if (this.curVertices.get(i)==1) {
				int vertexDensity = 0;
				ArrayList<Boolean> row = this.curAdjMatrix.get(i);
				for (int j=0;j<this.curAdjMatrix.size();j++) {
					if(row.get(j))
						vertexDensity++;
				}
				if (vertexDensity > highestVertexDensity) {
					highestVertexDensity = vertexDensity;
				}
			}
		}
		return highestVertexDensity;
	}
	
	/** Returns the highest density belonging to a defector.
	 * 
	 * @return
	 */
	public int getHighestDefectVertexDensity() {
		int highestVertexDensity = 0;
		for (int i=0;i<this.curAdjMatrix.size();i++) {
			if (this.curVertices.get(i)==0) {
				int vertexDensity = 0;
				ArrayList<Boolean> row = this.curAdjMatrix.get(i);
				for (int j=0;j<this.curAdjMatrix.size();j++) {
					if(row.get(j))
						vertexDensity++;
				}
				if (vertexDensity > highestVertexDensity) {
					highestVertexDensity = vertexDensity;
				}
			}
		}
		return highestVertexDensity;
	}
	
	/** Sets whether to show the edges of the graph in this visualisation.
	 * 
	 */
	public void setShowEdges(boolean bool) {
		this.showEdges = bool;
	}
	
	/** Shows whether the edges are displayed.
	 * 
	 * @return
	 */
	public boolean getShowEdges() {
		return this.showEdges;
	}
}
