package tool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

/** Dialog which is instantiated to add a vertex to the game.
 * 
 * @author Jackie, s1035578
 *
 */
public class RemoveVertexDialog extends JDialog implements ActionListener{

	private static JDialog dialog;
	private static GameGraph game;
	private static JTextField textField;
	private static ArrayList<Integer> vertices;
	
	private RemoveVertexDialog(JFrame frame, GameGraph game) {
		super(frame, "Remove a vertex", true);
		
		RemoveVertexDialog.game = game;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		getContentPane().add(initUserPanel(), BorderLayout.PAGE_START);
		getContentPane().add(initButtonPanel(), BorderLayout.PAGE_END);
		pack();
	}
	
	/** Panel where the user inputs information regarding the vertices to be removed
	 * 
	 * @return
	 */
	private JPanel initUserPanel() {
		//Set up the panel
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.PAGE_AXIS));
		
		JLabel q1Label = new JLabel("List the vertices to be removed:");
		q1Label.setFont(new Font("Dialog", Font.PLAIN, 14));
		userPanel.add(q1Label);
		userPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		textField = new JTextField();
		textField.setSize(new Dimension(150,40));
		userPanel.add(textField);
		userPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		userPanel.add(Box.createVerticalGlue());
		
		userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return userPanel;
	}
	
	/** Submit and cancel buttons
	 * 
	 * @return
	 */
	private JPanel initButtonPanel() {
		//Set up the panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		
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
	
	/** Main method which is accessed by other classes to create the dialog for
	 *  removing vertices.  This method returns an arraylist containing the vertices
	 *  to be removed.
	 * 
	 * @param frame
	 * @param game
	 * @return
	 */
	public static ArrayList<Integer> showDialog(JFrame frame, GameGraph game) {
		dialog = new RemoveVertexDialog(frame, game);
		dialog.setVisible(true);
		return vertices;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("submit")) {
			
			String userInput = textField.getText();
			ArrayList<Integer> vertIndex = new ArrayList<Integer>();
			
			if (userInput.matches("(\\d+\\,)*\\d+")) {
				String[] strVerts = userInput.split(",");
				boolean successful = true;
				for (int i=0;i<strVerts.length;i++) {
					Integer vert = Integer.parseInt(strVerts[i]);
					if (ToolMethods.isIn(vert, game.getExistVerticesIndex()))
						vertIndex.add(vert);
					else {
						JOptionPane.showMessageDialog(dialog, "Vertices given included a vertex which does not exist in the game, please check the vertices.");
						successful = false;
						break;
					}
				}
				if (successful) {
					RemoveVertexDialog.vertices = vertIndex;
					RemoveVertexDialog.dialog.dispose();
				}
			} else
				JOptionPane.showMessageDialog(dialog, "Input format was incorrect, please format as follows: \n x,y,z,...");
		}
		
		if (ae.getActionCommand().equals("cancel"))
			RemoveVertexDialog.dialog.setVisible(false);
	}
	
}
