package net.joshuahughes.worldwindearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.joshuahughes.worldwindearth.WorldwindEarth;

public enum Explore implements Listener{
	Earth,Sky,Mars,Moon;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldwindEarth.findWindow((Component) e.getSource()).getViewer().setExplore(this);
	}

}
