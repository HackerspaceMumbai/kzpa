package com.inventory;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {

	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, SchedulingService.class);

		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, service);
	}

	/*
	 * For 8:30 a.m.
	 */
	public void setAlarm(Context context) {
		alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// Set the alarm's trigger time to 8:30 a.m.
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 30);

		// Wake up the device to fire a one-time alarm in one minute.
		/*
		 * alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		 * SystemClock.elapsedRealtime() + 60*1000, alarmIntent);
		 * 
		 * // Wake up the device to fire the alarm in 30 minutes, and every 30
		 * minutes // after that.
		 * alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		 * AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR,
		 * alarmIntent);
		 */

		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				alarmIntent);

		ComponentName receiver = new ComponentName(context,
				BootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	// method not used. can be used to cancel the wake up
	public void cancelAlarm(Context context) {
		// If the alarm has been set, cancel it.
		if (alarmMgr != null) {
			alarmMgr.cancel(alarmIntent);
		}

		ComponentName receiver = new ComponentName(context,
				BootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}
}
