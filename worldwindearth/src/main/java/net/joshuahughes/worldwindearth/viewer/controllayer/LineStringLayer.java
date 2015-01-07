package net.joshuahughes.worldwindearth.viewer.controllayer;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Position.PositionList;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.Renderable;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import net.joshuahughes.worldwindearth.support.Support;
import net.joshuahughes.worldwindearth.viewer.Viewer;

public class LineStringLayer extends ControlLayer<KMLLineString>{
	private MouseAdapter adapter = new MouseAdapter( )
	{
		private boolean dragged = false;
		public void mousePressed(MouseEvent event){
			dragged=false;
		}
		public void mouseDragged(MouseEvent event){
			dragged=true;
		}
		public void mouseReleased(MouseEvent event){
			Viewer viewer = (Viewer) event.getComponent().getParent();
			Position position = viewer.getWwd().getCurrentPosition();
			if(event.getButton() == MouseEvent.BUTTON1 && !dragged && position!=null){
				addRenderable(createPointPlacemark(position,Color.red,.2));
				viewer.wwToKML();
			}
		}
		public void mouseClicked(MouseEvent event){
			Viewer viewer = (Viewer) event.getComponent().getParent();
			if(event.getButton() == MouseEvent.BUTTON3 && !dragged ){
				PointPlacemark lastIcon =  null;
				for(Renderable icon : getRenderables())lastIcon =(PointPlacemark) icon;
				if(lastIcon!=null)removeRenderable(lastIcon);
				viewer.wwToKML();
			}
		}
	};
	public LineStringLayer(KMLLineString object) {
		super(object);
		if(object.getCoordinates( )!=null && object.getCoordinates( ).list!=null)
			for(Position position : object.getCoordinates( ).list)
				addRenderable(createPointPlacemark(position,Color.red,.2));

	}
	protected void adjustPoint(Movable movable) {
		List<Position> list = new ArrayList<>();
		for(Renderable icon : getRenderables())
			list.add( ((PointPlacemark) icon).getPosition() );
		object.setField( Support.KMLTag.coordinates.name( ), new PositionList(list));
	}
	public MouseAdapter getAdapter(){
		return adapter ;
	}
}
