package net.joshuahughes.worldwindearth.viewer;

import java.awt.Component;

import javax.swing.JLabel;

import net.joshuahughes.worldwindearth.listener.Create;
import net.joshuahughes.worldwindearth.listener.Explore;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Reset;
import net.joshuahughes.worldwindearth.listener.Show_Navigation;
import net.joshuahughes.worldwindearth.listener.View_Size;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.GroundOverlay;
import de.micromata.opengis.kml.v_2_2_0.NetworkLink;
import de.micromata.opengis.kml.v_2_2_0.PhotoOverlay;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.gx.Tour;

public class TextViewer implements Viewer{
	JLabel label = new JLabel("empty viewer",JLabel.CENTER);
	public void setVisible(Overlay view, boolean show) {
		label.setText("overlay "+view.name()+" visibility: "+show);
	}

	public void add(Feature feature) {
		if(feature instanceof Placemark)
			label.setText("creating: "+((Placemark)feature).getGeometry().getClass().getSimpleName());
		else
			label.setText("creating: "+feature.getClass().getSimpleName());
	}

	public boolean remove(Feature feature) {
		return false;
	}

	public void add(String wmsPath) {
	}

	public boolean remove(String wmsPath) {
		return false;
	}

	public Component getViewer() {
		return label;
	}

	@Override
	public void setViewSize(View_Size viewSize) {
		label.setText(viewSize.name());
	}

	@Override
	public void setShowNavigation(Show_Navigation showNavigation) {
		label.setText(showNavigation.name());
		
	}

	@Override
	public void setReset(Reset reset) {
		label.setText(reset.name());
		
	}

	@Override
	public void setExplore(Explore explore) {
		label.setText(explore.name());
		
	}

	@Override
	public Feature create(Create creation) {
		Placemark placemark = new Placemark();
		Feature feature = placemark;
		if(Create.Placemark.equals(creation))
			placemark.createAndSetPoint();
		if(Create.Path.equals(creation))
			placemark.createAndSetLineString();
		if(Create.Model.equals(creation))
			placemark.createAndSetModel();
		if(Create.Polygon.equals(creation))
			placemark.createAndSetPolygon();
		if(Create.Image_Overlay.equals(creation))
			feature = new GroundOverlay();
		if(Create.Tour.equals(creation))
			feature = new Tour();
		if(Create.Image_Overlay.equals(creation))
			feature = new PhotoOverlay();
		if(Create.Network_Link.equals(creation))
			feature = new NetworkLink();
		label.setText("Returning:"+feature.toString());
		return feature;
	}
}
