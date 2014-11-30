package net.joshuahughes.javaearth.listener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.JFrame;

import net.joshuahughes.javaearth.WorldwindEarth;

public enum Single implements Listener{
	Open___{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Revert{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Post_to_Google_Earth_Community_Forum{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	View_in_Google_Maps{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Print___{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Server_Sign_Out{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Sign_in_to_Maps_Engine___{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Exit{
		@Override
		public void actionPerformed(ActionEvent e) {
			WorldwindEarth.findWindow((AbstractButton) e.getSource()).exit();
		}
	},
	Cut{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Copy{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Copy_As_Tracks{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Copy_Image{
		@Override
		public void actionPerformed(ActionEvent e) {
			WorldwindEarth frame = WorldwindEarth.findWindow((Component) e.getSource());
			Component panel = frame.getEarthviewer().getViewer();
			BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g2d = image.createGraphics();
			panel.paint(g2d);
			g2d.dispose();
			ImageTransferable transferable = new ImageTransferable( image );
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
		}
	},
	Copy_View_Location{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Paste{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Delete{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Find{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Refresh{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Rename{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Snapshot_View{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Sort_A__Z{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Delete_Contents{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Apply_Style_Template___{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Show_Elevation_Profile{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Properties{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},

	Toolbar{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof AbstractButton){
				WorldwindEarth.findWindow((AbstractButton) e.getSource()).toolbar(((AbstractButton)e.getSource()).isSelected());
			}
		}
	},
	Sidebar{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof AbstractButton){
				WorldwindEarth.findWindow((AbstractButton) e.getSource()).sidebar(((AbstractButton)e.getSource()).isSelected());
			}
		}
	},
	Full_Screen{
		private Dimension size;
		private Point location;
		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame frame = WorldwindEarth.findWindow((Component) e.getSource());
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice device=ge.getDefaultScreenDevice();
			Window fsw = device.getFullScreenWindow();
			frame.setVisible(false);
			frame.dispose();
			boolean goFullScreen = fsw != frame;
			frame.setUndecorated(goFullScreen);
			frame.setResizable(!goFullScreen);
			if (goFullScreen) {
				size = frame.getSize();
				location = frame.getLocation();
				device.setFullScreenWindow(frame);
				frame.validate();
			} else {
				device.setFullScreenWindow(null);
				frame.pack();
				if(size!=null)frame.setSize(size);
				if(location!=null)frame.setLocation(location);
				frame.setVisible(true);
			}
		}
	},
	Make_this_my_start_location{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());
		}
	},
	Folder{
		@Override
		public void actionPerformed(ActionEvent e) {
			WorldwindEarth.findWindow((Component) e.getSource()).getPanel().addNewFolder();
			
		}
	},
	Ruler{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	GPS{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Enter_Flight_Simulator___{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Options___{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Help_Resources{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Keyboard_Shortcuts{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Start__up_Tips{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Release_Notes{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	License{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Privacy{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Google_Earth_Community{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Upgrade_to_Google_Earth_Pro___{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	Send_Feedback{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},
	About_Google_Earth{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.name());			
		}
	},

	;

	@Override
	public abstract void actionPerformed(ActionEvent e);
	static class ImageTransferable implements Transferable
	{
		private Image image;

		public ImageTransferable (Image image)
		{
			this.image = image;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException
		{
			if (isDataFlavorSupported(flavor))
			{
				return image;
			}
			else
			{
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public boolean isDataFlavorSupported (DataFlavor flavor)
		{
			return flavor == DataFlavor.imageFlavor;
		}

		public DataFlavor[] getTransferDataFlavors ()
		{
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}
	}

}
