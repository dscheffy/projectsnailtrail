package org.projectsnailtrail.android;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.os.Environment;
import android.util.Log;

public class Debug {
	
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
	static File getDebugFile() throws IOException {
		File debugfile = null;
		File sdRoot = Environment.getExternalStorageDirectory();

		if (sdRoot.exists() && sdRoot.canWrite()) {
			File subDir = new File(sdRoot, "loc_tracker");
			subDir.mkdir();
			if (subDir.exists() && subDir.canWrite()) {
				debugfile = new File(subDir, "debug.txt");
				debugfile.createNewFile();
			}
		}
		if (debugfile == null || !debugfile.exists() || debugfile.isDirectory()) {
			throw new IOException("Can't work with the backup file");
		}
		return debugfile;
	}
	static void log(String msg){
		try{
			File file = getDebugFile();
			FileWriter os = new FileWriter(file, true);
			os.write(msg);
			os.write("\n");
			os.close();
		}catch(IOException ioe){
			Log.e("SnailTrailDebugger",ioe.getMessage());
		}

	}

}
