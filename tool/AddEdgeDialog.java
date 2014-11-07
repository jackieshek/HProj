package tool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

/** Dialog which is used to add an edge to the spatial game.
 * 
 * @author Jackie, s1035578
 *
 */
public class AddEdgeDialog extends JDialog implements ActionListener, ItemListener{

	private static AddEdgeDialog dialog;
	private static GameGraph game;
	private static JTextField textField;
	private static Integer[][] edgesInSplitArrays;
	private static JCheckBox checkBox;
	private static boolean checked = false;
	
	private AddEdgeDialog(JFrame frame, GameGraph game) {
		super(frame, "Add an edge", true);
		
		AddEdgeDialog.game = game;
		checked = false;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		getContentPane().add(initRandomPanel(), BorderLayout.PAGE_START);
		getContentPane().add(initUserPanel(), BorderLayout.CENTER);
		getContentPane().add(initButtonPanel(), BorderLayout.PAGE_END);
		pack();
	}
	
	/** Method which initialises the random edge checkbox panel
	 * 
	 * @return
	 */
	private JPanel initRandomPanel() {
		//Set up the panel
		JPanel randomPanel = new JPanel();
		
		randomPanel.setLayout(new BoxLayout(randomPanel, BoxLayout.PAGE_AXIS));
		
		checkBox = new JCheckBox("Insert a random edge");
		checkBox.addItemListener(this);
		
		checkBox.setFont(new Font("Dialog", Font.PLAIN, 14));
		
		randomPanel.add(checkBox);
		
		randomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return randomPanel;
	}
	
	/** Method which sets up the panel used to obtain user input
	 * 
	 * @return
	 */
	private JPanel initUserPanel() {
		//Set up the user input panel
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.PAGE_AXIS));
		
		JLabel q1Label = new JLabel("List the edge(s) to be added as tuples,");
		q1Label.setFont(new Font("Dialog", Font.PLAIN, 14));
		JLabel q2Label = new JLabel("in the format: (x,y);(w,v);...");
		q2Label.setFont(new Font("Dialog", Font.PLAIN, 14));
		userPanel.add(q1Label);
		userPanel.add(q2Label);
		userPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		textField = new JTextField();
		textField.setSize(new Dimension(150,40));
		userPanel.add(textField);
		userPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return userPanel;
	}
	
	/** Set up the button panel for submit and cancel buttons
	 * 
	 * @return
	 */
	private JPanel initButtonPanel() {
		//Set up the button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		
		buttonPanel.add(Box.createHorizontalGlue());
		
		JButton submitButton = new JButton("Submit");
		submitButton.setFont(new Font("Dialog", Font.PLAIN, 14));
		submitButton.setActionCommand("submit");
		submitButton.addActionListener(this);
		getRootPane().setDefaultButton(submitButton);
		buttonPanel.add(submitButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Dialog", Font.PLAIN, 14));
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return buttonPanel;
	}
	
	/** Method which allows methods outside of class to initiate this dialog object,
	 *  and then receive the return value on submission
	 * 
	 * @param frame
	 * @param game
	 * @return
	 */
	public static Integer[][] showDialog(JFrame frame, GameGraph game) {
		dialog = new AddEdgeDialog(frame, game);
		dialog.setVisible(true);
		
		return edgesInSplitArrays;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getActionCommand().equals("submit")) {
			
			if (checked) {	//If random edge, then set the return array to single row with -1 values, then close dialog
				edgesInSplitArrays = new Integer[][]{{-1},{-1}};
				AddEdgeDialog.dialog.setVisible(false);
			} else {	//Else obtain the node pairings for each edge, separating heads and tails
				String userInput = textField.getText();
				Integer[][] edges = ToolMethods.extractEdges(userInput);

				if (edges!=null) {	//Edge pairing input is not empty
					ArrayList<Integer> heads = new ArrayList<Integer>(Arrays.asList(edges[0]));
					ArrayList<Integer> tails = new ArrayList<Integer>(Arrays.asList(edges[1]));
					boolean allVerticesExist = true;

					for (int i=0;i<heads.size();i++) {
						if (!ToolMethods.isIn(heads.get(i), ToolMethods.getVerticesList(game.getVertices()))) {
							//Throw JOptionPane saying user specified a non existent vertex
							JOptionPane.showMessageDialog(dialog, "Input edges given, contains an index of a vertex which does not exist, please check the edges.");
							allVerticesExist = false;
							break;
						}
						if (!ToolMethods.isIn(tails.get(i), ToolMethods.getVerticesList(game.getVertices()))) {
							//Throw JOptionPane saying user specified a non existent vertex
							JOptionPane.showMessageDialog(dialog, "Input edges given, contains an index of a vertex which does not exist, please check the edges.");
							allVerticesExist = false;
							break;
						}
					}

					if (allVerticesExist) {	//Set the static return for the show method, and close the dialog
						edgesInSplitArrays = edges;
						AddEdgeDialog.dialog.setVisible(false);
					}
				} else	//Return an error as input was empty
					JOptionPane.showMessageDialog(dialog, "Input given for the edges was incorrect, please format it as (x,y);(v,w);...");
			}
		}
		
		if (ae.getActionCommand().equals("cancel"))
			AddEdgeDialog.dialog.setVisible(false);
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		Object source = ie.getItemSelectable();
		
		if(source == checkBox) {
			if (checked) {
				checked = false;
				textField.setEditable(true);
			} else {
				checked = true;
				textField.setEditable(false);
			}
		}
	}
	
}
