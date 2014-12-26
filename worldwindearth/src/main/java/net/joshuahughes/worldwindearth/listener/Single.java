package net.joshuahughes.worldwindearth.listener;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import net.joshuahughes.worldwindearth.WorldWindEarth;
import net.joshuahughes.worldwindearth.panel.EditorTreeModel;
import net.joshuahughes.worldwindearth.panel.EditorTreeModel.Type;
import net.joshuahughes.worldwindearth.support.Support;
import de.micromata.opengis.kml.v_2_2_0.Kml;

public enum Single implements Listener{
    Open___{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public String getDescription() {
                    return "Google Earth";
                }
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".kml") || f.getName().endsWith(".kmz");
                }
            });
            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                try{
                    WorldWindEarth.findWindow((Component) e.getSource()).getPanel().getTreeMap( ).get( Type.Places ).addToSelected( KMLRoot.createAndParse( chooser.getSelectedFile( ) ).getFeature( ) );
                }catch ( Exception e1 ){
                    e1.printStackTrace();
                }
        }
    },
    Revert{
        @Override
        public void actionPerformed(ActionEvent e) {
            WorldWindEarth.findWindow((Component) e.getSource()).getPanel().getTreeMap( ).get( Type.Places ).revertSelection();
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
            WorldWindEarth.findWindow((AbstractButton) e.getSource()).exit();
        }
    },
    Cut{
        @Override
        public void actionPerformed(ActionEvent e) {
            Copy.actionPerformed( e );
            Delete.actionPerformed( e );
        }
    },
    Copy{
        @Override
        public void actionPerformed(ActionEvent e) {
            WorldWindEarth frame = WorldWindEarth.findWindow((Component) e.getSource());
            DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) frame.getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).getSelectionPath( ).getLastPathComponent( );
            KMLAbstractFeature feature = ( KMLAbstractFeature ) node.getUserObject( );
            Kml kml = new Kml();
            kml.setFeature(Support.convert(feature));
            StringWriter stringWriter = new StringWriter();
            kml.marshal( stringWriter );
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents( new StringSelection(stringWriter.toString( )),new ClipboardOwner( ){public void lostOwnership( Clipboard clipboard, Transferable contents ){}} );
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
            WorldWindEarth frame = WorldWindEarth.findWindow((Component) e.getSource());
            Component panel = frame.getViewer();
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
            try {
                String candidateKMLString = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                KMLAbstractFeature feature = KMLRoot.createAndParse(new ByteArrayInputStream(candidateKMLString.getBytes(StandardCharsets.UTF_8))).getFeature();
                WorldWindEarth.findWindow((Component) e.getSource()).getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).addToSelected(feature);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    },
    Delete{
        @Override
        public void actionPerformed(ActionEvent e) {
            WorldWindEarth frame = WorldWindEarth.findWindow((Component) e.getSource());
            frame.getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).removeSelection();
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
            WorldWindEarth.findWindow((Component) e.getSource()).getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).rename( );
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
            WorldWindEarth frame = WorldWindEarth.findWindow((Component) e.getSource());
            DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) frame.getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).getSelectionPath( ).getLastPathComponent( );
            ArrayList<DefaultMutableTreeNode> children = new ArrayList<>( );
            for(int index=0;index<node.getChildCount( );index++)
                children.add( ( DefaultMutableTreeNode ) node.getChildAt( index ) );
            for(DefaultMutableTreeNode child : children)
                frame.getPanel( ).getTreeMap( ).get( EditorTreeModel.Type.Places ).getModel( ).removeNodeFromParent( child );
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
                WorldWindEarth.findWindow((AbstractButton) e.getSource()).toolbar(((AbstractButton)e.getSource()).isSelected());
            }
        }
    },
    Sidebar{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() instanceof AbstractButton){
                WorldWindEarth.findWindow((AbstractButton) e.getSource()).sidebar(((AbstractButton)e.getSource()).isSelected());
            }
        }
    },
    Full_Screen{
        private Dimension size;
        private Point location;
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = WorldWindEarth.findWindow((Component) e.getSource());
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
    Ruler{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() instanceof AbstractButton){
                WorldWindEarth.findWindow((AbstractButton) e.getSource()).ruler(((AbstractButton)e.getSource()).isSelected());
            }
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
    Email___{
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
