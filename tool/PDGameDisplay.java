package tool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;


/** Class for displaying the PDGame class, i.e. a Prisoner's Dilemma spatial
 *  game, once initialised from the MainMenu class by the user.
 *  
 * @author Jackie, s1035578
 *
 */

public class PDGameDisplay implements ActionListener{
	
	private PDGame game;
	private JFrame mainFrame;
	
	private JPanel graphDisplayPanel;
	private JPanel infoPanel;
	private JPanel buttonPanel;
	private String layout = "circ";
	private boolean firstTimeInitiation = true;
	private boolean firstInit = true;
	private boolean layoutChange = false;
	private boolean alteredGraph = false;
	private boolean resetting = false;
	private boolean alteredPast = false;
	
	public PDGameDisplay(PDGame game) {
		mainFrame = new JFrame();
		mainFrame.setTitle("Prisoner's Dilemma Spatial Game");
		this.game = game;
		init();
	}
	
	
	/** Initialises the various panels which make up the whole display of the
	 *  game, and puts it all together in one frame.
	 */
	private void init() {
		this.graphDisplayPanel = initGraphPanel();
		this.infoPanel = initInfoPanel();
		this.buttonPanel = initButtonPanel();
		
		mainFrame.getContentPane().add(graphDisplayPanel, BorderLayout.CENTER);
		mainFrame.getContentPane().add(infoPanel, BorderLayout.LINE_END);
		mainFrame.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				mainFrame.setVisible(true);
				if (firstInit) {
					mainFrame.pack();
					firstInit = false;
				}
			}
		});
	}
	
	/** Set up the panel which displays the graph of the spatial game
	 * 
	 * @return
	 */
	private JPanel initGraphPanel() {
		JPanel graphDisplayPanel = new JPanel();
		graphDisplayPanel.setLayout(new BoxLayout(graphDisplayPanel, BoxLayout.Y_AXIS));
		
		//Title
		JLabel graphLabel = new JLabel("Spatial Game Graph");
		graphLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		graphLabel.setLabelFor(graphDisplayPanel);
		graphDisplayPanel.add(graphLabel);
		graphDisplayPanel.add(Box.createRigidArea(new Dimension(0,2)));
		
		//Different layout view radiobuttons
		{
			JPanel viewPanel = new JPanel();
			viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.LINE_AXIS));
			
			ButtonGroup group = new ButtonGroup();
			
			JRadioButton circButton = new JRadioButton("Circular");
			circButton.setActionCommand("circ");
			circButton.addActionListener(this);
			if (layout.equals("circ"))
				circButton.setSelected(true);
			
			JRadioButton kkButton = new JRadioButton("KK");
			kkButton.setActionCommand("kk");
			kkButton.addActionListener(this);
			if (layout.equals("kk"))
				kkButton.setSelected(true);
			
			group.add(circButton);
			group.add(kkButton);
			
			viewPanel.add(circButton);
			viewPanel.add(kkButton);
			viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			//TODO: Add more layouts...
			
			graphDisplayPanel.add(viewPanel);
		}
		//Draw the graph
		if (firstTimeInitiation || layoutChange || alteredGraph || resetting) {
			Visualisation.drawGraph(game, graphDisplayPanel, layout);
			firstTimeInitiation = false;
			layoutChange = false;
			alteredGraph = false;
			if (resetting==true);
				resetting = false;
		} else
			Visualisation.repaintImage(game, graphDisplayPanel);
		graphDisplayPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		graphDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return graphDisplayPanel;
	}
	
	/** Set up the panel which displays the information of the graph
	 * 
	 * @return
	 */
	private JPanel initInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		
		//Title
		JLabel infoLabel = new JLabel("Information on the graph:");
		infoLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		infoLabel.setLabelFor(infoPanel);
		infoPanel.add(infoLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		//Number of vertices information
		JLabel novLabel = new JLabel("<html>Number of vertices: <font color = 'green'>"
				+game.getNumberOfVertices()+"<br></font></html>");
		novLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		infoPanel.add(novLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(0,2)));
		
		//Number of edges information
		JLabel noeLabel = new JLabel("<html>Number of edges: <font color = 'green'>"
				+game.getNumberOfEdges()+"<br></font></html>");
		noeLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		infoPanel.add(noeLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(0,2)));
		
		//Payoff Matrix information
		JLabel pmLabel = new JLabel("<html>Payoff matrix values:"
				+ "<font color='red'>"+game.getPayoffMatrix()[0]+" </font>"
				+ "<font color='blue'>"+game.getPayoffMatrix()[1]+" </font>"
				+ "<font color='#A52A2A'>"+game.getPayoffMatrix()[2]+" </font>"
				+ "<font color='#00FFFF'>"+game.getPayoffMatrix()[3]+" </font>"
				);
		pmLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		infoPanel.add(pmLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(0,2)));
		
		//Self interaction
		String sIstr = "No";
		if (game.isSelfInteraction())
			sIstr = "Yes";
		JLabel siLabel = new JLabel("Self interaction: "+sIstr);
		siLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		infoPanel.add(siLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(0,2)));
		
		//Number of cooperators
		JLabel nocLabel = new JLabel("<html>Number of cooperators: <font color = 'blue'>"
				+game.getDistribution()[0]+"<br></font></html>");
		nocLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		infoPanel.add(nocLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(0,2)));
		
		//Number of defectors
		JLabel nodLabel = new JLabel("<html>Number of defectors: <font color = 'red'>"
				+game.getDistribution()[1]+"<br></font></html>");
		nodLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		infoPanel.add(nodLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(0,2)));
		
		//Number of rounds
		JLabel norLabel = new JLabel("<html>Number of rounds: <font color = 'green'>"
				+game.getIterations()+"<br></font></html>");
		norLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		infoPanel.add(norLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(0,2)));
		
		//TODO: Add more...
		
		infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return infoPanel;
	}
	
	/** Set up the panel which has all buttons which interact with the graph
	 * 
	 * @return
	 */
	private JPanel initButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		
		//Main button: Play the game.
		JButton playButton = new JButton("<html><font color = 'green'>Play</font></html>");
		playButton.setFont(new Font("Dialog", Font.BOLD, 14));
		playButton.setActionCommand("play");
		playButton.addActionListener(this);
		mainFrame.getRootPane().setDefaultButton(playButton);
		buttonPanel.add(playButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		//Jump to end game button
		JButton endButton = new JButton("<html><font color = 'green'>End</font></html>");
		endButton.setFont(new Font("Dialog", Font.BOLD, 12));
		endButton.setActionCommand("end");
		endButton.addActionListener(this);
		buttonPanel.add(endButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		//Reset button
		JButton resButton = new JButton("Reset");
		resButton.setFont(new Font("Dialog", Font.BOLD, 14));
		resButton.setForeground(Color.RED);
		resButton.setActionCommand("reset");
		resButton.addActionListener(this);
		buttonPanel.add(resButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		//Add a vertex button
		JButton aavButton = new JButton("New vertex");
		aavButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		aavButton.setActionCommand("aav");
		aavButton.addActionListener(this);
		buttonPanel.add(aavButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		//Add an edge button
		JButton aaeButton = new JButton("New edge");
		aaeButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		aaeButton.setActionCommand("aae");
		aaeButton.addActionListener(this);
		buttonPanel.add(aaeButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		//Remove a vertex button
		JButton ravButton = new JButton("Remove vertex");
		ravButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		ravButton.setActionCommand("rav");
		ravButton.addActionListener(this);
		buttonPanel.add(ravButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		//Remove an edge button
		JButton raeButton = new JButton("Remove edge");
		raeButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		raeButton.setActionCommand("rae");
		raeButton.addActionListener(this);
		buttonPanel.add(raeButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		//Change strategy
		JButton chdButton = new JButton("Change strategy");
		chdButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		chdButton.setActionCommand("chd");
		chdButton.addActionListener(this);
		buttonPanel.add(chdButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		//TODO: Add more...
		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return buttonPanel;
	}
	
	/** Update the game, as well as the user interface
	 * 
	 */
	private void update() {
		this.game.updateGame();
		mainFrame.getContentPane().remove(graphDisplayPanel);
		mainFrame.getContentPane().remove(infoPanel);
		mainFrame.getContentPane().remove(buttonPanel);
		mainFrame.getContentPane().revalidate();
		mainFrame.getContentPane().repaint();
		init();
	}
	
	/** Update the game and interface to the end
	 * 
	 */
	private void updateToEnd() {
		this.game.updateToEnd();
		mainFrame.getContentPane().remove(graphDisplayPanel);
		mainFrame.getContentPane().remove(infoPanel);
		mainFrame.getContentPane().remove(buttonPanel);
		mainFrame.getContentPane().revalidate();
		mainFrame.getContentPane().repaint();
		init();
	}
	
	/** Reset the game and its interface to the original game
	 * 
	 */
	private void reset() {
		this.game.reset();
		mainFrame.getContentPane().remove(graphDisplayPanel);
		mainFrame.getContentPane().remove(infoPanel);
		mainFrame.getContentPane().remove(buttonPanel);
		mainFrame.getContentPane().revalidate();
		mainFrame.getContentPane().repaint();
		init();
	}
	
	/** Initiates the add vertex dialog frame, then adds a vertex according
	 *  to the user input
	 * @throws Exception
	 */
	private void newVertex() throws Exception {
		String[] data = AddVertexDialog.showDialog(mainFrame, game);
		
		boolean vertexStrat = Boolean.parseBoolean(data[0]);
		int vertex = 0;
		if (vertexStrat)
			vertex = 1;
		if (!data[1].isEmpty()) {
			ArrayList<Integer> neighbours = ToolMethods.extractVertices(data[1]);
			//Add the vertex to the graph
			this.game.addVertex(vertex, neighbours);
			
			mainFrame.getContentPane().remove(graphDisplayPanel);
			mainFrame.getContentPane().remove(infoPanel);
			mainFrame.getContentPane().remove(buttonPanel);
			mainFrame.getContentPane().revalidate();
			mainFrame.getContentPane().repaint();
			init();
		} else
			System.out.println("Adding a vertex was unsuccessful, as user closed dialog.");
	}
	
	/** Initiates the add edge dialog frame, then adds an edge(s) according
	 *  to the user input
	 */
	private void newEdge() {
		Integer[][] data = AddEdgeDialog.showDialog(mainFrame, game);
		
		if (data!=null) {
			if (data[0][0]!=-1) {	//Add an edge user specified
				Integer[] heads = data[0];
				Integer[] tails = data[1];
				
				for (int i=0;i<heads.length;i++) {
					this.game.addEdge(heads[i], tails[i]);
				}
				
			} else {	//Add a random edge
				int noOfVerts = game.getNumberOfVertices();
				double rand1 = Math.random()*noOfVerts;
				double rand2 = Math.random()*noOfVerts;

				int randVert1 = (int) rand1;
				int randVert2 = (int) rand2;
				
				//boolean value which checks if the randomly generated vertices already have an edge
				//between them.  If so then, get a new pair.
				boolean alreadyEdge = true;
				
				while (alreadyEdge && !ToolMethods.isDense(game)) {
					if (game.getAdjMatrix().get(randVert1).get(randVert2) || randVert1==randVert2) {
						rand1 = Math.random()*noOfVerts;
						rand2 = Math.random()*noOfVerts;
						randVert1 = (int) rand1;
						randVert2 = (int) rand2;
					} else {
						this.game.addEdge(randVert1, randVert2);
						alreadyEdge = false;
					}
				}
			}
			mainFrame.getContentPane().remove(graphDisplayPanel);
			mainFrame.getContentPane().remove(infoPanel);
			mainFrame.getContentPane().remove(buttonPanel);
			mainFrame.getContentPane().revalidate();
			mainFrame.getContentPane().repaint();
			init();
		} else
			System.out.println("Adding an edge failed, as user closed the dialog.");
	}
	
	/** Initiates the remove vertex dialog, and removes the vertices specified
	 *  by the user through the dialog
	 */
	private void removeVertex() {
		ArrayList<Integer> vertsToRemove = RemoveVertexDialog.showDialog(mainFrame, game);
		if (vertsToRemove!=null) {
			game.removeVertices(vertsToRemove);
			mainFrame.getContentPane().remove(graphDisplayPanel);
			mainFrame.getContentPane().remove(infoPanel);
			mainFrame.getContentPane().remove(buttonPanel);
			mainFrame.getContentPane().revalidate();
			mainFrame.getContentPane().repaint();
			init();
		} else
			System.out.println("Removing a vertex was unsuccessful, as user closed dialog");
	}
	
	/** Initiates the remove edge dialog.  Removes the edges that the user specifies
	 * 
	 */
	private void removeEdge() {
		Integer[][] data = RemoveEdgeDialog.showDialog(mainFrame, game);
		
		if (data!=null) {
			Integer[] heads = data[0];
			Integer[] tails = data[1];
			
			for (int i=0;i<heads.length;i++) {
				game.removeEdge(heads[i], tails[i]);
			}
			
			mainFrame.getContentPane().remove(graphDisplayPanel);
			mainFrame.getContentPane().remove(infoPanel);
			mainFrame.getContentPane().remove(buttonPanel);
			mainFrame.getContentPane().revalidate();
			mainFrame.getContentPane().repaint();
			init();
		} else
			System.out.println("Removing an edge failed, as user closed the dialog");
	}
	
	/** Creates the dialog which allows the user to change the current strategy
	 *  distribution of the game, and then performs those changes accordingly
	 */
	private void changeDistribution() {
		ArrayList<Integer> data = ChangeDistributionDialog.showDialog(mainFrame, game);
		
		if (data!=null) {
			int strategy = data.get(0);
			for (int i=1;i<data.size();i++) {
				game.changeStrategy(data.get(i), strategy);
			}
			
			mainFrame.getContentPane().remove(graphDisplayPanel);
			mainFrame.getContentPane().remove(infoPanel);
			mainFrame.getContentPane().remove(buttonPanel);
			mainFrame.getContentPane().revalidate();
			mainFrame.getContentPane().repaint();
			init();
		} else 
			System.out.println("Changing the distribution failed, as user closed the dialog");
	}
	
	@Deprecated
	public void setGame(PDGame game) {
		this.game = game;
	}
	
	@Deprecated
	public JFrame getFrame() {
		return mainFrame;
	}
	
	private void updateLayout() {
		mainFrame.getContentPane().remove(graphDisplayPanel);
		mainFrame.getContentPane().remove(infoPanel);
		mainFrame.getContentPane().remove(buttonPanel);
		mainFrame.getContentPane().revalidate();
		mainFrame.getContentPane().repaint();
		init();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("play")) {
			System.out.println("PRESSED THE PLAY BUTTON");
			update();
		}
		if (ae.getActionCommand().equals("end")) {
			System.out.println("PRESSED THE END BUTTON");
			updateToEnd();
		}
		if (ae.getActionCommand().equals("reset")) {
			System.out.println("PRESSED THE RESET BUTTON");
			if (this.alteredPast) {
				this.resetting = true;
				this.alteredPast = false;
			}
			reset();
		}
		if (ae.getActionCommand().equals("aav")) {
			System.out.println("PRESSED THE NEW VERTEX BUTTON");
			this.alteredGraph = true;
			this.alteredPast = true;
			try {
				newVertex();
			} catch (Exception e) {
				System.out.println("WOAH! Something bad happened when you tried to make a new vertex!");
			}
		}
		if (ae.getActionCommand().equals("aae")) {
			System.out.println("PRESSED THE NEW EDGE BUTTON");
			this.alteredGraph = true;
			this.alteredPast = true;
			newEdge();
		}
		if (ae.getActionCommand().equals("rav")) {
			System.out.println("PRESSED THE REMOVE VERTEX BUTTON");
			this.alteredGraph = true;
			this.alteredPast = true;
			removeVertex();
		}
		if (ae.getActionCommand().equals("rae")) {
			System.out.println("PRESSED THE REMOVE EDGE BUTTON");
			this.alteredGraph = true;
			this.alteredPast = true;
			removeEdge();
		}
		if (ae.getActionCommand().equals("chd")) {
			System.out.println("PRESSED THE CHANGE DISTRIBUTION BUTTON");
			changeDistribution();
		}
		if (ae.getActionCommand().equals("circ")) {
			System.out.println("PRESSED THE CIRCULAR LAYOUT RADIO BUTTON");
			this.layout = "circ";
			this.layoutChange = true;
			updateLayout();
		}
		if (ae.getActionCommand().equals("kk")) {
			System.out.println("PRESSED THE KK LAYOUT RADIO BUTTON");
			this.layout = "kk";
			this.layoutChange = true;
			updateLayout();
		}
	}
	
	/** Main method for testing purposes
	 * 
	 */
	/*
	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		int nov = GraphScanner.getVerticesNumber(file);
		ArrayList<Integer> verts = GraphScanner.getVerticesDistribution(file, nov);
		ArrayList<ArrayList<Boolean>> adjMat = GraphScanner.getAdjacencyMatrix(file, nov);

		double[] payoffs = new double[]{1.9,1,0,0};
		
		PDGame game = new PDGame(
				GameGeneration.generateClusterGraph(10, 3),
				GameGeneration.generateR1DefectDistribution(10),
				payoffs);
		game.selfNeighbour();
		game.setDebugMode(true);
		PDGameDisplay gd = new PDGameDisplay(game);
	}
	*/
	
}