package net.joshuahughes.worldwindearth.panel;

import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.joshuahughes.worldwindearth.WorldWindEarth;



public class Panel extends JPanel{
	private static final long serialVersionUID = 8681617797552918262L;
	private ArrayList<ButtonPanel> panelList = new ArrayList<>();
	EditorTreeModel.Type[] types = new EditorTreeModel.Type[]{EditorTreeModel.Type.Search,EditorTreeModel.Type.Places,EditorTreeModel.Type.Layers};
	LinkedHashMap<EditorTreeModel.Type,EditorTreeModel> editorMap = new LinkedHashMap<>();
	LinkedHashMap<EditorTreeModel.Type,PanelTree> treeMap = new LinkedHashMap<>();
	public Panel(){
		super(new GridBagLayout());
		for(EditorTreeModel.Type type : types){
			EditorTreeModel model = new EditorTreeModel(type);
			editorMap.put(type, model);
			PanelTree  panelTree = new PanelTree(model);
			treeMap.put(type, panelTree);
			JScrollPane pane = new JScrollPane(panelTree);
			panelList.add(new ButtonPanel(type.name(),pane));
			adjust();
		}
	}
	public void addNewFolder(){
		JDialog dlg = new JDialog();
		dlg.setVisible(true);
		
	}
	private void adjust(){
		removeAll();
		boolean allCollapsed = true;
		for(int index=0;index<panelList.size();index++){
			ButtonPanel panel =panelList.get(index);
			if(panel.isExpanded())
				allCollapsed=false;
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx=0;
			gbc.gridy=index;
			gbc.weightx=1;
			gbc.weighty=panel.isExpanded() ? 1 : 0;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.PAGE_START;
			if(index==panelList.size()-1 && allCollapsed){
				gbc.weighty=1;
				gbc.fill = GridBagConstraints.HORIZONTAL;
			}
			add(panel,gbc);
		}
		validate();
	}
	private class ButtonPanel extends JPanel{
		private static final long serialVersionUID = -1130765426898962907L;
		JCheckBox box;
		JComponent component;
		
		public ButtonPanel(String title,JComponent comp){
			super(new BorderLayout());
			setBorder(BorderFactory.createLineBorder(Color.black));
			box = new JCheckBox(title,false);
			component = comp;
			component.setBorder(BorderFactory.createLineBorder(Color.black));
			box.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ButtonPanel.this.removeAll();
					Component centerComponent = box;
					if(box.isSelected()){
						add(box,BorderLayout.NORTH);
						centerComponent = component;
					}
					add(centerComponent,BorderLayout.CENTER);
					Panel.this.adjust();
				}
			});
			box.doClick();
		}

		public boolean isExpanded() {
			return this.box.isSelected();
		}
	}
	public void open(File kmlFile) {
		KMLRoot root = editorMap.get(EditorTreeModel.Type.Places).add(kmlFile);
		WorldWindEarth.findWindow(this).getViewer().add(root);

	}
	public void setAddEnabled(boolean enabled) {
		treeMap.get(EditorTreeModel.Type.Places).setEnabled(enabled);
	}
}
