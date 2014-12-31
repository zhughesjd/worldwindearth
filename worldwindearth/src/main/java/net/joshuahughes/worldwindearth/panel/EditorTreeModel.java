package net.joshuahughes.worldwindearth.panel;

import gov.nasa.worldwind.ogc.kml.KMLAbstractContainer;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLDocument;
import gov.nasa.worldwind.ogc.kml.KMLFolder;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.joshuahughes.worldwindearth.support.Support;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;

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
	private static File getFile(){
		File directory = new File(System.getProperty("user.home")+"\\.worldwindjava\\");
		if(!directory.exists())directory.mkdirs();
		return new File(directory.getAbsolutePath()+"\\doc.kml");
	}
	private static DefaultMutableTreeNode create( Type type )
	{   
		try
		{
			String kmlString="<kml><Folder><name>"+type.name( )+"</name></Folder></kml>";
			KMLRoot root = KMLRoot.createAndParse(new ByteArrayInputStream(kmlString.getBytes( )));
			KMLFolder rootFolder = (KMLFolder) root.getFeature();
			Type[] children = Type.Places.equals( type )?new Type[]{Type.MyPlaces,Type.TemporaryPlaces}:Type.Layers.equals( type )?new Type[]{Type.PrimaryDatabase}:new Type[]{};
			for(Type childType : children){
				KMLAbstractContainer container = new KMLFolder(rootFolder.getNamespaceURI());
				if(childType.equals( Type.MyPlaces )){
					File file = getFile();
					if(!file.exists()){
						container = new KMLDocument(rootFolder.getNamespaceURI());
						container.setField(Support.KMLTag.name.name(),childType.name());
						Feature containerToSave = Support.convert(container);
						Kml kml = new Kml();
						kml.setFeature(containerToSave);
						FileOutputStream fos = new FileOutputStream(file);
						kml.marshal(fos);
						fos.close();
					}
					try{
						container = (KMLAbstractContainer) KMLRoot.createAndParse(file).getFeature();
					}catch(Exception e){
						file.delete();
						file = getFile();
						container = new KMLDocument(rootFolder.getNamespaceURI());
						container.setField(Support.KMLTag.name.name(),childType.name());
						Feature containerToSave = Support.convert(container);
						Kml kml = new Kml();
						kml.setFeature(containerToSave);
						FileOutputStream fos = new FileOutputStream(file);
						kml.marshal(fos);
						fos.close();
						container = (KMLAbstractContainer) KMLRoot.createAndParse(file).getFeature();
					}
				}
				container.setField(Support.KMLTag.name.name(),childType.name());
				rootFolder.addFeature(container);
			}
			return create(rootFolder);
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
	public void save() {
		Document document = Support.convert((KMLDocument)((DefaultMutableTreeNode)((DefaultMutableTreeNode)getRoot()).getFirstChild()).getUserObject());
		Kml kml = new Kml();
		kml.setFeature(document);
		try {
			kml.marshal(getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public String createUniqueId() {
		return (max(getRoot(),0)+1)+"";
	}
	private int max(DefaultMutableTreeNode node, int currentMax) {
		KMLAbstractFeature feature = (KMLAbstractFeature) node.getUserObject();
		Object id  = feature.getField(Support.KMLTag.id.name());
		if(id!=null){
		    try{
		        int intId = Integer.parseInt( id.toString( ) );
	            currentMax = Math.max(currentMax, intId);
		    }catch(Exception e){
		        
		    }
		}
		Enumeration<?> enumeration = node.children();
		while(enumeration.hasMoreElements())
			currentMax = Math.max(currentMax, max((DefaultMutableTreeNode) enumeration.nextElement(),currentMax));
		return currentMax;
	}
	public void remove(DefaultMutableTreeNode node,String deleteId) {
		KMLAbstractFeature feature = (KMLAbstractFeature) node.getUserObject();
		Object id  = feature.getField(Support.KMLTag.id.name());
		if(id!=null && id instanceof String && id.equals(deleteId)){
			this.removeNodeFromParent(node);
			return;
		}
		Enumeration<?> enumeration = node.children();
		while(enumeration.hasMoreElements())
			remove((DefaultMutableTreeNode) enumeration.nextElement(),deleteId);
	}
}
