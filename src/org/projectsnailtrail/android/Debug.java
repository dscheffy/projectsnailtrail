package org.projectsnailtrail.android;

import android.location.GpsSatellite;
import android.location.GpsStatus;

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

}
