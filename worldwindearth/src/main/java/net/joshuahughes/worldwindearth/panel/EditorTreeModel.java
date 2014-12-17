package net.joshuahughes.worldwindearth.panel;

import gov.nasa.worldwind.ogc.kml.KMLAbstractContainer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.stream.XMLStreamException;


public class EditorTreeModel extends DefaultTreeModel{
	private static final long serialVersionUID = -3930283193252605728L;
	public enum Type{MyPlaces,TemporaryPlaces,PrimaryDatabase,Search,Places(MyPlaces,TemporaryPlaces),Layers(PrimaryDatabase);Type[] subtypes;Type(Type... subtypes){this.subtypes=subtypes;}}
	DefaultMutableTreeNode root;
	public EditorTreeModel(Type type) {
		super(new DefaultMutableTreeNode(type,true),true);
		root = (DefaultMutableTreeNode) getRoot();
		for(int index=0;index<type.subtypes.length;index++)
			insertNodeInto(new DefaultMutableTreeNode(type.subtypes[index],true),root,index);
	}
	public KMLRoot add(File file){
		if(root.getChildCount()<=0)return null;
		DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getLastChild();
		DefaultMutableTreeNode grandchild =new DefaultMutableTreeNode(file,true);
		insertNodeInto(grandchild,child,child.getChildCount());
		try {
			KMLRoot value = KMLRoot.createAndParse(file);
			add(grandchild,value);
			return value;
		} catch (IOException | XMLStreamException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void add(DefaultMutableTreeNode parent,KMLAbstractObject object){
		if(object instanceof KMLRoot){
			add(parent,((KMLRoot)object).getFeature());
			return;
		}
		if(object instanceof KMLAbstractContainer){
			KMLAbstractContainer kmlContainer = (KMLAbstractContainer)object;
			DefaultMutableTreeNode container = new DefaultMutableTreeNode(kmlContainer,true);
			insertNodeInto(container, parent,parent.getChildCount());
			for(KMLAbstractObject kmlChild : kmlContainer.getFeatures())
				add(container,kmlChild);
			return;
		}
		insertNodeInto(new DefaultMutableTreeNode(object,false), parent, parent.getChildCount());
	}
}

