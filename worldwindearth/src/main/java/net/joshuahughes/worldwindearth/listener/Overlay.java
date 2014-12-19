package net.joshuahughes.worldwindearth.listener;

import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;

import net.joshuahughes.worldwindearth.WorldWindEarth;

public enum Overlay implements Listener{
	Status_Bar,Grid,Overview_Map,Scale_Legend,Tour_Guide,Atmosphere,Sun,Historical_Imagery,Water_Surface;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldWindEarth.findWindow((AbstractButton) e.getSource()).getViewer().setVisible(this,((AbstractButton) e.getSource()).isSelected());
	}

}
