package net.joshuahughes.worldwindearth.panel;

import gov.nasa.worldwind.ogc.kml.KMLAbstractContainer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLFolder;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.io.ByteArrayInputStream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class EditorTreeModel extends DefaultTreeModel{
    public enum Type{MyPlaces,TemporaryPlaces,PrimaryDatabase,Search,Places,Layers}
    private static final long serialVersionUID = -1910087161141056231L;
    private static DefaultMutableTreeNode rootNode;
    private KMLRoot kmlRoot; 
    public EditorTreeModel(Type type) {
        super(rootNode = create(type));
        this.kmlRoot = ((KMLFolder)rootNode.getUserObject()).getRoot();
    }
    public DefaultMutableTreeNode getRoot(){
        return ( DefaultMutableTreeNode ) super.getRoot( );
    }
    private static DefaultMutableTreeNode create( Type type )
    {   
        String kmlString="<kml><Folder>";
        kmlString+="<name>"+type.name( )+"</name>";
        Type[] children = Type.Places.equals( type )?new Type[]{Type.MyPlaces,Type.TemporaryPlaces}:Type.Layers.equals( type )?new Type[]{Type.PrimaryDatabase}:new Type[]{};
        for(Type childType : children){
            String container = childType.equals( Type.MyPlaces )?"Document":"Folder";
            kmlString+="<"+container+"><name>"+childType.name( )+"</name></"+container+">";
        }
        kmlString+="</Folder></kml>";
        try
        {
            KMLRoot root = KMLRoot.createAndParse(new ByteArrayInputStream(kmlString.getBytes( )));
            return create(root.getFeature( ));
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
        
        return null;

    }
    private static DefaultMutableTreeNode create( KMLAbstractFeature feature )
    {
        
        DefaultMutableTreeNode featureNode = new DefaultMutableTreeNode( feature );
        if(feature instanceof KMLAbstractContainer)
            for(KMLAbstractFeature child : (( KMLAbstractContainer ) feature).getFeatures( ))
                featureNode.add( create(child) );
        return featureNode ;
    }
    public void add(KMLAbstractFeature feature){
        DefaultMutableTreeNode parent = ( DefaultMutableTreeNode ) getRoot();
        if(parent.getChildCount( )>0) parent = ( DefaultMutableTreeNode ) parent.getLastChild( );
        add(parent,feature);
    }
    public void add(DefaultMutableTreeNode parent,KMLAbstractFeature feature){
        addKML(parent,feature);
        addTree(parent,feature);
        kmlRoot.requestRedraw();
    }
    private void addKML(DefaultMutableTreeNode parent,KMLAbstractFeature feature){
        ((KMLAbstractContainer)parent.getUserObject()).addFeature(feature);
    }
    private void addTree(DefaultMutableTreeNode parent,KMLAbstractFeature feature){
        if(!parent.getAllowsChildren( )) parent = ( DefaultMutableTreeNode ) parent.getParent( );
        if(feature instanceof KMLAbstractContainer){
            KMLAbstractContainer kmlContainer = (KMLAbstractContainer)feature;
            DefaultMutableTreeNode container = new DefaultMutableTreeNode(kmlContainer,true);
            insertNodeInto(container,parent,parent.getChildCount());
            for(KMLAbstractFeature kmlChild : kmlContainer.getFeatures())
                addTree(container,kmlChild);
        }
        else
            insertNodeInto(new DefaultMutableTreeNode(feature,false), parent, parent.getChildCount());
    }
    public void removeNodeFromParent( DefaultMutableTreeNode node )
    {
        KMLAbstractFeature feature = ( KMLAbstractFeature ) node.getUserObject( );
        KMLAbstractContainer container = ( KMLAbstractContainer ) ((DefaultMutableTreeNode)node.getParent( )).getUserObject( );
        container.removeFeature( feature );
        super.removeNodeFromParent( node );
    }
}
