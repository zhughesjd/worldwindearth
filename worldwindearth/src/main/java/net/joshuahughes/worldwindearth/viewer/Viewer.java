package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.StarsLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.Earth.BMNGOneImage;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLLinearRing;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLNetworkLink;
import gov.nasa.worldwind.ogc.kml.KMLPhotoOverlay;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.gx.GXTour;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.StatusLayer;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import net.joshuahughes.worldwindearth.listener.Add;
import net.joshuahughes.worldwindearth.listener.Explore;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Reset;
import net.joshuahughes.worldwindearth.listener.Show_Navigation;
import net.joshuahughes.worldwindearth.listener.View_Size;

public class Viewer extends JPanel{
	private static final long serialVersionUID = 8482957233805118951L;
	WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
    StatusBar statusBar = new StatusBar();
    ViewControlsLayer navigation = new ViewControlsLayer(){{setPosition(AVKey.NORTHEAST);}};
    RenderableLayer kmlLayer = new RenderableLayer();
    public Viewer(){
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
        wwd.getModel().getLayers().add(kmlLayer);

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
	public void add(String wmsPath) {
	}

	public boolean remove(String wmsPath) {
		return false;
	}

	public Component getViewer() {
		return this;
	}

	public void setViewSize(View_Size viewSize) {
	}

	public void setShowNavigation(Show_Navigation showNavigation) {
	}

	public void setReset(Reset reset) {
	}

	public void setExplore(Explore explore) {
	}

	public KMLAbstractObject create(Add creation) {
		KMLAbstractObject object = new KMLAbstractObject() {};
		String uri = null;
		if(Add.Placemark.equals(creation))
			object = new KMLPoint(uri);
		if(Add.Path.equals(creation))
			object = new KMLLinearRing(uri);
		if(Add.Polygon.equals(creation))
			object = new KMLPolygon(uri);
		if(Add.Model.equals(creation))
			object = new KMLModel(uri);
		if(Add.Tour.equals(creation))
			object = new GXTour(uri);
		if(Add.Photo.equals(creation))
			object = new KMLPhotoOverlay(uri);
		if(Add.Image_Overlay.equals(creation))
			object = new KMLGroundOverlay(uri);
		if(Add.Network_Link.equals(creation))
			object = new KMLNetworkLink(uri);
		return object;
	}
	public void add(KMLAbstractObject feature) {
		if(feature instanceof KMLRoot){
	        kmlLayer.addRenderable(new KMLController((KMLRoot) feature));
	        return;
		}
	}
	public boolean remove(KMLAbstractObject feature) {
		return false;
	}
}