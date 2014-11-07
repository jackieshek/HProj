package tool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/** Main user interface class which is loaded, i.e. the main(...) method is defined
 *  and class performs as the launcher for the whole application
 * 
 * @author Jackie, s1035578
 *
 */
public class MainMenu implements ActionListener {
	
	private JFrame mainFrame;
	private JFileChooser fileChooser;
	
	private JPanel titlePanel;
	private JPanel loadCreatePanel;
	
	private static JTextField uI_nov;
	private static JTextField uI_ep;
	private static JTextField uI_noc_or_cs;
	private static JTextField uI_props;
	private static JTextField uI_distProp;
	private static JTextField uI_pm;
	private static boolean sI = false; 
	
	private String graphStr = "Random Graph";
	private String distStr = "1-Defect";
	
	public MainMenu() {
		mainFrame = new JFrame();
		mainFrame.setTitle("Spatial Games Main Menu");
		fileChooser = new JFileChooser();
		init();
	}
	
	/** Main initialisation method of the main menu
	 * 
	 */
	private void init() {
		this.titlePanel = initTitlePanel();
		this.loadCreatePanel = initLoadAndCreatePanel();
		
		mainFrame.getContentPane().add(titlePanel, BorderLayout.PAGE_START);
		mainFrame.getContentPane().add(loadCreatePanel, BorderLayout.CENTER);
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				mainFrame.setVisible(true);
				mainFrame.pack();
			}
		});
		
	}
	
	/** Title panel initialisation
	 * 
	 * @return
	 */
	private JPanel initTitlePanel() {
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.PAGE_AXIS));
		
		File imageFile = new File("toolTitle.png");
		Image title;
		JLabel titlePic = null;
		try {
			title = ImageIO.read(imageFile);
//			title = Toolkit.getDefaultToolkit().getImage(getClass().getResource("toolTitle.png"));
			titlePic = new JLabel(new ImageIcon(title));
		} catch (Exception e) {
			//If image file cannot be found, then just print plain text
			titlePic = new JLabel("Spatial Games");
			titlePic.setFont(new Font("dialog", Font.BOLD, 30));
			System.out.println("Didn't read the image");
		}
		
		titlePanel.add(titlePic);
		return titlePanel;
	}
	
	/** Initialise the panel which allows the user to submit a .txt file, by selecting one
	 *  from their files directories after pressing the load button; or by inputting settings
	 *  in the correct input fields.
	 *  
	 * @return
	 */
	private JPanel initLoadAndCreatePanel() {
		
		JPanel loadCreatePanel = new JPanel();
		loadCreatePanel.setLayout(new BoxLayout(loadCreatePanel, BoxLayout.PAGE_AXIS));
		
		{	//Load panel, where the user presses the load button, and selects an appropriate file
			JPanel loadPanel = new JPanel();
			
			JButton loadButton = new JButton("Load...");
			loadButton.setActionCommand("load");
			loadButton.addActionListener(this);
			loadPanel.add(loadButton);
			
			loadPanel.setBorder(BorderFactory.createTitledBorder("<html><font size = 6>Load a game</font></html>"));
			
			loadCreatePanel.add(loadPanel);
			loadCreatePanel.add(Box.createRigidArea(new Dimension(0,10)));
		}
		
		{	//Panel where the user inputs the settings for the game, by use of text fields, dropboxes and checkbox
			JPanel createPanel = new JPanel();
			createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.LINE_AXIS));
			
			{	//Sub-panel dedicated to the graph of the game
				JPanel graphPanel = new JPanel();
				graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.PAGE_AXIS));
				
				JLabel numberOfVerticesLabel = new JLabel("Number of vertices:");
				numberOfVerticesLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
				numberOfVerticesLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				graphPanel.add(numberOfVerticesLabel);
				graphPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				uI_nov = new JTextField();
				uI_nov.setPreferredSize(new Dimension(100,20));
				uI_nov.setMaximumSize(uI_nov.getPreferredSize());
				graphPanel.add(uI_nov);
				graphPanel.add(Box.createRigidArea(new Dimension(0,15)));
				
				JLabel typeOfGrpLbl = new JLabel("Type of graph:");
				typeOfGrpLbl.setFont(new Font("Dialog", Font.PLAIN, 14));
				graphPanel.add(typeOfGrpLbl);
				graphPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				String[] grpStrings = {"Random Graph",
						"Cycle", "Random Tree", "Regular Random Tree", 
						"Regular Almost Complete Tree", "Cluster",
						"Even Bipartite", "Random Bipartite",
						"Proportional Bipartite"};
				
				JComboBox grpList = new JComboBox(grpStrings);
				grpList.setPreferredSize(new Dimension(300, 30));
				grpList.setMaximumSize(grpList.getPreferredSize());
				grpList.setSelectedIndex(0);
				grpList.setActionCommand("combo");
				grpList.addActionListener(this);
				
				graphPanel.add(grpList);
				
				JLabel edgeProbsLabel = new JLabel("The edge probability:");
				edgeProbsLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
				graphPanel.add(edgeProbsLabel);
				graphPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				uI_ep = new JTextField();
				uI_ep.setPreferredSize(new Dimension(100,20));
				uI_ep.setMaximumSize(uI_ep.getPreferredSize());
				graphPanel.add(uI_ep);
				graphPanel.add(Box.createRigidArea(new Dimension(0,15)));
				
				JLabel noOfChild_clusterSize = new JLabel("Number of children/nodes per cluster:");
				noOfChild_clusterSize.setFont(new Font("Dialog", Font.PLAIN, 14));
				graphPanel.add(noOfChild_clusterSize);
				graphPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				uI_noc_or_cs = new JTextField();
				uI_noc_or_cs.setPreferredSize(new Dimension(100,20));
				uI_noc_or_cs.setMaximumSize(uI_noc_or_cs.getPreferredSize());
				graphPanel.add(uI_noc_or_cs);
				graphPanel.add(Box.createRigidArea(new Dimension(0,15)));
				
				JLabel propSize = new JLabel("Proportion of sizes of two sets:");
				propSize.setFont(new Font("Dialog", Font.PLAIN, 14));
				graphPanel.add(propSize);
				graphPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				uI_props = new JTextField();
				uI_props.setPreferredSize(new Dimension(100,20));
				uI_props.setMaximumSize(uI_props.getPreferredSize());
				graphPanel.add(uI_props);
				graphPanel.add(Box.createRigidArea(new Dimension(0,15)));
				
				graphPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				
				createPanel.add(graphPanel);
			}
			
			{	//Sub-panel dedicated to the strategy distribution of the game.
				JPanel distPanel = new JPanel();
				distPanel.setLayout(new BoxLayout(distPanel, BoxLayout.PAGE_AXIS));
				
				JLabel distTitle = new JLabel("Select the starting distribution:");
				distTitle.setFont(new Font("Dialog", Font.PLAIN, 14));
				distPanel.add(distTitle);
				distPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				String[] distList = {"1-Defect", "p-Proportional Defect",
						"p-Random Defect"
				};
				
				JComboBox distOptions = new JComboBox(distList);
				distOptions.setPreferredSize(new Dimension(300, 30));
				distOptions.setMaximumSize(distOptions.getPreferredSize());
				distOptions.setSelectedIndex(0);
				distOptions.setActionCommand("dist");
				distOptions.addActionListener(this);
				
				distPanel.add(distOptions);
				
				JLabel propLabel = new JLabel("Probability/Proportion p:");
				propLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
				distPanel.add(propLabel);
				distPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				uI_distProp = new JTextField();
				uI_distProp.setPreferredSize(new Dimension(100,20));
				uI_distProp.setMaximumSize(uI_distProp.getPreferredSize());
				distPanel.add(uI_distProp);
				distPanel.add(Box.createRigidArea(new Dimension(0,15)));
				
				JLabel payoffLbl = new JLabel("Values of the payoff matrix:");
				payoffLbl.setFont(new Font("Dialog", Font.PLAIN, 14));
				distPanel.add(payoffLbl);
				distPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				uI_pm = new JTextField();
				uI_pm.setPreferredSize(new Dimension(100,20));
				uI_pm.setMaximumSize(uI_pm.getPreferredSize());
				distPanel.add(uI_pm);
				distPanel.add(Box.createRigidArea(new Dimension(0,15)));
				
				JCheckBox sIcheck = new JCheckBox("Self Interaction?");
				sIcheck.setFont(new Font("Dialog", Font.PLAIN, 14));
				sIcheck.setActionCommand("si");
				sIcheck.addActionListener(this);
				distPanel.add(sIcheck);
				distPanel.add(Box.createRigidArea(new Dimension(0,5)));
				
				{
					JPanel btnPanel = new JPanel();
					btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
					
					JButton submitbtn = new JButton("Submit");
					submitbtn.setActionCommand("submit");
					submitbtn.addActionListener(this);
					btnPanel.add(submitbtn);
					
					distPanel.add(btnPanel);
				}				
				distPanel.add(Box.createVerticalGlue());
				
				createPanel.add(distPanel);
			}
			createPanel.setBorder(BorderFactory.createTitledBorder("<html><font size = 6>Create a game</font></html>"));
			loadCreatePanel.add(createPanel);
			
		}
		
		return loadCreatePanel;
	}
	
	public static void main(String[] args) {
		MainMenu menu = new MainMenu();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		//Action event for when the load button is pressed
		if (ae.getActionCommand().equals("load")) {
			int returnVal = fileChooser.showOpenDialog(mainFrame);
			
			//Checks if the file is provided
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				String errorMsg = "File given was not in the correct format, please refer to the guidelines";
				
				//Check the provided file is not a .txt file, throw an exception dialog
				if (!fileChooser.getTypeDescription(file).equals("Text Document")) {
					JOptionPane.showMessageDialog(titlePanel, "File provided was not a .txt file, please provide a correctly formatted .txt file.");
				} else {	//Is .txt file so parse it
					boolean flagToProceed = true;
					
					int nov = 0;
					ArrayList<ArrayList<Boolean>> adjMatrix = null;
					ArrayList<Integer> startDist = null;
					double[] pm = new double[4];
					try {	//Parse file for vertices number, else throw error msg
						nov = GraphScanner.getVerticesNumber(file);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, errorMsg);
						flagToProceed = false;
					}
					
					try {	//Get matrix for graph
						adjMatrix = GraphScanner.getAdjacencyMatrix(file, nov);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, errorMsg);
						flagToProceed = false;
					}
					
					try {	//Get strategy distribution
						startDist = GraphScanner.getVerticesDistribution(file, nov);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, errorMsg);
						flagToProceed = false;
					}
					
					try {	//Get the payoff values
						pm = GraphScanner.getPayoffValues(file);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, errorMsg);
						pm = null;
					}
					
					if (flagToProceed) {	//Make game if obtained required info
						if (pm==null) {
							PDGame game = new PDGame(adjMatrix, startDist);
							PDGameDisplay gameDis = new PDGameDisplay(game);
						} else {
							PDGame game = new PDGame(adjMatrix, startDist, pm);
							PDGameDisplay gameDis = new PDGameDisplay(game);
						}
					}
				}
			}
		}
		
		//Action event for drop box selection on type of graph
		if (ae.getActionCommand().equals("combo")) {
			JComboBox source = (JComboBox)ae.getSource();
			graphStr = (String)source.getSelectedItem();
		}
		
		//Action event for drop box selection on strategy distribution type
		if (ae.getActionCommand().equals("dist")) {
			JComboBox source = (JComboBox)ae.getSource();
			distStr = (String)source.getSelectedItem();
		}
		
		//Action event for self interaction check box
		if (ae.getActionCommand().equals("si")) {
			if (sI)
				sI = false;
			else
				sI = true;
		}
		
		//Action event for submission button
		if (ae.getActionCommand().equals("submit")) {
			
			boolean flagToProceed = true;
			
			String novStr = uI_nov.getText();
			String epStr = uI_ep.getText();
			String noc_or_csStr = uI_noc_or_cs.getText();
			String propsStr = uI_props.getText();
			String distPropStr = uI_distProp.getText();
			String pmStr = uI_pm.getText();
			
			int nov = 0;
			double ep = 0;
			int noc_or_cs = 0;
			double props = 0;
			double distProp = 0;
			double[] pm = new double[4];
			
			try {	//Parse the number of vertices
				nov = Integer.parseInt(novStr);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainFrame, "Number of vertices given was not an integer. Please provide an integer.");
				flagToProceed = false;
			}
			if (nov <= 0) {	//If given number of vertices is less than 0 do not proceed
				JOptionPane.showMessageDialog(mainFrame, "Number of vertices given was less than 1. Please provide a positive integer.");
				flagToProceed = false;
			}
			
			if (graphStr.equals("Random Graph")
					|| graphStr.equals("Even Bipartite")
					|| graphStr.equals("Random Bipartite")
					|| graphStr.equals("Proportional Bipartite")) {
				try {	//If graph of certain type, get edge probability
					ep = Double.parseDouble(epStr);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, "Edge probability given was not a number. Please provide a number.");
					flagToProceed = false;
				}
			}
			if (!(ep<=1 && ep >=0)) {	//Ensure edge probability between 0 and 1
				JOptionPane.showMessageDialog(mainFrame, "Edge probability given was not between 0 and 1. Please provide a number between 0 and 1.");
				flagToProceed = false;
			}
			
			if (graphStr.equals("Regular Random Tree")
					|| graphStr.equals("Regular Almost Complete Tree")
					|| graphStr.equals("Cluster")) {
				try {	//If graph of certain type, get number of children or cluster size
					noc_or_cs = Integer.parseInt(noc_or_csStr);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, "Number of children/nodes per cluster is not a number. Please provide a number.");
					flagToProceed = false;
				}
			}
			if (noc_or_cs > nov) {	//Ensure number of children/cluster size is less than vertex number
				JOptionPane.showMessageDialog(mainFrame, "Number of children/nodes per cluster is greater than the number of vertices. Please provide a smaller number.");
				flagToProceed = false;
			}
			
			if (graphStr.equals("Proportional Bipartite")) {
				try {	//If graph is proportional bipartite, get the proportion between the two sides
					props = Double.parseDouble(propsStr);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, "Proportional split given was not a number. Please provide a number.");
					flagToProceed = false;
				}
			}
			if (!(props<=1 && props >=0)) {	//Ensure the proportion is between 0 and 1
				JOptionPane.showMessageDialog(mainFrame, "Proportional split given was not between 0 and 1. Please provide a number between 0 and 1.");
				flagToProceed = false;
			}
			
			if (distStr.equals("p-Proportional Defect")
					|| distStr.equals("p-Random Defect")) {
				try {	//If distribution is of certain type, get probability
					distProp = Double.parseDouble(distPropStr);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, "Proportional split/probability, for distribution, given was not a number. Please provide a number.");
					flagToProceed = false;
				}
			}
			if (!(distProp<=1 && props >=0)) {	//Ensure probability is between 0 and 1
				JOptionPane.showMessageDialog(mainFrame, "Proportional split/probability, for distribution, given was not a number between 0 and 1. Please provide a correct number.");
				flagToProceed = false;
			}
			
			if (!pmStr.isEmpty()) {	//Get the payoff values if present and parse, else use default
				String[] pmStrArr = pmStr.split(",");

				for (int i=0;i<4;i++) {
					try {
						pm[i] = Double.parseDouble(pmStrArr[i]);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, "The payoff matrix provided was incorrect. \n"
								+ "Please ensure you provide numbers, and the input is provided in the correct format \n"
								+ "e.g. 5,3,1,0");
						flagToProceed = false;
					}
				}
			}
			
			if (flagToProceed) {	//Make the game if everything necessary is collected
				ArrayList<ArrayList<Boolean>> adjMatrix = null;
				ArrayList<Integer> startDist = null;
				
				if (graphStr.equals("Random Graph")) {
					adjMatrix = GameGeneration.generateConnectedERGraph(nov, ep);
				}
				if (graphStr.equals("Cycle")) {
					adjMatrix = GameGeneration.generatePolygonGraph(nov);
				}
				if (graphStr.equals("Random Tree")) {
					adjMatrix = GameGeneration.generateRandomTreeGraph(nov);
				}
				if (graphStr.equals("Regular Random Tree")) {
					adjMatrix = GameGeneration.generateRandomNChildsTree(nov, noc_or_cs);
				}
				if (graphStr.equals("Regular Almost Complete Tree")) {
					adjMatrix = GameGeneration.generateNChildsTree(nov, noc_or_cs);
				}
				if (graphStr.equals("Cluster")) {
					adjMatrix = GameGeneration.generateClusterGraph(nov, noc_or_cs);
				}
				if (graphStr.equals("Even Bipartite")) {
					adjMatrix = GameGeneration.generateEvenRandomBipartiteGraph(nov, ep);
				}
				if (graphStr.equals("Random Bipartite")) {
					adjMatrix = GameGeneration.generateRandomBipartiteGraph(nov, ep);
				}
				if (graphStr.equals("Proportional Bipartite")) {
					adjMatrix = GameGeneration.generatePropBipartiteGraph(nov, ep, props);
				}
				
				if (distStr.equals("1-Defect")) {
					startDist = GameGeneration.generateR1DefectDistribution(nov);
				}
				if (distStr.equals("p-Proportional Defect")) {
					startDist = GameGeneration.generateProportionalDistribution(nov, distProp);
				}
				if (distStr.equals("p-Random Defect")) {
					startDist = GameGeneration.generateRandomDistribution(nov, distProp);
				}
				
				if (!pmStr.isEmpty()) {
					PDGame game = new PDGame(adjMatrix, startDist, pm);
					if (sI)
						game.selfNeighbour();
					PDGameDisplay gameDis = new PDGameDisplay(game);
				} else {
					PDGame game = new PDGame(adjMatrix, startDist);
					if (sI)
						game.selfNeighbour();
					PDGameDisplay gameDis = new PDGameDisplay(game);
				}
			}
		}
	}
}
