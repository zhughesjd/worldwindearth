package net.joshuahughes.javaearth.viewer.worldwind;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.StarsLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.Earth.BMNGOneImage;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.StatusLayer;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import net.joshuahughes.javaearth.listener.Create;
import net.joshuahughes.javaearth.listener.Explore;
import net.joshuahughes.javaearth.listener.Overlay;
import net.joshuahughes.javaearth.listener.Reset;
import net.joshuahughes.javaearth.listener.Show_Navigation;
import net.joshuahughes.javaearth.listener.View_Size;
import net.joshuahughes.javaearth.viewer.Viewer;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.GroundOverlay;
import de.micromata.opengis.kml.v_2_2_0.NetworkLink;
import de.micromata.opengis.kml.v_2_2_0.PhotoOverlay;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.gx.Tour;

public class WorldwindViewer extends JPanel implements Viewer{
	private static final long serialVersionUID = 8482957233805118951L;
	WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
    StatusBar statusBar = new StatusBar();
    ViewControlsLayer navigation = new ViewControlsLayer(){{setPosition(AVKey.NORTHEAST);}};
    public WorldwindViewer(){
    	super(new BorderLayout());
        wwd.setModel(new BasicModel(){{getLayers().removeAll();}});
		wwd.getModel().getLayers().add(new SkyGradientLayer(){{setName(Overlay.Atmosphere.name());}});
		wwd.getModel().getLayers().add(new WorldMapLayer(){{setName(Overlay.Overview_Map.name());setPosition(AVKey.SOUTHEAST);}});
		wwd.getModel().getLayers().add(new ScalebarLayer(){{setName(Overlay.Scale_Legend.name());setPosition(AVKey.SOUTHWEST);}});
		wwd.getModel().getLayers().add(new LatLonGraticuleLayer(){{setName(Overlay.Grid.name());}});
		wwd.getModel().getLayers().add(new StatusLayer(){{setName(Overlay.Status_Bar.name());}});
        wwd.getModel().getLayers().add(new StarsLayer());
        wwd.getModel().getLayers().add(new BMNGOneImage());
        wwd.getModel().getLayers().add(navigation);
        wwd.addSelectListener(new ViewControlsSelectListener(wwd,navigation));
    	add(wwd, BorderLayout.CENTER);
    	add(statusBar, BorderLayout.PAGE_END);
        statusBar.setEventSource(wwd);
        for (Layer layer : wwd.getModel().getLayers())
        {
            if (layer instanceof SelectListener)
            {
                wwd.addSelectListener((SelectListener) layer);
            }
        }

    }
	public void setVisible(final Overlay overlay, boolean show) {
		if(Overlay.Status_Bar.equals(overlay)){
			removeAll();
	    	add(wwd, BorderLayout.CENTER);
	    	if(show)
	    		add(statusBar, BorderLayout.PAGE_END);
	    	revalidate();
		}
		Layer layer = wwd.getModel().getLayers().getLayerByName(overlay.name());
		if(layer !=null)layer.setEnabled(show);
		wwd.redraw();
	}
	public void setVisible(Class<Layer> clazz,boolean show){
	}
	public void add(Feature feature) {
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
		return this;
	}

	@Override
	public void setViewSize(View_Size viewSize) {
	}

	@Override
	public void setShowNavigation(Show_Navigation showNavigation) {
	}

	@Override
	public void setReset(Reset reset) {
	}

	@Override
	public void setExplore(Explore explore) {
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
		return feature;
	}
}
