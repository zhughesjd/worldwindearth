package net.joshuahughes.worldwindearth.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.joshuahughes.worldwindearth.WorldwindEarth;

public enum View_Size implements ActionListener{
	_720_486_NTSC,_720_576_PAL,_864_486_NTSC_16_9,_1024_576_PAL_16_9,_1920_1080p_HDTV,_1280_720p_HDTV,_825_480p_DVD;
	@Override
	public void actionPerformed(ActionEvent e) {
		WorldwindEarth.findWindow((Component) e.getSource()).getEarthviewer().setViewSize(this);
	}

}
