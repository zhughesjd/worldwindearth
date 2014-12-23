package net.joshuahughes.worldwindearth;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLFolder;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLTraversalContext;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.io.ByteArrayInputStream;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import net.joshuahughes.worldwindearth.dialog.RulerDialog;
import net.joshuahughes.worldwindearth.dialog.addedit.AddEditDialog;
import net.joshuahughes.worldwindearth.listener.Add;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Single;
import net.joshuahughes.worldwindearth.menubar.MenuBar;
import net.joshuahughes.worldwindearth.panel.EditorTreeModel;
import net.joshuahughes.worldwindearth.panel.Panel;
import net.joshuahughes.worldwindearth.panel.PanelTree;
import net.joshuahughes.worldwindearth.support.Support;
import net.joshuahughes.worldwindearth.toolbar.ToolBar;
import net.joshuahughes.worldwindearth.viewer.Viewer;

public class WorldWindEarth extends JFrame{
    private static final long serialVersionUID = -5689158502214134655L;
    MenuBar menubar = new MenuBar();
    Viewer viewer = new Viewer();
    Panel panel = new Panel();
    ToolBar toolbar = new ToolBar();
    JPanel toolBarEarthPanel = new JPanel(new BorderLayout());
    JSplitPane panelToolBarEarthPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel,toolBarEarthPanel);
    RulerDialog rulerDialog = new RulerDialog(this);
    KMLTraversalContext tc = new KMLTraversalContext( );
    public WorldWindEarth(){
        for(Entry<EditorTreeModel.Type, PanelTree> e : panel.getTreeMap( ).entrySet( )){
            KMLRoot kmlRoot = ((KMLFolder)e.getValue( ).getModel( ).getRoot().getUserObject( )).getRoot( );
            viewer.add( kmlRoot );
        }
        tc.initialize( );
        setTitle("Worldwind Earth");
        toolBarEarthPanel.add(viewer,BorderLayout.CENTER);
        setJMenuBar(menubar);
        for(Overlay overlay : Overlay.values())
            viewer.setVisible(overlay,menubar.get(overlay).isSelected());
        menubar.doClicks();
    }
    public Viewer getViewer() {
        return viewer;
    }
    public static void main(String[] args){
        WorldWindEarth earth = new WorldWindEarth();
        earth.setSize(1000, 500);
        earth.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        earth.setVisible(true);
    }
    public void toolbar(boolean selected) {
        if(selected)
            toolBarEarthPanel.add(toolbar, BorderLayout.NORTH);
        else
            toolBarEarthPanel.remove(toolbar);
        revalidate();
    }
    public void sidebar(boolean selected) {
        menubar.get(Single.Sidebar).setSelected(selected);
        toolbar.get(Single.Sidebar).setSelected(selected);
        if(selected){
            remove(toolBarEarthPanel);
            panelToolBarEarthPane.add(toolBarEarthPanel, JSplitPane.RIGHT);
            panelToolBarEarthPane.setDividerLocation(1d/3d);
            setContentPane(panelToolBarEarthPane);
        }else{
            panelToolBarEarthPane.remove(toolBarEarthPanel);
            setContentPane(toolBarEarthPanel);
        }
        revalidate();
    }
    public void ruler(boolean selected) {
        menubar.get(Single.Ruler).setSelected(selected);
        toolbar.get(Single.Ruler).setSelected(selected);
        rulerDialog.setVisible(selected);
    }

    public void exit() {
        System.exit(1);
    }
    public static WorldWindEarth findWindow(Component c) {
        if (c instanceof Window) {
            return (WorldWindEarth) c;
        } else if (c instanceof JPopupMenu) {
            JPopupMenu pop = (JPopupMenu) c;
            return findWindow(pop.getInvoker());
        } else {
            Container parent = c.getParent();
            return parent == null ? null : findWindow(parent);
        }
    }
    public void add(Add add) {
        KMLAbstractFeature feature = create(add);
        //		if(add.equals(Add.Folder))feature = new KMLFolder(uri);
        //		if(add.equals(Add.Photo))feature = new KMLPhotoOverlay(uri);
        //		if(add.equals(Add.Image_Overlay))feature = new KMLGroundOverlay(uri);
        //		if(add.equals(Add.Network_Link))feature = new KMLNetworkLink(uri);
        //        if(add.equals(Add.Path))feature = new KMLGeometryPlacemark(uri,new KMLLineString(uri));
        //        if(add.equals(Add.Polygon))feature = new KMLGeometryPlacemark(uri,new KMLPolygon(uri));
        //        if(add.equals(Add.Model))feature = new KMLGeometryPlacemark(uri,new KMLEditableModel(uri,viewer.getPosition()));

        if(feature!=null){
            feature.setField( Support.KMLTag.name.name(), add.name( ).replace('_', ' ') );
            new AddEditDialog(this,feature);
        }
    }
    private static long id = 0;
    private KMLAbstractFeature create( Add add )
    {
        try
        {
            String kmlString = "<kml>";
            if(Add.Placemark.equals( add )){
                Position position = viewer.getPosition( );
                double lon = position.getLongitude( ).getDegrees( );
                double lat = position.getLatitude( ).getDegrees( );
                kmlString+="<Placemark id=\""+(id++)+"\"><name>Untitled Placemark</name><Point><coordinates>"+lon+","+lat+",0</coordinates></Point></Placemark>";
            }
            kmlString+="</kml>";
            ByteArrayInputStream bais = new ByteArrayInputStream(kmlString.getBytes( ));
            KMLPlacemark placemark = ( KMLPlacemark ) KMLRoot.createAndParse(bais).getFeature( );
            return placemark;
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
        return null;
    }
    public void setAddEnabled(boolean enabled) {
        this.menubar.setAddEnabled(enabled);
        this.toolbar.setAddEnabled(enabled);
        this.panel.getTreeMap( ).get( EditorTreeModel.Type.Places ).setEnabled( enabled );
    }
    public Panel getPanel( )
    {
        return this.panel;
    }
}
