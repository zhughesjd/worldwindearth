package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Position.PositionList;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLAbstractGeometry;
import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLAbstractTimePrimitive;
import gov.nasa.worldwind.ogc.kml.KMLAbstractView;
import gov.nasa.worldwind.ogc.kml.KMLAlias;
import gov.nasa.worldwind.ogc.kml.KMLCamera;
import gov.nasa.worldwind.ogc.kml.KMLData;
import gov.nasa.worldwind.ogc.kml.KMLDocument;
import gov.nasa.worldwind.ogc.kml.KMLExtendedData;
import gov.nasa.worldwind.ogc.kml.KMLFolder;
import gov.nasa.worldwind.ogc.kml.KMLLatLonAltBox;
import gov.nasa.worldwind.ogc.kml.KMLLineString;
import gov.nasa.worldwind.ogc.kml.KMLLinearRing;
import gov.nasa.worldwind.ogc.kml.KMLLink;
import gov.nasa.worldwind.ogc.kml.KMLLocation;
import gov.nasa.worldwind.ogc.kml.KMLLookAt;
import gov.nasa.worldwind.ogc.kml.KMLModel;
import gov.nasa.worldwind.ogc.kml.KMLMultiGeometry;
import gov.nasa.worldwind.ogc.kml.KMLOrientation;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;
import gov.nasa.worldwind.ogc.kml.KMLPoint;
import gov.nasa.worldwind.ogc.kml.KMLPolygon;
import gov.nasa.worldwind.ogc.kml.KMLRegion;
import gov.nasa.worldwind.ogc.kml.KMLResourceMap;
import gov.nasa.worldwind.ogc.kml.KMLScale;
import gov.nasa.worldwind.ogc.kml.KMLSchemaData;
import gov.nasa.worldwind.ogc.kml.KMLSimpleData;
import gov.nasa.worldwind.ogc.kml.KMLTimeSpan;
import gov.nasa.worldwind.ogc.kml.KMLTimeStamp;
import gov.nasa.worldwind.util.xml.atom.AtomPerson;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import de.micromata.opengis.kml.v_2_2_0.AbstractObject;
import de.micromata.opengis.kml.v_2_2_0.AbstractView;
import de.micromata.opengis.kml.v_2_2_0.Alias;
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Camera;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.LatLonAltBox;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Link;
import de.micromata.opengis.kml.v_2_2_0.Location;
import de.micromata.opengis.kml.v_2_2_0.LookAt;
import de.micromata.opengis.kml.v_2_2_0.Model;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Orientation;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import de.micromata.opengis.kml.v_2_2_0.RefreshMode;
import de.micromata.opengis.kml.v_2_2_0.Region;
import de.micromata.opengis.kml.v_2_2_0.ResourceMap;
import de.micromata.opengis.kml.v_2_2_0.Scale;
import de.micromata.opengis.kml.v_2_2_0.SchemaData;
import de.micromata.opengis.kml.v_2_2_0.SimpleData;
import de.micromata.opengis.kml.v_2_2_0.TimePrimitive;
import de.micromata.opengis.kml.v_2_2_0.TimeSpan;
import de.micromata.opengis.kml.v_2_2_0.TimeStamp;
import de.micromata.opengis.kml.v_2_2_0.atom.Author;

public class Support {
	public static List<String> validNames = Arrays.asList("getGeometry");
	public static enum KMLTag{name,description, coordinates, id}
	public static AbstractObject get(KMLAbstractObject kmlObject) {
		if(kmlObject instanceof KMLPlacemark) return convert((KMLPlacemark)kmlObject);
		if(kmlObject instanceof KMLFolder) return convert((KMLFolder)kmlObject);
		if(kmlObject instanceof KMLDocument) return convert((KMLDocument)kmlObject);
		return null;
	}
	public static Document convert(KMLDocument get) {
		if(get == null) return null;
		Document set = new Document();
		setPrimitives(set, get);
		synchronize(set, get);
		for(KMLAbstractFeature kmlFeature : get.getFeatures())
			set.getFeature().add(convert(kmlFeature));
		return set;
	}
	private static Folder convert(KMLFolder get) {
		if(get == null) return null;
		Folder set = new Folder();
		setPrimitives(set, get);
		synchronize(set, get);
		for(KMLAbstractFeature kmlFeature : get.getFeatures())
			set.getFeature().add(convert(kmlFeature));
		return set;
	}
	private static Feature convert(KMLAbstractFeature set){
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
	}
	public static void setPrimitives(Object set,Object get){
		for(Method sm : set.getClass( ).getMethods( )){
			if(!sm.getName().startsWith("set")) continue;
			String setName = sm.getName().substring(3).toLowerCase();
			for(Method gm : get.getClass( ).getMethods( )){
				if(!gm.getName().startsWith("get")) continue;
				String getName = gm.getName().substring(3).toLowerCase();
				if(!setName.contains(getName) && !getName.contains(setName))continue;
				for(Entry<Class<?>, Class<?>> e : primitiveMap.entrySet( )){
					if((sm.getParameterTypes( )[0].equals( e.getKey( ) ) && gm.getReturnType().equals( e.getValue( ) )) || (gm.getReturnType( ).equals( e.getKey( ) ) && sm.getParameterTypes( )[0].equals( e.getValue( ) )) ){
						try {
							sm.invoke( set, gm.invoke( get ) );
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						continue;
					}
				}
			}
		}

	}
	public static void synchronize(Feature set,KMLAbstractFeature get){
		set.setAbstractView(convert(get.getView()));
		set.setAtomAuthor(convert(get.getAuthor()));
		set.setExtendedData(convert(get.getExtendedData()));
		set.setStyleUrl(get.getStyleUrl().getCharacters());
	}
	public static Placemark convert(KMLPlacemark get){
		if(get == null) return null;
		Placemark set = new Placemark();
		setPrimitives(set, get);
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
		setPrimitives(set, get);
		return set;
	}
	public static TimeSpan convert(KMLTimeSpan get) {
		if(get == null) return null;
		TimeSpan set = new TimeSpan();
		setPrimitives(set, get);
		return set;
	}
	private static Region convert(KMLRegion get) {
		if(get == null) return null;
		Region set = new Region();
		setPrimitives(set, get);
		set.setLatLonAltBox(convert(get.getLatLonAltBox()));
		return set;
	}
	private static LatLonAltBox convert(KMLLatLonAltBox get) {
		if(get == null) return null;
		LatLonAltBox set = new LatLonAltBox();
		setPrimitives(set, get);
		if(get.getAltitudeMode()!=null)
			set.setAltitudeMode(AltitudeMode.fromValue(get.getAltitudeMode().trim()));
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
		setPrimitives(set, get);
		for(KMLData kmlData : get.getData())
			set.getData().add(convert(kmlData));
		for(KMLSchemaData kmlSchema : get.getSchemaData())
			set.getSchemaData().add(convert(kmlSchema));
		return set;
	}
	public static SimpleData convert(KMLSimpleData get){
		if(get == null) return null;
		SimpleData set = new SimpleData(get.getCharacters());
		setPrimitives(set, get);
		return set;
	}
	public static SchemaData convert(KMLSchemaData get){
		if(get == null) return null;
		SchemaData set = new SchemaData();
		setPrimitives(set, get);
		for(KMLSimpleData kmlData : get.getSimpleData())
			set.getSimpleData().add(convert(kmlData));
		return set;
	}
	public static Data convert(KMLData get){
		if(get == null) return null;
		Data set = new Data(get.getValue());
		setPrimitives(set, get);
		return set;
	}
	public static Link convert(KMLLink get){
		if(get == null) return null;
		Link set = new Link();
		setPrimitives(set, get);
		set.setRefreshMode(RefreshMode.valueOf(get.getRefreshMode().trim()));
		return set;
	}
	public static Author convert(AtomPerson get){
		if(get == null) return null;
		Author set = new Author();
		setPrimitives(set, get);
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
		setPrimitives(set, get);
		if(get.getAltitudeMode()!=null)
			set.setAltitudeMode(AltitudeMode.fromValue(get.getAltitudeMode().trim()));
		return set;
	}
	public static Camera convert(KMLCamera get){
		if(get == null) return null;
		Camera set = new Camera();
		setPrimitives(set, get);
		if(get.getAltitudeMode()!=null)
			set.setAltitudeMode(AltitudeMode.fromValue(get.getAltitudeMode().trim()));
		return set;
	}
	public static Polygon convert(KMLPolygon get){
		if(get == null) return null;
		Polygon set = new Polygon();
		setPrimitives(set, get);
		if(get.getAltitudeMode()!=null)
			set.setAltitudeMode(AltitudeMode.fromValue(get.getAltitudeMode().trim()));
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
		setPrimitives(set, get);
		for(KMLAbstractGeometry kmlGeom :get.getGeometries())
			set.getGeometry().add(convert(kmlGeom));
		return set;

	}
	public static Model convert(KMLModel get){
		if(get == null) return null;
		Model set = new Model();
		setPrimitives(set, get);
		if(get.getAltitudeMode()!=null)
			set.setAltitudeMode(AltitudeMode.fromValue(get.getAltitudeMode().trim()));
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
		setPrimitives(set, get);
		return set;
	}
	private static ResourceMap convert(KMLResourceMap get) {
		if(get == null) return null;
		ResourceMap set = new ResourceMap();
		setPrimitives(set, get);
		for(KMLAlias kmlAlias : get.getAliases())
			set.getAlias().add(convert(kmlAlias));
		return set;
	}
	private static Alias convert(KMLAlias get) {
		if(get == null) return null;
		Alias set = new Alias();
		setPrimitives(set, get);
		return set;
	}
	private static Orientation convert(KMLOrientation get) {
		if(get == null) return null;
		Orientation set = new Orientation();
		setPrimitives(set, get);
		return set;
	}
	private static Location convert(KMLLocation get) {
		if(get == null) return null;
		Location set = new Location();
		setPrimitives(set, get);
		return set;
	}
	public static LineString convert(KMLLineString get){
		if(get == null) return null;
		LineString set = new LineString();
		setPrimitives(set, get);
		if(get.getAltitudeMode()!=null)
			set.setAltitudeMode(AltitudeMode.fromValue(get.getAltitudeMode().trim()));
		set.setCoordinates(convert(get.getCoordinates()));
		return set;
	}
	public static LinearRing convert(KMLLinearRing get){
		if(get == null) return null;
		LinearRing set = new LinearRing();
		setPrimitives(set, get);
		if(get.getAltitudeMode()!=null)
			set.setAltitudeMode(AltitudeMode.fromValue(get.getAltitudeMode().trim()));
		set.setCoordinates(convert(get.getCoordinates()));
		return set;
	}
	public static Point convert(KMLPoint get){
		if(get == null) return null;
		Point set = new Point();
		setPrimitives(set, get);
		set.setCoordinates(Collections.singletonList(convert(get.getCoordinates())));
		return set;
	}
	public static List<Coordinate> convert(PositionList positList){
		if(positList == null) return null;
		ArrayList<Coordinate> list = new ArrayList<Coordinate>();
		for(Position posit : positList.list)
			list.add(new Coordinate(posit.getLongitude().getDegrees(), posit.getLongitude().getDegrees(), posit.getAltitude()));
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
}
