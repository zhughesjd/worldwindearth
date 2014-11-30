package net.joshuahughes.javaearth.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;

public class Places extends JPanel{
	private static final long serialVersionUID = -1145830879917087516L;
	EditorTreeModel model = new EditorTreeModel();
	public Places(){
		super(new BorderLayout());
		JTree tree = new JTree(model);
//		tree.setRootVisible(false);
		add(tree);
	}
}
