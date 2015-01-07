package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
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
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.StatusLayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.joshuahughes.worldwindearth.listener.Explore;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Reset;
import net.joshuahughes.worldwindearth.listener.Show_Navigation;
import net.joshuahughes.worldwindearth.listener.View_Size;

public class Viewer extends JPanel{
	private static final long serialVersionUID = 8482957233805118951L;
	private static String imagePath;
	private static KMLRoot emptyRoot;
	static{
		BufferedImage image = new BufferedImage(30,30,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setBackground(Color.white);
		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		try {
			File file = File.createTempFile("temp", ".jpg");
			ImageIO.write(image, "png",file);
			imagePath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		addAdapter();
	}
	protected void wwToKML() {
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
	}
	public void edit(KMLAbstractFeature feature) {
		if(feature !=null){
			editController.setKmlRoot(feature.getRoot( ));
			if(feature instanceof KMLPlacemark){
				final KMLPlacemark placemark = (KMLPlacemark) feature;
				if(placemark.getGeometry() instanceof KMLPoint)
					wwd.getModel().getLayers().add(controlLayer = new PointLayer((KMLPoint) placemark.getGeometry()));
				if(placemark.getGeometry() instanceof KMLLineString || placemark.getGeometry() instanceof KMLPolygon){
					KMLLineString lineString = placemark.getGeometry( ) instanceof KMLLineString?(KMLLineString)placemark.getGeometry( ):((KMLPolygon)placemark.getGeometry( )).getOuterBoundary( );
					wwd.getModel().getLayers().add(controlLayer = new LineStringLayer(lineString));
				}
			}
			if(feature instanceof KMLGroundOverlay){
				KMLGroundOverlay overlay = (KMLGroundOverlay) feature;
				if(overlay.getLatLonBox()!=null)wwd.getModel().getLayers().add(controlLayer = new LatLonBoxLayer(overlay.getLatLonBox()));
			}
			wwd.redraw();
		}
	}
	private Renderable createPointPlacemark(Position position,Color color,double scale) {
		return createPointPlacemark(position, color, scale,new Offset( 0.5, 0.5, AVKey.FRACTION, AVKey.FRACTION ));
	}
	private Renderable createPointPlacemark(Position position,Color color,double scale, Offset offset) {
		PointPlacemark placemark = new PointPlacemark(position);
		PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
		attrs.setImageAddress(imagePath);
		attrs.setImageColor(color);
		attrs.setImageOffset( offset );
		attrs.setScale(scale);
		placemark.setAttributes(attrs);
		return placemark;
	}
	private void addAdapter( )
	{
		MouseAdapter adapter = new MouseAdapter( )
		{
			private boolean dragged = false;
			public void mousePressed(MouseEvent event){
				dragged=false;
			}
			public void mouseDragged(MouseEvent event){
				dragged=true;
			}
			public void mouseReleased(MouseEvent event){
				if(event.getButton() == MouseEvent.BUTTON1 && !dragged && wwd.getCurrentPosition()!=null && controlLayer instanceof LineStringLayer){
					controlLayer.addRenderable(createPointPlacemark(wwd.getCurrentPosition(),Color.red,.2));
					wwToKML();
				}
			}
			public void mouseClicked(MouseEvent event){
				if(event.getButton() == MouseEvent.BUTTON3){
					PointPlacemark lastIcon =  null;
					for(Renderable icon : controlLayer.getRenderables())lastIcon =(PointPlacemark) icon;
					if(lastIcon!=null)controlLayer.removeRenderable(lastIcon);
					wwToKML();
				}
			}
		};
		wwd.addMouseListener(adapter);
		wwd.addMouseMotionListener(adapter);

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
