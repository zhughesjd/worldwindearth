package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.IconLayer;
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
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.render.UserFacingIcon;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.StatusLayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import net.joshuahughes.worldwindearth.listener.Explore;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Reset;
import net.joshuahughes.worldwindearth.listener.Show_Navigation;
import net.joshuahughes.worldwindearth.listener.View_Size;
import net.joshuahughes.worldwindearth.support.Support;

public class Viewer extends JPanel{
	private static final long serialVersionUID = 8482957233805118951L;
	WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
	KMLExcludedDragger dragger = new KMLExcludedDragger(wwd);
	StatusBar statusBar = new StatusBar();
    ViewControlsLayer navigation = new ViewControlsLayer(){{setPosition(AVKey.NORTHEAST);}};
    RenderableLayer kmlLayer = new RenderableLayer();
    IconLayer editLayer = new IconLayer();
	private KMLAbstractFeature feature;
	KMLController editController;
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
        dragger.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adjust();
			}
		});
        wwd.getModel().getLayers().add(kmlLayer);
        wwd.getModel().getLayers().add(editLayer);
        getWwd().addSelectListener(dragger);
    }
	protected void adjust() {
		Position position = dragger.getPosition();
		if(feature!=null && feature instanceof KMLPlacemark && ((KMLPlacemark)feature).getGeometry() instanceof KMLPoint){
			((KMLPlacemark)feature).applyChange(Support.create(position.getLatitude().getDegrees(),position.getLongitude().getDegrees()));
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


    public void add(KMLRoot kmlRoot) {
        kmlLayer.addRenderable(new KMLController(kmlRoot));
    }
    public void stopEditing(){
    	if(editController!=null)
    		kmlLayer.removeRenderable(editController);
    	editLayer.removeAllIcons();
    }
    public void edit(KMLRoot root) {
    	stopEditing();
    	KMLAbstractFeature candidateFeature = root.getFeature();
    	if(candidateFeature !=null && candidateFeature instanceof KMLPlacemark && ((KMLPlacemark)candidateFeature).getGeometry() instanceof KMLPoint){
    		editController = new KMLController( root);
    		feature = candidateFeature;
    		KMLPoint point = (KMLPoint) ((KMLPlacemark)feature).getGeometry();
            BufferedImage image = new BufferedImage(30,30,BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = image.createGraphics();
            g2d.setBackground(new Color(0,0,0,1));
            g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
    		editLayer.addIcon(new UserFacingIcon(image,Position.fromDegrees(point.getCoordinates().getLatitude().getDegrees(),point.getCoordinates().getLongitude().getDegrees())));
    		kmlLayer.addRenderable(editController);
    		wwd.redraw();
    	}
    	
    }
	public Sector getViewExtents(){
        View view = wwd.getView();
        Rectangle viewport = view.getViewport();
        ArrayList<LatLon> corners = new ArrayList<LatLon>();
        corners.add( view.computePositionFromScreenPoint(viewport.getMinX(), viewport.getMinY()));
        corners.add( view.computePositionFromScreenPoint(viewport.getMinX(), viewport.getMaxY()));
        corners.add( view.computePositionFromScreenPoint(viewport.getMaxX(), viewport.getMaxY()));
        corners.add( view.computePositionFromScreenPoint(viewport.getMaxX(), viewport.getMinY()));
        if(Sector.isSector( corners )) 
            return Sector.boundingSector(corners);
        return Sector.FULL_SPHERE;
	}
	public Position getPosition() {
		return wwd.getView().getCurrentEyePosition();
	}
    public void setViewSize(View_Size viewSize) {
    }

    public void setShowNavigation(Show_Navigation showNavigation) {
    }

    public void setReset(Reset reset) {
    }

    public void setExplore(Explore explore) {
    }
    public WorldWindowGLCanvas getWwd( )
    {
        return this.wwd;
    }
}
