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
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.StatusLayer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;

import javax.swing.JPanel;

import net.joshuahughes.worldwindearth.listener.Explore;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Reset;
import net.joshuahughes.worldwindearth.listener.Show_Navigation;
import net.joshuahughes.worldwindearth.listener.View_Size;
import net.joshuahughes.worldwindearth.viewer.controllayer.ControlLayer;
import net.joshuahughes.worldwindearth.viewer.controllayer.EmptyLayer;
import net.joshuahughes.worldwindearth.viewer.controllayer.LatLonBoxLayer;
import net.joshuahughes.worldwindearth.viewer.controllayer.LineStringLayer;
import net.joshuahughes.worldwindearth.viewer.controllayer.PointLayer;

public class Viewer extends JPanel{
	private static final long serialVersionUID = 8482957233805118951L;
	private static KMLRoot emptyRoot;
	static{
		try {
			emptyRoot = KMLRoot.createAndParse(new ByteArrayInputStream("<kml></kml>".getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
	KMLExcludedDragger dragger = new KMLExcludedDragger(wwd);
	StatusBar statusBar = new StatusBar();
	RenderableLayer kmlLayer = new RenderableLayer();
	ControlLayer<? extends KMLAbstractObject> controlLayer;
	KMLController editController = new KMLController(emptyRoot);
	public Viewer(){
		super(new BorderLayout());
		ViewControlsLayer navigation = new ViewControlsLayer(){{setPosition(AVKey.NORTHEAST);}};
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
				wwToKML();
			}
		});
		wwd.getModel().getLayers().add(kmlLayer);
		getWwd().addSelectListener(dragger);
		kmlLayer.addRenderable(editController);
	}
	public void wwToKML() {
		controlLayer.adjust(dragger.getMovable());
		wwd.redraw( );
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
		editController.setKmlRoot(emptyRoot);
		wwd.getModel().getLayers().remove(controlLayer);
		if(controlLayer instanceof LineStringLayer){
			LineStringLayer layer = (LineStringLayer) controlLayer;
			wwd.removeMouseListener(layer.getAdapter());
			wwd.removeMouseMotionListener(layer.getAdapter());
		}
		controlLayer = null;
	}
	public void edit(KMLAbstractFeature feature) {
		controlLayer = new EmptyLayer(feature);
		editController.setKmlRoot(feature.getRoot( ));
		if(feature instanceof KMLPlacemark){
			final KMLPlacemark placemark = (KMLPlacemark) feature;
			if(placemark.getGeometry() instanceof KMLPoint)
				controlLayer = new PointLayer((KMLPoint) placemark.getGeometry());
			if(placemark.getGeometry() instanceof KMLLineString || placemark.getGeometry() instanceof KMLPolygon){
				KMLLineString lineString = placemark.getGeometry( ) instanceof KMLLineString?(KMLLineString)placemark.getGeometry( ):((KMLPolygon)placemark.getGeometry( )).getOuterBoundary( );
				controlLayer = new LineStringLayer(lineString);
			}
		}
		if(feature instanceof KMLGroundOverlay){
			KMLGroundOverlay overlay = (KMLGroundOverlay) feature;
			if(overlay.getLatLonBox()!=null)
				controlLayer = new LatLonBoxLayer(overlay.getLatLonBox());
		}
		wwd.getModel().getLayers().add(controlLayer);
		if(controlLayer instanceof LineStringLayer){
			LineStringLayer layer = (LineStringLayer) controlLayer;
			wwd.addMouseListener(layer.getAdapter());
			wwd.addMouseMotionListener(layer.getAdapter());
		}
		wwd.redraw();
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
