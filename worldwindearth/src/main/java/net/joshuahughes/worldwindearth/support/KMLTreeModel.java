package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLAbstractContainer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class KMLTreeModel extends DefaultTreeModel{
	private static final long serialVersionUID = -1910087161141056231L;
	public KMLTreeModel(KMLAbstractContainer container) {
		super(new DefaultMutableTreeNode(container,true));
	}
	public void add(DefaultMutableTreeNode parent,KMLAbstractFeature object){
		if(object instanceof KMLAbstractContainer){
			KMLAbstractContainer kmlContainer = (KMLAbstractContainer)object;
			DefaultMutableTreeNode container = new DefaultMutableTreeNode(kmlContainer,true);
			insertNodeInto(container, parent,parent.getChildCount());
			for(KMLAbstractFeature kmlChild : kmlContainer.getFeatures())
				add(container,kmlChild);
			return;
		}
		insertNodeInto(new DefaultMutableTreeNode(object,false), parent, parent.getChildCount());
	}
}
