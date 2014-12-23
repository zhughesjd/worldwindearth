package net.joshuahughes.worldwindearth.panel;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import net.joshuahughes.worldwindearth.listener.Save;
import net.joshuahughes.worldwindearth.listener.Single;
import net.joshuahughes.worldwindearth.menubar.MenuBar;
import net.joshuahughes.worldwindearth.menubar.MenuItem;
import net.joshuahughes.worldwindearth.support.Support;

import com.jidesoft.swing.CheckBoxTree;

public class PanelTree extends CheckBoxTree{
	private static final long serialVersionUID = -1292091002325519400L;
	public PanelTree(EditorTreeModel model){
		super(model);
		ToolTipManager.sharedInstance().registerComponent(this);
		setRootVisible(false);
		setCellRenderer(new TreeCellRenderer() {
			JLabel label = new JLabel(); 
			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				label.setForeground(Color.black);
				label.setOpaque(selected);
				label.setBackground(selected?Color.gray:Color.white);
				String text = value.toString();
				String tooltip = null;
				if(value instanceof DefaultMutableTreeNode)
					value = ((DefaultMutableTreeNode)value).getUserObject();
				if(value instanceof File)
					text = ((File)value).getName();
				if(value instanceof KMLAbstractObject){
					text = ((KMLAbstractFeature)value).getName();
					tooltip = ((KMLAbstractFeature)value).getDescription();
				}
				label.setText(text);
				if(tooltip!=null)label.setToolTipText(tooltip);
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
	@Override
	public String getToolTipText(MouseEvent evt) {
		if (getRowForLocation(evt.getX(), evt.getY()) == -1)
			return null;
		TreePath curPath = getPathForLocation(evt.getX(), evt.getY());
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)curPath.getLastPathComponent();
		if(node.getUserObject() instanceof KMLAbstractObject){
			Object object = ((KMLAbstractObject) node.getUserObject()).getField(Support.KMLTag.description.name());
			return object==null?"null":object.toString();
		}
		return "no tip";
	}
	public void addToSelected(KMLAbstractFeature feature) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) this.getModel().getRoot()).getLastChild();
		if(getSelectionPath()!=null){
			node = (DefaultMutableTreeNode) getSelectionPath().getLastPathComponent();
			if(!node.getAllowsChildren()) node = (DefaultMutableTreeNode) node.getParent();
		}
		EditorTreeModel model = (EditorTreeModel) getModel();
		model.add(node,feature);
	}
	public EditorTreeModel getModel(){
	    return ( EditorTreeModel ) super.getModel( );
	}
    public void removeSelection( )
    {
        getModel().removeNodeFromParent( ( DefaultMutableTreeNode ) this.getSelectionPath( ).getLastPathComponent( ) );
    }
}
