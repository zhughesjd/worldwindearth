package net.joshuahughes.worldwindearth.menubar;

import java.awt.event.ActionListener;

import javax.swing.JRadioButtonMenuItem;

public class RadioButtonMenuItem extends JRadioButtonMenuItem{
	private static final long serialVersionUID = 2127006366371855724L;
	public RadioButtonMenuItem(ActionListener listener) {
		String display = listener.toString().replace("_"," ");
		if(display.startsWith(" "))
		{
			String[] array = display.split(" ");
			String append = array.length>5?" "+array[4]+":"+array[5]:"";
			display = array[1]+" x "+array[2]+" ("+ array[3]+ append+ ")";
		}
		setText(display);
		addActionListener(listener);
	}
}
