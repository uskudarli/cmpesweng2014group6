package Dutluk;

import java.util.Date;

public class Place {
	private int PlaceID;
	public String Name;
	private double Longtitude;
	private double Latitude;
	private Date CreatedOn;
	private Date UpdatedOn;
	public Place(){
		
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public double getLongtitude() {
		return Longtitude;
	}
	public void setLongtitude(double longtitude) {
		Longtitude = longtitude;
	}
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public Date getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(Date createdOn) {
		CreatedOn = createdOn;
	}
	public Date getUpdatedOn() {
		return UpdatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		UpdatedOn = updatedOn;
	}
	public int getPlaceID() {
		return PlaceID;
	}
	public void setPlaceID(int id) {
		PlaceID = id;
	}
}
