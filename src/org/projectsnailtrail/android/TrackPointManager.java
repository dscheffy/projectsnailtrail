package org.projectsnailtrail.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.projectsnailtrail.writable.TrackPoint;

import android.location.Location;
import android.os.Environment;
import android.util.Log;

public class TrackPointManager {

	private TrackPointManager(){};

	private static TrackPointManager tpm;
	private boolean fullListInitiated=false;
	/**
	 * a pointer to the first item that has not yet been persisted to disk
	 */
	private int firstUnsavedItem=0;
	private List<TrackPoint> trackPoints=new ArrayList<TrackPoint>();
	
	public static TrackPointManager getInstance(){
		if(tpm==null) tpm=new TrackPointManager();
		return tpm;
	}
	public List<TrackPoint> getPointsInMemory(){
		return new ReadOnlyList<TrackPoint>(trackPoints);
	}
	public List<TrackPoint> getAllPoints(){
		try{
			updateFromDisk();
		}catch (IOException ioe){
			//not really sure what to do here...
			Log.e("TrackPointManager",ioe.getMessage());
		}
		return trackPoints;
//		return new ReadOnlyList<TrackPoint>(trackPoints);
	}
	public void addTrackPoint(TrackPoint tp){
		trackPoints.add(tp);
	}
	public void addLocation(Location location, boolean isGps){
		TrackPoint tp = new TrackPoint();
		tp.setLatitude(location.getLatitude());
		tp.setLongitude(location.getLongitude());
		tp.setAccuracy((int)location.getAccuracy());
		tp.setTimestamp(location.getTime());
		tp.setGps(isGps);
		addTrackPoint(tp);
	}
	//The next few methods really need to be syncronized (potentially across processes ?with a file lock?, not just across threads)
	public void persistToDisk() throws IOException {
		int length = trackPoints.size();
		if(length!=firstUnsavedItem){
			File file = getFile();
			OutputStream os = new FileOutputStream(file,true);
			for(TrackPoint tp : trackPoints.subList(firstUnsavedItem, length)){
				tp.write(os);
			}
			firstUnsavedItem=length;
		}
	}
	void updateFromDisk() throws IOException {
		if(fullListInitiated) return;
		
		List<TrackPoint> temp = new ArrayList<TrackPoint>();
		InputStream is = new FileInputStream(getFile());
		for(TrackPoint tp : TrackPoint.iterate(is)){
			temp.add(tp);
		}
		int length = trackPoints.size();
		if(length!=firstUnsavedItem){
			temp.addAll(trackPoints.subList(firstUnsavedItem, length));
		}
		// I could just set trackPoints to temp, but then any readonly lists lying around would have an old reference
		trackPoints.clear();
		trackPoints.addAll(temp);
		fullListInitiated=true;
	}
	private File getFile() throws IOException{
		File logfile = null;
		File sdRoot = Environment.getExternalStorageDirectory();

		if(sdRoot.exists() && sdRoot.canWrite()){
			File subDir = new File(sdRoot,"loc_tracker");
			subDir.mkdir();
			if(subDir.exists() && subDir.canWrite()){
				logfile = new File(subDir,"locations");
				logfile.createNewFile();
			}
		}
		if(logfile==null||!logfile.exists()||logfile.isDirectory()){
			throw new IOException("Can't work with the backup file");
		}
		return logfile;
	}
	private void writeMessage(Location location){
		FileOutputStream out = null;
		try {
			File sdRoot = Environment.getExternalStorageDirectory();

			if(sdRoot.exists() && sdRoot.canWrite()){
				File subDir = new File(sdRoot,"loc_tracker");
				subDir.mkdir();
				if(subDir.exists() && subDir.canWrite()){
					File logfile = new File(subDir,"locations");
					logfile.createNewFile();
					TrackPoint tp = new TrackPoint();
					tp.setLatitude(location.getLatitude());
					tp.setLongitude(location.getLongitude());
					tp.setTimestamp(location.getTime());
					tp.setAccuracy((int)location.getAccuracy());
					out = new FileOutputStream(logfile, true);
					tp.write(out);
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
	
	class ReadOnlyList<T> implements List<T> {
		private List<T> wrappedList;

		ReadOnlyList(List<T> list){
			wrappedList=list;
		}

		public void add(int location, Object object) {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public boolean add(Object object) {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public boolean addAll(Collection arg0) {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public boolean addAll(int arg0, Collection arg1) {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public void clear() {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public boolean contains(Object object) {
			return wrappedList.contains(object);
		}

		public boolean containsAll(Collection arg0) {
			return wrappedList.containsAll(arg0);
		}

		public boolean equals(Object object) {
			return wrappedList.equals(object);
		}

		public T get(int location) {
			return wrappedList.get(location);
		}

		public int hashCode() {
			return wrappedList.hashCode();
		}

		public int indexOf(Object object) {
			return wrappedList.indexOf(object);
		}

		public boolean isEmpty() {
			return wrappedList.isEmpty();
		}

		public Iterator iterator() {
			//really this needs to return a read only iterator
			return wrappedList.iterator();
		}

		public int lastIndexOf(Object object) {
			return wrappedList.lastIndexOf(object);
		}

		public ListIterator listIterator() {
			return wrappedList.listIterator();
		}

		public ListIterator listIterator(int location) {
			return wrappedList.listIterator(location);
		}

		public T remove(int location) {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public boolean remove(Object object) {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public boolean removeAll(Collection arg0) {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public boolean retainAll(Collection arg0) {
			return wrappedList.retainAll(arg0);
		}

		public Object set(int location, Object object) {
			throw new UnsupportedOperationException("This is a read only list");
		}

		public int size() {
			return wrappedList.size();
		}

		public List subList(int start, int end) {
			return new ReadOnlyList(wrappedList.subList(start, end));
		}

		public Object[] toArray() {
			//not sure if this potentially exposes the underlying object of some implementations
			return wrappedList.toArray();
		}

		public Object[] toArray(Object[] array) {
			return wrappedList.toArray(array);
		}
		
	}
}
