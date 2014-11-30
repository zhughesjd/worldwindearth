package net.joshuahughes.javaearth.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.joshuahughes.javaearth.dialog.Folder;



public class Panel extends JPanel{
	private static final long serialVersionUID = 8681617797552918262L;

	public Panel(){
		super(new GridBagLayout());
		add(Search.class.getSimpleName(),new Search());
		add(Places.class.getSimpleName(),new Places());
		add(Layers.class.getSimpleName(),new Layers());
	}
	private ArrayList<ButtonPanel> panelList = new ArrayList<>();
	public void add(String title,JComponent comp){
		panelList.add(new ButtonPanel(title, comp));
		adjust();
	}
	public void addNewFolder(){
		Folder dlg = new Folder();
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
}
