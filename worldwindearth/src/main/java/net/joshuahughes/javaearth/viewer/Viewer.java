package net.joshuahughes.javaearth.viewer;

import java.awt.Component;

import net.joshuahughes.javaearth.listener.Create;
import net.joshuahughes.javaearth.listener.Explore;
import net.joshuahughes.javaearth.listener.Overlay;
import net.joshuahughes.javaearth.listener.Reset;
import net.joshuahughes.javaearth.listener.Show_Navigation;
import net.joshuahughes.javaearth.listener.View_Size;
import de.micromata.opengis.kml.v_2_2_0.Feature;

public interface Viewer {
	public void setVisible(Overlay overlay,boolean show);
	public void setViewSize(View_Size viewSize);
	public void setShowNavigation(Show_Navigation showNavigation);
	public void setReset(Reset reset);
	public void setExplore(Explore explore);
	public void add(Feature feature);
	public boolean remove(Feature feature);
	public void add(String wmsPath);
	public boolean remove(String wmsPath);
	public Component getViewer();
	public Feature create(Create creation);
}
