package com.example.jome17wave.jome_map;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Map implements Serializable {
	
	private int id;
	private String name;
	private String side;
	private String type;
	private String direction;
	private String level;
	private String tidal;
	private double latitude;
	private double longitude;
	
	
	public Map(int id, String name, String side, String type, String direction, String level, String tidal,
			double latitude, double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.side = side;
		this.type = type;
		this.direction = direction;
		this.level = level;
		this.tidal = tidal;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public LatLng setLatLng(){
		return new LatLng(latitude, longitude);
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSide() {
		return side;
	}


	public void setSide(String side) {
		this.side = side;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getDirection() {
		return direction;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	public String getTidal() {
		return tidal;
	}


	public void setTidal(String tidal) {
		this.tidal = tidal;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


}
