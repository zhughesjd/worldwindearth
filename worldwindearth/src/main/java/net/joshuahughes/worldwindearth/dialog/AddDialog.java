package net.joshuahughes.worldwindearth.dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AddDialog extends JDialog{
	private static final long serialVersionUID = 881876593112086204L;
	JTextField name = new JTextField("Untitled");
	JTextArea comments = new JTextArea();
	JButton okButton = new JButton("Ok");
	JButton cancelButton = new JButton("Cancel");
	public AddDialog() {
		setTitle("NASA Earth - New "+this.getClass().getSimpleName());
	}
}
