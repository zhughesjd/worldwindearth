package net.joshuahughes.worldwindearth.dialog.addedit;

import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.joshuahughes.worldwindearth.WorldWindEarth;

public class AddEditDialog extends JDialog{
	private static final long serialVersionUID = 881876593112086204L;
	JTextField nameField = new JTextField("Untitled");
	JTextArea comments = new JTextArea();
	JButton okButton = new JButton("Ok");
	JButton cancelButton = new JButton("Cancel");
	WorldWindEarth earth;
	JButton button = new JButton("Icon");
	JPanel namePanel = new JPanel(new FlowLayout());
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel panel = new JPanel();
	JOptionPane pane = new JOptionPane(panel,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
	public AddEditDialog(WorldWindEarth earth,String prefix,KMLAbstractObject object) {
		super(earth,false);

		namePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=gbc.gridy = 0;
		gbc.weightx=gbc.weighty = 1;
		namePanel.add(new JLabel("Name:"), gbc);
		gbc.gridx++;
		namePanel.add(nameField, gbc);
		
		panel.setLayout(new BorderLayout());
		panel.add(namePanel, BorderLayout.NORTH);
		panel.add(tabbedPane, BorderLayout.CENTER);
		this.setContentPane(pane);
		this.earth = earth;
		setSize(500,1000);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AddEditDialog.this.earth.setAddEnabled(true);
			}
		});
		setTitle("World Wind Earth - "+prefix+" "+object.getClass().getSimpleName());
		earth.setAddEnabled(false);
		setVisible(true);
	}
}
