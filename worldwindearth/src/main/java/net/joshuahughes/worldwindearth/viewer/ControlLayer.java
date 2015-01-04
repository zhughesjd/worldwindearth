package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Position.PositionList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLLatLonBox;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.ogc.kml.KMLLinearRing;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.gx.GXLatLongQuad;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Renderable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.joshuahughes.worldwindearth.support.Support;

public class ControlLayer extends RenderableLayer{
	private static String imagePath;
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
	}
	private KMLAbstractObject object;

	public void setObject(final KMLAbstractObject object){
		this.object = object;
		if(object instanceof KMLPoint || object instanceof KMLModel){
			Position position = object instanceof KMLPoint? ((KMLPoint) object).getCoordinates():((KMLModel) object).getLocation().getPosition();
			object.getRoot( ).addPropertyChangeListener( new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					Position position = object instanceof KMLPoint? ((KMLPoint) object).getCoordinates():((KMLModel) object).getLocation().getPosition();
					((PointPlacemark)getRenderables().iterator( ).next( )).setPosition( position );
				}
			});
			addRenderable(createPointPlacemark(position,new Color(0,0,0,1),1));
		}
		if(object instanceof KMLLineString || object instanceof KMLPolygon){
			KMLLineString lineString = object instanceof KMLLineString?(KMLLineString)object:((KMLPolygon)object).getOuterBoundary( );
			if(lineString.getCoordinates( )!=null && lineString.getCoordinates( ).list!=null)
				for(Position position : lineString.getCoordinates( ).list)
					addRenderable(createPointPlacemark(position,Color.red,.2));
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
	protected void wwToKML(Movable movable) {
		Position position = movable.getReferencePosition();
		if(object instanceof KMLPolygon)
			object = ((KMLPolygon)object).getOuterBoundary();
		Object value = null;
		if(object instanceof KMLPoint)
			((KMLPoint)object).applyChange( Support.createPoint(position.getLatitude().getDegrees(),position.getLongitude().getDegrees()));
		if(object instanceof KMLLineString || object instanceof KMLLinearRing){
			ArrayList<PointPlacemark> iconList = new ArrayList<>();
			for(Renderable icon : getRenderables()){
				iconList.add( (PointPlacemark) icon );
			}
			List<Position> list = new ArrayList<>();
			for(PointPlacemark icon : iconList)
				list.add( new Position(icon.getPosition( ),0) );
			value = new PositionList(list);
		}
		object.setField( Support.KMLTag.coordinates.name( ), value);
		object.applyChange( object );
		if(object instanceof KMLLatLonBox){
			KMLGroundOverlay overlay = (KMLGroundOverlay) object;
			if(overlay.getLatLonBox()!=null)adjust(overlay.getLatLonBox(),movable);
			if(overlay.getLatLonQuad()!=null)adjust(overlay.getLatLonQuad(),movable);
		}
		ArrayList<Position> newList = new ArrayList<Position>();
		for(Renderable icon : getRenderables()) newList.add(((PointPlacemark) icon).getPosition());
		object.getRoot().firePropertyChange("",null, null);
	}
	private void adjust(GXLatLongQuad latLonQuad, Movable movable) {

	}
	private void adjust(KMLLatLonBox latLonBox, Movable movable) {
		if(!(movable instanceof PointPlacemark))return;
		PointPlacemark point = (PointPlacemark) movable;
		ArrayList<PointPlacemark> list = new ArrayList<>();
		for(Renderable r : getRenderables())list.add((PointPlacemark) r);
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
		KMLLatLonBox box = (KMLLatLonBox) object;
		box.setField(Support.KMLTag.north.name(),ne.getPosition().getLatitude().getDegrees());
		box.setField(Support.KMLTag.east.name(),ne.getPosition().getLongitude().getDegrees());
		box.setField(Support.KMLTag.south.name(),sw.getPosition().getLatitude().getDegrees());
		box.setField(Support.KMLTag.west.name(),sw.getPosition().getLongitude().getDegrees());
		box.applyChange(box);
	}

}
