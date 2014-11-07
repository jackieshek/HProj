package tool;

import java.util.ArrayList;
import java.util.Arrays;

public class PDGame extends GameGraph{

	/** Payoff values for player 1 are read in the order:
	 * 				Player 1 does:		Player 2 does:
	 *  payoff[0] = Defect			/	Co-op
	 *  payoff[1] = Co-op			/	Co-op
	 *  payoff[2] = Defect			/	Defect
	 *  payoff[3] = Co-op			/	Defect
	 *  
	 *  Strategies:
	 *  0 = Defect;
	 *  1 = Cooperation;
	 *  2 = Tit-for-Tat
	 */
	private static final double[] DEFAULT_PD_PAYOFF = {5,3,1,0};
	
	private final boolean useDefault;
	private final double[] payoffValues;
	
	private int iterations;
	
	private boolean debug = false;
	private boolean oscilEnd = false;
	private int method = 0;
	
	/** Construct the spatial PD game with the given adjacency matrix, and
	 *  starting vertices distribution with the default payoff matrix.
	 * 
	 * @param matrix
	 * @param vertices
	 */
	public PDGame(ArrayList<ArrayList<Boolean>> matrix, ArrayList<Integer> vertices) {
		super(matrix, vertices);
		this.useDefault = true;
		this.payoffValues = DEFAULT_PD_PAYOFF;
	}
	
	/** Construct the spatial PD game with the given adjacency matrix, 
	 *  vertices distribution and payoff matrix.
	 * 
	 * @param matrix
	 * @param vertices
	 * @param userSettings
	 */
	public PDGame(ArrayList<ArrayList<Boolean>> matrix, ArrayList<Integer> vertices, double[] userSettings) {
		super(matrix, vertices);
		this.useDefault = false;
		this.payoffValues = userSettings;
	}
	
	/** This calculates the wrong strategy in terms of spatial games.
	 *  Calculation of strategy here is rational, following the Nash equilibrium
	 *  route.
	 * 
	 * @param vertex
	 * @return
	 */
	@Deprecated
	public Integer oldCalculateStrategy(int vertex) {
		ArrayList<Boolean> neighbours = this.getAdjMatrix().get(vertex);
		ArrayList<Integer> vertices = this.getVertices();
		/** Below calculates the payoffs for the vertex, either choosing to cooperate
		 *  or to defect, based on the states of its neighbours from the current state. 
		 */
		int coopPayoff = 0;
		int defectPayoff = 0;
		for (int i=0;i<neighbours.size();i++) {
			if (neighbours.get(i) == true) { 
				if (vertices.get(i) == 1) {
					coopPayoff += payoffValues[1];
					defectPayoff += payoffValues[0];
				} else {
					coopPayoff += payoffValues[3];
					defectPayoff += payoffValues[2];
				}
			}
		}
		System.out.println("COOP PAYOFF IS " + coopPayoff);
		System.out.println("DEFECT PAYOFF IS " + defectPayoff);
		if (coopPayoff >= defectPayoff) {
			System.out.println("RETURNED TRUE");
			return 1;
		}
		System.out.println("RETURNED FALSE");
		return 0;
	}
	
	/** Calculates the payoff of a given vertex.  Its payoff is the
	 *  total payoff of all payoffs of all games it plays with its
	 *  neighbours.
	 * 
	 * @param vertex
	 * @return
	 */
	private double calculatePayoff(int vertex) {
		ArrayList<Boolean> neighbours = this.getAdjMatrix().get(vertex);
		ArrayList<Integer> vertices = this.getVertices();
		Integer vertexState = vertices.get(vertex);
		ArrayList<Integer> prevVertices = this.getPrevVertices();
		
		double payoff = 0;
		
		for (int i=0;i<neighbours.size();i++) {
			if (neighbours.get(i)!=null) {
				if (neighbours.get(i) == true) {
					if (vertices.get(i) == 0) {	//If neighbour is defecting
						if (vertexState==0)
							payoff += payoffValues[2];
						if (vertexState==1)
							payoff += payoffValues[3];
						if (vertexState==3)
							payoff += payoffValues[2];
						if (vertexState==2) {
							if (iterations==0) {
								payoff += payoffValues[3];
							} else {
								if (prevVertices.get(i)==0)
									payoff += payoffValues[2];
								if (prevVertices.get(i)==1)
									payoff += payoffValues[3];
								if (prevVertices.get(i)==2)
									payoff += payoffValues[3];
							}
						}
					} else if (vertices.get(i) == 1) {	//If neighbour is cooperating
						if (vertexState==0)
							payoff += payoffValues[0];
						if (vertexState==1)
							payoff += payoffValues[1];
						if (vertexState==3)
							payoff += payoffValues[1];
						if (vertexState==2) {
							if (iterations==0) {
								payoff += payoffValues[1];
							} else {
								if (prevVertices.get(i)==0)
									payoff += payoffValues[0];
								if (prevVertices.get(i)==1)
									payoff += payoffValues[1];
								if (prevVertices.get(i)==2)
									payoff += payoffValues[1];
							}
						}
					} else {	//Neighbour plays tit-for-tat
						if (vertexState==0)
							payoff += payoffValues[1];
						if (vertexState==1)
							payoff += payoffValues[2];
						if (vertexState==3)
							payoff += payoffValues[1];
						if (vertexState==2) {
							if (iterations==0) {
								payoff += payoffValues[1];
							} else {
								if (prevVertices.get(i)==0)
									payoff += payoffValues[0];
								if (prevVertices.get(i)==1)
									payoff += payoffValues[1];
								if (prevVertices.get(i)==2)
									payoff += payoffValues[1];
							}
						}
					}
				}
			}
		}
		return payoff;
	}
	
	/** Calculates the total payoff and normalises it.  The total
	 *  payoff is obtained from summation of all payoffs from all
	 *  games it plays with its neighbours.
	 * 
	 * @param vertex
	 * @return
	 */
	private double calculateAveragePayoff(int vertex) {
		ArrayList<Boolean> neighbours = this.getAdjMatrix().get(vertex);
		ArrayList<Integer> vertices = this.getVertices();
		Integer vertexState = vertices.get(vertex);
		ArrayList<Integer> prevVertices = this.getPrevVertices();
		
		double payoff = 0;
		int noOfNeighbours = 0;
		
		for (int i=0;i<neighbours.size();i++) {
			if (neighbours.get(i)!=null) {
				if (neighbours.get(i) == true) {
					noOfNeighbours++;
					if (vertices.get(i) == 0) {	//If neighbour is defecting
						if (vertexState==0)
							payoff += payoffValues[2];
						if (vertexState==1)
							payoff += payoffValues[3];
						if (vertexState==3) 
							payoff += payoffValues[2];
						if (vertexState==2) {
							if (iterations==0) {
								payoff += payoffValues[3];
							} else {
								if (prevVertices.get(i)==0)
									payoff += payoffValues[2];
								if (prevVertices.get(i)==1)
									payoff += payoffValues[3];
								if (prevVertices.get(i)==2)
									payoff += payoffValues[3];
							}
						}
					} else if (vertices.get(i) == 1) {	//If neighbour is cooperating
						if (vertexState==0)
							payoff += payoffValues[0];
						if (vertexState==1)
							payoff += payoffValues[1];
						if (vertexState==3)
							payoff += payoffValues[1];
						if (vertexState==2) {
							if (iterations==0) {
								payoff += payoffValues[1];
							} else {
								if (prevVertices.get(i)==0)
									payoff += payoffValues[0];
								if (prevVertices.get(i)==1)
									payoff += payoffValues[1];
								if (prevVertices.get(i)==2)
									payoff += payoffValues[1];
							}
						}
					} else {	//Neighbour is playing tit-for-tat
						if (vertexState==0)
							payoff += payoffValues[1];
						if (vertexState==1)
							payoff += payoffValues[2];
						if (vertexState==3)
							payoff += payoffValues[1];
						if (vertexState==2) {
							if (iterations==0) {
								payoff += payoffValues[1];
							} else {
								if (prevVertices.get(i)==0)
									payoff += payoffValues[0];
								if (prevVertices.get(i)==1)
									payoff += payoffValues[1];
								if (prevVertices.get(i)==2)
									payoff += payoffValues[1];
							}
						}
					}
				}
			}
		}
		return payoff/noOfNeighbours;
	}
	
	@Deprecated
	public void oldUpdateGame() {
		ArrayList<Integer> updatedVertices = new ArrayList<Integer>();
		System.out.println("NUMBER OF VERTICES IN GRAPH: " + getVertices().size());
		System.out.println("UPDATED VERTICES SIZE IS: " + updatedVertices.size());
		System.out.println("----------------------------------------");
		for (int i=0;i<this.getVertices().size();i++) {
			System.out.println("GOT TO UPDATING VERTEX " + i);
			updatedVertices.add(oldCalculateStrategy(i));
			System.out.println("----------------------------------------");
		}
		super.setVertices(updatedVertices);
	}

	/** Calculate the strategy the vertex will play for the next round.
	 *  The strategy it will play is the strategy employed by the 
	 *  highest scoring vertex in the neighbourhood, i.e. of all
	 *  the players and itself whoever scores the highest for this
	 *  round.
	 * 
	 * @param vertex
	 * @return
	 */
	private Integer calculateStrategy(int vertex) {
		ArrayList<Boolean> neighbours = this.getAdjMatrix().get(vertex);
		
		if (getVertices().get(vertex)!=null) {
			double bestPayoff;
			if (method==1)
				bestPayoff = calculateAveragePayoff(vertex);
			else
				bestPayoff = calculatePayoff(vertex);
			if (debug)
				System.out.println("OWN PAYOFF IS " + bestPayoff);
			int bestNeighbourIndex = vertex;

			for (int i=0;i<neighbours.size();i++) {
				if (neighbours.get(i)!=null) {
					if (neighbours.get(i) == true) {
						if (debug)
							System.out.println("THIS IS NEIGHBOUR VERTEX: "+i);
						double iPayoff;
						if (method==0)
							iPayoff = calculatePayoff(i);
						else
							iPayoff = calculateAveragePayoff(i);
						if (debug)
							System.out.println("PAYOFF OF VERTEX " + i + " IS " + iPayoff);
						if (iPayoff>bestPayoff || 
								(iPayoff==bestPayoff && this.getVertices().get(i)==0)) { //Resolves tie breakers, vertex adopts defect on tie
							if (debug)
								System.out.println("CHANGES");
							bestPayoff = iPayoff;
							bestNeighbourIndex = i;
						}
					}
				}
			}
			Integer returnvalue = bestNeighbourIndex==vertex ? getVertices().get(vertex) : getVertices().get(bestNeighbourIndex);
			if (debug)
				System.out.println("RETURNED VALUE IS OF VERTEX " + bestNeighbourIndex + " WITH " + returnvalue);
			return getVertices().get(bestNeighbourIndex);
		}
		return null;
	}
	
	/** Calculates what strategy the vertex will play in the next
	 *  round probabilistically, with dependence on total number of
	 *  neighbours cooperating and defecting.
	 * 
	 * @param vertex
	 * @return
	 */
	private Integer calculateProbStrategy(int vertex) {
		ArrayList<Boolean> neighbours = this.getAdjMatrix().get(vertex);
		
		if (getVertices().get(vertex)!=null) {
			double coopScore = 0;
			double defeScore = 0;
			
			if (getVertices().get(vertex)==1) {
				coopScore += calculatePayoff(vertex);
			} else if (getVertices().get(vertex)==0) {
				defeScore += calculatePayoff(vertex);
			}
			
			for (int i=0;i<neighbours.size();i++) {
				if (neighbours.get(i)!=null) {
					if (neighbours.get(i)==true) {
						if (getVertices().get(i)==1) {
							coopScore += calculatePayoff(i);
						} else if (getVertices().get(i)==0) {
							defeScore += calculatePayoff(i);
						}
					}
				}
			}
			double totalScore = coopScore + defeScore;
			double coopProb = coopScore/totalScore;
			
			double prob = Math.random();
			if (prob<coopProb) {
				return 1;
			}
			return 0;
		}
		return null;
	}
	
	/** Update the game, progress to the next round in spatial PD.
	 * 
	 */
	public void updateGame() {
		iterations++;
		setPrev(this.getAdjMatrix(), this.getVertices());
		ArrayList<Integer> updatedVertices = new ArrayList<Integer>();
		if (debug) {
			System.out.println("NUMBER OF VERTICES IN GRAPH: " + getVertices().size());
			System.out.println("UPDATED VERTICES SIZE IS: " + updatedVertices.size());
			System.out.println("----------------------------------------");
		}
		for (int i=0;i<this.getVertices().size();i++){
			if (getVertices().get(i)!=null) {
				if (debug)
					System.out.println("GOT TO UPDATING VERTEX " + i);
				if (method==2)
					updatedVertices.add(i, calculateProbStrategy(i));
				else
					updatedVertices.add(i, calculateStrategy(i));
				if (debug)
					System.out.println("----------------------------------------");
			} else
				updatedVertices.add(null);
		}
		super.setVertices(updatedVertices);
	}
	
	/** Resets the spatial game.
	 * 
	 */
	@Override
	public void reset() {
		this.iterations=0;
		super.reset();
	}
	
	/** Returns how many rounds have been played.
	 * 
	 * @return
	 */
	public int getIterations() {
		return this.iterations;
	}
	
	/** Updates/plays the game until it reaches a stable point or
	 *  when the number of rounds reaches 500, which indicates that
	 *  the game has an oscillating equilibrium.
	 * 
	 */
	public void updateToEnd() {
		while (!atStablePoint()) {
			updateGame();
			if (this.iterations>500) {
				this.oscilEnd = true;
				break;
			}
		}
	}
	
	public void updateTo50() {
		while (iterations<50) {
			updateGame();
		}
	}
	
	/** Returns whether the game has an oscillating equilibrium.
	 *  Passively updates the game to the end.
	 * 
	 * @return
	 */
	public boolean isOscil() {
		return oscilEnd;
	}
	
	/** Checks whether the game is defection dominated at end game.
	 * 
	 * @return
	 */
	public boolean defectionDominated() {
		updateToEnd();
		if (ToolMethods.allFalse(this.getVertices()))
			return true;
		return false;
	}
	
	public boolean probDefectionDominated() {
		updateTo50();
		if (ToolMethods.allFalse(this.getVertices()))
			return true;
		return false;
	}
	
	/** Checks whether the game is cooperation dominated at end game.
	 * 
	 * @return
	 */
	public boolean cooperationDominated() {
		updateToEnd();
		if (ToolMethods.allTrueInteger(this.getVertices()))
			return true;
		return false;
	}
	
	public boolean probCooperationDominated() {
		if (ToolMethods.allTrueInteger(this.getVertices()))
			return true;
		return false;
	}
	
	/** Returns the percentage of cooperating vertices at the end of 
	 *  the game.
	 * 
	 * @return
	 */
	public double getCoopPercentage() {
		double coops = super.getCoopVertices().size();
		double noOfVertices = super.getNumberOfVertices();
		
		return coops/noOfVertices;
	}
	
	/** Returns the payoff matrix.
	 * 
	 * @return
	 */
	public double[] getPayoffMatrix() {
		return payoffValues;
	}
	
	/** Sets the debug mode.  Debug mode will print various game
	 *  updating information to the terminal when the game is played.
	 * 
	 * @param bool
	 */
	public void setDebugMode(boolean bool) {
		this.debug = bool;
	}
	
	/** Sets how the game calculates how to update a vertices 
	 *  strategy. 
	 * 
	 * @param method
	 */
	public void setCalcPayoffMethod(int method) {
		this.method = method;
	}
}
