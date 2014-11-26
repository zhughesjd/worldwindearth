package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.StarsLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.Earth.BMNGOneImage;
import gov.nasa.worldwind.wms.WMSTiledImageLayer;

import java.awt.Component;
import java.util.Map.Entry;

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

public class WorldwindViewer implements Viewer{
    WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
    WorldMapLayer overviewMap = new WorldMapLayer(){{setPosition(AVKey.SOUTHEAST);}};
    ViewControlsLayer navigation = new ViewControlsLayer(){{setPosition(AVKey.NORTHEAST);}};
    ScalebarLayer scaleLegend = new ScalebarLayer(){{setPosition(AVKey.SOUTHWEST);}};
    SkyGradientLayer atmosphere = new SkyGradientLayer();
    public WorldwindViewer(){
    	BasicModel model = new BasicModel();
        for(Layer layer : model.getLayers()){
        	System.out.println(layer.getName()+" *** "+layer.getClass());
        	if(layer instanceof WMSTiledImageLayer){
        		WMSTiledImageLayer wmsLayer = (WMSTiledImageLayer) layer;
        		for(Entry<String, Object> e : wmsLayer.getEntries()){
        			System.out.println(e);
        		}
        	}
			System.out.println("------------------");
        }
        model.getLayers().removeAll();
        model.getLayers().add(new StarsLayer());
        model.getLayers().add(atmosphere);
        model.getLayers().add(new BMNGOneImage());
        model.getLayers().add(overviewMap);
        model.getLayers().add(navigation);
        model.getLayers().add(scaleLegend);
        wwd.setModel(model);
        wwd.addSelectListener(new ViewControlsSelectListener(wwd,navigation));

    }
	public void setVisible(Overlay view, boolean show) {
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
		return wwd;
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
