package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class KMLRootImpl extends KMLAbstractImpl<KMLRoot>{
	public KMLRootImpl(KMLRoot root){
		super(root);
	}
    /**
     * Export the placemark to KML as a {@code <Placemark>} element. The {@code output} object will receive the data.
     * This object must be one of: java.io.Writer java.io.OutputStream javax.xml.stream.XMLStreamWriter
     *
     * @param output Object to receive the generated KML.
     * @param output 
     *
     * @throws XMLStreamException If an exception occurs while writing the KML
     * @throws IOException        if an exception occurs while exporting the data.
     * @see #export(String, Object)
     */
    protected void exportAsKML(String mimeType, Object output, XMLStreamWriter xmlWriter) throws IOException, XMLStreamException
    {
        xmlWriter.writeStartElement(Support.KMLTag.kml.name());
        if(object.getNamespaceURI()!=null && !object.getNamespaceURI().isEmpty())xmlWriter.writeAttribute(Support.KMLTag.xmlns.name(), object.getNamespaceURI());
        export(mimeType,object.getFeature(), output);
        xmlWriter.writeEndElement();
    }

}
