package net.joshuahughes.javaearth.toolbar;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import net.joshuahughes.javaearth.listener.Create;
import net.joshuahughes.javaearth.listener.Email;
import net.joshuahughes.javaearth.listener.Listener;
import net.joshuahughes.javaearth.listener.Overlay;
import net.joshuahughes.javaearth.listener.Save;
import net.joshuahughes.javaearth.listener.Single;

public class ToolBar extends JToolBar{
	private static final long serialVersionUID = 7888415930797592696L;
	List<AbstractButton> buttonList = Arrays.<AbstractButton>asList(
		new ToggleButton(Single.Sidebar),
		
		new Button(Create.Placemark),
		new Button(Create.Polygon),
		new Button(Create.Path),
		new Button(Create.Image_Overlay),
		new Button(Create.Tour),
		
		new ToggleButton(Overlay.Historical_Imagery),
		new ToggleButton(Overlay.Sun),
		
		new ExploreButton(),
		
		new ToggleButton(Single.Ruler),
		
		new Button(Email.Email_Placemark___),
		new Button(Single.Print___),
		new Button(Save.Save_Image___),
		new Button(Single.View_in_Google_Maps),

		new Button(Single.Sign_in_to_Maps_Engine___)
	);
	List<Integer> separationIndices = Arrays.asList(0,5,7,8,9);
	public ToolBar(){
		setFloatable(false);
		for(int index=0;index<buttonList.size();index++){
			add(buttonList.get(index));
			if(separationIndices.contains(index)) addSeparator();
			if(index == buttonList.size()-2)add(new JPanel());
		}
	}
	public AbstractButton get(Listener listener){
		for(Component comp : getComponents()){
			if(comp instanceof AbstractButton){
				AbstractButton button = (AbstractButton) comp;
				if(Arrays.asList(button.getActionListeners()).contains(listener))
						return button;
			}
			
		}
		return null;
	}

}
