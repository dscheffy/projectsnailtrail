package org.projectsnailtrail.android;

import java.io.IOException;

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
//	GpsStatus.Listener gpsStatusListener;
	TrackPointManager trackPointManager;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		gpsListener = new MyLocationListener(true);
		networkListener = new MyLocationListener(false);
//		gpsStatusListener = new MyGpsStatusListener();
		trackPointManager = TrackPointManager.getInstance();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	static String gpsToString(GpsStatus status, int eventType){
		StringBuilder sb = new StringBuilder();
		sb.append(eventType);
		sb.append("|ttff:");
		sb.append(status.getTimeToFirstFix());
		sb.append("|maxSatellites:");
		sb.append(status.getMaxSatellites());
		sb.append("|");
		int i=0;
		for(GpsSatellite sat : status.getSatellites()){
			i++;
			sb.append("Satellite_");
			sb.append(i);
			sb.append("::hasAlmanac:");
			sb.append(sat.hasAlmanac());
			sb.append("::hasEphemeris:");
			sb.append(sat.hasEphemeris());
			sb.append("::usedInFix:");
			sb.append(sat.usedInFix());
			sb.append("|");
		}
		return sb.toString();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i("JeffersonService","onStart: "+intent.getAction() + SystemClock.elapsedRealtime());
		// Register the listener with the Location Manager to receive location updates
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//		locationManager.addGpsStatusListener(gpsStatusListener);
//		GpsStatus status = locationManager.getGpsStatus(null);
//		Log.i("LocationTrackingService.onStart",gpsToString(status, 0));
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
	//	locationManager.removeGpsStatusListener(gpsStatusListener);
		try{
			trackPointManager.persistToDisk();
		}catch(IOException ioe){
			//TODO: do something here
			//oh well, we tried...
		}
		super.onDestroy();
	}
	
	class MyLocationListener implements LocationListener {
		boolean isGps=false;
		public MyLocationListener(boolean gps){
			isGps=gps;
		}
		public void onLocationChanged(Location location) {
			Log.i("LocationTrackingService","adding a new location");
			trackPointManager.addLocation(location, isGps);
			try{
				trackPointManager.persistToDisk();
			} catch (IOException ioe){
				//eh..
				Log.e("LocationTrackingService:onLocationChanged",ioe.getMessage());
			}
			
//			LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//			stopSelf();
//			locationManager.removeUpdates(gpsListener);
//			locationManager.removeUpdates(networkListener);
			
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
	class MyGpsStatusListener implements GpsStatus.Listener {
		
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			GpsStatus status = locationManager.getGpsStatus(null);
			Log.i("LocationTrackingService.onStart.onGpsStatusChanged",gpsToString(status, event));
		}
	}
	 
}
