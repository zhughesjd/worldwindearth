package net.joshuahughes.worldwindearth;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import net.joshuahughes.worldwindearth.listener.Overlay;
import net.joshuahughes.worldwindearth.listener.Single;
import net.joshuahughes.worldwindearth.menubar.MenuBar;
import net.joshuahughes.worldwindearth.panel.Panel;
import net.joshuahughes.worldwindearth.toolbar.ToolBar;
import net.joshuahughes.worldwindearth.viewer.Viewer;
import net.joshuahughes.worldwindearth.viewer.WorldwindViewer;

public class WorldwindEarth extends JFrame{
	private static final long serialVersionUID = -5689158502214134655L;
	MenuBar menubar = new MenuBar();
	Panel panel = new Panel();
	ToolBar toolbar = new ToolBar();
	Viewer viewer = new WorldwindViewer();
	JPanel toolBarEarthPanel = new JPanel(new BorderLayout());
	JSplitPane panelToolBarEarthPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel,toolBarEarthPanel);
	public WorldwindEarth(){
		setTitle("Worldwind Earth");
		toolBarEarthPanel.add(viewer.getViewer(),BorderLayout.CENTER);
		setJMenuBar(menubar);
		for(Overlay overlay : Overlay.values())
			viewer.setVisible(overlay,menubar.get(overlay).isSelected());
		menubar.doClicks();
	}
	public MenuBar getMenubar() {
		return menubar;
	}
	public Panel getPanel() {
		return panel;
	}
	public ToolBar getToolBar() {
		return toolbar;
	}
	public Viewer getEarthviewer() {
		return viewer;
	}
	public static void main(String[] args){
		WorldwindEarth earth = new WorldwindEarth();
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
		getMenubar().get(Single.Sidebar).setSelected(selected);
		getToolBar().get(Single.Sidebar).setSelected(selected);
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
	public void exit() {
		System.exit(1);
	}
	public static WorldwindEarth findWindow(Component c) {
		if (c instanceof Window) {
			return (WorldwindEarth) c;
		} else if (c instanceof JPopupMenu) {
			JPopupMenu pop = (JPopupMenu) c;
			return findWindow(pop.getInvoker());
		} else {
			Container parent = c.getParent();
			return parent == null ? null : findWindow(parent);
		}
	}
}
