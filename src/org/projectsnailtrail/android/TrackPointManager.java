package org.projectsnailtrail.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.projectsnailtrail.writable.TrackPoint;

import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class TrackPointManager {

	private TrackPointManager() {
	};

	private static TrackPointManager tpm;
	private List<TrackPoint> trackPoints = new ArrayList<TrackPoint>();

	public static TrackPointManager getInstance() {
		if (tpm == null)
			tpm = new TrackPointManager();
		return tpm;
	}

	public List<TrackPoint> getPointsInMemory() {
		return trackPoints;
	}

	public List<TrackPoint> getAllPoints(Uri uri) {
		List<TrackPoint> trackPoints = new ArrayList<TrackPoint>();
		try {
			URL url = new URL(uri.getScheme(), uri.getHost(), uri.getPath());
			InputStream is = url.openStream();
			for (TrackPoint tp : TrackPoint.iterate(is)) {
				trackPoints.add(tp);
			}
			is.close();
		} catch (IOException ioe) {
			// not really sure what to do here...
			Log.e("TrackPointManager", ioe.getMessage());
		}
		return trackPoints;
	}

	public void addTrackPoint(TrackPoint tp) throws IOException {
		File file = getCurrentFile();
		OutputStream os = new FileOutputStream(file, true);
		tp.write(os);
		os.close();
	}

	public void addLocation(Location location, boolean isGps)
			throws IOException {
		TrackPoint tp = new TrackPoint();
		tp.setLatitude(location.getLatitude());
		tp.setLongitude(location.getLongitude());
		tp.setAccuracy((int) location.getAccuracy());
		tp.setTimestamp(location.getTime());
		tp.setGps(isGps);
		addTrackPoint(tp);

	}

	public File[] getTrailFileNames() throws IOException {
		File[] fileNames = null;
		File sdRoot = Environment.getExternalStorageDirectory();
		if (sdRoot.exists() && sdRoot.canWrite()) {
			File subDir = new File(sdRoot, "loc_tracker");
			subDir.mkdir();
			if (subDir.exists()) {
				fileNames = subDir.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String filename) {
						return filename.contains("trail_");
					}
				});
			}
		}
		return fileNames;

	}

	private String convertFileNameToDateAndTime(String fileName) {
		String time = fileName.substring(fileName.lastIndexOf('_') + 1,
				fileName.lastIndexOf('.'));
		if (time.equalsIgnoreCase("current"))
			return time;
		Date date = new Date(Long.valueOf(time));
		return date.toString();
	}

	private File getCurrentFile() throws IOException {
		File logfile = null;
		File sdRoot = Environment.getExternalStorageDirectory();

		if (sdRoot.exists() && sdRoot.canWrite()) {
			File subDir = new File(sdRoot, "loc_tracker");
			subDir.mkdir();
			if (subDir.exists() && subDir.canWrite()) {
				logfile = new File(subDir, "trail_current.dat");
				if (logfile.length() > 1000) {
					File newName = new File(subDir, "trail_"
							+ System.currentTimeMillis() + ".dat");
					logfile.renameTo(newName);
				}
				logfile.createNewFile();
			}
		}
		if (logfile == null || !logfile.exists() || logfile.isDirectory()) {
			throw new IOException("Can't work with the backup file");
		}
		return logfile;
	}
}
