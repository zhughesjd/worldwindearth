package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Position.PositionList;
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
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.render.UserFacingIcon;
import gov.nasa.worldwind.render.WWIcon;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.StatusLayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
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
    WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
    KMLExcludedDragger dragger = new KMLExcludedDragger(wwd);
    StatusBar statusBar = new StatusBar();
    ViewControlsLayer navigation = new ViewControlsLayer(){{setPosition(AVKey.NORTHEAST);}};
    RenderableLayer kmlLayer = new RenderableLayer();
    IconLayer editLayer = new IconLayer();
    private KMLAbstractFeature feature;
    KMLController editController;
    protected Point mousePressed;
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
                wwToKML();
            }
        });
        wwd.getModel().getLayers().add(kmlLayer);
        wwd.getModel().getLayers().add(editLayer);
        getWwd().addSelectListener(dragger);
    }
    protected void wwToKML() {
        Position position = dragger.getPosition();
        if(feature!=null && feature instanceof KMLPlacemark){
            KMLPlacemark placemark = (KMLPlacemark) feature;
            KMLAbstractGeometry geometry = placemark.getGeometry();
            if(geometry instanceof KMLPolygon)
                geometry = ((KMLPolygon)geometry).getOuterBoundary();
            Object value = null;
            if(geometry instanceof KMLPoint)
                ((KMLPoint)geometry).applyChange( Support.createPoint(position.getLatitude().getDegrees(),position.getLongitude().getDegrees()));
            if(geometry instanceof KMLLineString || geometry instanceof KMLLinearRing){
                ArrayList<WWIcon> iconList = new ArrayList<>();
                for(WWIcon icon : editLayer.getIcons( ))
                    iconList.add( icon );
                sort(iconList);
                List<Position> list = new ArrayList<>();
                for(WWIcon icon : iconList)
                    list.add( new Position(icon.getPosition( ),0) );
                value = new PositionList(list);
            }
            geometry.setField( Support.KMLTag.coordinates.name( ), value);
            geometry.applyChange( geometry );
            wwd.redraw( );
        }
        ArrayList<Position> newList = new ArrayList<Position>();
        for(WWIcon icon : editLayer.getIcons()) newList.add(icon.getPosition());
        feature.getRoot().firePropertyChange("",null, null);
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
    private static int index=0;
    public void edit(KMLRoot root) {
        index=0;
        feature = root.getFeature();
        stopEditing();
        if(feature !=null){
            if(feature instanceof KMLPlacemark){
                final KMLPlacemark placemark = (KMLPlacemark) feature;
                editController = new KMLController(feature.getRoot( ));
                kmlLayer.addRenderable(editController);
                if(placemark.getGeometry() instanceof KMLPoint || placemark.getGeometry() instanceof KMLModel){
                    BufferedImage image = new BufferedImage(30,30,BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D g2d = image.createGraphics();
                    g2d.setBackground(new Color(0,0,0,1));
                    g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
                    Position position = placemark.getGeometry() instanceof KMLPoint? ((KMLPoint) placemark.getGeometry()).getCoordinates():((KMLModel) placemark.getGeometry()).getLocation().getPosition();
                    feature.getRoot( ).addPropertyChangeListener( new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent event) {
                            Position position = placemark.getGeometry() instanceof KMLPoint? ((KMLPoint) placemark.getGeometry()).getCoordinates():((KMLModel) placemark.getGeometry()).getLocation().getPosition();
                            editLayer.getIcons( ).iterator( ).next( ).setPosition( position );
                        }
                    });
                    editLayer.addIcon(new UserFacingIcon(image,position));
                }
                if(placemark.getGeometry() instanceof KMLLineString || placemark.getGeometry() instanceof KMLPolygon){
                    KMLLineString lineString = placemark.getGeometry( ) instanceof KMLLineString?(KMLLineString)placemark.getGeometry( ):((KMLPolygon)placemark.getGeometry( )).getOuterBoundary( );
                    if(lineString.getCoordinates( )!=null && lineString.getCoordinates( ).list!=null)
                        for(Position position : lineString.getCoordinates( ).list)
                            add(position);
                    addAdapter();
                }
            }
            if(feature instanceof KMLGroundOverlay){
                KMLGroundOverlay overlay = ( KMLGroundOverlay ) feature;
                KMLIcon icon = overlay.getIcon();
                icon.setField(Support.KMLTag.futurehref.name(),icon.getHref());
                BufferedImage image = Support.invalidImage();
                try {
					 image = ImageIO.read(new File(icon.getHref()));
				} catch (IOException e) {
				}
                try {
					File tempFile = File.createTempFile("file","png");
					ImageIO.write(image, "png",tempFile);
					icon.setField(Support.KMLTag.href.name(), tempFile.getCanonicalPath());
					icon.applyChange(icon);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
                
                KMLLatLonBox box = overlay.getLatLonBox( );
                add(box);
                addAdapter();
            }
            wwd.redraw();
        }
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
        for(Position posit : new Position[]{nw,ne,se,sw}){
            BufferedImage image = new BufferedImage(30,30,BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = image.createGraphics();
            g2d.setBackground(Color.red);
            g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
        	UserFacingIcon icon = new UserFacingIcon(image, posit);
        	editLayer.addIcon(icon);
        }
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
                if(event.getButton() == MouseEvent.BUTTON3) return;
                if(!dragged){
                    if(feature instanceof KMLPlacemark)
                        add(wwd.getCurrentPosition( ));
                    wwToKML();
                }
            }
            public void mouseClicked(MouseEvent event){
                if(event.getButton() == MouseEvent.BUTTON3){
                    WWIcon lastIcon =  null;
                    for(WWIcon icon : editLayer.getIcons())
                        lastIcon = lastIcon==null?icon:(int)icon.getValue(Integer.class.getSimpleName()) > (int)lastIcon.getValue(Integer.class.getSimpleName())?icon:lastIcon;
                        if(lastIcon!=null)
                            editLayer.removeIcon(lastIcon);
                        wwToKML();
                }
            }
        };
        getWwd( ).addMouseListener(adapter);
        getWwd( ).addMouseMotionListener(adapter);
        
    }
    private void add( Position position )
    {
        BufferedImage image = new BufferedImage(5,5,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = image.createGraphics();
        g2d.setBackground(Color.red);
        g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
        UserFacingIcon newIcon = new UserFacingIcon(image,position);
        newIcon.setValue( Integer.class.getSimpleName( ), index++ );
        editLayer.addIcon(newIcon);
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
