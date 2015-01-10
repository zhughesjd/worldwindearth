package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;

public abstract class KMLAbstractFeatureImpl extends KMLAbstractObjectImpl<KMLAbstractFeature>{

	public KMLAbstractFeatureImpl(KMLAbstractFeature object) {
		super(object);
		
//		String[] values = {object.getAddress(),object.getDescription(),object.getName(),object.getPhoneNumber(),object.getSnippetText()};
	}
}
