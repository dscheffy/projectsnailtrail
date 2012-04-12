package com.hello.jefferson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;


public class LocationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("LocationReceiver","onReceive");
		// TODO Auto-generated method stub
	    Bundle b = intent.getExtras();
	    Location loc = (Location)b.get(android.location.LocationManager.KEY_LOCATION_CHANGED);
	    writeMessage(String.valueOf(loc));
	
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
