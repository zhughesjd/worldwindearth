package net.joshuahughes.javaearth.menubar;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import net.joshuahughes.javaearth.listener.Create;
import net.joshuahughes.javaearth.listener.Email;
import net.joshuahughes.javaearth.listener.Listener;
import net.joshuahughes.javaearth.listener.Overlay;
import net.joshuahughes.javaearth.listener.Reset;
import net.joshuahughes.javaearth.listener.Save;
import net.joshuahughes.javaearth.listener.Show_Navigation;
import net.joshuahughes.javaearth.listener.Single;
import net.joshuahughes.javaearth.listener.View_Size;

public class MenuBar extends JMenuBar{
	private static final long serialVersionUID = 6746752440165017942L;
	List<AbstractButton> fileList = Arrays.<AbstractButton>asList(
			new MenuItem(Single.Open___),
			new RadioMenu(Save.class),
			new MenuItem(Single.Revert),
			null,
			new RadioMenu(Email.class),
			new MenuItem(Single.Post_to_Google_Earth_Community_Forum),
			new MenuItem(Single.View_in_Google_Maps),
			null,
			new MenuItem(Single.Print___),
			null,
			new MenuItem(Single.Server_Sign_Out),
			null,
			new MenuItem(Single.Sign_in_to_Maps_Engine___),
			null,
			new MenuItem(Single.Exit)
	);
	List<AbstractButton> editList = Arrays.<AbstractButton>asList(
			new MenuItem(Single.Cut),
			new MenuItem(Single.Copy),
			new MenuItem(Single.Copy_As_Tracks),
			new MenuItem(Single.Copy_Image),
			new MenuItem(Single.Copy_View_Location),
			new MenuItem(Single.Paste),
			new MenuItem(Single.Delete),
			null,
			new MenuItem(Single.Find),
			null,
			new MenuItem(Single.Refresh),
			new MenuItem(Single.Rename),
			new MenuItem(Single.Snapshot_View),
			new MenuItem(Single.Sort_A__Z),
			new MenuItem(Single.Delete_Contents),
			null,
			new MenuItem(Single.Apply_Style_Template___),
			null,
			new MenuItem(Single.Show_Elevation_Profile),
			null,
			new MenuItem(Single.Properties)
	);
	List<AbstractButton> viewList = Arrays.<AbstractButton>asList(
			new CheckBoxMenuItem(Single.Toolbar),
			new CheckBoxMenuItem(Single.Sidebar),
			null,
			new MenuItem(Single.Full_Screen),
			new RadioMenu(View_Size.class),
			null,
			new RadioMenu(Show_Navigation.class),
			null,
			new CheckBoxMenuItem(Overlay.Status_Bar),
			new CheckBoxMenuItem(Overlay.Grid),
			new CheckBoxMenuItem(Overlay.Overview_Map),
			new CheckBoxMenuItem(Overlay.Scale_Legend),
			new CheckBoxMenuItem(Overlay.Tour_Guide),
			null,
			new CheckBoxMenuItem(Overlay.Atmosphere),
			new CheckBoxMenuItem(Overlay.Sun),
			new CheckBoxMenuItem(Overlay.Historical_Imagery),
			new CheckBoxMenuItem(Overlay.Water_Surface),
			null,
			new RadioMenu(Show_Navigation.class),
			null,
			new RadioMenu(Reset.class),
			null,
			new MenuItem(Single.Make_this_my_start_location)
	);
	List<AbstractButton> toolsList = Arrays.<AbstractButton>asList(
			new CheckBoxMenuItem(Single.Ruler),
			null,
			new MenuItem(Single.GPS),
			null,
			new MenuItem(Single.Enter_Flight_Simulator___),
			null,
			new MenuItem(Single.Options___)
	);
	List<AbstractButton> addList = Arrays.<AbstractButton>asList(
			new MenuItem(Single.Folder),
			null,
			new MenuItem(Create.Placemark),
			null,
			new MenuItem(Create.Path),
			new MenuItem(Create.Polygon),
			new MenuItem(Create.Model),
			new MenuItem(Create.Tour),
			null,
			new MenuItem(Create.Photo),
			new MenuItem(Create.Image_Overlay),
			null,
			new MenuItem(Create.Network_Link)
	);
	List<AbstractButton> helpList = Arrays.<AbstractButton>asList(
			new MenuItem(Single.Help_Resources),
			new MenuItem(Single.Keyboard_Shortcuts),
			new MenuItem(Single.Start__up_Tips),
			null,
			new MenuItem(Single.Release_Notes),
			new MenuItem(Single.License),
			new MenuItem(Single.Privacy),
			null,
			new MenuItem(Single.Google_Earth_Community),
			null,
			new MenuItem(Single.Upgrade_to_Google_Earth_Pro___),
			new MenuItem(Single.Send_Feedback),
			new MenuItem(Single.About_Google_Earth)
	);
	public static enum MenuName{File,Edit,View,Tools,Add,Help};
	List<List<AbstractButton>> lists = Arrays.asList(fileList,editList,viewList,toolsList,addList,helpList);
	public MenuBar(){
		for(int index=0;index<lists.size();index++){
			List<AbstractButton> list = lists.get(index);
			JMenu menu = new JMenu(MenuName.values()[index].name());
			add(menu);
			for(AbstractButton button : list){
				if(button == null){menu.addSeparator();continue;}
				menu.add(button);
			}
		}
	}
	public AbstractButton get(Listener listener){
		for(int index=0;index<getMenuCount();index++){
			JMenu topMenu = (JMenu) getMenu(index);
			for(Component comp : topMenu.getMenuComponents()){
				if(comp instanceof AbstractButton){
					AbstractButton button = (AbstractButton) comp;
					if(Arrays.asList(button.getActionListeners()).contains(listener))
							return button;
				}
			}
		}
		return null;
	}
	public void doClicks() {
		get(Single.Sidebar).doClick();
		get(Single.Toolbar).doClick();
		for(List<AbstractButton> list : lists)
			for(AbstractButton button : list){
				if(button instanceof RadioMenu && ((RadioMenu)button).getSubElements()[0] instanceof RadioButtonMenuItem)
					((RadioButtonMenuItem)((RadioMenu)button).getSubElements()[0]).doClick();
			}
	}
}
