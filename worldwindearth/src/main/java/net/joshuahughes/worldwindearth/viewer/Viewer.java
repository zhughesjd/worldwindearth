package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Position.PositionList;
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
import gov.nasa.worldwind.ogc.kml.KMLAbstractGeometry;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLIcon;
import gov.nasa.worldwind.ogc.kml.KMLLatLonBox;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.ogc.kml.KMLLinearRing;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.gx.GXLatLongQuad;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.WWIcon;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.StatusLayer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.joshuahughes.worldwindearth.listener.Explore;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Reset;
import net.joshuahughes.worldwindearth.listener.Show_Navigation;
import net.joshuahughes.worldwindearth.listener.View_Size;
import net.joshuahughes.worldwindearth.support.Support;

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
	RenderableLayer controlLayer = new RenderableLayer();
	KMLController editController = new KMLController(emptyRoot);
	KMLAbstractFeature feature;
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
		wwd.getModel().getLayers().add(controlLayer);
		getWwd().addSelectListener(dragger);
		kmlLayer.addRenderable(editController);
	}
	protected void wwToKML() {
		if(dragger.getMovable() == null) return;
		Position position = dragger.getMovable().getReferencePosition();
		if(feature!=null){
			if(feature instanceof KMLPlacemark){
				KMLPlacemark placemark = (KMLPlacemark) feature;
				KMLAbstractGeometry geometry = placemark.getGeometry();
				if(geometry instanceof KMLPolygon)
					geometry = ((KMLPolygon)geometry).getOuterBoundary();
				Object value = null;
				if(geometry instanceof KMLPoint)
					((KMLPoint)geometry).applyChange( Support.createPoint(position.getLatitude().getDegrees(),position.getLongitude().getDegrees()));
				if(geometry instanceof KMLLineString || geometry instanceof KMLLinearRing){
					ArrayList<PointPlacemark> iconList = new ArrayList<>();
					for(Renderable icon : controlLayer.getRenderables()){
						iconList.add( (PointPlacemark) icon );
					}
					List<Position> list = new ArrayList<>();
					for(PointPlacemark icon : iconList)
						list.add( new Position(icon.getPosition( ),0) );
					value = new PositionList(list);
				}
				geometry.setField( Support.KMLTag.coordinates.name( ), value);
				geometry.applyChange( geometry );
			}
			if(feature instanceof KMLGroundOverlay){
				KMLGroundOverlay overlay = (KMLGroundOverlay) feature;
				if(overlay.getLatLonBox()!=null)adjust(overlay.getLatLonBox(),dragger.getMovable());
				if(overlay.getLatLonQuad()!=null)adjust(overlay.getLatLonQuad(),dragger.getMovable());
			}
			wwd.redraw( );
		}
		ArrayList<Position> newList = new ArrayList<Position>();
		for(Renderable icon : controlLayer.getRenderables()) newList.add(((PointPlacemark) icon).getPosition());
		feature.getRoot().firePropertyChange("",null, null);
	}
	private void adjust(GXLatLongQuad latLonQuad, Movable movable) {
		
	}
	private void adjust(KMLLatLonBox latLonBox, Movable movable) {
		if(!(movable instanceof PointPlacemark))return;
		PointPlacemark point = (PointPlacemark) movable;
		ArrayList<PointPlacemark> list = new ArrayList<>();
		for(Renderable r : controlLayer.getRenderables())list.add((PointPlacemark) r);
		PointPlacemark nw = list.get(0);
		PointPlacemark ne = list.get(1);
		PointPlacemark se = list.get(2);
		PointPlacemark sw = list.get(3);
		PointPlacemark center = list.get(4);
		if(point==center){
			double oldCenterLat = (nw.getPosition().getLatitude().getDegrees()+se.getPosition().getLatitude().getDegrees())/2;
			double oldCenterLon = (nw.getPosition().getLongitude().getDegrees()+se.getPosition().getLongitude().getDegrees())/2;
			double latDiff = center.getPosition().getLatitude().getDegrees()-oldCenterLat;
			double lonDiff = center.getPosition().getLongitude().getDegrees()-oldCenterLon;
			Position diff = Position.fromDegrees(latDiff, lonDiff);
			for(PointPlacemark pp : new PointPlacemark[]{nw,ne,se,sw})
				pp.setPosition(pp.getPosition().add(diff));
		}
		if(point==center){
			double oldCenterLat = (nw.getPosition().getLatitude().getDegrees()+se.getPosition().getLatitude().getDegrees())/2;
			double oldCenterLon = (nw.getPosition().getLongitude().getDegrees()+se.getPosition().getLongitude().getDegrees())/2;
			double latDiff = center.getPosition().getLatitude().getDegrees()-oldCenterLat;
			double lonDiff = center.getPosition().getLongitude().getDegrees()-oldCenterLon;
			Position diff = Position.fromDegrees(latDiff, lonDiff);
			for(PointPlacemark pp : new PointPlacemark[]{nw,ne,se,sw})
				pp.setPosition(pp.getPosition().add(diff));
		}
		if(point==nw){
			ne.setPosition(Position.fromDegrees(nw.getPosition().getLatitude().getDegrees(),ne.getPosition().getLongitude().getDegrees()));
			sw.setPosition(Position.fromDegrees(sw.getPosition().getLatitude().getDegrees(),nw.getPosition().getLongitude().getDegrees()));
		}
		if(point==ne){
			nw.setPosition(Position.fromDegrees(ne.getPosition().getLatitude().getDegrees(),nw.getPosition().getLongitude().getDegrees()));
			se.setPosition(Position.fromDegrees(se.getPosition().getLatitude().getDegrees(),ne.getPosition().getLongitude().getDegrees()));
		}
		if(point==se){
			sw.setPosition(Position.fromDegrees(se.getPosition().getLatitude().getDegrees(),sw.getPosition().getLongitude().getDegrees()));
			ne.setPosition(Position.fromDegrees(ne.getPosition().getLatitude().getDegrees(),se.getPosition().getLongitude().getDegrees()));
		}
		if(point==sw){
			se.setPosition(Position.fromDegrees(sw.getPosition().getLatitude().getDegrees(),se.getPosition().getLongitude().getDegrees()));
			nw.setPosition(Position.fromDegrees(nw.getPosition().getLatitude().getDegrees(),sw.getPosition().getLongitude().getDegrees()));
		}
		center.setPosition(Position.fromDegrees((nw.getPosition().getLatitude().getDegrees()+sw.getPosition().getLatitude().getDegrees())/2,(nw.getPosition().getLongitude().getDegrees()+se.getPosition().getLongitude().getDegrees())/2));
		KMLGroundOverlay overlay = (KMLGroundOverlay) feature;
		overlay.getLatLonBox().setField(Support.KMLTag.north.name(),ne.getPosition().getLatitude().getDegrees());
		overlay.getLatLonBox().setField(Support.KMLTag.east.name(),ne.getPosition().getLongitude().getDegrees());
		overlay.getLatLonBox().setField(Support.KMLTag.south.name(),sw.getPosition().getLatitude().getDegrees());
		overlay.getLatLonBox().setField(Support.KMLTag.west.name(),sw.getPosition().getLongitude().getDegrees());
		overlay.applyChange(overlay);
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
		controlLayer.removeAllRenderables();
	}
	public void edit(KMLAbstractFeature feature) {
		this.feature = feature;
		if(feature !=null){
			if(feature instanceof KMLPlacemark){
				final KMLPlacemark placemark = (KMLPlacemark) feature;
				if(placemark.getGeometry() instanceof KMLPoint || placemark.getGeometry() instanceof KMLModel){
					editController.setKmlRoot(feature.getRoot( ));
					Position position = placemark.getGeometry() instanceof KMLPoint? ((KMLPoint) placemark.getGeometry()).getCoordinates():((KMLModel) placemark.getGeometry()).getLocation().getPosition();
					feature.getRoot( ).addPropertyChangeListener( new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent event) {
							Position position = placemark.getGeometry() instanceof KMLPoint? ((KMLPoint) placemark.getGeometry()).getCoordinates():((KMLModel) placemark.getGeometry()).getLocation().getPosition();
							((PointPlacemark)controlLayer.getRenderables().iterator( ).next( )).setPosition( position );
						}
					});
					controlLayer.addRenderable(createPointPlacemark(position,new Color(0,0,0,1),1));
				}
				if(placemark.getGeometry() instanceof KMLLineString || placemark.getGeometry() instanceof KMLPolygon){
					editController.setKmlRoot(feature.getRoot( ));
					KMLLineString lineString = placemark.getGeometry( ) instanceof KMLLineString?(KMLLineString)placemark.getGeometry( ):((KMLPolygon)placemark.getGeometry( )).getOuterBoundary( );
					if(lineString.getCoordinates( )!=null && lineString.getCoordinates( ).list!=null)
						for(Position position : lineString.getCoordinates( ).list)
							controlLayer.addRenderable(createPointPlacemark(position,Color.red,.2));
					addAdapter();
				}
			}
			if(feature instanceof KMLGroundOverlay){
				editController.setKmlRoot(feature.getRoot( ));
				KMLGroundOverlay overlay = ( KMLGroundOverlay ) feature;
				KMLIcon icon = overlay.getIcon();
				icon.setField(Support.KMLTag.futurehref.name(),icon.getHref());
				BufferedImage image = Support.invalidImage();
				try {
					String href = icon.getHref();
					image = href.startsWith("http://")?ImageIO.read(new URL(icon.getHref())):ImageIO.read(new File(icon.getHref()));
				} catch (IOException e) {
				}
				drawHandles(image);
				try {
					File tempFile = File.createTempFile("file",".jpg");
					ImageIO.write(image, "jpg",tempFile);
					icon.setField(Support.KMLTag.href.name(), tempFile.getCanonicalPath());
					icon.applyChange(icon);
				} catch (IOException e) {
					e.printStackTrace();
				}

				KMLLatLonBox box = overlay.getLatLonBox( );
				add(box);
				addAdapter();
//				kmlLayer.addRenderable(editController = new KMLController(feature.getRoot( )));
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
	private void drawHandles(BufferedImage image) {
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.green);
		g2d.setStroke(new BasicStroke(10));
		double fraction = .15;
		g2d.drawLine(0,0,(int)(fraction*image.getWidth()),0);
		g2d.drawLine(0,0,0,(int)(fraction*image.getHeight()));
		g2d.drawLine(image.getWidth(),0,(int)((1-fraction)*image.getWidth()),0);
		g2d.drawLine(image.getWidth(),0,image.getWidth(),(int)(fraction*image.getHeight()));
		g2d.drawLine(0,image.getHeight(),0,(int)((1-fraction)*image.getHeight()));
		g2d.drawLine(0,image.getHeight(),(int)(fraction*image.getWidth()),image.getHeight());
		g2d.drawLine(image.getWidth(),image.getHeight(),(int)((1-fraction)*image.getWidth()),image.getHeight());
		g2d.drawLine(image.getWidth(),image.getHeight(),image.getWidth(),(int)((1-fraction)*image.getHeight()));

		int w2 = image.getWidth()/2;
		int h2 = image.getHeight()/2;
		g2d.drawLine(w2,h2,w2+(int)((fraction)*image.getWidth()),h2);
		g2d.drawLine(w2,h2,w2-(int)((fraction)*image.getWidth()),h2);
		g2d.drawLine(w2,h2,w2,h2+(int)((fraction)*image.getHeight()));
		g2d.drawLine(w2,h2,w2,h2-(int)((fraction)*image.getHeight()));
	}
	private void add( KMLLatLonBox box )
	{
		double n = box.getNorth();
		double s = box.getSouth();
		double e = box.getEast();
		double w = box.getWest();
		Position nw = Position.fromDegrees( n,w );
		Position ne = Position.fromDegrees( n,e );
		Position se = Position.fromDegrees( s,e );
		Position sw = Position.fromDegrees( s,w );
		Position center = Position.fromDegrees((n+s)/2,(e+w)/2);
		Color color = new Color(0,255,0,1);
		double[] xs = {0,1,1,0};
		double[] ys = {1,1,0,0};
		int index=0;
		for(Position position : new Position[]{nw,ne,se,sw}){
			Offset offset = new Offset(xs[index],ys[index], AVKey.FRACTION, AVKey.FRACTION );
			controlLayer.addRenderable(createPointPlacemark(position,color,1,offset));
			index++;
		}
		controlLayer.addRenderable(createPointPlacemark(center,color,2));
	}
	private void addAdapter( )
	{
		MouseAdapter adapter = new MouseAdapter( )
		{
			private boolean dragged;
			public void mousePressed(MouseEvent event){
				dragged=false;
			}
			public void mouseDragged(MouseEvent event){
				dragged=true;
			}
			public void mouseReleased(MouseEvent event){
				if(event.getButton() == MouseEvent.BUTTON3 || wwd.getCurrentPosition()==null) return;
				if(!dragged){
					if(feature instanceof KMLPlacemark)
						controlLayer.addRenderable(createPointPlacemark(wwd.getCurrentPosition(),Color.red,.2));
					wwToKML();
				}
			}
			public void mouseClicked(MouseEvent event){
				if(event.getButton() == MouseEvent.BUTTON3){
					PointPlacemark lastIcon =  null;
					for(Renderable icon : controlLayer.getRenderables())lastIcon =(PointPlacemark) icon;
					if(lastIcon!=null)
						controlLayer.removeRenderable(lastIcon);
					wwToKML();
				}
			}
		};
		getWwd( ).addMouseListener(adapter);
		getWwd( ).addMouseMotionListener(adapter);

	}
	protected void sort( ArrayList<WWIcon> iconList )
	{
		Collections.sort( iconList,new Comparator<WWIcon>(){
			public int compare(WWIcon i1,WWIcon i2){
				return ((Integer)i1.getValue( Integer.class.getSimpleName( ) )).compareTo((Integer)i2.getValue( Integer.class.getSimpleName( ) ));
			}

			@Override
			public Comparator<WWIcon> reversed( )
			{
				return null;
			}

			@Override
			public Comparator<WWIcon> thenComparing( Comparator<? super WWIcon> other )
			{
				return null;
			}

			@Override
			public <U> Comparator<WWIcon> thenComparing( Function<? super WWIcon, ? extends U> keyExtractor, Comparator<? super U> keyComparator )
			{
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<WWIcon> thenComparing( Function<? super WWIcon, ? extends U> keyExtractor )
			{
				return null;
			}

			@Override
			public Comparator<WWIcon> thenComparingInt( ToIntFunction<? super WWIcon> keyExtractor )
			{
				return null;
			}

			@Override
			public Comparator<WWIcon> thenComparingLong( ToLongFunction<? super WWIcon> keyExtractor )
			{
				return null;
			}

			@Override
			public Comparator<WWIcon> thenComparingDouble( ToDoubleFunction<? super WWIcon> keyExtractor )
			{
				return null;
			}
		});

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
