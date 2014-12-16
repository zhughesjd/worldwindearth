package net.joshuahughes.javaearth.panel;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

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
				if(value instanceof File)
					text = ((File)value).getName();
				if(value instanceof KMLAbstractObject)
					text = ((KMLAbstractFeature)value).getName();
				label.setText(text);
				return label;
			}});
	}
}
