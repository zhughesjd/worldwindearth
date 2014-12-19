package net.joshuahughes.worldwindearth.listener;

import java.awt.event.ActionEvent;

public enum Email implements Listener{
	Email_Placemark___,Email_View___,Email_Image___;
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(Email.this.name());
	}

}
