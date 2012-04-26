package org.projectsnailtrail.android;

import java.util.List;

import org.projectsnailtrail.writable.TrackPoint;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapViewActivity extends MapActivity {
	public static final String ACTION_MAIN = "org.projectsnailtrail.android.ACTION_MAIN";
	List<Overlay> mapOverlays;
	TrackPointOverlay trackPointOverlay;
	MapView mapView;
	boolean freshView=true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        mapView = (MapView)findViewById(R.id.mapview);
        mapOverlays = mapView.getOverlays();
    }
    @Override
    public void onResume(){
        super.onResume();
        List<TrackPoint> points = TrackPointManager.getInstance().getAllPoints(getIntent().getData());
        if(freshView){
	        int minLat = Integer.MAX_VALUE;
	        int maxLat = Integer.MIN_VALUE;
	        int minLong = Integer.MAX_VALUE;
	        int maxLong = Integer.MIN_VALUE;
	        for(TrackPoint tp : points){
	        	int latE6 = (int)(tp.getLatitude()*1000000);
	        	int longE6 = (int)(tp.getLongitude()*1000000);
	        	if(latE6>maxLat) maxLat=latE6;
	        	if(latE6<minLat) minLat=latE6;
	        	if(longE6>maxLong) maxLong=longE6;
	        	if(longE6<minLong) minLong=longE6;
	        }
	        GeoPoint firstGeo = new GeoPoint((minLat+maxLat)/2,(minLong+maxLong)/2);
	        MapController controller = mapView.getController();
	        controller.setCenter(firstGeo);
	        controller.zoomToSpan(maxLat-minLat,maxLong-minLong);
	        freshView=false;
        }
        trackPointOverlay = new TrackPointOverlay(points);
        mapOverlays.clear();
        mapOverlays.add(trackPointOverlay);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
