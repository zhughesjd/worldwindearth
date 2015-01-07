package net.joshuahughes.worldwindearth.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.joshuahughes.worldwindearth.support.Support;
import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLIcon;
import gov.nasa.worldwind.ogc.kml.KMLLatLonBox;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.Renderable;

public class LatLonBoxLayer extends ControlLayer<KMLLatLonBox>{
	public LatLonBoxLayer(KMLLatLonBox object) {
		super(object);
		KMLGroundOverlay overlay = (KMLGroundOverlay) object.getParent();
		KMLIcon icon = overlay.getIcon();
		icon.setField(Support.KMLTag.futurehref.name(),icon.getHref());
		BufferedImage image = Support.invalidImage();
		try {
			String href = icon.getHref();
			image = href.startsWith("http://")?ImageIO.read(new URL(icon.getHref())):ImageIO.read(new File(icon.getHref()));
		} catch (IOException e) {
		}
		drawHandles(image);
		try {
			File tempFile = File.createTempFile("file",".jpg");
			ImageIO.write(image, "jpg",tempFile);
			icon.setField(Support.KMLTag.href.name(), tempFile.getCanonicalPath());
			icon.applyChange(icon);
		} catch (IOException e) {
			e.printStackTrace();
		}
		add();
	}
	private void drawHandles(BufferedImage image) {
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.green);
		g2d.setStroke(new BasicStroke(10));
		double fraction = .15;
		g2d.drawLine(0,0,(int)(fraction*image.getWidth()),0);
		g2d.drawLine(0,0,0,(int)(fraction*image.getHeight()));
		g2d.drawLine(image.getWidth(),0,(int)((1-fraction)*image.getWidth()),0);
		g2d.drawLine(image.getWidth(),0,image.getWidth(),(int)(fraction*image.getHeight()));
		g2d.drawLine(0,image.getHeight(),0,(int)((1-fraction)*image.getHeight()));
		g2d.drawLine(0,image.getHeight(),(int)(fraction*image.getWidth()),image.getHeight());
		g2d.drawLine(image.getWidth(),image.getHeight(),(int)((1-fraction)*image.getWidth()),image.getHeight());
		g2d.drawLine(image.getWidth(),image.getHeight(),image.getWidth(),(int)((1-fraction)*image.getHeight()));

		int w2 = image.getWidth()/2;
		int h2 = image.getHeight()/2;
		g2d.drawLine(w2,h2,w2+(int)((fraction)*image.getWidth()),h2);
		g2d.drawLine(w2,h2,w2-(int)((fraction)*image.getWidth()),h2);
		g2d.drawLine(w2,h2,w2,h2+(int)((fraction)*image.getHeight()));
		g2d.drawLine(w2,h2,w2,h2-(int)((fraction)*image.getHeight()));
	}
	private void add( )
	{
		double n = object.getNorth();
		double s = object.getSouth();
		double e = object.getEast();
		double w = object.getWest();
		Position nw = Position.fromDegrees( n,w );
		Position ne = Position.fromDegrees( n,e );
		Position se = Position.fromDegrees( s,e );
		Position sw = Position.fromDegrees( s,w );
		Position center = Position.fromDegrees((n+s)/2,(e+w)/2);
		Color color = new Color(0,255,0,1);
		double[] xs = {0,1,1,0};
		double[] ys = {1,1,0,0};
		int index=0;
		for(Position position : new Position[]{nw,ne,se,sw}){
			Offset offset = new Offset(xs[index],ys[index], AVKey.FRACTION, AVKey.FRACTION );
			addRenderable(createPointPlacemark(position,color,1,offset));
			index++;
		}
		addRenderable(createPointPlacemark(center,color,2));
	}
	protected void adjustPoint(Movable movable) {
		if(!(movable instanceof PointPlacemark))return;
		PointPlacemark point = (PointPlacemark) movable;
		ArrayList<PointPlacemark> list = new ArrayList<>();
		for(Renderable r : getRenderables())list.add((PointPlacemark) r);
		PointPlacemark nw = list.get(0);
		PointPlacemark ne = list.get(1);
		PointPlacemark se = list.get(2);
		PointPlacemark sw = list.get(3);
		PointPlacemark center = list.get(4);
		if(point==center){
			double oldCenterLat = (nw.getPosition().getLatitude().getDegrees()+se.getPosition().getLatitude().getDegrees())/2;
			double oldCenterLon = (nw.getPosition().getLongitude().getDegrees()+se.getPosition().getLongitude().getDegrees())/2;
			double latDiff = center.getPosition().getLatitude().getDegrees()-oldCenterLat;
			double lonDiff = center.getPosition().getLongitude().getDegrees()-oldCenterLon;
			Position diff = Position.fromDegrees(latDiff, lonDiff);
			for(PointPlacemark pp : new PointPlacemark[]{nw,ne,se,sw})
				pp.setPosition(pp.getPosition().add(diff));
		}
		if(point==center){
			double oldCenterLat = (nw.getPosition().getLatitude().getDegrees()+se.getPosition().getLatitude().getDegrees())/2;
			double oldCenterLon = (nw.getPosition().getLongitude().getDegrees()+se.getPosition().getLongitude().getDegrees())/2;
			double latDiff = center.getPosition().getLatitude().getDegrees()-oldCenterLat;
			double lonDiff = center.getPosition().getLongitude().getDegrees()-oldCenterLon;
			Position diff = Position.fromDegrees(latDiff, lonDiff);
			for(PointPlacemark pp : new PointPlacemark[]{nw,ne,se,sw})
				pp.setPosition(pp.getPosition().add(diff));
		}
		if(point==nw){
			ne.setPosition(Position.fromDegrees(nw.getPosition().getLatitude().getDegrees(),ne.getPosition().getLongitude().getDegrees()));
			sw.setPosition(Position.fromDegrees(sw.getPosition().getLatitude().getDegrees(),nw.getPosition().getLongitude().getDegrees()));
		}
		if(point==ne){
			nw.setPosition(Position.fromDegrees(ne.getPosition().getLatitude().getDegrees(),nw.getPosition().getLongitude().getDegrees()));
			se.setPosition(Position.fromDegrees(se.getPosition().getLatitude().getDegrees(),ne.getPosition().getLongitude().getDegrees()));
		}
		if(point==se){
			sw.setPosition(Position.fromDegrees(se.getPosition().getLatitude().getDegrees(),sw.getPosition().getLongitude().getDegrees()));
			ne.setPosition(Position.fromDegrees(ne.getPosition().getLatitude().getDegrees(),se.getPosition().getLongitude().getDegrees()));
		}
		if(point==sw){
			se.setPosition(Position.fromDegrees(sw.getPosition().getLatitude().getDegrees(),se.getPosition().getLongitude().getDegrees()));
			nw.setPosition(Position.fromDegrees(nw.getPosition().getLatitude().getDegrees(),sw.getPosition().getLongitude().getDegrees()));
		}
		center.setPosition(Position.fromDegrees((nw.getPosition().getLatitude().getDegrees()+sw.getPosition().getLatitude().getDegrees())/2,(nw.getPosition().getLongitude().getDegrees()+se.getPosition().getLongitude().getDegrees())/2));
		object.setField(Support.KMLTag.north.name(),ne.getPosition().getLatitude().getDegrees());
		object.setField(Support.KMLTag.east.name(),ne.getPosition().getLongitude().getDegrees());
		object.setField(Support.KMLTag.south.name(),sw.getPosition().getLatitude().getDegrees());
		object.setField(Support.KMLTag.west.name(),sw.getPosition().getLongitude().getDegrees());
		object.applyChange(object);
	}
}
