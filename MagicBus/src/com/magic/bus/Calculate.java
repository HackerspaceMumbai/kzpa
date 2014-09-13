package com.magic.bus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import android.app.Activity;
import android.util.Log;

public class Calculate extends Activity {
	URL url;
	String data = null;
	String currentLoc;
	String[] latlon;
	double lat;
	double lon;
	String currentAdd;
	String currentTime;
	String currentName;
	String currentEmail;
	int currentRun;

	public Calculate() {
		// TODO Auto-generated constructor stub
		Log.d("Calculate called", "Object Created");

	}

	public void getLatLan(String location) {
		// TODO Auto-generated method stub
		currentLoc = location;
		latlon = currentLoc.split("\\s*,\\s*");
		lat = Double.parseDouble(latlon[0]);
		lon = Double.parseDouble(latlon[1]);
		Log.d("Location", currentLoc);

	}

	public void getAddress(String address) {
		// TODO Auto-generated method stub
		currentAdd = address;
		Log.d("Address", currentAdd);

	}

	public void getName(String name) {
		// TODO Auto-generated method stub
		currentName = name;
		Log.d("Name", currentName);

	}

	public void getTime(String time) {
		// TODO Auto-generated method stub
		currentTime = time;
		Log.d("Time", currentTime);

	}

	public void getEmail(String email) {
		// TODO Auto-generated method stub
		currentEmail = email;
		Log.d("Email", currentEmail);

	}

	public void getRun(int i) {
		// TODO Auto-generated method stub
		currentRun = i;
	}

	public void post() throws Exception {

		/*
		 * Log.d("Recored updated", currentRun + currentName + currentEmail +
		 * lat + lon + currentTime + currentRun); Thread t = new Thread(new
		 * Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub
		 * String data; try { Log.d("HTTP POST", currentAdd + currentEmail +
		 * currentLoc + currentName + currentTime); // Get user defined values
		 * data = URLEncoder.encode("name", "UTF-8") + "=" +
		 * URLEncoder.encode(currentName, "UTF-8");
		 * 
		 * data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
		 * URLEncoder.encode(currentEmail, "UTF-8");
		 * 
		 * data += "&" + URLEncoder.encode("challenge", "UTF-8") + "=" +
		 * URLEncoder.encode("JP Morgan", "UTF-8");
		 * 
		 * 
		 * data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" +
		 * URLEncoder.encode(latlon[0], "UTF-8");
		 * 
		 * data += "&" + URLEncoder.encode("lon", "UTF-8") + "=" +
		 * URLEncoder.encode(latlon[1], "UTF-8");
		 * 
		 * data += "&" + URLEncoder.encode("age", "UTF-8") + "=" +
		 * URLEncoder.encode("21", "UTF-8");
		 * 
		 * data += "&" + URLEncoder.encode("run", "UTF-8") + "=" +
		 * URLEncoder.encode(currentRun + "", "UTF-8");
		 * 
		 * data += "&" + URLEncoder.encode("time", "UTF-8") + "=" +
		 * URLEncoder.encode(currentTime, "UTF-8");
		 * 
		 * String text = ""; BufferedReader reader = null;
		 * 
		 * // Send data
		 * 
		 * // Defined URL where to send data URL url = new URL(
		 * "http://ec2-54-179-175-140.ap-southeast-1.compute.amazonaws.com:8080/UserLocServlet.java"
		 * );
		 * 
		 * // Send POST data request URLConnection conn = url.openConnection();
		 * conn.setDoOutput(true); OutputStreamWriter wr = new
		 * OutputStreamWriter( conn.getOutputStream()); wr.write(data);
		 * wr.flush();
		 * 
		 * // Get the server response reader = new BufferedReader(new
		 * InputStreamReader( conn.getInputStream())); StringBuilder sb = new
		 * StringBuilder(); String line = null;
		 * 
		 * // Read Server Response while ((line = reader.readLine()) != null) {
		 * // Append server response in string sb.append(line + "\n"); }
		 * 
		 * text = sb.toString(); Log.d("HTTP RESPONSE", text); } catch
		 * (Exception ex) { ex.printStackTrace(); } finally {
		 * 
		 * }
		 * 
		 * } }); try { t.start(); } catch (Exception e) { } }
		 */

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					url = new URL(
							"http://www.magic-bus.webuda.com/postdata.php");

					data = URLEncoder.encode("name", "UTF-8") + "="
							+ URLEncoder.encode(currentName, "UTF-8");

					data += "&" + URLEncoder.encode("email", "UTF-8") + "="
							+ URLEncoder.encode(currentEmail, "UTF-8");

					data += "&" + URLEncoder.encode("id", "UTF-8") + "="
							+ URLEncoder.encode(currentRun + "", "UTF-8");

					data += "&" + URLEncoder.encode("challenge", "UTF-8") + "="
							+ URLEncoder.encode("JP Morgan", "UTF-8");

					data += "&" + URLEncoder.encode("lat", "UTF-8") + "="
							+ URLEncoder.encode(latlon[0], "UTF-8");

					data += "&" + URLEncoder.encode("lon", "UTF-8") + "="
							+ URLEncoder.encode(latlon[1], "UTF-8");

					data += "&" + URLEncoder.encode("age", "UTF-8") + "="
							+ URLEncoder.encode("21", "UTF-8");

					data += "&" + URLEncoder.encode("run", "UTF-8") + "="
							+ URLEncoder.encode(currentRun + "", "UTF-8");

					data += "&" + URLEncoder.encode("time", "UTF-8") + "="
							+ URLEncoder.encode(currentTime, "UTF-8");
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				byte[] bytes = data.getBytes();

				HttpURLConnection conn = null;
				try {
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.setFixedLengthStreamingMode(bytes.length);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded;charset=UTF-8");

					// post the request
					OutputStream out = conn.getOutputStream();
					out.write(bytes);
					out.close();
					// handle the response
					int status = conn.getResponseCode();
					if (status != 200) {
						throw new IOException("Post failed with error code "
								+ status);
					} else {
						// Get the server response
						BufferedReader reader;
						reader = new BufferedReader(new InputStreamReader(
								conn.getInputStream()));
						StringBuilder sb = new StringBuilder();
						String line = null;

						// Read Server Response
						while ((line = reader.readLine()) != null) {
							// Append server response in string
							sb.append(line + "\n");
						}
						String text;
						text = sb.toString();

						Log.d("Post passed", status + "");
						Log.d("HTTP RESPONSE", text);

					}
					conn.disconnect();
				} catch (ProtocolException p) {
					p.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			/*
			 * db.addUpdate(new DatabaseStructure(currentRun, currentName,
			 * currentEmail, lat, lon, currentTime, currentRun));
			 */

		});
		t.start();

	}
}