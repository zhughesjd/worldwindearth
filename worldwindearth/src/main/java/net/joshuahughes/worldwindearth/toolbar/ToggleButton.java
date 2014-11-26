package net.joshuahughes.worldwindearth.toolbar;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import net.joshuahughes.worldwindearth.listener.Listener;

public class ToggleButton extends JToggleButton{
	private static final long serialVersionUID = -672984889605110698L;
	public ToggleButton(Listener listener) {
		addActionListener(listener);
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("./images/"+listener.toString()+"_off.png");
		if(inputStream!=null)
			try {
				setIcon(new ImageIcon(ImageIO.read(inputStream)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		inputStream = getClass().getClassLoader().getResourceAsStream("./images/"+listener.toString()+"_on.png");
		if(inputStream!=null)
			try {
				setSelectedIcon(new ImageIcon(ImageIO.read(inputStream)));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
