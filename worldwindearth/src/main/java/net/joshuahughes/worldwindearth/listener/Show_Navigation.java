package net.joshuahughes.worldwindearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.worldwindearth.WorldWindEarth;

public enum Show_Navigation implements Listener{
	Automatically,Always,Compass_Only,Never;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldWindEarth.findWindow((Component) e.getSource()).getViewer().setShowNavigation(this);
	}

}
