package net.joshuahughes.worldwindearth.dialog.addedit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.joshuahughes.worldwindearth.WorldWindEarth;
import net.joshuahughes.worldwindearth.listener.Add;

public class AddEditDialog extends JDialog{
	private static final long serialVersionUID = 881876593112086204L;
	JTextField name = new JTextField("Untitled");
	JTextArea comments = new JTextArea();
	JButton okButton = new JButton("Ok");
	JButton cancelButton = new JButton("Cancel");
	WorldWindEarth earth;
	public AddEditDialog(WorldWindEarth earth) {
		super(earth,false);
		this.earth = earth;
		setSize(500,1000);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AddEditDialog.this.earth.setAddEnabled(true);
			}
		});

	}
	public void set(Add add){
		setTitle("World Wind Earth - New "+add.name());
		earth.setAddEnabled(false);
	}
}
