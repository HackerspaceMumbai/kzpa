package com.magic.bus;

public class DatabaseStructure {

	int _id;
	String _name;
	String _email;
	double _lat;
	double _lon;
	String _time;
	int _run;

	public DatabaseStructure() {
	}

	public DatabaseStructure(int id, String name, String email, Double lat,
			Double lon, String time, int run) {

		this._id = id;
		this._name = name;
		this._email = email;
		this._lat = lat;
		this._lon = lon;
		this._time = time;
		this._run = run;

	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}

	public Double get_lat() {
		return _lat;
	}

	public void set_lat(Double _lat) {
		this._lat = _lat;
	}

	public Double get_lon() {
		return _lon;
	}

	public void set_lon(Double _lon) {
		this._lon = _lon;
	}

	public String get_time() {
		return _time;
	}

	public void set_time(String _time) {
		this._time = _time;
	}

	public int get_run() {
		return _run;
	}

	public void set_run(int _run) {
		this._run = _run;
	}

	/*
	 * public DatabaseStructure(int id, String name, String email) { this._id =
	 * id; this._name = name; this._email = email; }
	 * 
	 * public DatabaseStructure(String name, String email) { this._name = name;
	 * this._email = email; }
	 */
}
