package com.inventory;

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
	private static final String DATABASE_NAME = "inventoryManager";
	private static final String TABLE_INVENTORY = "inventory";

	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_INVENTORY = "inventory";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_INVENTORY_TABLE = "CREATE TABLE " + TABLE_INVENTORY + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
				+ KEY_INVENTORY + " TEXT" + ")";
		db.execSQL(CREATE_INVENTORY_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
		onCreate(db);
	}

	public void addInventory(Inventory invent) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, invent.get_title());
		values.put(KEY_INVENTORY, invent.get_inventory());

		db.insert(TABLE_INVENTORY, null, values);
		db.close();

	}

	// Getting single inventory based on id
	public Inventory getInventory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_INVENTORY, new String[] { KEY_ID,
				KEY_TITLE, KEY_INVENTORY }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Inventory invent = new Inventory(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));

		return invent;
	}

	// for getting the inventory of passed name 'itemtoupdate'
	public String getThresholdInventory(String itemToUpdate) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_INVENTORY, new String[] { KEY_ID,
				KEY_TITLE, KEY_INVENTORY }, KEY_TITLE + "=?",
				new String[] { itemToUpdate }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		String itemToUpdateValue = cursor.getString(2);

		return itemToUpdateValue;
	}

	// get everything
	public List<Inventory> getAllInventory() {

		List<Inventory> inventList = new ArrayList<Inventory>();

		String selectQuery = "SELECT  * FROM " + TABLE_INVENTORY;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Inventory contact = new Inventory();
				contact.set_id(Integer.parseInt(cursor.getString(0)));
				contact.set_title(cursor.getString(1));
				contact.set_inventory(cursor.getString(2));

				inventList.add(contact);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return inventList;
	}

	// get everything below threshold for the background task
	public List<Inventory> getThresholdBackgroundInventory() {

		List<Inventory> inventList = new ArrayList<Inventory>();

		String selectQuery = "SELECT * FROM " + TABLE_INVENTORY + " WHERE "
				+ KEY_INVENTORY + " < 20";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Inventory contact = new Inventory();
				contact.set_id(Integer.parseInt(cursor.getString(0)));
				contact.set_title(cursor.getString(1));
				contact.set_inventory(cursor.getString(2));

				inventList.add(contact);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return inventList;
	}

	// dont know why i made this. dont touch :/
	public List<String> getOnlyInventory() {
		List<String> labels = new ArrayList<String>();

		String selectQuery = "SELECT  * FROM " + TABLE_INVENTORY;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				labels.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return labels;
	}

	// get the number of items. not used currently
	public int getInventoryCount() {
		String countQuery = "SELECT  * FROM " + TABLE_INVENTORY;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// deprecated. use updateTheInventory
	public int updateInventory(Inventory invent) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, invent.get_title());
		values.put(KEY_INVENTORY, invent.get_inventory());

		// updating row
		return db.update(TABLE_INVENTORY, values, KEY_ID + " = ?",
				new String[] { String.valueOf(invent.get_id()) });
	}

	// subtracts the item 'invent' by 5
	public void updateTheInventory(String invent) {

		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("UPDATE " + TABLE_INVENTORY + " SET " + KEY_INVENTORY + "="
				+ KEY_INVENTORY + "-5 WHERE " + KEY_TITLE + " like " + "'"
				+ invent + "'");
	}

	// not used now.
	public void deleteInventory(Inventory invent) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_INVENTORY, KEY_ID + " = ?",
				new String[] { String.valueOf(invent.get_id()) });
		db.close();
	}

	// clean the DB
	public void deleteAllInventory() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DatabaseHelper.TABLE_INVENTORY, null, null);
	}

}
