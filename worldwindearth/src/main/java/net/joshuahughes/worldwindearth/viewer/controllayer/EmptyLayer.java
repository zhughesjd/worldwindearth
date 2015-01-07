package net.joshuahughes.worldwindearth.viewer.controllayer;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;

public class EmptyLayer extends ControlLayer<KMLAbstractFeature>{
	public EmptyLayer(KMLAbstractFeature object) {
		super(object);
	}
	public void adjust(Movable movable) {
		
	}
	protected void adjustPoint(Movable movable) {
	}
}
