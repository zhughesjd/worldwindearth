package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;

import java.awt.Component;

import net.joshuahughes.worldwindearth.listener.Create;
import net.joshuahughes.worldwindearth.listener.Explore;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Reset;
import net.joshuahughes.worldwindearth.listener.Show_Navigation;
import net.joshuahughes.worldwindearth.listener.View_Size;

public interface Viewer {
	public void setVisible(Overlay overlay,boolean show);
	public void setViewSize(View_Size viewSize);
	public void setShowNavigation(Show_Navigation showNavigation);
	public void setReset(Reset reset);
	public void setExplore(Explore explore);
	public void add(KMLAbstractObject feature);
	public boolean remove(KMLAbstractObject feature);
	public void add(String wmsPath);
	public boolean remove(String wmsPath);
	public Component getViewer();
	public KMLAbstractObject create(Create creation);
}
