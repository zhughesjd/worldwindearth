package net.joshuahughes.worldwindearth.dialog.addedit;

import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import net.joshuahughes.worldwindearth.support.Support;

public class LatitdueLongitudePanel extends AbstractPanel
{
	private static final long serialVersionUID = -1260396815326982917L;
	JTextField latField = new JTextField(){private static final long serialVersionUID = 1L;{setName("Latitude");}};
	JTextField lonField = new JTextField(){private static final long serialVersionUID = 1L;{setName("Longitude");}};
	private KMLPlacemark placemark;
	public LatitdueLongitudePanel(final KMLPlacemark placemark){
		super(new GridBagLayout( ));
		this.placemark = placemark;
		adjust();
		placemark.getRoot().addPropertyChangeListener( new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					adjust();
				}
		});
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=gbc.gridy=0;
		gbc.weighty=1;
		for(JTextField field : new JTextField[]{latField,lonField}){
			field.addFocusListener(new FocusAdapter() {
				double lat,lon;
				@Override
				public void focusLost(FocusEvent e) {
					try{
						double latitude = Double.parseDouble(latField.getText().trim());
						double longitude = Double.parseDouble(lonField.getText().trim());
						KMLPoint point = (KMLPoint)placemark.getGeometry();
						point.applyChange( Support.createPoint(latitude,longitude) );
						placemark.getRoot().firePropertyChange("",null,null);

					}catch(NumberFormatException exception){
						latField.setText(lat+"");
						lonField.setText(lon+"");
					}
				}
				@Override
				public void focusGained(FocusEvent e) {

					lat = Double.parseDouble(latField.getText().trim());
					lon = Double.parseDouble(lonField.getText().trim());
				}
			});
			gbc.weightx=.1;
			gbc.gridx=0;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.LINE_END;
			add(new JLabel(field.getName( )+":"),gbc);
			gbc.weightx=.9;
			gbc.anchor = GridBagConstraints.LINE_START;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx++;
			add(field,gbc);
			gbc.gridy++;
		}
	}
	public void adjust(){
		if(placemark.getGeometry() instanceof KMLPoint){
			KMLPoint point = (KMLPoint) placemark.getGeometry();
			latField.setText(point.getCoordinates().getLatitude().getDegrees()+"");
			lonField.setText(point.getCoordinates().getLongitude().getDegrees()+"");
		}
		if(placemark.getGeometry() instanceof KMLModel){
			KMLModel model = (KMLModel) placemark.getGeometry();
			latField.setText(model.getLocation().getLatitude()+"");
			lonField.setText(model.getLocation().getLongitude()+"");
		}
	}
}
