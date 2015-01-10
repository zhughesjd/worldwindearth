package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLAbstractGeometry;
import gov.nasa.worldwind.ogc.kml.KMLMultiGeometry;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class KMLMultiGeometryImpl extends KMLAbstractImpl<KMLMultiGeometry>{

	private KMLPlacemark placemark;

	public KMLMultiGeometryImpl(KMLPlacemark placemark,KMLMultiGeometry object) {
		super(object);
		this.placemark = placemark;
	}

	@Override
	protected void exportAsKML(String mimeType,XMLStreamWriter xmlWriter) throws IOException, XMLStreamException {
        xmlWriter.writeStartElement(Support.KMLTag.MultiGeometry.name());
        for(KMLAbstractGeometry geom : object.getGeometries())
        export(mimeType,placemark,geom,xmlWriter);
        xmlWriter.writeEndElement();
		
	}

}
