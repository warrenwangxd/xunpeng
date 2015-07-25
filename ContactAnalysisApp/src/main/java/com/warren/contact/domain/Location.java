package com.warren.contact.domain;

import java.util.Date;

public class Location {
	double latitude;
	double Longitude;
	String locationFeature;//定位的地标
	Date locTime;
	public Date getLocTime() {
		return locTime;
	}
	public void setLocTime(Date locTime) {
		this.locTime = locTime;
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	public String getLocationFeature() {
		return locationFeature;
	}
	public void setLocationFeature(String locationAdress) {
		this.locationFeature = locationAdress;
	}
	public Location(double latitude,double logitude) {
		this.latitude=latitude;
		this.Longitude=logitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
