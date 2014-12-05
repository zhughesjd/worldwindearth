package net.joshuahughes.javaearth.panel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import com.jidesoft.swing.CheckBoxTree;

import de.micromata.opengis.kml.v_2_2_0.Feature;

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
				label.setText(((Feature)value).getName());
				return label;
			}});
	}
}
