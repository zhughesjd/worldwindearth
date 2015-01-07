package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.render.PointPlacemark;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.joshuahughes.worldwindearth.support.Support;

public class PointLayer extends ControlLayer<KMLPoint>{
	public PointLayer(KMLPoint object) {
		super(object);
		addRenderable(createPointPlacemark(object.getCoordinates(),new Color(0,0,0,1),1));
		object.getRoot( ).addPropertyChangeListener( new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				((PointPlacemark)getRenderables().iterator( ).next( )).setPosition( PointLayer.this.object.getCoordinates() );
			}
		});

	}
	protected void adjustPoint(Movable movable) {
		Position position = movable.getReferencePosition();
		object.applyChange( Support.createPoint(position.getLatitude().getDegrees(),position.getLongitude().getDegrees()));
	}

}
