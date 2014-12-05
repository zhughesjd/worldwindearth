package net.joshuahughes.javaearth.panel;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;

public class EditorTreeModel implements TreeModel{
	public enum Model{Search,Places,Layers;Kml kml = new Kml();Folder folder = kml.createAndSetFolder();}
	private static final Folder myPlaces = Model.Places.folder.createAndAddFolder();
	private static final Folder temporaryPlaces = Model.Places.folder.createAndAddFolder();
	private static final Folder primaryDatabase = Model.Layers.folder.createAndAddFolder();
	static {
		myPlaces.setName("My Places");
		temporaryPlaces.setName("Temporary Places");
		primaryDatabase.setName("Primary Database");
		for(Folder folder : new Folder[]{Model.Search.folder,myPlaces,temporaryPlaces,primaryDatabase}){
			folder.createAndAddDocument();
			folder.getFeature().clear();
		}
	}
	private Folder root;
	public EditorTreeModel(Folder folder){
		this.root = folder;
	}
	ArrayList<TreeModelListener> listenerList = new ArrayList<TreeModelListener>();
	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent instanceof Folder) return ((Folder)parent).getFeature().get(index);
		if(parent instanceof Document) return ((Document)parent).getFeature().get(index);
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if(parent instanceof Folder) return ((Folder)parent).getFeature().size();
		if(parent instanceof Document) return ((Document)parent).getFeature().size();
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		if(node instanceof Folder) return ((Folder)node).getFeature().size()==0;
		if(node instanceof Document) return ((Document)node).getFeature().size()==0;
		return true;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if(parent instanceof Folder)return ((Folder)parent).getFeature().indexOf(child);
		if(parent instanceof Document) return ((Document)parent).getFeature().indexOf(child);
		return -1;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		
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
}
