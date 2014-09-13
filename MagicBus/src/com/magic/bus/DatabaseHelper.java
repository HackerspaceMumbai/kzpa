package com.magic.bus;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 helper class for all database operations
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "magicrun";
	private static final String TABLE_MAGIC = "magiclocation";

	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_LAT = "lat";
	private static final String KEY_LON = "lon";
	private static final String KEY_TIME = "time";
	private static final String KEY_RUN = "run";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String CREATE_MAGIC_TABLE = "CREATE TABLE " + TABLE_MAGIC + "("
				+ KEY_ID + " INTEGER," + KEY_TITLE + " TEXT," + KEY_EMAIL
				+ " TEXT," + KEY_LAT + " DOUBLE," + KEY_LON + " DOUBLE,"
				+ KEY_TIME + " TEXT," + KEY_RUN + " INTEGER" + ")";
		db.execSQL(CREATE_MAGIC_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAGIC);

		// Create tables again
		onCreate(db);
	}

	public void addUpdate(DatabaseStructure location) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, location.get_id());
		values.put(KEY_TITLE, location.get_name());
		values.put(KEY_EMAIL, location.get_email());
		values.put(KEY_LAT, location.get_lat());
		values.put(KEY_LON, location.get_lon());
		values.put(KEY_TIME, location.get_time());
		values.put(KEY_RUN, location.get_run());

		// Inserting Row
		db.insert(TABLE_MAGIC, null, values);
		db.close(); // Closing database connection
	}

	public DatabaseStructure getLocation(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MAGIC, new String[] { KEY_ID, KEY_TITLE,
				KEY_EMAIL, KEY_LAT, KEY_LON, KEY_TIME, KEY_RUN },
				KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor != null)
			cursor.moveToFirst();

		DatabaseStructure location = new DatabaseStructure(
				Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), Double.parseDouble(cursor.getString(3)),
				Double.parseDouble(cursor.getString(4)), cursor.getString(5),
				Integer.parseInt(cursor.getString(6)));

		return location;
	}

	// Getting All Contacts
	public List<DatabaseStructure> getAllContacts() {
		List<DatabaseStructure> locationList = new ArrayList<DatabaseStructure>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MAGIC;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				DatabaseStructure contact = new DatabaseStructure();
				contact.set_id(Integer.parseInt(cursor.getString(0)));
				contact.set_name(cursor.getString(1));
				contact.set_email(cursor.getString(2));
				contact.set_lat(Double.parseDouble(cursor.getString(3)));
				contact.set_lon(Double.parseDouble(cursor.getString(4)));
				contact.set_time(cursor.getString(5));
				contact.set_run(Integer.parseInt(cursor.getString(6)));

				// Adding contact to list
				locationList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return locationList;
	}

	public DatabaseStructure getLocationByRun(int run) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MAGIC, new String[] { KEY_ID, KEY_TITLE,
				KEY_EMAIL, KEY_LAT, KEY_LON, KEY_TIME, KEY_RUN }, KEY_RUN
				+ "=?", new String[] { String.valueOf(run) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		DatabaseStructure location = new DatabaseStructure(
				Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), Double.parseDouble(cursor.getString(3)),
				Double.parseDouble(cursor.getString(4)), cursor.getString(5),
				Integer.parseInt(cursor.getString(6)));

		return location;
	}

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MAGIC;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
}