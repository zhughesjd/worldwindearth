package net.joshuahughes.worldwindearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.worldwindearth.WorldwindEarth;

public enum Show_Navigation implements Listener{
	Automatically,Always,Compass_Only,Never;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldwindEarth.findWindow((Component) e.getSource()).getEarthviewer().setShowNavigation(this);
	}

}
