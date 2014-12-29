package net.joshuahughes.worldwindearth.dialog.addedit;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

import net.joshuahughes.worldwindearth.support.Support;

public class LatitdueLongitudePanel extends AbstractPanel
{
	private static final long serialVersionUID = -1260396815326982917L;
	JTextField latField = new JTextField(){private static final long serialVersionUID = 1L;{setName("Latitude");}};
	JTextField lonField = new JTextField(){private static final long serialVersionUID = 1L;{setName("Longitude");}};
	public LatitdueLongitudePanel(final KMLPlacemark placemark){
		super(new GridBagLayout( ));
		if(placemark.getGeometry() instanceof KMLPoint){
			final KMLPoint point = (KMLPoint) placemark.getGeometry();
			latField.setText(point.getCoordinates().getLatitude().getDegrees()+"");
			lonField.setText(point.getCoordinates().getLongitude().getDegrees()+"");
			point.getRoot().addPropertyChangeListener( new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					Object object = event.getNewValue();
					if(object != null && object instanceof List){
						List<?> list = (List<?>) object;
						if(list.size()>0 && list.get(0) instanceof Position){
							Position position = (Position) list.get(0);
							latField.setText(position.getLatitude().getDegrees()+"");
							lonField.setText(position.getLongitude().getDegrees()+"");
						}
					}
				}
			});
		}
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
						Position oldPosition = ((KMLPoint)placemark.getGeometry()).getCoordinates();
						placemark.applyChange( Support.create(latitude,longitude) );
						placemark.getRoot().firePropertyChange(Position.class.getName(), Arrays.asList(oldPosition), Arrays.asList(((KMLPoint)placemark.getGeometry()).getCoordinates()));

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
}
