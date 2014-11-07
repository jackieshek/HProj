package tool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

/** Dialog class, which is instantiated every time the new vertex button is pressed.
 * 
 * @author Jackie, s1035578
 */

public class AddVertexDialog extends JDialog implements ActionListener {

	private static AddVertexDialog dialog;
	private static GameGraph game;
	private static boolean vertex = true;
	private static JTextField textField;
	private static String neighbours;

	private AddVertexDialog(JFrame frame, GameGraph game) {
		super(frame, "Add a vertex", true);
		
		AddVertexDialog.game = game;
		this.neighbours = new String();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		vertex=true;
		
		getContentPane().add(initRadioPanel(), BorderLayout.PAGE_START);
		getContentPane().add(initUserPanel(), BorderLayout.CENTER);
		getContentPane().add(initButtonPanel(), BorderLayout.PAGE_END);
		pack();
	}
	
	/** Initialise the selection panel for whether the vertex is cooperating or defecting
	 * 
	 * @return
	 */
	private JPanel initRadioPanel() {
		//Panel where user selects whether the vertex is cooperating or defecting
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(0,1));

		JLabel q1Label = new JLabel("Is the vertex...");
		q1Label.setFont(new Font("Dialog", Font.PLAIN, 14));
		radioPanel.add(q1Label);

		ButtonGroup group = new ButtonGroup();

		JRadioButton coopButton = new JRadioButton("Cooperating?");
		coopButton.setActionCommand("coop");
		coopButton.addActionListener(this);
		coopButton.setSelected(true);

		JRadioButton defectButton = new JRadioButton("Defecting?");
		defectButton.setActionCommand("defect");
		defectButton.addActionListener(this);

		group.add(coopButton);
		group.add(defectButton);	

		radioPanel.add(coopButton);
		radioPanel.add(defectButton);
		radioPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		return radioPanel;
	}

	/** Sets up the panel which the user inputs the neighbours of the new vertex
	 * 
	 * @return
	 */
	private JPanel initUserPanel() {
		//Set up the panel which asks for information
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.PAGE_AXIS));

		JLabel q1Label = new JLabel("List the adjacent vertices: ");
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

	/** Button panel for submit and cancel initialisation
	 * 
	 * @return
	 */
	private JPanel initButtonPanel(){
		//Set up the button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

		buttonPanel.add(Box.createHorizontalGlue());

		JButton submitButton = new JButton("Submit");
		submitButton.setFont(new Font("Dialog", Font.PLAIN, 14));
		submitButton.setActionCommand("submit");
		getRootPane().setDefaultButton(submitButton);
		submitButton.addActionListener(this);
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
	public static String[] showDialog(JFrame frame, GameGraph game) {
		dialog = new AddVertexDialog(frame, game);
		dialog.setVisible(true);
		return new String[] {String.valueOf(vertex), neighbours};
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getActionCommand().equals("coop"))
			AddVertexDialog.vertex = true;
		
		if (ae.getActionCommand().equals("defect"))
			AddVertexDialog.vertex = false;
		
		if (ae.getActionCommand().equals("submit")) {
			
			String userInput = textField.getText();
			ArrayList<Integer> neighbourVerts = new ArrayList<Integer>();
			boolean successfulInput = true;
			
			try {
				 neighbourVerts = ToolMethods.extractVertices(userInput);
			} catch (Exception e) {
				System.out.println("The format which you have listed the vertices are incorrect, please refer to the guidelines.");
				e.printStackTrace();
			}
			
			//Need to check if the given list of vertices, are indeed vertices of the graph.
			if (neighbourVerts!=null) {
				for (int i=0;i<neighbourVerts.size();i++) {
					ArrayList<Integer> verticesIndex = ToolMethods.getVerticesList(game.getVertices());
					if (!ToolMethods.isIn(neighbourVerts.get(i), verticesIndex)) {
						//Throw JOptionPane which says, the user listed a vertex not in the actual game
						successfulInput = false;
						JOptionPane.showMessageDialog(AddVertexDialog.dialog, "Among the list of vertices given, is an invalid vertex, \n please check that the list is correct.");
						break;
					}
				}
				if (successfulInput) {
					AddVertexDialog.neighbours = AddVertexDialog.textField.getText();
					AddVertexDialog.dialog.dispose();
				}
			} else
				JOptionPane.showMessageDialog(AddVertexDialog.dialog, "List of vertices given was incorrectly formatted.\n Please format as such 1,2,4,...");
		}
		
		if (ae.getActionCommand().equals("cancel"))
			AddVertexDialog.dialog.setVisible(false);
	}
}
