package net.joshuahughes.worldwindearth.panel;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import net.joshuahughes.worldwindearth.listener.Save;
import net.joshuahughes.worldwindearth.listener.Single;
import net.joshuahughes.worldwindearth.menubar.MenuBar;
import net.joshuahughes.worldwindearth.menubar.MenuItem;

import com.jidesoft.swing.CheckBoxTree;

public class PanelTree extends CheckBoxTree{
	private static final long serialVersionUID = -1292091002325519400L;
	public PanelTree(EditorTreeModel model){
		super(model);
		setRootVisible(false);
		setCellRenderer(new TreeCellRenderer() {
			JLabel label = new JLabel(); 
			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				String text = value.toString();
				if(value instanceof DefaultMutableTreeNode)
					value = ((DefaultMutableTreeNode)value).getUserObject();
				if(value instanceof File)
					text = ((File)value).getName();
				if(value instanceof KMLAbstractObject)
					text = ((KMLAbstractFeature)value).getName();
				label.setText(text);
				return label;
			}});
		addMouseListener(new MouseAdapter() {
			private void myPopupEvent(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				JTree tree = (JTree)e.getSource();
				TreePath path = tree.getPathForLocation(x, y);
				if (path == null)
					return;	

				tree.setSelectionPath(path);

				Object object = path.getLastPathComponent();
				if(object instanceof DefaultMutableTreeNode){
					Object userObject = ((DefaultMutableTreeNode)object).getUserObject();
					boolean isNotType = !(userObject instanceof EditorTreeModel.Type);
					JPopupMenu popup = new JPopupMenu();
					JMenu add = new JMenu("Add");
					for(AbstractButton button : MenuBar.createAddList()){
						if(button == null){add.addSeparator();continue;}
						add.add(button);
					}
					popup.add(add);
					popup.addSeparator();
					if(isNotType)popup.add(new MenuItem(Single.Cut));
					popup.add(new MenuItem(Single.Copy));
					if(isNotType)popup.add(new MenuItem(Single.Delete));
					popup.add(new MenuItem(Single.Delete_Contents));
					popup.addSeparator();
					popup.add(new MenuItem(Single.Rename));
					popup.addSeparator();
					popup.add(new MenuItem(Save.Save_Place_As___));
					if(isNotType)popup.add(new MenuItem(Single.Post_to_Google_Earth_Community_Forum));
					popup.add(new MenuItem(Single.Email___));
					popup.addSeparator();
					popup.add(new MenuItem(Single.Snapshot_View));
					if(isNotType)popup.add(new MenuItem(Single.Sort_A__Z));
					if(isNotType)popup.addSeparator();
					if(isNotType)popup.add(new MenuItem(Single.Properties));
					popup.show(tree, x, y);
				}
			}
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) myPopupEvent(e);
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) myPopupEvent(e);
			}
		});
	}
}