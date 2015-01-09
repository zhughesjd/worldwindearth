package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLConstants;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
//		if(object.getNamespaceURI()!=null && !object.getNamespaceURI().isEmpty())
//			xmlWriter.writeAttribute(Support.KMLTag.xmlns.name(), object.getNamespaceURI());
		export(mimeType,object.getFeature(), output);
		xmlWriter.writeEndElement();
	}
	public static void main(String[] args) throws Exception{
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = factory.createXMLStreamWriter(System.out);
		writer.writeStartElement("HW");
		writer.writeStartElement("HW2");
		writer.writeEndElement();
		writer.writeEndElement();
		writer.close();
		KMLRoot root = KMLRoot.createAndParse("C:/Users/sandra/Desktop/image.kml");
		KMLRootImpl rootImpl = new KMLRootImpl(root);
		Writer stringWriter = new StringWriter();
		rootImpl.export(KMLConstants.KML_MIME_TYPE,stringWriter);
		String xmlString = stringWriter.toString();
		xmlString = xmlString.replace("<kml", "<kml>").replace(">></kml>", "></kml>");
		System.out.println(xmlString);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.transform(new StreamSource(new StringReader(xmlString)), new StreamResult(System.out));    
	}
}
