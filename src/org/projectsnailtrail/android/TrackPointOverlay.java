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
//	    paint.setDither(true);
	        paint.setColor(Color.BLUE);
	        paint.setStyle(Paint.Style.FILL_AND_STROKE);
	        paint.setStrokeJoin(Paint.Join.ROUND);
	        paint.setStrokeCap(Paint.Cap.ROUND);
//	        paint.setAlpha(64);

//	        pathPaint.setDither(true);
	        pathPaint.setColor(Color.RED);
	        pathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	        pathPaint.setStrokeJoin(Paint.Join.ROUND);
	        pathPaint.setStrokeCap(Paint.Cap.ROUND);
	        pathPaint.setAlpha(32);
	        pathPaint.setStrokeWidth(4);

		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
		Projection proj = mapView.getProjection();
//		int metersPerPixel = mapView.getLatitudeSpan()/(4*mapView.getHeight());
		Point prevPoint = null;
		long prevTimeStamp = 0;
		for(TrackPoint trackPoint : trackPoints){
			if(trackPoint.getTimestamp()<prevTimeStamp) continue;
			prevTimeStamp = trackPoint.getTimestamp();
			Point point = new Point();
			GeoPoint geoPoint = new GeoPoint((int)(trackPoint.getLatitude()*1000000),(int)(trackPoint.getLongitude()*1000000));
			proj.toPixels(geoPoint,point);
			if(trackPoint.isGps()) 
			{paint.setColor(Color.BLUE);}else{paint.setColor(Color.GREEN);}
//			paint.setStrokeWidth(trackPoint.getAccuracy()/(metersPerPixel+1));//just a kluge to avoid divide by zero issues
			paint.setStrokeWidth(2*proj.metersToEquatorPixels(trackPoint.getAccuracy()));
			paint.setAlpha((int)(256/(Math.sqrt(trackPoint.getAccuracy())+1)));//Totally klugie!!! need to fix this, but not really important for now.
			canvas.drawPoint(point.x, point.y, paint);
			if(prevPoint!=null){
				canvas.drawLine(point.x,point.y,prevPoint.x,prevPoint.y,pathPaint);
			}
			prevPoint=point;
			
		}
	}
	

	
}
