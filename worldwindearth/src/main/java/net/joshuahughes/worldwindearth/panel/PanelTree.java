package net.joshuahughes.worldwindearth.panel;

import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLDocument;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLNetworkLink;
import gov.nasa.worldwind.ogc.kml.KMLPhotoOverlay;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import net.joshuahughes.worldwindearth.listener.Save;
import net.joshuahughes.worldwindearth.listener.Single;
import net.joshuahughes.worldwindearth.menubar.MenuBar;
import net.joshuahughes.worldwindearth.menubar.MenuItem;
import net.joshuahughes.worldwindearth.support.Support;

import com.jidesoft.swing.CheckBoxTree;

public class PanelTree extends CheckBoxTree{
    private static final long serialVersionUID = -1292091002325519400L;
    public PanelTree(EditorTreeModel model){
        super(model);
        ToolTipManager.sharedInstance().registerComponent(this);
        setRootVisible(false);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            private static final long serialVersionUID = 1L;
            JLabel label = new JLabel(); 
            @Override
            public Component getTreeCellRendererComponent(JTree tree,
                    Object value, boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                label.setForeground(Color.black);
                label.setOpaque(selected);
                label.setBackground(selected?Color.gray:Color.white);
                String text = value.toString();
                String tooltip = null;
                if(value instanceof DefaultMutableTreeNode)
                    value = ((DefaultMutableTreeNode)value).getUserObject();
                if(value instanceof File)
                    text = ((File)value).getName();
                if(value instanceof KMLAbstractObject){
                    text = ((KMLAbstractFeature)value).getName();
                    tooltip = ((KMLAbstractFeature)value).getDescription();
                }
                label.setText(text);
                if(tooltip!=null)label.setToolTipText(tooltip);
                return label;
            }}; 
        setCellRenderer(renderer);        
        addMouseListener(new MouseAdapter() {
            private void myPopupEvent(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                JTree tree = (JTree)e.getSource();
                TreePath path = tree.getPathForLocation(x, y);
                if (path == null)
                    return;	

                tree.setSelectionPath(path);
                List<Class<? extends KMLAbstractObject>> refreshableClasses = Arrays.asList( KMLModel.class,KMLPhotoOverlay.class,KMLGroundOverlay.class,KMLNetworkLink.class );
                Object object = path.getLastPathComponent();
                if(object instanceof DefaultMutableTreeNode){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)object;
                    boolean isRootChild = PanelTree.this.getModel( ).getRoot( ).isNodeChild( node );
                    JPopupMenu popup = new JPopupMenu();
                    JMenu add = new JMenu("Add");
                    for(AbstractButton button : MenuBar.createAddList()){
                        if(button == null){add.addSeparator();continue;}
                        add.add(button);
                    }
                    popup.add(add);
                    popup.addSeparator();
                    if(!isRootChild)popup.add(new MenuItem(Single.Cut));
                    popup.add(new MenuItem(Single.Copy));
                    if(!isRootChild)popup.add(new MenuItem(Single.Delete));
                    if(!node.isLeaf( ))popup.add(new MenuItem(Single.Delete_Contents));
                    popup.addSeparator();
                    boolean addSeparator = false;
                    if(!isRootChild){
                        if(refreshableClasses.contains( node.getUserObject( ).getClass( ) ))popup.add(new MenuItem(Single.Refresh));
                        popup.add(new MenuItem(Single.Rename));
                        addSeparator=true;
                    }
                    if(node.getUserObject( ) instanceof KMLDocument){
                        popup.add(new MenuItem(Single.Revert));
                        addSeparator=true;
                    }
                    if(addSeparator) popup.addSeparator();
                    if(descendant((DefaultMutableTreeNode)PanelTree.this.getModel().getRoot( ).getLastChild( ),node)){
                        popup.add(new MenuItem(Save.Save_to_My_Places));
                        popup.addSeparator();
                    }
                    popup.add(new MenuItem(Save.Save_Place_As___));
                    if(!isRootChild)popup.add(new MenuItem(Single.Post_to_Google_Earth_Community_Forum));
                    popup.add(new MenuItem(Single.Email___));
                    popup.addSeparator();
                    popup.add(new MenuItem(Single.Snapshot_View));
                    if(!isRootChild){
                        popup.add(new MenuItem(Single.Sort_A__Z));
                        popup.addSeparator();
                        popup.add(new MenuItem(Single.Properties));
                    }
                    popup.show(tree, x, y);
                }
            }
            private boolean descendant( DefaultMutableTreeNode parent, DefaultMutableTreeNode node )
            {
                if(parent.equals( node )) return true;
                for(int index=0;index<parent.getChildCount( );index++){
                    if(parent.getChildAt( index ).equals( node ))
                        return true;
                    if(descendant( ( DefaultMutableTreeNode ) parent.getChildAt( index ), node ))return true;
                }
                return false;
            }
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) myPopupEvent(e);
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) myPopupEvent(e);
            }

        });
    }
    @Override
    public String getToolTipText(MouseEvent evt) {
        if (getRowForLocation(evt.getX(), evt.getY()) == -1)
            return null;
        TreePath curPath = getPathForLocation(evt.getX(), evt.getY());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)curPath.getLastPathComponent();
        if(node.getUserObject() instanceof KMLAbstractObject){
            Object object = ((KMLAbstractObject) node.getUserObject()).getField(Support.KMLTag.description.name());
            return object==null?"null":object.toString();
        }
        return "no tip";
    }
    public void alterTree(KMLAbstractFeature feature) {
    	remove(feature.getField(Support.KMLTag.id.name()).toString());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) this.getModel().getRoot()).getLastChild();
        if(getSelectionPath()!=null){
            node = (DefaultMutableTreeNode) getSelectionPath().getLastPathComponent();
            if(!node.getAllowsChildren()) node = (DefaultMutableTreeNode) node.getParent();
        }
        EditorTreeModel model = (EditorTreeModel) getModel();
        model.add(node,feature);
    }
    public void remove(String id) {
    	getModel().remove(getModel().getRoot(),id);
	}
	public EditorTreeModel getModel(){
        return ( EditorTreeModel ) super.getModel( );
    }
    public void removeSelection( )
    {
        getModel().removeNodeFromParent( ( DefaultMutableTreeNode ) this.getSelectionPath( ).getLastPathComponent( ) );
    }
    public void startEditingAtPath(TreePath path) {
        for(int index=0;index<getModel().getRoot( ).getChildCount( );index++)
            if(path.getLastPathComponent( ).equals( getModel().getRoot( ).getChildAt( index ) ))
                return;
        super.startEditingAtPath( path );
    }
    public void setEditable(boolean flag){
        super.setEditable( flag );
        DefaultTreeCellEditor editor = new DefaultTreeCellEditor( this,( DefaultTreeCellRenderer ) this.getActualCellRenderer( ) ){
            public Component getTreeCellEditorComponent( JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row ){
                final KMLAbstractFeature feature = (KMLAbstractFeature)((DefaultMutableTreeNode)value).getUserObject( );
                final JTextField field = new JTextField( feature.getName( ) ); 
                field.addFocusListener( new FocusAdapter(){
                    public void focusLost( FocusEvent e ){
                        feature.setField( Support.KMLTag.name.name( ), field.getText( ) );
                }} );
                return field;
            }
        };
        setCellEditor( editor );
    }
    public void rename() {
       startEditingAtPath(getSelectionPath( ));
    }
    public void revertSelection( )
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)getSelectionPath( ).getLastPathComponent( );
        DefaultMutableTreeNode parent = ( DefaultMutableTreeNode ) node.getParent( );
        Object object = node.getUserObject( );
        if(object instanceof KMLDocument){
            try
            {
                InputStream inputStream = ((KMLDocument)object).getRoot( ).getKMLDoc( ).getKMLStream( );
                KMLRoot kmlRoot = KMLRoot.createAndParse( inputStream );
                inputStream.close( );
                removeSelection( );
                getModel().add( parent, kmlRoot.getFeature( ) );
            }
            catch ( Exception e1 )
            {
                e1.printStackTrace();
            }
        }

    }
}
