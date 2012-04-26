package org.projectsnailtrail.android;

import java.io.IOException;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class PassiveLocationReceiver extends BroadcastReceiver {
	public static String ACTION_PASSIVE_LOCATION_UPDATE = "org.projectsnailtrail.action.PASSIVE_LOCATION_UPDATE";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
		if(extras.containsKey(LocationManager.KEY_LOCATION_CHANGED)){
			Log.i("PassiveLocationUpdateReceiver", "New location received");
			Location location = (Location)extras.getParcelable(LocationManager.KEY_LOCATION_CHANGED);
			Bundle moreExtras = location.getExtras();
			Set<String> keys = moreExtras.keySet();
			StringBuilder sb = new StringBuilder("extras:");
			for(String key : keys){
				sb.append(key).append(":");
				sb.append(moreExtras.get(key)).append(":::");
			}
			Log.i("PassiveLocationUpdateReceiver", "adding a new location:" + location.getProvider() + ":" + sb.toString());
			Log.i("PassiveLocationUpdateReceiver", "age of location:"+String.valueOf(location.getTime()-System.currentTimeMillis()));
			if(moreExtras.containsKey("networkLocationSource") && "cached".equalsIgnoreCase(moreExtras.getString("networkLocationSource"))){
				//ignore this location -- it's cached and likely old/wrong
				return;
			}
			
			try{
				TrackPointManager.getInstance().addLocation(location, "gps".equalsIgnoreCase(location.getProvider()));
				if(!"gps".equalsIgnoreCase(location.getProvider())){
					Debug.log(sb.toString());
				}
			} catch (IOException ioe){
				//eh..
				Log.e("PassiveLocationUpdateReceiver",ioe.getMessage());
			}
			
		}
	}

}
