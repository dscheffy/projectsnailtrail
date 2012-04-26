package org.projectsnailtrail.android;

import java.io.IOException;
import java.util.Set;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class LocationTrackingService extends Service  {
	LocationListener gpsListener;
	LocationListener networkListener;
	TrackPointManager trackPointManager;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		gpsListener = new MyLocationListener("GPS");
		networkListener = new MyLocationListener("NETWORK");
		trackPointManager = TrackPointManager.getInstance();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i("JeffersonService","onStart: "+intent.getAction() + SystemClock.elapsedRealtime());

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		//request updates with no-op listeners, the actual logic will take place in the passive listener
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 1000, gpsListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600000, 1000, networkListener);
		

		//stopSelf();
	}

	//Note, this is the method I should be overriding, the above is deprecated
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		// TODO Auto-generated method stub
//		return super.onStartCommand(intent, flags, startId);
//	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(gpsListener);
		locationManager.removeUpdates(networkListener);
		super.onDestroy();
	}
	
	class MyLocationListener implements LocationListener {
		String type;
		public MyLocationListener(String type){
			this.type=type;
		}
		public void onLocationChanged(Location location) {
			Log.i("LocationTrackingService:onLocationChanged","just logging here, nothing else");
			
		}
	
		public void onProviderDisabled(String provider) {
			Log.i("LocationTrackingService","onProviderDisabled");
			// TODO Auto-generated method stub
			
		}
	
		public void onProviderEnabled(String provider) {
			Log.i("LocationTrackingService","onProviderEnabled");
			// TODO Auto-generated method stub
			
		}
	
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i("LocationTrackingService","onStatusChanged");
			// TODO Auto-generated method stub
			
		}
	}
	 
}
