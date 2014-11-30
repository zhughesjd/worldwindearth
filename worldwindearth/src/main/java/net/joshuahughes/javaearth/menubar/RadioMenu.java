package net.joshuahughes.javaearth.menubar;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;

import net.joshuahughes.javaearth.listener.Explore;
import net.joshuahughes.javaearth.listener.Show_Navigation;
import net.joshuahughes.javaearth.listener.View_Size;

public class RadioMenu extends JMenu{
	private static final long serialVersionUID = 1596168515604230025L;
	List<?> radioClasses = Arrays.asList(Show_Navigation.class,View_Size.class,Explore.class);
	ButtonGroup group = new ButtonGroup();
	public <E extends Enum<E>> RadioMenu(Class<E> clazz) {
		super(clazz.getSimpleName().replace('_', ' '));
		for(E e : clazz.getEnumConstants()){
			AbstractButton button = radioClasses.contains(clazz)?new RadioButtonMenuItem((ActionListener)e):new MenuItem((ActionListener)e);
			add(button);
			if(button instanceof RadioButtonMenuItem)
				group.add(button);
		}
	}
}
