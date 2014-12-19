package net.joshuahughes.worldwindearth.dialog;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import net.joshuahughes.worldwindearth.WorldWindEarth;

public class RulerDialog extends JDialog{
	private static final long serialVersionUID = 2843223331438820933L;
	public RulerDialog(final WorldWindEarth earth){
		super(earth,false);
		setTitle("Ruler");
		setMinimumSize(new Dimension(450,280));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				earth.ruler(false);
			}
		});
	}
}
