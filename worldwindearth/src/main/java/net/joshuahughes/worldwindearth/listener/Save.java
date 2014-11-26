package net.joshuahughes.worldwindearth.listener;

import java.awt.event.ActionEvent;

public enum Save implements Listener{
	Save_to_My_Places,Save_Place_As___,Save_My_Places,Save_Image___;
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(Save.this.name());
	}

}
