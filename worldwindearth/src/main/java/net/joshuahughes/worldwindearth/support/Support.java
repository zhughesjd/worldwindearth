package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import de.micromata.opengis.kml.v_2_2_0.AbstractObject;

public class Support {
    public static List<String> validNames = Arrays.asList("getGeometry");
    public static enum KMLTag{name,description, coordinates, id}
    public static AbstractObject get(KMLAbstractObject kmlObject) {
        AbstractObject object = null;
        try
        {
            Class<?> clazz = Class.forName( "de.micromata.opengis.kml.v_2_2_0."+kmlObject.getClass( ).getSimpleName( ).substring( 3 ) );
            object = ( AbstractObject ) clazz.newInstance( );
        }
        catch ( ClassNotFoundException e )
        {
            System.err.println("DNE:"+kmlObject );
        }
        catch ( Exception e )
        {
            System.err.println("DNE constuctor:"+kmlObject );
        }
        synchronize( object, kmlObject );
        return object;
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
    public static void synchronize(Object set,Object get){
        for(Method sm : set.getClass( ).getMethods( ))
            for(Method gm : get.getClass( ).getMethods( ))
                try{
                    if(sm.getParameterCount( )!=1 || gm.getParameterCount( )!=0) continue;
                    if(!sm.getName( ).equals( "s"+gm.getName( ).substring( 1, gm.getName( ).length( ) ) )) continue;
                    if(sm.getParameterTypes( )[0].equals( gm.getReturnType( ) )){
                        sm.invoke( set, gm.invoke( get ) );
                        continue;
                    }
                    for(Entry<Class<?>, Class<?>> e : primitiveMap.entrySet( )){
                        if((sm.getParameterTypes( )[0].equals( e.getKey( ) ) && gm.getReturnType().equals( e.getValue( ) )) || (gm.getReturnType( ).equals( e.getKey( ) ) && sm.getParameterTypes( )[0].equals( e.getValue( ) )) ){
                            sm.invoke( set, gm.invoke( get ) );
                            continue;
                        }
                    }
                    if(AbstractObject.class.isAssignableFrom( sm.getParameterTypes( )[0])){
                        Object candidate = gm.invoke( get);
                        if(candidate!=null && KMLAbstractObject.class.isAssignableFrom( candidate.getClass( ) )){
                            sm.invoke( set, get(( KMLAbstractObject ) candidate) );
                            continue;
                        }
                    }
                }
                catch ( Exception e ){
                    e.printStackTrace();
                }
    }
}
