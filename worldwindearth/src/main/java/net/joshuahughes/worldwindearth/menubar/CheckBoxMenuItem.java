package net.joshuahughes.worldwindearth.menubar;

import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

public class CheckBoxMenuItem extends JCheckBoxMenuItem{
	private static final long serialVersionUID = 2127006366371855724L;
	public CheckBoxMenuItem(ActionListener listener){
		setText(listener.toString().replace('_', ' '));
		addActionListener(listener);
	}
}
