package net.joshuahughes.worldwindearth.menubar;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class MenuItem extends JMenuItem{
	private static final long serialVersionUID = 2127006366371855724L;
	public MenuItem(ActionListener e) {
		setText(e.toString().replace("___","...").replace("__", "-").replace('_', ' '));
		addActionListener(e);
	}
}
