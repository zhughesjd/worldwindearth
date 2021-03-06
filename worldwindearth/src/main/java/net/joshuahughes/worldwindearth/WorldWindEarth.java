package net.joshuahughes.worldwindearth;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLFolder;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import net.joshuahughes.worldwindearth.dialog.RulerDialog;
import net.joshuahughes.worldwindearth.dialog.addedit.AddPropertiesDialog;
import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Single;
import net.joshuahughes.worldwindearth.menubar.MenuBar;
import net.joshuahughes.worldwindearth.panel.EditorTreeModel;
import net.joshuahughes.worldwindearth.panel.Panel;
import net.joshuahughes.worldwindearth.panel.PanelTree;
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
    public WorldWindEarth(){
        for(Entry<EditorTreeModel.Type, PanelTree> e : panel.getTreeMap( ).entrySet( )){
            KMLRoot kmlRoot = ((KMLFolder)e.getValue( ).getModel( ).getRoot().getUserObject( )).getRoot( );
            viewer.add( kmlRoot );
        }
        setTitle("Worldwind Earth");
        toolBarEarthPanel.add(viewer,BorderLayout.CENTER);
        setJMenuBar(menubar);
        for(Overlay overlay : Overlay.values())
            viewer.setVisible(overlay,menubar.get(overlay).isSelected());
        addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				panel.getTreeMap().get(EditorTreeModel.Type.Places).getModel().save();
			}
		});
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
        if (c instanceof WorldWindEarth) {
            return (WorldWindEarth) c;
        }else if (c instanceof JPopupMenu) {
            JPopupMenu pop = (JPopupMenu) c;
            return findWindow(pop.getInvoker());
        } else {
            Container parent = c.getParent();
            return parent == null ? null : findWindow(parent);
        }
    }
    public void edit(KMLAbstractFeature feature) {
        if(feature==null) return;
        viewer.edit(feature);
        new AddPropertiesDialog(this,feature);
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
