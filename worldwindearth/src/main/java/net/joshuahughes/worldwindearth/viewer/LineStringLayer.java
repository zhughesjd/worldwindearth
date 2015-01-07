package net.joshuahughes.worldwindearth.viewer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.joshuahughes.worldwindearth.support.Support;
import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Position.PositionList;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.Renderable;

public class LineStringLayer extends ControlLayer<KMLLineString>{
	public LineStringLayer(KMLLineString object) {
		super(object);
		if(object.getCoordinates( )!=null && object.getCoordinates( ).list!=null)
			for(Position position : object.getCoordinates( ).list)
				addRenderable(createPointPlacemark(position,Color.red,.2));

	}
	public void adjustMe(Movable movable) {
		List<Position> list = new ArrayList<>();
		for(Renderable icon : getRenderables())
			list.add( ((PointPlacemark) icon).getPosition() );
		object.setField( Support.KMLTag.coordinates.name( ), new PositionList(list));
		object.applyChange( object );

	}

}
