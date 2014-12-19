package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLAbstractGeometry;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;

public class KMLGeometryPlacemark extends KMLPlacemark
{
    public KMLGeometryPlacemark( String namespaceURI,KMLAbstractGeometry geometry)
    {
        super( namespaceURI );
        super.setGeometry( geometry );
    }
}
