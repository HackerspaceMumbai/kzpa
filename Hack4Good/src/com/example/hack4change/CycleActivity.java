package com.example.hack4change;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class CycleActivity extends FragmentActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	// DatabaseHelper db;

	GoogleMap googleMap;
	Location currentLocation;

	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;

	String addressText, name, email;

	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;

	boolean mUpdatesRequested = false;

	// upload server data
	Calculate send = new Calculate();

	// end of upload server data

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cycle);

		// db = new DatabaseHelper(getApplicationContext());

		// turn on wifi
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);

		// Getting GoogleMap object from the fragment
		googleMap = fm.getMap();

		// Enabling Layers of Google Map
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setZoomGesturesEnabled(true);
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(true);

		// Signin data
		/*
		 * Bundle bundle = new Bundle(); bundle = getIntent().getExtras(); name
		 * = bundle.getString("name"); email = bundle.getString("email");
		 */
		send.getTime(java.text.DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime()));
		send.getName("Kandarp Khandwala");
		send.getEmail("kandarpck@gmail.com");
		send.getRun(3);
		// end of signin data

		mLocationRequest = LocationRequest.create();
		mLocationRequest
				.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest
				.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		mUpdatesRequested = false;

		mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES,
				Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();

		mLocationClient = new LocationClient(this, this, this);
		if (servicesConnected()) {
			Log.d("MainActivity", "1");
			if (!(mLocationClient.isConnected())) {
				Log.d("MainActivity", "2");
				mLocationClient.connect();
			} else {
				Log.d("MainActivity", "3");

				mLocationClient.disconnect();
			}
		}
		Log.d("created", "blah");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onStop() {

		if (mLocationClient.isConnected()) {
			stopPeriodicUpdates();
		}

		mLocationClient.disconnect();

		super.onStop();
	}

	@Override
	public void onPause() {

		// Save the current setting for updates
		mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED,
				mUpdatesRequested);
		mEditor.commit();

		super.onPause();
	}

	@Override
	public void onStart() {

		super.onStart();

		if (!(mLocationClient.isConnected())) {
			Log.d("MainActivity", "2");
			mLocationClient.connect();
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		// turn on wifi
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		mLocationClient.connect();

		// If the app already has a setting for getting location updates, get it
		if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
			mUpdatesRequested = mPrefs.getBoolean(
					LocationUtils.KEY_UPDATES_REQUESTED, false);

			// Otherwise, turn off location updates until requested
		} else {
			mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
			mEditor.commit();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		switch (requestCode) {

		case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST:

			switch (resultCode) {
			// If Google Play services resolved the problem
			case Activity.RESULT_OK:

				Log.d(LocationUtils.APPTAG, getString(R.string.resolved));

				break;

			default:
				// Log the result
				Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));

				break;
			}

			// If any other request code was received
		default:
			// Report that this Activity received an unknown requestCode
			Log.d(LocationUtils.APPTAG,
					getString(R.string.unknown_activity_request_code,
							requestCode));

			break;
		}
	}

	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG,
					getString(R.string.play_services_available));

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(),
						LocationUtils.APPTAG);
			}
			return false;
		}
	}

	public void getLocation(View v) {

		if (servicesConnected()) {
			currentLocation = mLocationClient.getLastLocation();
			LatLng mumbai = new LatLng(19.17, 72.83);

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mumbai, 14));
		}
	}

	@SuppressLint("NewApi")
	public void getAddress(View v) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
				&& !Geocoder.isPresent()) {
			// No geocoder is present. Issue an error message
			Toast.makeText(this, R.string.no_geocoder_available,
					Toast.LENGTH_LONG).show();
			return;
		}

		if (servicesConnected()) {

			// Get the current location
			Location currentLocation = mLocationClient.getLastLocation();
			LatLng mumbai = new LatLng(19.17, 72.83);

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mumbai, 14));

			// Start the background task
			(new CycleActivity.GetAddressTask(this)).execute(currentLocation);

		}
	}

	public void stopUpdates(View v) {
		mUpdatesRequested = false;

		if (servicesConnected()) {
			stopPeriodicUpdates();
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		mUpdatesRequested = true;
		if (mUpdatesRequested) {
			LatLng mumbai = new LatLng(19.17, 72.83);

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mumbai, 11));

			startPeriodicUpdates();
		}
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		if (connectionResult.hasResolution()) {
			try {

				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {

			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	@Override
	public void onLocationChanged(Location location) {

		// Report to the UI that the location was updated
		// upload lat lan to server
		// call add if not present
		// execute post
		if (mUpdatesRequested) {
			send.getLatLan(LocationUtils.getLatLng(this, location));
			(new CycleActivity.GetAddressTask(this)).execute(location);
			doMyCommand();
		}
	}

	private void doMyCommand() {
		// TODO Auto-generated method stub
		try {
			if (mUpdatesRequested) {

				send.getAddress(addressText);
				send.post();
				// db.addUpdate(new DatabaseStructure(send.getc, name, email,
				// lat, lon, time, run)))
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startPeriodicUpdates() {
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);
	}

	protected class GetAddressTask extends AsyncTask<Location, Void, String> {

		Context localContext;

		public GetAddressTask(Context context) {

			super();
			localContext = context;
		}

		@Override
		protected String doInBackground(Location... params) {

			Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

			// Create a list to contain the result address
			List<Address> addresses = null;

			// Get the current location from the input parameter list
			Location location = params[0];

			// Try to get an address for the current location. Catch IO or
			// network problems.
			try {

				addresses = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);

			} catch (IOException exception1) {

				// Log an error and return an error message
				Log.e(LocationUtils.APPTAG,
						getString(R.string.IO_Exception_getFromLocation));

				exception1.printStackTrace();

				return (getString(R.string.IO_Exception_getFromLocation));

			} catch (IllegalArgumentException exception2) {

				String errorString = getString(
						R.string.illegal_argument_exception,
						location.getLatitude(), location.getLongitude());
				Log.e(LocationUtils.APPTAG, errorString);
				exception2.printStackTrace();

				//
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {

				// Get the first address
				Address address = addresses.get(0);

				// Format the first line of address
				addressText = getString(
						R.string.address_output_string,

						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "",

						// Locality is usually a city
						address.getLocality(),

						// The country of the address
						address.getCountryName());

				// Return the text
				return addressText;

				// If there aren't any addresses, post a message
			} else {
				return getString(R.string.no_address_found);
			}
		}

		@Override
		protected void onPostExecute(String address) {

		}
	}

	private void showErrorDialog(int errorCode) {

		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				this, LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null) {

			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);

			// Show the error dialog in the DialogFragment
			errorFragment.show(getSupportFragmentManager(),
					LocationUtils.APPTAG);
		}
	}

	public static class ErrorDialogFragment extends DialogFragment {

		private Dialog mDialog;

		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

}
