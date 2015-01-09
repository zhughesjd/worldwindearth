package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.Exportable;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLAbstractGeometry;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLConstants;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.ogc.kml.KMLLinearRing;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLMultiGeometry;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.impl.KMLExtrudedPolygonImpl;
import gov.nasa.worldwind.ogc.kml.impl.KMLLineStringPlacemarkImpl;
import gov.nasa.worldwind.ogc.kml.impl.KMLPointPlacemarkImpl;
import gov.nasa.worldwind.ogc.kml.impl.KMLPolygonImpl;
import gov.nasa.worldwind.ogc.kml.impl.KMLSurfacePolygonImpl;
import gov.nasa.worldwind.ogc.kml.impl.KMLTraversalContext;
import gov.nasa.worldwind.util.Logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class KMLAbstractImpl<O extends KMLAbstractObject> implements Exportable{
	protected O object;
	public KMLAbstractImpl(O object) {
		this.object = object;
	}
	@Override
    /** {@inheritDoc} */
    public String isExportFormatSupported(String format)
    {
        if (KMLConstants.KML_MIME_TYPE.equalsIgnoreCase(format))
            return Exportable.FORMAT_SUPPORTED;
        else
            return Exportable.FORMAT_NOT_SUPPORTED;
    }
    /**
     * Export the Placemark. The {@code output} object will receive the exported data. The type of this object depends
     * on the export format. The formats and object types supported by this class are:
     * <p/>
     * <pre>
     * Format                                         Supported output object types
     * ================================================================================
     * KML (application/vnd.google-earth.kml+xml)     java.io.Writer
     *                                                java.io.OutputStream
     *                                                javax.xml.stream.XMLStreamWriter
     * </pre>
     *
     * @param mimeType MIME type of desired export format.
     * @param output   An object that will receive the exported data. The type of this object depends on the export
     *                 format (see above).
     *
     * @throws IOException If an exception occurs writing to the output object.
     */
    public void export(String mimeType, Object output) throws IOException
    {
        if (mimeType == null)
        {
            String message = Logging.getMessage("nullValue.Format");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (output == null)
        {
            String message = Logging.getMessage("nullValue.OutputBufferIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (KMLConstants.KML_MIME_TYPE.equalsIgnoreCase(mimeType))
        {
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

                exportAsKML(mimeType,output,xmlWriter);

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
        else
        {
            String message = Logging.getMessage("Export.UnsupportedFormat", mimeType);
            Logging.logger().warning(message);
            throw new UnsupportedOperationException(message);
        }
    }
    protected abstract void exportAsKML(String mimeType, Object output, XMLStreamWriter xmlWriter) throws IOException, XMLStreamException;
    public void export(String mimeType,KMLAbstractFeature feature, Object output)
    {
    	if(feature == null)return;
    	if(feature instanceof KMLPlacemark){
    		KMLPlacemark placemark = (KMLPlacemark) feature;
    		export(mimeType,placemark,placemark.getGeometry(),object);
    	}
    }
    protected void export(String mimeType,KMLPlacemark placemark,KMLAbstractGeometry geom,Object output)
    {
        if (geom == null)
            return;
        KMLTraversalContext tc = new KMLTraversalContext();
        Exportable exportable = null;
        if (geom instanceof KMLPoint)
        	exportable = getPointRenderable(tc,placemark,(KMLPoint)geom,mimeType,output);
        else if (geom instanceof KMLLinearRing) // since LinearRing is a subclass of LineString, this test must precede
            exportable = getLinearRingRenderable(tc,placemark,(KMLLinearRing)geom,mimeType,output);
        else if (geom instanceof KMLLineString)
            exportable = getLineStringRenderable(tc,placemark,(KMLLineString)geom,mimeType,output);
        else if (geom instanceof KMLPolygon)
            exportable = getPolygonRenderable(tc,placemark,(KMLPolygon)geom,mimeType,output);
        else if (geom instanceof KMLMultiGeometry)
            exportable = getMultiGeometryRenderable(tc,placemark,(KMLMultiGeometry)geom,mimeType,output);
        else if (geom instanceof KMLModel)
        	exportable = getModelRenderable(tc,placemark,(KMLModel)geom,mimeType,output);
        if(exportable != null)
        try {
			exportable.export(mimeType, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    protected Exportable getMultiGeometryRenderable(KMLTraversalContext tc, KMLPlacemark placemark,KMLMultiGeometry geom, String mimeType, Object output)
    {
        return new KMLMultiGeometryImpl(placemark,geom);
    }
    protected Exportable getModelRenderable(KMLTraversalContext tc, KMLPlacemark placemark,KMLModel geom, String mimeType, Object output)
    {
//    	return new KMLModelPlacemarkImpl(tc,placemark, geom);
    	return null;
    }

    protected Exportable getPointRenderable(KMLTraversalContext tc, KMLPlacemark placemark,KMLPoint geom, String mimeType, Object output)
    {
        if (geom.getCoordinates() == null)
            return null;
        return new KMLPointPlacemarkImpl(tc,placemark,geom);
    }

    protected Exportable getLineStringRenderable(KMLTraversalContext tc, KMLPlacemark placemark,KMLLineString geom, String mimeType, Object output)
    {
        if (geom.getCoordinates() == null)
            return null;
        return new KMLLineStringPlacemarkImpl(tc,placemark,geom);
    }

    protected Exportable getLinearRingRenderable(KMLTraversalContext tc, KMLPlacemark placemark,KMLLinearRing geom, String mimeType, Object output)
    {
        KMLLinearRing shape = (KMLLinearRing) geom;

        if (shape.getCoordinates() == null)
            return null;

        KMLLineStringPlacemarkImpl impl = new KMLLineStringPlacemarkImpl(tc, placemark, geom);
        if (impl.getAltitudeMode() == WorldWind.CLAMP_TO_GROUND) // See note in google's version of KML spec
            impl.setPathType(AVKey.GREAT_CIRCLE);

        return impl;
    }

    protected Exportable getPolygonRenderable(KMLTraversalContext tc, KMLPlacemark placemark,KMLPolygon geom, String mimeType, Object output)
    {
        KMLPolygon shape = (KMLPolygon) geom;

        if (shape.getOuterBoundary().getCoordinates() == null)
            return null;

        if ("clampToGround".equals(shape.getAltitudeMode()) || !this.isValidAltitudeMode(shape.getAltitudeMode()))
            return new KMLSurfacePolygonImpl(tc, placemark, geom);
        else if (shape.isExtrude())
            return new KMLExtrudedPolygonImpl(tc, placemark, geom);
        else
            return new KMLPolygonImpl(tc, placemark, geom);
    }
    /**
     * Indicates whether or not an altitude mode equals one of the altitude modes defined in the KML specification.
     *
     * @param altMode Altitude mode test.
     *
     * @return True if {@code altMode} is one of "clampToGround", "relativeToGround", or "absolute".
     */
    protected boolean isValidAltitudeMode(String altMode)
    {
        return "clampToGround".equals(altMode)
            || "relativeToGround".equals(altMode)
            || "absolute".equals(altMode);
    }

}
