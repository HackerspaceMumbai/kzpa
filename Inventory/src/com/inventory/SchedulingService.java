package com.inventory;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SchedulingService extends IntentService {
	public SchedulingService() {
		super("SchedulingService");
	}

	public static final String TAG = "Schedule";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	@Override
	protected void onHandleIntent(Intent intent) {

		Context context = getApplicationContext();
		DatabaseHelper db = new DatabaseHelper(context);
		List<Inventory> invent = new ArrayList<Inventory>();

		try {
			invent = db.getThresholdBackgroundInventory();
			for (Inventory in : invent) {
				String log = "ID: " + in.get_id() + ", Title: "
						+ in.get_title() + ", Count: " + in.get_inventory();

				Log.d("Inventory: ", log);

				if (invent != null) {
					sendCheckInventNotification("Inventory running low", in.get_title(), in.get_inventory());
					Log.i(TAG, "Check your inventory, idiot :P");
				} else {
					sendAllFineNotification("Inventory above threshold");
					Log.i(TAG, "Wow, you are safe for now :D");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		AlarmReceiver.completeWakefulIntent(intent);
	}

	private void sendAllFineNotification(String msg) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Inventory Alert")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	private void sendCheckInventNotification(String msg, String title, String invent) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Inventory Alert for " + title + "(" + invent + ")")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}