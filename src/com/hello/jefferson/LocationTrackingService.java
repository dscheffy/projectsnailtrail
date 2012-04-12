package com.hello.jefferson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class LocationTrackingService extends Service  implements LocationListener {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		writeMessage("Starting Service at "+SystemClock.elapsedRealtime());
		Log.i("JeffersonService","onStart: "+intent.getAction() + SystemClock.elapsedRealtime());
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, this);

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
		locationManager.removeUpdates(this);
		super.onDestroy();
	}
	

	public void onLocationChanged(Location arg0) {
		Log.i("LocationTrackingService","onLocationChanged");
		writeMessage(String.valueOf(arg0));
		
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
	private void writeMessage(String message){
		FileOutputStream out = null;
		try {
			File sdRoot = Environment.getExternalStorageDirectory();

			if(sdRoot.exists() && sdRoot.canWrite()){
				File subDir = new File(sdRoot,"loc_tracker");
				subDir.mkdir();
				if(subDir.exists() && subDir.canWrite()){
					File logfile = new File(subDir,"locations");
					logfile.createNewFile();
					out = new FileOutputStream(logfile, true);
					out.write(message.getBytes("UTF-8"));
					out.write("\n".getBytes("UTF-8"));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e("Can't write to file", e.getLocalizedMessage());
		} catch (IOException e) {
		// TODO Auto-generated catch block
		Log.e("Can't write to file", e.getLocalizedMessage());
	}
	
	}
}
