package net.joshuahughes.javaearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.javaearth.WorldwindEarth;

public enum Reset implements Listener{
	Tilt,Compass,Tilt_and_Compass;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldwindEarth.findWindow((Component) e.getSource()).getEarthviewer().setReset(this);
	}

}
