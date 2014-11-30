package net.joshuahughes.worldwindearth.panel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;

public class EditorTreeModel implements TreeModel{
	ArrayList<TreeModelListener> listenerList = new ArrayList<TreeModelListener>();
	Places myPlaces = new Places("My");
	Places temporaryPlaces = new Places("Temporary");
	@Override
	public Object getRoot() {
		return this;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent == this){
			return index==0?myPlaces:temporaryPlaces;
		}
		if(parent instanceof Places)
			return ((Places) parent).get(index);
		if(parent instanceof Kml){
			Feature feature = ((Kml)parent).getFeature();
			if(feature instanceof Folder)
				return ((Folder)feature).getFeature().get(index);
			if(feature instanceof Document)
				return ((Document)feature).getFeature().get(index);
			return feature;
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if(parent == this)
			return 2;
		if(parent == myPlaces)
			return myPlaces.size();
		if(parent == temporaryPlaces)
			return temporaryPlaces.size();
		if(parent instanceof Kml){
			Feature feature = ((Kml)parent).getFeature();
			if(feature instanceof Folder)
				return ((Folder)feature).getFeature().size();
			if(feature instanceof Document)
				return ((Document)feature).getFeature().size();
			return 0;
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		if(node == this)return false;
		if(node == myPlaces)
			return myPlaces.size()==0;
		if(node == temporaryPlaces)
			return temporaryPlaces.size()==0;
		if(node instanceof Kml){
			Feature feature = ((Kml)node).getFeature();
			if(feature instanceof Folder)
				return ((Folder)node).getFeature().size()==0;
			if(feature instanceof Document)
				return ((Document)node).getFeature().size()==0;
		}
		return true;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if(parent == this && child == myPlaces) return 0;
		if(parent == this && child == temporaryPlaces) return 1;
		if(parent == myPlaces) return myPlaces.indexOf(child);
		if(parent == temporaryPlaces) return temporaryPlaces.indexOf(child);
		if(parent instanceof Kml){
			Feature feature = ((Kml)parent).getFeature();
			if(feature instanceof Folder)
				return ((Folder)parent).getFeature().indexOf(child);
			if(feature instanceof Document)
				return ((Document)parent).getFeature().indexOf(child);
		}
		return -1;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		if(!listenerList.contains(l))
			listenerList.add(l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(l);
	}
	public static class Places extends ArrayList<Kml>{
		private static final long serialVersionUID = -5522526751242524415L;
		private String prefix;
		public Places(String prefix){
			this.prefix = prefix;
		}
		public String toString(){
			return prefix+" Places";
		}
	}
	public static void main(String[] args) throws Exception{
		Kml kml = new Kml();
		Folder folder = kml.createAndSetFolder();
		folder.createAndAddPlacemark();
		ByteArrayOutputStream content = new ByteArrayOutputStream();
		kml.marshal(content);
		kml = Kml.unmarshal(new String(content.toByteArray()));
		System.out.println(kml.getFeature());
	}
}
