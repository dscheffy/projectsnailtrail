package org.projectsnailtrail.android;

import java.util.ArrayList;
import java.util.List;

import org.projectsnailtrail.writable.TrackPoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class TrackPointOverlay extends Overlay {

	private List<TrackPoint> trackPoints;// = new ArrayList<TrackPoint>();
	private Paint paint = new Paint();
	private Paint pathPaint = new Paint();
	
	public TrackPointOverlay(List<TrackPoint> points) {
		super();
		trackPoints = points;
		//Set the default paint style for track points -- some attributes will be defined/overridden on a per point basis
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        
        //Set the default paint style for lines between consecutive track points
        pathPaint.setColor(Color.RED);
        pathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setAlpha(32);
        pathPaint.setStrokeWidth(4);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
		Projection proj = mapView.getProjection();
		Point prevPoint = null;
		long prevTimeStamp = 0;
		for(TrackPoint trackPoint : trackPoints){
			if(trackPoint.getTimestamp()<prevTimeStamp) continue;
			prevTimeStamp = trackPoint.getTimestamp();
			Point point = new Point();
			GeoPoint geoPoint = new GeoPoint((int)(trackPoint.getLatitude()*1000000),(int)(trackPoint.getLongitude()*1000000));
			proj.toPixels(geoPoint,point);
			if(trackPoint.isGps()) {
				// blue dots for gps fixes
				paint.setColor(Color.BLUE);
			} else {
				// green dots for network fixes -- so far these are looking to be of questionable integrity
				paint.setColor(Color.GREEN);
			}

			//the doc isn't too clear on what stroke width means -- from trial and error it seems to be diameter, not radius
			//of course the doc isn't so clear on what accuracy means either -- I'm assuming it refers to radius
			//multiplying by 2 based on assumption that stroke width is diameter and accuracy represents radius
			//metersToEquatorPixels() just seems like the stupidest approach as it will be off as you approach poles
			paint.setStrokeWidth(2*proj.metersToEquatorPixels(trackPoint.getAccuracy()));

			//I erroneously chose square root so that the opacity would be linear with respect to the area of the circle,
			//but if I did that correctly I would have gone with square.  The problem with 1/x^2 or even 1/x was that the
			//circles became too transparent too quickly.  I tried log, but it had the opposite effect.  
			//in the end, sqrt just looked nice...
			paint.setAlpha((int)(256/(Math.sqrt(trackPoint.getAccuracy())+1)));

			canvas.drawPoint(point.x, point.y, paint);
			if(prevPoint!=null){
				//don't draw a path for the first point
				canvas.drawLine(point.x,point.y,prevPoint.x,prevPoint.y,pathPaint);
			}
			prevPoint=point;
			
		}
	}
	

	
}
