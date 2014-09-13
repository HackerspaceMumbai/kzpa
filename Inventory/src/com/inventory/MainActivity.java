package com.inventory;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener {

	DatabaseHelper db = new DatabaseHelper(this);
	Spinner spinner;
	String itemToUpdate;

	// set up the alarm manager
	AlarmReceiver alarm = new AlarmReceiver();

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// start the alarm on create
		alarm.setAlarm(this);

		Button download = (Button) findViewById(R.id.button1);
		Button subtract = (Button) findViewById(R.id.button2);
		Button viewDb = (Button) findViewById(R.id.button3);
		Button clearDb = (Button) findViewById(R.id.button4);
		Button searchDb = (Button) findViewById(R.id.button5);

		// make spinner and load in ot spinner
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);
		loadSpinner();

		download.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				db.addInventory(new Inventory("Soap", "50"));
				db.addInventory(new Inventory("Perfume", "20"));
				db.addInventory(new Inventory("Cream", "33"));
				db.addInventory(new Inventory("Moisturizer", "24"));
				loadSpinner();
			}
		});

		clearDb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				db.deleteAllInventory();
				loadSpinner();
			}
		});

		searchDb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						SearchableActivity.class);
				startActivity(intent);
			}
		});

		subtract.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				checkForThreshold();
				try {
					Toast.makeText(getApplicationContext(),
							"Subtracted 5 from " + itemToUpdate,
							Toast.LENGTH_SHORT).show();
					db.updateTheInventory(itemToUpdate);

				} catch (Exception e) {

					Log.e("Subtract", "No value in itemToUpdate");
				}

			}

			private void checkForThreshold() {
				// TODO Auto-generated method stub
				String itemToUpdateValue = db
						.getThresholdInventory(itemToUpdate);
				Log.d("Check Threshold", itemToUpdateValue);

				if (Integer.parseInt(itemToUpdateValue) < 20) {
					Notification mNotification = new Notification.Builder(
							getApplicationContext())

							.setContentTitle(
									"Inventory " + itemToUpdate
											+ " is getting low")
							.setContentText(
									"Stock for " + itemToUpdate + " is "
											+ itemToUpdateValue)
							.setSmallIcon(R.drawable.ic_launcher).build();

					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.notify(0, mNotification);

				}
			}
		});

		viewDb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						ViewData.class);
				startActivity(intent);
			}
		});

	}

	private void loadSpinner() {
		// TODO Auto-generated method stub
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());

		List<String> labels = db.getOnlyInventory();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, labels);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(dataAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

		String label = arg0.getItemAtPosition(arg2).toString();

		Toast.makeText(arg0.getContext(), "You selected: " + label,
				Toast.LENGTH_LONG).show();
		itemToUpdate = label;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
