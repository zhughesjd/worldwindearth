package net.joshuahughes.worldwindearth.toolbar;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import net.joshuahughes.worldwindearth.listener.Explore;

public class ExploreButton extends JButton{
	private static final long serialVersionUID = -672984889605110698L;
	public ExploreButton() {
		String text = "Explore";
		setName(text);
		setToolTipText(text);
		addActionListener(Explore.Earth);
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("./images/Explore.png");
		if(inputStream!=null)
			try {
				setIcon(new ImageIcon(ImageIO.read(inputStream)));
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
}
