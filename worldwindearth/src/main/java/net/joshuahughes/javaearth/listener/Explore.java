package net.joshuahughes.javaearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.javaearth.WorldwindEarth;

public enum Explore implements Listener{
	Earth,Sky,Mars,Moon;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldwindEarth.findWindow((Component) e.getSource()).getEarthviewer().setExplore(this);
	}

}
