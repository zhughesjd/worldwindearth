package net.joshuahughes.worldwindearth.toolbar;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import net.joshuahughes.worldwindearth.listener.Listener;

public class Button extends JButton{
	private static final long serialVersionUID = -672984889605110698L;
	public Button(Listener listener) {
		addActionListener(listener);
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("./images/"+listener.toString()+".png");
		if(inputStream!=null)
			try {
				setIcon(new ImageIcon(ImageIO.read(inputStream)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			setText(listener.toString().replace('_',' ').substring(0, 7));
	}
}
