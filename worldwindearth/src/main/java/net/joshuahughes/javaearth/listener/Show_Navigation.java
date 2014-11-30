package net.joshuahughes.javaearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.javaearth.WorldwindEarth;

public enum Show_Navigation implements Listener{
	Automatically,Always,Compass_Only,Never;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldwindEarth.findWindow((Component) e.getSource()).getEarthviewer().setShowNavigation(this);
	}

}
