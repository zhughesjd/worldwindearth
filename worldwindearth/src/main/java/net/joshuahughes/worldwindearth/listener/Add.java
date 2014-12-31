package net.joshuahughes.worldwindearth.listener;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLRoot;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;

import net.joshuahughes.worldwindearth.WorldWindEarth;
import net.joshuahughes.worldwindearth.panel.EditorTreeModel;
import net.joshuahughes.worldwindearth.support.Support;

public enum Add implements Listener{
    Folder,Placemark,Path,Polygon,Model,Tour,Photo,Image_Overlay,Network_Link;
    private String tooltip;
    Add(){
        tooltip = (this.equals("Tour")?"Record a ":"Add ")+name();
    }
    public String getToolTip(){
        return tooltip;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        WorldWindEarth earth = WorldWindEarth.findWindow( (Component)e.getSource( ) );
        KMLAbstractFeature feature = create(this,earth.getViewer( ).getPosition( ));
        feature.setField(Support.KMLTag.name.name(),"Untitled"+name( ).replace('_', ' ') );
        feature.setField(Support.KMLTag.id.name(),earth.getPanel().getTreeMap().get(EditorTreeModel.Type.Places).getModel().createUniqueId());
        earth.edit( feature );
    }
    private static long id = 0;
    private KMLAbstractFeature create( Add add,Position position)
    {
        try
        {
        	// *****
        	// *****
        	// *****
        	//	Model:
        	//	http://earth-api-samples.googlecode.com/svn/trunk/examples/static/splotchy_box.dae
        	// *****
        	// *****
        	// *****
            String kmlString = "<kml>";
            if(Add.Folder.equals( add ))
                kmlString +="<Folder></Folder>";
            if(Add.Placemark.equals( add ))
                kmlString+="<Placemark><Point><coordinates>"+position.getLongitude( ).getDegrees( )+","+position.getLatitude( ).getDegrees( )+",0</coordinates></Point></Placemark>";
            if(Add.Path.equals( add ))
                kmlString+="<Placemark><LineString><tessellate>1</tessellate><coordinates></coordinates></LineString></Placemark>";
            if(Add.Polygon.equals( add ))
                kmlString+="<Placemark><Polygon><outerBoundaryIs><LinearRing><coordinates></coordinates></LinearRing></outerBoundaryIs></Polygon></Placemark>";
            if(Add.Photo.equals( add ))
                kmlString+="<PhotoOverlay></PhotoOverlay>";
            if(Add.Image_Overlay.equals( add ))
                kmlString+="<GroundOverlay><Icon><viewBoundScale>0.75</viewBoundScale></Icon><LatLonBox><north>31.46519058816173</north><south>26.94948039449266</south><east>-100.2544422612532</east><west>-105.4279090375434</west></LatLonBox></GroundOverlay>";
            if(Add.Network_Link.equals( add ))
                kmlString+="<NetworkLink><Link></Link></NetworkLink>";
            kmlString+="</kml>";
            ByteArrayInputStream bais = new ByteArrayInputStream(kmlString.getBytes( ));
            KMLAbstractFeature feature = KMLRoot.createAndParse(bais).getFeature( );
            feature.setField( Support.KMLTag.name.name( ), "Untitled "+add.name( ).replace( '_', ' ' ) );
            feature.setField( Support.KMLTag.id.name( ), ""+id++ );
            return feature;
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
        return null;
    }
}
