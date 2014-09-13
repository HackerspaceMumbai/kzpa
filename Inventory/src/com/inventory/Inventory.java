package com.inventory;

public class Inventory {

	int _id;
	String _title;
	String _inventory;

	
	public Inventory(int id, String ttle, String invent) {
		this._id = id;
		this._title = ttle;
		this._inventory = invent;
	}

	public Inventory(String ttle, String invent) {
		this._title = ttle;
		this._inventory = invent;
	}

	public Inventory() {
		// TODO Auto-generated constructor stub
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_inventory() {
		return _inventory;
	}

	public void set_inventory(String _inventory) {
		this._inventory = _inventory;
	}

}
