package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLAbstractGeometry;
import gov.nasa.worldwind.ogc.kml.KMLMultiGeometry;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import net.joshuahughes.worldwindearth.support.Support.KMLTag;

public class KMLMultiGeometryImpl extends KMLAbstractObjectImpl<KMLMultiGeometry>{
	public KMLMultiGeometryImpl(KMLPlacemark placemark,KMLMultiGeometry object) {
		super(object);
		for(KMLAbstractGeometry geom : object.getGeometries())
			children.add(KMLAbstractObjectImpl.export(placemark, geom));
	}
	@Override
	protected KMLTag getTag() {
		return KMLTag.MultiGeometry;
	}

}
