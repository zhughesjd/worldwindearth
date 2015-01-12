package net.joshuahughes.worldwindearth.support;

import net.joshuahughes.worldwindearth.support.Support.KMLTag;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;

public abstract class KMLAbstractFeatureImpl extends KMLAbstractObjectImpl<KMLAbstractFeature>{

	public KMLAbstractFeatureImpl(KMLAbstractFeature object) {
		super(object);
		stringElementMap.put(KMLTag.address, object.getAddress());
		stringElementMap.put(KMLTag.description, object.getDescription());
		stringElementMap.put(KMLTag.ExtendedData, object.getExtendedData().getCharacters());
		stringElementMap.put(KMLTag.name, object.getName());
		stringElementMap.put(KMLTag.phoneNumber, object.getPhoneNumber());
		stringElementMap.put(KMLTag.Snippet,object.getSnippetText());
		stringElementMap.put(KMLTag.styleUrl, object.getStyleUrl().getCharacters());
		stringElementMap.put(KMLTag.visibility, object.getVisibility().toString());
		stringElementMap.put(KMLTag.open, object.getOpen().toString());
		children.add(new KMLRegionImpl(object.getRegion()));
	}
}
