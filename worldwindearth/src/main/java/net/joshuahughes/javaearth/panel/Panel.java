package net.joshuahughes.javaearth.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.joshuahughes.javaearth.WorldwindEarth;
import net.joshuahughes.javaearth.panel.EditorTreeModel.Id;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;



public class Panel extends JPanel{
	private static final long serialVersionUID = 8681617797552918262L;
	public enum KmlType{Search,Places,Layers}
	private ArrayList<ButtonPanel> panelList = new ArrayList<>();
	LinkedHashMap<KmlType,EditorTreeModel> editorMap = new LinkedHashMap<>();
	LinkedHashMap<KmlType,PanelTree> treeMap = new LinkedHashMap<>();
	public Panel(){
		super(new GridBagLayout());
		LinkedHashSet<Folder> fix = new LinkedHashSet<>();
		for(KmlType type : KmlType.values()){
			Folder folder = new Kml().createAndSetFolder();
			if(KmlType.Search.equals(type)){
				fix.add(folder);
			}
			if(KmlType.Places.equals(type)){
				Folder myPlaces = folder.createAndAddFolder();
				myPlaces.setName("My Places");
				fix.add(myPlaces);
				Folder temporaryPlaces = folder.createAndAddFolder();
				temporaryPlaces.setName("Temporary Places");
				fix.add(temporaryPlaces);
			}
			if(KmlType.Layers.equals(type)){
				Folder primaryDatabase = folder.createAndAddFolder();
				primaryDatabase.setName("Primary Database");
				fix.add(primaryDatabase);
			}
			for(Folder fixFolder : fix){
				fixFolder.createAndAddDocument();
				fixFolder.getFeature().clear();
				if(!"My Places".equals(fixFolder.getName())) fixFolder.setId(Id.append.name());
			}
			EditorTreeModel model = new EditorTreeModel(folder);
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
	public void append(KmlType type,Feature feature) {
		if(KmlType.Places.equals(type)){
			editorMap.get(type).append(feature);
			WorldwindEarth.findWindow((Component)this).getEarthviewer().add(feature);
			update(type);
		}
	}
	private void update(KmlType type) {
		treeMap.get(type).setModel(editorMap.get(type));
		treeMap.get(type).repaint();
		treeMap.get(type).revalidate();
		treeMap.get(type).updateUI();
	}
}
