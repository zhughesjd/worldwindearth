package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Position.PositionList;
import gov.nasa.worldwind.ogc.kml.KMLPoint;

import java.util.Arrays;

public class KMLEditableModel extends KMLPoint
{
    public KMLEditableModel( String namespaceURI,Position point)
    {
        super( namespaceURI );
		setCoordinates(new PositionList(Arrays.asList(point)));
    }
}
