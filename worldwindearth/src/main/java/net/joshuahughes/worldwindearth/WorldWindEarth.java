package net.joshuahughes.worldwindearth;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLFolder;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.ogc.kml.KMLNetworkLink;
import gov.nasa.worldwind.ogc.kml.KMLPhotoOverlay;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLTraversalContext;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.io.File;

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
import net.joshuahughes.worldwindearth.support.KMLEditableModel;
import net.joshuahughes.worldwindearth.support.KMLEditablePoint;
import net.joshuahughes.worldwindearth.support.KMLGeometryPlacemark;
import net.joshuahughes.worldwindearth.support.Support;
import net.joshuahughes.worldwindearth.toolbar.ToolBar;
import net.joshuahughes.worldwindearth.viewer.Viewer;

public class WorldWindEarth extends JFrame{
	private static final long serialVersionUID = -5689158502214134655L;
	MenuBar menubar = new MenuBar();
	Panel panel = new Panel();
	ToolBar toolbar = new ToolBar();
	Viewer viewer = new Viewer();
	JPanel toolBarEarthPanel = new JPanel(new BorderLayout());
	JSplitPane panelToolBarEarthPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel,toolBarEarthPanel);
	RulerDialog rulerDialog = new RulerDialog(this);
	KMLTraversalContext tc = new KMLTraversalContext( );
	public WorldWindEarth(){
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
	public void open(File kmlFile) {
		KMLRoot root = panel.open(kmlFile);
		viewer.add(root);
	}
	public void add(Add add) {
		KMLAbstractFeature feature = null;
		String uri = null;
		if(add.equals(Add.Folder))feature = new KMLFolder(uri);
		if(add.equals(Add.Photo))feature = new KMLPhotoOverlay(uri);
		if(add.equals(Add.Image_Overlay))feature = new KMLGroundOverlay(uri);
		if(add.equals(Add.Network_Link))feature = new KMLNetworkLink(uri);
        if(add.equals(Add.Placemark))feature = new KMLGeometryPlacemark(uri,new KMLEditablePoint(uri,viewer.getPosition()));
        if(add.equals(Add.Path))feature = new KMLGeometryPlacemark(uri,new KMLLineString(uri));
        if(add.equals(Add.Polygon))feature = new KMLGeometryPlacemark(uri,new KMLPolygon(uri));
        if(add.equals(Add.Model))feature = new KMLGeometryPlacemark(uri,new KMLEditableModel(uri,viewer.getPosition()));
        
		if(feature!=null){
		    feature.setField( Support.KMLTag.name.name(), add.name( ).replace('_', ' ') );
		    new AddEditDialog(this,"New",feature);
		}
	}
	public void setAddEnabled(boolean enabled) {
		this.menubar.setAddEnabled(enabled);
		this.toolbar.setAddEnabled(enabled);
		this.panel.setAddEnabled(enabled);
	}
	public void add(KMLAbstractFeature feature) {
		panel.add(EditorTreeModel.Type.Places,feature);
	}
}
