package net.joshuahughes.worldwindearth.support;

import gov.nasa.worldwind.ogc.kml.KMLLatLonAltBox;
import gov.nasa.worldwind.ogc.kml.KMLLod;
import gov.nasa.worldwind.ogc.kml.KMLRegion;
import net.joshuahughes.worldwindearth.support.Support.KMLTag;

public class KMLRegionImpl extends KMLAbstractObjectImpl<KMLRegion>{
	public KMLRegionImpl(KMLRegion object) {
		super(object);
		children.add(new KMLLatLonAltBoxImpl(object.getLatLonAltBox()));
		children.add(new KMLLodImpl(object.getLod()));
	}
	@Override
	protected KMLTag getTag() {
		return KMLTag.MultiGeometry;
	}
	public static class KMLLatLonAltBoxImpl extends KMLAbstractObjectImpl<KMLLatLonAltBox>{
		public static enum Type{};
		public KMLLatLonAltBoxImpl(KMLLatLonAltBox object) {
			super(object);
			stringElementMap.put(KMLTag.north, object.getNorth().toString());
			stringElementMap.put(KMLTag.south, object.getSouth().toString());
			stringElementMap.put(KMLTag.east, object.getEast().toString());
			stringElementMap.put(KMLTag.west, object.getWest().toString());
			stringElementMap.put(KMLTag.altitudeMode, object.getAltitudeMode().toString());
		}

		@Override
		protected KMLTag getTag() {
			return KMLTag.LatLonAltBox;
		}
		public Enum<?> get(){
			return KMLTag.north;
		}
	}

	public static class KMLLodImpl extends KMLAbstractObjectImpl<KMLLod>{
		public KMLLodImpl(KMLLod object) {
			super(object);
			stringElementMap.put(KMLTag.minFadeExtent, object.getMinFadeExtent().toString());
			stringElementMap.put(KMLTag.maxFadeExtent, object.getMaxFadeExtent().toString());
			stringElementMap.put(KMLTag.minLodPixels, object.getMinLodPixels().toString());
			stringElementMap.put(KMLTag.maxLodPixels, object.getMaxLodPixels().toString());
		}

		@Override
		protected KMLTag getTag() {
			return KMLTag.LatLonAltBox;
		}
	}

}
