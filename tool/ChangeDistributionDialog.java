package tool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/** Dialog class that changes the distribution.
 * 
 * @author Jackie, s1035578
 *
 */
public class ChangeDistributionDialog extends JDialog implements ActionListener{

	private static ChangeDistributionDialog dialog;
	private static GameGraph game;
	private static boolean setVerticesTo = true;
	private static JTextField textField;
	private static ArrayList<Integer> vertices;
	
	private ChangeDistributionDialog(JFrame frame, GameGraph game) {
		super(frame, "Change the distribution", true);
		
		ChangeDistributionDialog.game = game;
		ChangeDistributionDialog.vertices = new ArrayList<Integer>();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setVerticesTo = true;
		
		getContentPane().add(initRadioPanel(), BorderLayout.PAGE_START);
		getContentPane().add(initUserPanel(), BorderLayout.CENTER);
		getContentPane().add(initButtonPanel(), BorderLayout.PAGE_END);
		pack();
	}
	
	/** Set up panel which sets the new strategy
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
	
	/** Panel asking for the set of vertices to change to the new strategy
	 * 
	 * @return
	 */
	private JPanel initUserPanel() {
		//Set up the panel which asks for information
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.PAGE_AXIS));

		JLabel q1Label = new JLabel("List of vertices to change: ");
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
	
	/** Button panel containing the submit and cancel buttons
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
	public static ArrayList<Integer> showDialog(JFrame frame, GameGraph game) {
		dialog = new ChangeDistributionDialog(frame, game);
		dialog.setVisible(true);
		return vertices; 
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getActionCommand().equals("coop"))
			ChangeDistributionDialog.setVerticesTo = true;
		
		if (ae.getActionCommand().equals("defect"))
			ChangeDistributionDialog.setVerticesTo = false;
		
		if (ae.getActionCommand().equals("submit")) {
			
			String userInput = textField.getText();
			ArrayList<Integer> extractedVerts = new ArrayList<Integer>();
			boolean successfulInput = true;
			
			try {
				extractedVerts = ToolMethods.extractVertices(userInput);
			} catch (Exception e) {
				System.out.println("The format which you have listed the vertices are incorrect, please refer to the guidelines.");
				e.printStackTrace();
			}
			
			//Need to check if the given list of vertices, are indeed vertices of the graph.
			if (extractedVerts!=null) {
				for (int i=0;i<extractedVerts.size();i++) {
					ArrayList<Integer> verticesIndex = ToolMethods.getVerticesList(game.getVertices());
					if (!ToolMethods.isIn(extractedVerts.get(i), verticesIndex)) {
						//Throw JOptionPane which says, the user listed a vertex not in the actual game
						successfulInput = false;
						JOptionPane.showMessageDialog(ChangeDistributionDialog.dialog, "Among the list of vertices given, is an invalid vertex, \n please check that the list is correct.");
						break;
					}
				}
				if (successfulInput) {	//If the input error, set up the return arraylist with correct values
					int header=0;
					if (setVerticesTo)
						header=1;
					ChangeDistributionDialog.vertices.add(header);
					ChangeDistributionDialog.vertices.addAll(extractedVerts);
					ChangeDistributionDialog.dialog.dispose();
				}
			} else
				JOptionPane.showMessageDialog(ChangeDistributionDialog.dialog, "List of vertices given was incorrectly formatted.\n Please format as such 1,2,4,...");
		}
		
		if (ae.getActionCommand().equals("cancel"))
			ChangeDistributionDialog.dialog.setVisible(false);

	}
}
