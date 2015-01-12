package net.joshuahughes.worldwindearth.exportable;

import gov.nasa.worldwind.Exportable;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLConstants;
import gov.nasa.worldwind.util.Logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class KMLExportable<O extends KMLAbstractObject> implements Exportable{
	public enum AttributeTag{id,targetId}
	protected O object;
	protected LinkedHashMap<Enum<?>,String> attributeMap = new LinkedHashMap<>();
	protected LinkedHashMap<Enum<?>,String> stringElementMap = new LinkedHashMap<>();
	protected ArrayList<Exportable> children = new ArrayList<>();
	public KMLExportable(O object) {
		this.object = object;
		attributeMap.put(AttributeTag.id, object.getId());
		attributeMap.put(AttributeTag.targetId, object.getTargetId());
	}

	@Override
	public String isExportFormatSupported(String mimeType) {
		if (KMLConstants.KML_MIME_TYPE.equalsIgnoreCase(mimeType))
			return Exportable.FORMAT_SUPPORTED;
		else
			return Exportable.FORMAT_NOT_SUPPORTED;
	}

	@Override
	public void export(String mimeType, Object output) throws IOException,UnsupportedOperationException {
		try
		{
			XMLStreamWriter xmlWriter = null;
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			boolean closeWriterWhenFinished = true;

			if (output instanceof XMLStreamWriter)
			{
				xmlWriter = (XMLStreamWriter) output;
				closeWriterWhenFinished = false;
			}
			else if (output instanceof Writer)
			{
				xmlWriter = factory.createXMLStreamWriter((Writer) output);
			}
			else if (output instanceof OutputStream)
			{
				xmlWriter = factory.createXMLStreamWriter((OutputStream) output);
			}
			if (xmlWriter == null)
			{
				String message = Logging.getMessage("Export.UnsupportedOutputObject");
				Logging.logger().warning(message);
				throw new IllegalArgumentException(message);
			}
			
			xmlWriter.writeStartElement(getTag().name());
			for(Entry<Enum<?>, String> entry : attributeMap.entrySet())
				if(entry.getValue()!=null && !entry.getValue().isEmpty())
					xmlWriter.writeAttribute(entry.getKey().name(), entry.getValue());
			for(Entry<Enum<?>, String> entry : stringElementMap.entrySet())
				if(entry.getValue()!=null && !entry.getValue().isEmpty()){
					xmlWriter.writeStartElement(entry.getKey().name());
						xmlWriter.writeCharacters(entry.getValue());
					xmlWriter.writeEndElement();
				}
			for(Exportable child : children)
				child.export(mimeType,xmlWriter);
			xmlWriter.writeEndElement();
			
			xmlWriter.flush();

			if (closeWriterWhenFinished)
				xmlWriter.close();


		}
		catch (XMLStreamException e)
		{
			Logging.logger().throwing(getClass().getName(), "export", e);
			throw new IOException(e);
		}
	}

	protected abstract Enum<?> getTag();

}
