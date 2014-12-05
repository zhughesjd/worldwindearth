package net.joshuahughes.javaearth.panel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;

public class EditorTreeModel implements TreeModel{
	public static enum Id{append}
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
	public void valueForPathChanged(TreePath path, Object value) {
	    File oldFile = (File) path.getLastPathComponent();
	    String fileParentPath = oldFile.getParent();
	    String newFileName = (String) value;
	    File targetFile = new File(fileParentPath, newFileName);
	    oldFile.renameTo(targetFile);
	    File parent = new File(fileParentPath);
	    int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
	    Object[] changedChildren = { targetFile };
	    fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
	 
	  }
	 
	  private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
	    TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
	    Iterator<TreeModelListener> iterator = listenerList.iterator();
	    TreeModelListener listener = null;
	    while (iterator.hasNext()) {
	      listener = (TreeModelListener) iterator.next();
	      listener.treeNodesChanged(event);
	    }
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
	int index=0;
	public void append(Feature feature) {
		feature.setId("Id"+index++);
		if(Id.append.name().equals(root.getId()))
			root.getFeature().add(feature);
		for(Feature f : root.getFeature()){
			Folder folder = (Folder) f;
			if(Id.append.name().equals(folder.getId()))
				folder.getFeature().add(feature);
		}
	}
}
