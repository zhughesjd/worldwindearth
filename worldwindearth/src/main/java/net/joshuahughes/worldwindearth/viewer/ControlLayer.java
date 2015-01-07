package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Renderable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class ControlLayer<E extends KMLAbstractObject> extends RenderableLayer{
	protected static String imagePath;
	static{
		BufferedImage image = new BufferedImage(30,30,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setBackground(Color.white);
		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		try {
			File file = File.createTempFile("temp", ".jpg");
			ImageIO.write(image, "png",file);
			imagePath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected E object;
	public ControlLayer(E object){
		this.object = object;
	}
	protected Renderable createPointPlacemark(Position position,Color color,double scale) {
		return createPointPlacemark(position, color, scale,new Offset( 0.5, 0.5, AVKey.FRACTION, AVKey.FRACTION ));
	}
	protected Renderable createPointPlacemark(Position position,Color color,double scale, Offset offset) {
		PointPlacemark placemark = new PointPlacemark(position);
		PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
		attrs.setImageAddress(imagePath);
		attrs.setImageColor(color);
		attrs.setImageOffset( offset );
		attrs.setScale(scale);
		placemark.setAttributes(attrs);
		return placemark;
	}
	public void adjust(Movable movable){
		adjustMe(movable);
		object.getRoot().firePropertyChange("",null, null);
	}
	protected abstract void adjustMe(Movable movable);

}
