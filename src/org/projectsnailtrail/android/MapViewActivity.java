package org.projectsnailtrail.android;

import java.util.List;

import org.projectsnailtrail.writable.TrackPoint;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapViewActivity extends MapActivity {
	List<Overlay> mapOverlays;
	Drawable drawable;
	TrackPointOverlay trackPointOverlay;
	MapView mapView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        mapView = (MapView)findViewById(R.id.mapview);
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        trackPointOverlay = new TrackPointOverlay(drawable);
        List<TrackPoint> points = TrackPointManager.getInstance().getAllPoints();
        for(TrackPoint tp : points){
            GeoPoint point = new GeoPoint((int)(tp.getLatitude()*1000000),(int)(tp.getLongitude()*1000000));
            OverlayItem overlayitem = new OverlayItem(point, "", "");
            trackPointOverlay.addOverlay(overlayitem);
        }
        mapOverlays.add(trackPointOverlay);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
