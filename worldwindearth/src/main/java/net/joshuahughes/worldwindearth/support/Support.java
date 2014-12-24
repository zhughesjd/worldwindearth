package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLAbstractObject;
import gov.nasa.worldwind.ogc.kml.KMLPlacemark;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public class Support {
	public static List<String> validNames = Arrays.asList("getGeometry");
	public static enum KMLTag{name,description, coordinates, id}
	public static String get(KMLAbstractObject object,int level) {
		String prefix="";for(int index=0;index<level;index++)prefix+="\t";
		String tag = getTag(object);
		String string = prefix+"<"+tag+">\n";
		for(Entry<String, Object> entry : object.getFields().getEntries()){
			if(entry.getValue() instanceof KMLAbstractObject)
				string += get((KMLAbstractObject) entry.getValue(),level+1).replace("<CharactersContent>", "").replace("</CharactersContent>","");
			if(entry.getValue() instanceof String || entry.getValue() instanceof Long || entry.getValue() instanceof Integer || entry.getValue() instanceof Float || entry.getValue() instanceof Double || entry.getValue() instanceof Boolean)
				string+=prefix+"\t<"+entry.getKey()+">"+entry.getValue()+"</"+entry.getKey()+">\n";
		}
		for(Method method : object.getClass().getMethods()){
			if(validNames.contains(method.getName())){
				try {
					method.setAccessible(true);
					Object value = method.invoke(object);
					if(value !=null && value instanceof KMLAbstractObject)
						string += get((KMLAbstractObject) value,level+1);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		KMLPlacemark pm = null;
		string += prefix+"</"+tag+">\n";
		return string;
	}
	private static String getTag(KMLAbstractObject feature) {
		String name = feature.getClass().getSimpleName().substring(3, feature.getClass().getSimpleName().length());
		return name;
	};
	
}
