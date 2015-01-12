package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLConstants;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.joshuahughes.worldwindearth.support.Support.KMLTag;

public class KMLRootImpl extends KMLAbstractObjectImpl<KMLRoot>{
	public KMLRootImpl(KMLRoot root){
		super(root);
		attributeMap.put(KMLTag.namespaceURI,root.getNamespaceURI());
		children.add(KMLAbstractObjectImpl.export(root.getFeature()));
	}
	protected KMLTag getTag(){
		return KMLTag.kml;
	}
	public static void main(String[] args) throws Exception{
		KMLRoot root = KMLRoot.createAndParse("C:/Users/sandra/Desktop/image.kml");
		KMLRootImpl rootImpl = new KMLRootImpl(root);
		Writer stringWriter = new StringWriter();
		rootImpl.export(KMLConstants.KML_MIME_TYPE,stringWriter);
		String xmlString = stringWriter.toString();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.transform(new StreamSource(new StringReader(xmlString)), new StreamResult(System.out));    
	}
}
