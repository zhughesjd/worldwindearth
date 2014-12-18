package net.joshuahughes.worldwindearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.worldwindearth.WorldWindEarth;

public enum Reset implements Listener{
	Tilt,Compass,Tilt_and_Compass;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldWindEarth.findWindow((Component) e.getSource()).getViewer().setReset(this);
	}

}
