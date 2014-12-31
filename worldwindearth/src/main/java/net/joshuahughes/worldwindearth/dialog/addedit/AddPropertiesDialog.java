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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import net.joshuahughes.worldwindearth.WorldWindEarth;
import net.joshuahughes.worldwindearth.panel.EditorTreeModel;
import net.joshuahughes.worldwindearth.support.Support;

public class AddPropertiesDialog extends JDialog{
	private static final long serialVersionUID = 881876593112086204L;
	JTextField nameField = new JTextField("Untitled");
	WorldWindEarth earth;
	JButton iconButton = new JButton("Icon");
	JTabbedPane tabbedPane = new JTabbedPane();
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	public AddPropertiesDialog(final WorldWindEarth earth,final KMLAbstractFeature feature) {
		super(earth,false);
		String prefix = feature.getRoot( ) == ((KMLFolder)earth.getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).getModel( ).getRoot( ).getUserObject( )).getRoot( )?"Edit":"New";
		setSize(500,500);
		this.earth = earth;
		JPanel panel = new JPanel();
		JPanel namePanel = new JPanel(new FlowLayout());
		namePanel.setLayout(new BorderLayout());
        namePanel.add(new JLabel("Name:"),BorderLayout.WEST);
        nameField.setText(feature.getName());
		namePanel.add(nameField,BorderLayout.CENTER);
		if(feature instanceof KMLPlacemark && ((KMLPlacemark)feature).getGeometry( ) instanceof KMLPoint)
		    namePanel.add(iconButton,BorderLayout.EAST);
		if((feature instanceof KMLPlacemark && ((KMLPlacemark)feature).getGeometry( ) instanceof KMLModel) || feature instanceof KMLPhotoOverlay || feature instanceof KMLGroundOverlay || feature instanceof KMLNetworkLink)
		    namePanel.add(new LinkPanel(),BorderLayout.SOUTH);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints( );
        gbc.gridx=gbc.gridy=0;
        gbc.weightx=gbc.weighty=.05;
        gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(namePanel,gbc);
		gbc.gridy++;
		panel.add(getPanel(feature),gbc);
        gbc.gridy++;
		for(JPanel pnl : getPanels(feature))
		    tabbedPane.addTab( pnl.getName(), pnl );
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty=.9;
		panel.add(tabbedPane,gbc);
        gbc.weightx=gbc.weighty=.05;	
        gbc.gridx=0;
        gbc.gridy++;
		panel.add(create(), gbc);
		setContentPane(panel);
		setSize(500,500);
		addComponentListener( new ComponentAdapter() {
			public void componentHidden(ComponentEvent event){
				earth.getViewer().stopEditing();
				AddPropertiesDialog.this.earth.getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).setEnabled( true );
			}
		});
		setTitle("World Wind Earth - "+prefix+" "+feature.getField(Support.KMLTag.name.name()));
		earth.setAddEnabled(false);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				earth.getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).alterTree(feature);
				AddPropertiesDialog.this.setVisible(false);
				AddPropertiesDialog.this.earth.setAddEnabled(true);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddPropertiesDialog.this.setVisible(false);
				AddPropertiesDialog.this.earth.setAddEnabled(true);
			}
		});
		nameField.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				feature.setField(Support.KMLTag.name.name(),nameField.getText());
				feature.applyChange( feature );
				earth.getViewer( ).getWwd( ).redraw( );
			}
			
		});
		setVisible(true);
	}
	
    private JPanel create() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints( );
        gbc.gridx=gbc.gridy=0;
        gbc.weightx=gbc.weighty=1;
        panel.add(new JPanel(),gbc);
        gbc.weightx=gbc.weighty=0;
        gbc.gridx++;
        panel.add(okButton,gbc);
        gbc.gridx++;
        panel.add(cancelButton,gbc);
		return panel;
	}
	private JPanel[] getPanels( KMLAbstractFeature feature )
    {
    	ArrayList<JPanel> panelList = new ArrayList<JPanel>();
    	panelList.add(new DescriptionPanel(feature));
        return panelList.toArray(new JPanel[0]);
    }
    private JPanel getPanel( KMLAbstractFeature feature )
    {
        if(feature instanceof KMLFolder || feature instanceof KMLNetworkLink)return new ExpandedOptionPanel( );
        if(feature instanceof KMLPhotoOverlay || feature instanceof KMLGroundOverlay)return new ImagePhotoPanel( );
        if(feature instanceof KMLPlacemark){
            KMLPlacemark placemark = ( KMLPlacemark ) feature;
            if(placemark.getGeometry( ) instanceof KMLPoint || placemark.getGeometry( ) instanceof KMLModel) return new LatitdueLongitudePanel(placemark);
        }
        return new JPanel();
    }
}
