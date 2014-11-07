package tool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

/** Dialog which removes a specified edge from the spatial game.
 * 
 * @author Jackie, s1035578
 *
 */
public class RemoveEdgeDialog extends JDialog implements ActionListener{

	private static JDialog dialog;
	private static GameGraph game;
	private static JTextField textField;
	private static Integer[][] edgesInSplitArrays;
	
	private RemoveEdgeDialog(JFrame frame, GameGraph game) {
		super(frame, "Remove an edge", true);
		
		RemoveEdgeDialog.game = game;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		getContentPane().add(initUserPanel(), BorderLayout.PAGE_START);
		getContentPane().add(initButtonPanel(), BorderLayout.PAGE_END);
		pack();
	}
	
	/** Panel which sets up fields to for the user to input the edges to be removed
	 * 
	 * @return
	 */
	private JPanel initUserPanel() {
		//Set up the panel
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.PAGE_AXIS));
		
		JLabel q1Label = new JLabel("List the edges to be removed in the format:");
		q1Label.setFont(new Font("Dialog", Font.PLAIN, 14));
		JLabel q2Label = new JLabel("(x,y);(v,w);...");
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
	
	/** Button panel containing submit and cancel
	 * 
	 * @return
	 */
	private JPanel initButtonPanel() {
		//Set up the panel
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
		buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return buttonPanel;
	}
	
	/** Method used by other classes to create a removeEdgeDialog, and obtain the
	 *  data needed for the parent/caller class to remove edges.
	 * 
	 * @param frame
	 * @param game
	 * @return
	 */
	public static Integer[][] showDialog(JFrame frame, GameGraph game) {
		dialog = new RemoveEdgeDialog(frame, game);
		dialog.setVisible(true);
		
		return edgesInSplitArrays;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getActionCommand().equals("submit")) {
			
			String userInput = textField.getText();
			Integer[][] edges = ToolMethods.extractEdges(userInput);
			
			if (edges!=null) {
				ArrayList<Integer> heads = new ArrayList<Integer>(Arrays.asList(edges[0]));
				ArrayList<Integer> tails = new ArrayList<Integer>(Arrays.asList(edges[1]));
				boolean allVerticesExist = true;
				boolean allEdgesExist = true;
				
				for (int i=0;i<heads.size();i++) {
					if (!ToolMethods.isIn(heads.get(i), ToolMethods.getVerticesList(game.getVertices()))) {
						//Throw JOptionPane saying user specified a non existent vertex
						JOptionPane.showMessageDialog(dialog, "Input edges given, contains an index of a vertex which does not exist, please check the edges.");
						allVerticesExist = false;
						break;
					}
					if (!ToolMethods.isIn(tails.get(i), ToolMethods.getVerticesList(game.getVertices()))) {
						JOptionPane.showMessageDialog(dialog, "Input edges given, contains an index of a vertex which does not exist, please check the edges.");
						allVerticesExist = false;
						break;
					}
					if (!game.getAdjMatrix().get(heads.get(i)).get(tails.get(i)).equals(true)) {
						JOptionPane.showMessageDialog(dialog, "One (some) of the edges given lists an edge which does not exist, please check the edges given.");
						allEdgesExist = false;
						break;
					}
				}
				
				if (allVerticesExist && allEdgesExist) {
					edgesInSplitArrays = edges;
					RemoveEdgeDialog.dialog.setVisible(false);
				}
			} else
				JOptionPane.showMessageDialog(dialog, "Input given for the edges was incorrect, please format it as (x,y);(v,w);...");
		}
		
		if (ae.getActionCommand().equals("cancel"))
			RemoveEdgeDialog.dialog.setVisible(false);
	}
	
}
