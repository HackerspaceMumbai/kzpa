package com.magic.bus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Display extends Activity implements OnItemSelectedListener {

	Spinner run;
	int selectedsignal;
	String text = "";
	double totalDistance = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);

		// init example series data
		GraphViewSeries exampleSeries = new GraphViewSeries(
				new GraphViewData[] { new GraphViewData(1, 2.0d),
						new GraphViewData(2, 1.5d), new GraphViewData(3, 2.5d),
						new GraphViewData(4, 1.0d) });

		GraphView graphView = new LineGraphView(this // context
				, "GraphViewDemo" // heading
		);
		graphView.addSeries(exampleSeries); // data

		LinearLayout layout = (LinearLayout) findViewById(R.id.graphpage);
		layout.addView(graphView);

		run = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.runs, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		run.setAdapter(adapter);
		// dropdown
		run.setOnItemSelectedListener(this);
		// run.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		selectedsignal = arg2;

		Log.d("getting data for run ", selectedsignal + "");
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String data;
				try {
					// Get user defined values
					data = URLEncoder.encode("run", "UTF-8") + "="
							+ URLEncoder.encode(selectedsignal + "", "UTF-8");

					BufferedReader reader = null;

					// Send data

					// Defined URL where to send data
					URL url = new URL(
							"http://magic-bus.webuda.com/getdatabyrun.php");

					// Send POST data request
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();

					// Get the server response
					reader = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;

					// Read Server Response
					while ((line = reader.readLine()) != null) {
						// Append server response in string
						sb.append(line + "\n");
					}

					text = sb.toString();
					Log.d("HTTP RESPONSE", text);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {

				}
				String lat[] = text.split(",");
				Log.d("Last", lat[lat.length - 1]);
				String lon[] = lat[lat.length - 1].split(":");

				for (int i = 0; i < lat.length - 2; i++) {
					totalDistance += Haversine.haversine(
							Double.parseDouble(lat[i]),
							Double.parseDouble(lon[i]),
							Double.parseDouble(lat[i + 1]),
							Double.parseDouble(lon[i + 1]));
				}

			}
		});
		try {
			t.start();
		} catch (Exception e) {
		}
		TextView tv = (TextView) findViewById(R.id.output);
		TextView tv2 = (TextView) findViewById(R.id.outputmoney);
		tv.setText("Kilometers walked: " + totalDistance);
		tv2.setText("Money donated to charity: " + (totalDistance * 2.5) + "");
		totalDistance = 0;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
