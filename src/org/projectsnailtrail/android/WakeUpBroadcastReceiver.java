package org.projectsnailtrail.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

public class WakeUpBroadcastReceiver extends BroadcastReceiver {

	public final static int WAIT_PERIOD = 1000 * 60 * 2;
	public final static String ALARM_ACTION = "com.hello.jefferson.ALARM_ACTION";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("JeffersonsReceiver","onReceive: "+intent.getAction() + SystemClock.elapsedRealtime());
		Intent trackLocation = new Intent(context, LocationTrackingService.class);
		context.startService(trackLocation);

	}
	public static void enable(Context context){
		Log.i("JeffersonsReceiver","ARMING!");
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// setup pending intent
		Intent alarmIntent = new Intent(WakeUpBroadcastReceiver.ALARM_ACTION);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0,
		alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// now go ahead and set the alarm
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		SystemClock.elapsedRealtime(),
		WakeUpBroadcastReceiver.WAIT_PERIOD, pIntent);

	}
	public static void disable(Context context){
		Log.i("JeffersonsReceiver","DISARMING!");
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(WakeUpBroadcastReceiver.ALARM_ACTION);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0,
		alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pIntent);
		Intent trackLocation = new Intent(context, LocationTrackingService.class);
		context.stopService(trackLocation);
		
	}

}
