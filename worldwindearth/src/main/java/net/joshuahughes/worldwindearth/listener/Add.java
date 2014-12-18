package net.joshuahughes.worldwindearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.worldwindearth.WorldWindEarth;

public enum Add implements Listener{
	Folder,Placemark,Path,Polygon,Model,Image_Overlay,GroundOverlay,Tour,Photo,Network_Link;
	private String tooltip;
	Add(){
		tooltip = (this.name().equals("Tour")?"Record a ":"Add ")+name();
	}
	public String getToolTip(){
		return tooltip;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldWindEarth.findWindow((Component) e.getSource()).add(this);
	}

}
