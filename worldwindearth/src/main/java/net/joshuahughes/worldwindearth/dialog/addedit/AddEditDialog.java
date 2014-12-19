package net.joshuahughes.worldwindearth.dialog.addedit;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLFolder;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLNetworkLink;
import gov.nasa.worldwind.ogc.kml.KMLPhotoOverlay;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;

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
	WorldWindEarth earth;
	JButton iconButton = new JButton("Icon");
	JPanel namePanel = new JPanel(new FlowLayout());
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel panel = new JPanel();
	JOptionPane pane = new JOptionPane(panel,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
	public AddEditDialog(WorldWindEarth earth,String prefix,KMLAbstractFeature feature) {
		super(earth,false);
        System.out.println(feature);
        System.out.println(feature.getName( ));
		this.earth = earth;
		namePanel.setLayout(new BorderLayout());
        namePanel.add(new JLabel("Name:"),BorderLayout.WEST);
        nameField.setText( "Untitled "+feature.getName( ) );
		namePanel.add(nameField,BorderLayout.CENTER);
		if(feature instanceof KMLPlacemark && ((KMLPlacemark)feature).getGeometry( ) instanceof KMLPoint)
		    namePanel.add(iconButton,BorderLayout.EAST);
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints( );
        gbc.gridx=gbc.gridy=0;
        gbc.weightx=05;
        gbc.weighty=.05;
        gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(namePanel,gbc);
		gbc.gridy++;
		panel.add(getPanel(feature),gbc);
        gbc.gridy++;
		for(JPanel panel : getPanels(feature))
		    tabbedPane.addTab( panel.getName(), panel );
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty=.9;
		panel.add(tabbedPane,gbc);
		this.setContentPane(pane);
		this.earth = earth;
		setSize(500,1000);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AddEditDialog.this.earth.setAddEnabled(true);
			}
		});
		setTitle("World Wind Earth - "+prefix+" "+feature.getName( ).replace( "_"," " ));
		earth.setAddEnabled(false);
		setVisible(true);
	}
    private JPanel[] getPanels( KMLAbstractFeature feature )
    {
        return new JPanel[]{new JPanel()};
    }
    private JPanel getPanel( KMLAbstractFeature feature )
    {
        if(feature instanceof KMLFolder || feature instanceof KMLNetworkLink)return new ExpandedOptionPanel( );
        if(feature instanceof KMLPhotoOverlay || feature instanceof KMLGroundOverlay)return new ImagePhotoPanel( );
        if(feature instanceof KMLPlacemark){
            KMLPlacemark placemark = ( KMLPlacemark ) feature;
            if(placemark.getGeometry( ) instanceof KMLPoint || placemark.getGeometry( ) instanceof KMLModel) return new LatitdueLongitudePanel( );
        }
        return new JPanel();
    }
}
