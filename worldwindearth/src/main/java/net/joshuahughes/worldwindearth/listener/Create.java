package net.joshuahughes.worldwindearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.worldwindearth.WorldwindEarth;

public enum Create implements Listener{
	Placemark,Path,Polygon,Model,Image_Overlay,GroundOverlay,Tour,Photo,Network_Link;
	private String tooltip;
	Create(){
		tooltip = (this.name().equals("Tour")?"Record a ":"Add ")+name();
	}
	public String getToolTip(){
		return tooltip;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldwindEarth.findWindow((Component) e.getSource()).getViewer().create(this);
	}

}
