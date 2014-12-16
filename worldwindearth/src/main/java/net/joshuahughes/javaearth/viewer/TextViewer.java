package net.joshuahughes.javaearth.viewer;

import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLLinearRing;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLNetworkLink;
import gov.nasa.worldwind.ogc.kml.KMLPhotoOverlay;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.gx.GXTour;

import java.awt.Component;

import javax.swing.JLabel;

import net.joshuahughes.javaearth.listener.Create;
import net.joshuahughes.javaearth.listener.Explore;
import net.joshuahughes.javaearth.listener.Overlay;
import net.joshuahughes.javaearth.listener.Reset;
import net.joshuahughes.javaearth.listener.Show_Navigation;
import net.joshuahughes.javaearth.listener.View_Size;

public class TextViewer implements Viewer{
	JLabel label = new JLabel("empty viewer",JLabel.CENTER);
	public void setVisible(Overlay view, boolean show) {
		label.setText("overlay "+view.name()+" visibility: "+show);
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
	public void add(KMLAbstractObject feature) {
		label.setText("adding: "+feature.getClass().getSimpleName());
	}

	@Override
	public boolean remove(KMLAbstractObject feature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public KMLAbstractObject create(Create creation) {
		KMLAbstractObject object = new KMLAbstractObject() {};
		String uri = null;
		if(Create.Placemark.equals(creation))
			object = new KMLPoint(uri);
		if(Create.Path.equals(creation))
			object = new KMLLinearRing(uri);
		if(Create.Model.equals(creation))
			object = new KMLModel(uri);
		if(Create.Polygon.equals(creation))
			object = new KMLPolygon(uri);
		if(Create.Image_Overlay.equals(creation))
			object = new KMLPhotoOverlay(uri);
		if(Create.Tour.equals(creation))
			object = new GXTour(uri);
		if(Create.Network_Link.equals(creation))
			object = new KMLNetworkLink(uri);
		label.setText("Returning:"+object.getClass().getName());
		return object;
	}
}
