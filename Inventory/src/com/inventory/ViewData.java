package com.inventory;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ViewData extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewdb);

		DatabaseHelper db = new DatabaseHelper(this);
		String log = null;
		String full_log = "";
		Log.d("Reading: ", "Reading all contacts..");
		List<Inventory> invent = db.getAllInventory();

		for (Inventory in : invent) {
			log = "ID: " + in.get_id() + ", Title: " + in.get_title()
					+ ", Count: " + in.get_inventory();

			Log.d("Inventory: ", log);

			full_log += "\n" + log;
		}

		TextView tv = (TextView) findViewById(R.id.textView1);
		if (full_log != null)
			tv.setText(full_log);
	}
}