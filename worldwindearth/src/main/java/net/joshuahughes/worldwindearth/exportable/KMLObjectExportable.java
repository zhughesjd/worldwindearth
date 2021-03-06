package net.joshuahughes.worldwindearth.exportable;

import gov.nasa.worldwind.ogc.kml.KMLRoot;
import net.joshuahughes.worldwindearth.support.KMLAbstractObjectImpl;

public class KMLObjectExportable extends KMLExportable<KMLRoot> {
	public enum Tag{kml,namespaceURI}
	public KMLObjectExportable(KMLRoot object) {
		super(object);
		attributeMap.put(Tag.namespaceURI,object.getNamespaceURI());
		children.add(KMLAbstractObjectImpl.export(object.getFeature()));
	}

	@Override
	protected Enum<?> getTag() {
		return Tag.kml;
	}

}
