package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Position.PositionList;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLAbstractGeometry;
import gov.nasa.worldwind.ogc.kml.KMLAbstractOverlay;
import gov.nasa.worldwind.ogc.kml.KMLAbstractStyleSelector;
import gov.nasa.worldwind.ogc.kml.KMLAbstractTimePrimitive;
import gov.nasa.worldwind.ogc.kml.KMLAbstractView;
import gov.nasa.worldwind.ogc.kml.KMLAlias;
import gov.nasa.worldwind.ogc.kml.KMLBalloonStyle;
import gov.nasa.worldwind.ogc.kml.KMLCamera;
import gov.nasa.worldwind.ogc.kml.KMLData;
import gov.nasa.worldwind.ogc.kml.KMLDocument;
import gov.nasa.worldwind.ogc.kml.KMLExtendedData;
import gov.nasa.worldwind.ogc.kml.KMLFolder;
import gov.nasa.worldwind.ogc.kml.KMLGroundOverlay;
import gov.nasa.worldwind.ogc.kml.KMLIcon;
import gov.nasa.worldwind.ogc.kml.KMLIconStyle;
import gov.nasa.worldwind.ogc.kml.KMLImagePyramid;
import gov.nasa.worldwind.ogc.kml.KMLItemIcon;
import gov.nasa.worldwind.ogc.kml.KMLLabelStyle;
import gov.nasa.worldwind.ogc.kml.KMLLatLonAltBox;
import gov.nasa.worldwind.ogc.kml.KMLLatLonBox;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.ogc.kml.KMLLineStyle;
import gov.nasa.worldwind.ogc.kml.KMLLinearRing;
import gov.nasa.worldwind.ogc.kml.KMLLink;
import gov.nasa.worldwind.ogc.kml.KMLListStyle;
import gov.nasa.worldwind.ogc.kml.KMLLocation;
import gov.nasa.worldwind.ogc.kml.KMLLookAt;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLMultiGeometry;
import gov.nasa.worldwind.ogc.kml.KMLOrientation;
import gov.nasa.worldwind.ogc.kml.KMLPair;
import gov.nasa.worldwind.ogc.kml.KMLPhotoOverlay;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLPolyStyle;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.KMLRegion;
import gov.nasa.worldwind.ogc.kml.KMLResourceMap;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.KMLScale;
import gov.nasa.worldwind.ogc.kml.KMLSchemaData;
import gov.nasa.worldwind.ogc.kml.KMLScreenOverlay;
import gov.nasa.worldwind.ogc.kml.KMLSimpleData;
import gov.nasa.worldwind.ogc.kml.KMLStyle;
import gov.nasa.worldwind.ogc.kml.KMLStyleMap;
import gov.nasa.worldwind.ogc.kml.KMLTimeSpan;
import gov.nasa.worldwind.ogc.kml.KMLTimeStamp;
import gov.nasa.worldwind.ogc.kml.KMLVec2;
import gov.nasa.worldwind.ogc.kml.gx.GXPlaylist;
import gov.nasa.worldwind.ogc.kml.gx.GXTour;
import gov.nasa.worldwind.util.xml.atom.AtomPerson;
import gov.nasa.worldwind.util.xml.xal.XALAddressDetails;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import de.micromata.opengis.kml.v_2_2_0.AbstractView;
import de.micromata.opengis.kml.v_2_2_0.Alias;
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.BalloonStyle;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Camera;
import de.micromata.opengis.kml.v_2_2_0.ColorMode;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.DisplayMode;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.GridOrigin;
import de.micromata.opengis.kml.v_2_2_0.GroundOverlay;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.ImagePyramid;
import de.micromata.opengis.kml.v_2_2_0.ItemIcon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LabelStyle;
import de.micromata.opengis.kml.v_2_2_0.LatLonAltBox;
import de.micromata.opengis.kml.v_2_2_0.LatLonBox;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Link;
import de.micromata.opengis.kml.v_2_2_0.ListItemType;
import de.micromata.opengis.kml.v_2_2_0.ListStyle;
import de.micromata.opengis.kml.v_2_2_0.Location;
import de.micromata.opengis.kml.v_2_2_0.LookAt;
import de.micromata.opengis.kml.v_2_2_0.Model;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Orientation;
import de.micromata.opengis.kml.v_2_2_0.Overlay;
import de.micromata.opengis.kml.v_2_2_0.Pair;
import de.micromata.opengis.kml.v_2_2_0.PhotoOverlay;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import de.micromata.opengis.kml.v_2_2_0.RefreshMode;
import de.micromata.opengis.kml.v_2_2_0.Region;
import de.micromata.opengis.kml.v_2_2_0.ResourceMap;
import de.micromata.opengis.kml.v_2_2_0.Scale;
import de.micromata.opengis.kml.v_2_2_0.SchemaData;
import de.micromata.opengis.kml.v_2_2_0.ScreenOverlay;
import de.micromata.opengis.kml.v_2_2_0.Shape;
import de.micromata.opengis.kml.v_2_2_0.SimpleData;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import de.micromata.opengis.kml.v_2_2_0.StyleState;
import de.micromata.opengis.kml.v_2_2_0.TimePrimitive;
import de.micromata.opengis.kml.v_2_2_0.TimeSpan;
import de.micromata.opengis.kml.v_2_2_0.TimeStamp;
import de.micromata.opengis.kml.v_2_2_0.Units;
import de.micromata.opengis.kml.v_2_2_0.Vec2;
import de.micromata.opengis.kml.v_2_2_0.atom.Author;
import de.micromata.opengis.kml.v_2_2_0.gx.Playlist;
import de.micromata.opengis.kml.v_2_2_0.gx.Tour;
import de.micromata.opengis.kml.v_2_2_0.xal.AddressDetails;

public class Support {
	public static enum KMLTag{name,description, coordinates, id, futurevisibility, north,east,south,west, futurehref, href, xmlns, kml, MultiGeometry, Folder, targetId, namespaceURI}
	public static Document convert(KMLDocument get) {
		if(get == null) return null;
		Document set = new Document();
		synchronize(set, get);
		for(KMLAbstractFeature kmlFeature : get.getFeatures())
			set.getFeature().add(convert(kmlFeature));
		return set;
	}
	private static Folder convert(KMLFolder get) {
		if(get == null) return null;
		Folder set = new Folder();
		synchronize(set, get);
		for(KMLAbstractFeature kmlFeature : get.getFeatures())
			set.getFeature().add(convert(kmlFeature));
		return set;
	}
	public static ScreenOverlay convert(KMLScreenOverlay get){
		if(get == null) return null;
		ScreenOverlay set = new ScreenOverlay();
		synchronize(set, get);
		set.setOverlayXY(convert(get.getOverlayXY()));
		set.setRotationXY(convert(get.getRotationXY()));
		set.setScreenXY(convert(get.getScreenXY()));
		set.setSize(convert(get.getSize()));
		return set;
	}
	public static Vec2 convert(KMLVec2 get) {
		if(get == null) return null;
		Vec2 set = new Vec2();
		synchronize(set, get);
		set.setXunits(convert(Units.class,get.getXunits()));
		set.setYunits(convert(Units.class,get.getYunits()));
		return set;
	}
	public static GroundOverlay convert(KMLGroundOverlay get){
		if(get == null) return null;
		GroundOverlay set = new GroundOverlay();
		synchronize(set, get);
		set.setAltitudeMode(convert(AltitudeMode.class,get.getAltitudeMode()));
		set.setLatLonBox(convert(get.getLatLonBox()));
		return set;
	}
	public static PhotoOverlay convert(KMLPhotoOverlay get){
		if(get == null) return null;
		PhotoOverlay set = new PhotoOverlay();
		synchronize(set, get);
		set.setImagePyramid(convert(get.getImagePyramid()));
		set.setPoint(convert(get.getPoint()));
		set.setShape(convert(Shape.class,get.getShape()));
		return set;
	}
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E convert(Class<E> enumT,String get){
		if(get == null) return null;
		try {
			Method method = enumT.getMethod("fromValue", String.class);
			return (E) method.invoke(null,get);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static ImagePyramid convert(KMLImagePyramid get) {
		if(get == null) return null;
		ImagePyramid set = new ImagePyramid();
		synchronize(set, get);
		set.setGridOrigin(convert(GridOrigin.class,get.getGridOrigin()));
		return set;
	}
	private static Icon convert(KMLIcon get) {
		if(get == null) return null;
		Icon set = new Icon();
		synchronize(set, get);
		set.setRefreshMode(convert(RefreshMode.class,get.getRefreshMode()));
		return set;
	}
	private static LatLonBox convert(KMLLatLonBox get) {
		if(get == null) return null;
		LatLonBox set = new LatLonBox();
		synchronize(set, get);
		return set;
	}
	private static Tour convert(GXTour get){
		if(get == null) return null;
		Tour set = new Tour();
		synchronize(set, get);
		set.setPlaylist(convert(get.getPlaylist()));
		return set;
	}
	private static Playlist convert(GXPlaylist get) {
		if(get == null) return null;
		Playlist set = new Playlist();
		synchronize(set, get);
		return set;
	}
	public static Kml convert(KMLRoot get){
		if(get == null) return null;
		Kml set = new Kml();
		synchronize(set, get);
		set.setFeature(convert(get.getFeature()));
		return set;
	}
	public static Feature convert(KMLAbstractFeature get){
		if(get instanceof KMLPlacemark)
			return convert((KMLPlacemark)get);
		if(get instanceof KMLFolder)
			return convert((KMLFolder)get);
		if(get instanceof KMLDocument)
			return convert((KMLDocument)get);
		if(get instanceof KMLGroundOverlay)
			return convert((KMLGroundOverlay)get);
		if(get instanceof KMLPhotoOverlay)
			return convert((KMLPhotoOverlay)get);
		if(get instanceof KMLScreenOverlay)
			return convert((KMLScreenOverlay)get);
		if(get instanceof GXTour)
			return convert((GXTour)get);
		return null;
	}
	public static final LinkedHashMap<Class<?>,Class<?>> primitiveMap = new LinkedHashMap<>( );
	static{

		primitiveMap.put( boolean.class, Boolean.class); 
		primitiveMap.put( byte.class, Byte.class );
		primitiveMap.put( char.class, Character.class ); 
		primitiveMap.put( float.class, Float.class ); 
		primitiveMap.put( int.class, Integer.class ); 
		primitiveMap.put( long.class, Long.class ); 
		primitiveMap.put( short.class, Short.class ); 
		primitiveMap.put( double.class, Double.class ); 
		primitiveMap.put( String.class, String.class ); 
	}
	public static void synchronize(Object set,Object get){
		for(Method sm : set.getClass( ).getMethods( )){
			if(!sm.getName().startsWith("set")) continue;
			String setName = sm.getName().substring(3).toLowerCase();
			for(Method gm : get.getClass( ).getMethods( )){
				if(!gm.getName().startsWith("get")) continue;
				String getName = gm.getName().substring(3).toLowerCase();
				if(!setName.equals(getName))continue;
				for(Entry<Class<?>, Class<?>> e : primitiveMap.entrySet( )){
					if(
							(sm.getParameterTypes( )[0].equals( e.getKey( ) ) && gm.getReturnType().equals( e.getKey( ) )) || 
							(sm.getParameterTypes( )[0].equals( e.getValue( ) ) && gm.getReturnType().equals( e.getValue( ) )) || 
							(sm.getParameterTypes( )[0].equals( e.getKey( ) ) && gm.getReturnType().equals( e.getValue( ) )) || 
							(gm.getReturnType( ).equals( e.getKey( ) ) && sm.getParameterTypes( )[0].equals( e.getValue( ) )) )
					{
						try {
							Object test = gm.invoke( get );
							if(test!=null)
								sm.invoke( set, test );
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						continue;
					}
				}
			}
		}
		if(set instanceof Feature && get instanceof KMLAbstractFeature){
			Feature sf = (Feature) set;
			KMLAbstractFeature gf = (KMLAbstractFeature) get;
			sf.setAbstractView(convert(gf.getView()));
			sf.setAtomAuthor(convert(gf.getAuthor()));
			sf.setExtendedData(convert(gf.getExtendedData()));
			if(gf.getStyleUrl()!=null)sf.setStyleUrl(gf.getStyleUrl().getCharacters());
			for(KMLAbstractStyleSelector style : gf.getStyleSelectors( )){
				sf.getStyleSelector( ).add( convert(style) );
			}
			sf.setXalAddressDetails(convert(gf.getAddressDetails()));
		}
		if(set instanceof Overlay && get instanceof KMLAbstractOverlay){
			Overlay so = (Overlay) set;
			KMLAbstractOverlay go = (KMLAbstractOverlay) get;
			so.setIcon(convert(go.getIcon()));
		}
	}
	private static StyleSelector convert( KMLAbstractStyleSelector get )
	{
		return get instanceof KMLStyle?convert((KMLStyle)get):convert((KMLStyleMap)get);
	}
	public static Style convert(KMLStyle get){
		if(get == null) return null;
		Style set = new Style();
		synchronize(set, get);
		set.setBalloonStyle( convert(get.getBaloonStyle( )) );
		set.setIconStyle( convert(get.getIconStyle( )) );
		set.setLabelStyle( convert(get.getLabelStyle( )) );
		set.setLineStyle( convert(get.getLineStyle( )) );
		set.setListStyle( convert(get.getListStyle( )) );
		set.setPolyStyle( convert(get.getPolyStyle( )) );
		return set;
	}
	private static PolyStyle convert( KMLPolyStyle get )
	{
		if(get == null) return null;
		PolyStyle set = new PolyStyle();
		synchronize(set, get);
		set.setColorMode( convert(ColorMode.class,get.getColorMode( )) );
		return set;
	}
	private static ListStyle convert( KMLListStyle get )
	{
		if(get == null) return null;
		ListStyle set = new ListStyle();
		synchronize(set, get);
		for(KMLItemIcon kmlIcon : get.getItemIcons( ))
			set.getItemIcon( ).add( convert(kmlIcon) );
		set.setListItemType( convert(ListItemType.class,get.getListItemType( )) );
		return set;
	}
	private static ItemIcon convert( KMLItemIcon get )
	{
		if(get == null) return null;
		ItemIcon set = new ItemIcon();
		synchronize(set, get);
		return set;
	}
	private static LineStyle convert( KMLLineStyle get )
	{
		if(get == null) return null;
		LineStyle set = new LineStyle();
		synchronize(set, get);
		set.setColorMode( convert(ColorMode.class,get.getColorMode( )) );
		return set;
	}
	private static LabelStyle convert( KMLLabelStyle get )
	{
		if(get == null) return null;
		LabelStyle set = new LabelStyle();
		synchronize(set, get);
		set.setColorMode( convert(ColorMode.class,get.getColorMode( )) );
		return set;
	}
	private static IconStyle convert( KMLIconStyle get )
	{
		if(get == null) return null;
		IconStyle set = new IconStyle();
		synchronize(set, get);
		set.setColorMode( convert(ColorMode.class,get.getColorMode( )) );
		set.setHotSpot( convert(get.getHotSpot( )) );
		set.setIcon( convert(get.getIcon( )) );
		return set;
	}
	private static BalloonStyle convert( KMLBalloonStyle get )
	{
		if(get == null) return null;
		BalloonStyle set = new BalloonStyle();
		synchronize(set, get);
		set.setDisplayMode( convert(DisplayMode.class,get.getDisplayMode( )) );
		return set;
	}
	public static StyleMap convert(KMLStyleMap get){
		if(get == null) return null;
		StyleMap set = new StyleMap();
		synchronize(set, get);
		for(KMLPair kmlPair : get.getPairs( ))
			set.getPair( ).add( convert(kmlPair) );
		return set;
	}
	private static Pair convert( KMLPair get )
	{
		if(get == null) return null;
		Pair set = new Pair();
		synchronize(set, get);
		set.setKey( convert(StyleState.class,get.getKey( )) );
		set.setStyleSelector( convert(get.getStyleSelector( )) );
		return set;
	}
	private static AddressDetails convert(XALAddressDetails get) {
		if(get == null) return null;
		return null;
	}

	public static Placemark convert(KMLPlacemark get){
		if(get == null) return null;
		Placemark set = new Placemark();
		synchronize(set, get);
		set.setGeometry(convert(get.getGeometry()));
		set.setRegion(convert(get.getRegion()));
		set.setSnippetd(get.getSnippetText());
		set.setTimePrimitive(convert(get.getTimePrimitive()));
		return set;
	}
	public static TimePrimitive convert(KMLAbstractTimePrimitive kmlTime) {
		if(kmlTime instanceof KMLTimeSpan )
			return convert((KMLTimeSpan)kmlTime);
		else
			return convert((KMLTimeStamp)kmlTime);
	}
	public static TimeStamp convert(KMLTimeStamp get) {
		if(get == null) return null;
		TimeStamp set = new TimeStamp();
		synchronize(set, get);
		return set;
	}
	public static TimeSpan convert(KMLTimeSpan get) {
		if(get == null) return null;
		TimeSpan set = new TimeSpan();
		synchronize(set, get);
		return set;
	}
	private static Region convert(KMLRegion get) {
		if(get == null) return null;
		Region set = new Region();
		synchronize(set, get);
		set.setLatLonAltBox(convert(get.getLatLonAltBox()));
		return set;
	}
	private static LatLonAltBox convert(KMLLatLonAltBox get) {
		if(get == null) return null;
		LatLonAltBox set = new LatLonAltBox();
		synchronize(set, get);
		set.setAltitudeMode(convert(AltitudeMode.class,get.getAltitudeMode()));
		return set;
	}
	public static Geometry convert(KMLAbstractGeometry get){
		if(get == null) return null;
		Geometry set = null;
		if(get instanceof KMLPoint) set = convert((KMLPoint)get);
		if(get instanceof KMLLinearRing) set = convert((KMLLinearRing)get);
		if(get instanceof KMLPolygon) set = convert((KMLPolygon)get);
		if(get instanceof KMLModel) set = convert((KMLModel)get);
		if(get instanceof KMLLineString) set = convert((KMLLineString)get);
		if(get instanceof KMLMultiGeometry) set = convert((KMLMultiGeometry)get);

		return set;
	}
	public static ExtendedData convert(KMLExtendedData get){
		if(get == null) return null;
		ExtendedData set = new ExtendedData();
		synchronize(set, get);
		for(KMLData kmlData : get.getData())
			set.getData().add(convert(kmlData));
		for(KMLSchemaData kmlSchema : get.getSchemaData())
			set.getSchemaData().add(convert(kmlSchema));
		return set;
	}
	public static SimpleData convert(KMLSimpleData get){
		if(get == null) return null;
		SimpleData set = new SimpleData(get.getCharacters());
		synchronize(set, get);
		return set;
	}
	public static SchemaData convert(KMLSchemaData get){
		if(get == null) return null;
		SchemaData set = new SchemaData();
		synchronize(set, get);
		for(KMLSimpleData kmlData : get.getSimpleData())
			set.getSimpleData().add(convert(kmlData));
		return set;
	}
	public static Data convert(KMLData get){
		if(get == null) return null;
		Data set = new Data(get.getValue());
		synchronize(set, get);
		return set;
	}
	public static Link convert(KMLLink get){
		if(get == null) return null;
		Link set = new Link();
		synchronize(set, get);
		set.setRefreshMode(RefreshMode.valueOf(get.getRefreshMode().trim()));
		return set;
	}
	public static Author convert(AtomPerson get){
		if(get == null) return null;
		Author set = new Author();
		synchronize(set, get);
		if(get.getName()!=null)set.addToNameOrUriOrEmail(get.getName());
		if(get.getUri()!=null)set.addToNameOrUriOrEmail(get.getUri());
		if(get.getEmail()!=null)set.addToNameOrUriOrEmail(get.getEmail());
		return set;
	}
	public static AbstractView convert(KMLAbstractView get){
		if(get instanceof KMLLookAt)
			return convert((KMLLookAt)get);
		else 
			return convert((KMLCamera)get);
	}
	public static LookAt convert(KMLLookAt get){
		if(get == null) return null;
		LookAt set = new LookAt();
		synchronize(set, get);
		set.setAltitudeMode(null);
		set.setAltitudeMode(convert(AltitudeMode.class,get.getAltitudeMode()));
		return set;
	}
	public static Camera convert(KMLCamera get){
		if(get == null) return null;
		Camera set = new Camera();
		synchronize(set, get);
		set.setAltitudeMode(convert(AltitudeMode.class,get.getAltitudeMode()));
		return set;
	}
	public static Polygon convert(KMLPolygon get){
		if(get == null) return null;
		Polygon set = new Polygon();
		synchronize(set, get);
		set.setAltitudeMode(convert(AltitudeMode.class,get.getAltitudeMode()));
		if(get.getInnerBoundaries()!=null)
			for(KMLLinearRing kmlBoundary : get.getInnerBoundaries()){
				Boundary boundary = new Boundary();
				boundary.setLinearRing(convert(kmlBoundary));
				set.getInnerBoundaryIs().add(boundary);
			}
		Boundary boundary = new Boundary();
		boundary.setLinearRing(convert(get.getOuterBoundary()));
		set.setOuterBoundaryIs(boundary);
		return set;
	}
	public static MultiGeometry convert(KMLMultiGeometry get){
		if(get == null) return null;
		MultiGeometry set = new MultiGeometry();
		synchronize(set, get);
		for(KMLAbstractGeometry kmlGeom :get.getGeometries())
			set.getGeometry().add(convert(kmlGeom));
		return set;

	}
	public static Model convert(KMLModel get){
		if(get == null) return null;
		Model set = new Model();
		synchronize(set, get);
		set.setAltitudeMode(convert(AltitudeMode.class,get.getAltitudeMode()));
		set.setLink(convert(get.getLink()));
		set.setLocation(convert(get.getLocation()));
		set.setOrientation(convert(get.getOrientation()));
		set.setResourceMap(convert(get.getResourceMap()));
		set.setScale(convert(get.getScale()));
		return set;
	}
	private static Scale convert(KMLScale get) {
		if(get == null) return null;
		Scale set = new Scale();
		synchronize(set, get);
		return set;
	}
	private static ResourceMap convert(KMLResourceMap get) {
		if(get == null) return null;
		ResourceMap set = new ResourceMap();
		synchronize(set, get);
		for(KMLAlias kmlAlias : get.getAliases())
			set.getAlias().add(convert(kmlAlias));
		return set;
	}
	private static Alias convert(KMLAlias get) {
		if(get == null) return null;
		Alias set = new Alias();
		synchronize(set, get);
		return set;
	}
	private static Orientation convert(KMLOrientation get) {
		if(get == null) return null;
		Orientation set = new Orientation();
		synchronize(set, get);
		return set;
	}
	private static Location convert(KMLLocation get) {
		if(get == null) return null;
		Location set = new Location();
		synchronize(set, get);
		return set;
	}
	public static LineString convert(KMLLineString get){
		if(get == null) return null;
		LineString set = new LineString();
		synchronize(set, get);
		set.setAltitudeMode(convert(AltitudeMode.class,get.getAltitudeMode()));
		set.setCoordinates(convert(get.getCoordinates()));
		return set;
	}
	public static LinearRing convert(KMLLinearRing get){
		if(get == null) return null;
		LinearRing set = new LinearRing();
		synchronize(set, get);
		set.setAltitudeMode(convert(AltitudeMode.class,get.getAltitudeMode()));
		set.setCoordinates(convert(get.getCoordinates()));
		return set;
	}
	public static Point convert(KMLPoint get){
		if(get == null) return null;
		Point set = new Point();
		synchronize(set, get);
		set.setCoordinates(Collections.singletonList(convert(get.getCoordinates())));
		return set;
	}
	public static List<Coordinate> convert(PositionList positList){
		if(positList == null) return null;
		ArrayList<Coordinate> list = new ArrayList<Coordinate>();
		for(Position posit : positList.list)
			list.add(new Coordinate(posit.getLongitude().getDegrees(), posit.getLatitude().getDegrees(), posit.getAltitude()));
		return list;
	}
	public static Coordinate convert(Position position){
		if(position==null)return null;
		return new Coordinate(position.getLongitude().getDegrees(),position.getLatitude().getDegrees(),position.getAltitude());
	}
	public static boolean nameTest(Method sm, Method gm) {
		if(!sm.getName().startsWith("set") && !gm.getName().startsWith("get"))return false;
		if(sm.getName().equals("setOuterBoundaryIs") && gm.getName().equals("getOuterBoundary"))return true;
		if(sm.getName().equals("setAbstractView") && gm.getName().equals("getView")) return true;
		return sm.getName( ).equals( "s"+gm.getName( ).substring( 1, gm.getName( ).length( ) ) );
	}
	public static KMLPoint createPoint(double lat,double lon){
	    return createPoint(Position.fromDegrees( lat, lon ));
	}
	public static KMLAbstractFeature clone(KMLAbstractFeature feature) {
		Kml kml = new Kml();
		kml.setFeature(convert(feature));
		StringWriter writer = new StringWriter();
		kml.marshal(writer);
		try {
			return KMLRoot.createAndParse(new ByteArrayInputStream(writer.toString().getBytes())).getFeature();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    public static KMLPoint createPoint( Position position )
    {
        try
        {
            KMLPoint point = (KMLPoint) (( KMLPlacemark ) KMLRoot.createAndParse(new ByteArrayInputStream(("<kml><Placemark><name>Placemark</name><Point><coordinates>"+position.getLongitude( ).getDegrees( )+","+position.getLatitude( ).getDegrees( )+",0</coordinates></Point></Placemark></kml>").getBytes( ))).getFeature( )).getGeometry();
            point.setField( Support.KMLTag.coordinates.name( ), point );
            return point;
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
        return null;
    }
	public static BufferedImage invalidImage() {
		BufferedImage image = new BufferedImage(100,100,BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setBackground(Color.lightGray);
		g2d.clearRect(0, 0, image.getWidth(),image.getHeight());
		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(.1f*image.getWidth()));
		g2d.drawRect(0,0, image.getWidth(), image.getHeight());
		g2d.setFont(new Font("Serif", Font.BOLD,50));
		g2d.drawString("X",.5f,.5f);
		return image;
	}
}
