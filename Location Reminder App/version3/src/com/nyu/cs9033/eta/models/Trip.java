package com.nyu.cs9033.eta.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Trip implements Parcelable {
	private static final String TAG = "Trip";

	private String tripId;
	private String tripName;
	private String date;
	private String destination;
	private String friends;
	private ArrayList<String> people;
	private static final String JSON_ID = "id";
	private static final String JSON_DESTINATION = "destination";
	private static final String JSON_PEOPLE = "people";
	private static final String JSON_DATE = "date";
	
	/**
	 * Parcelable creator. Do not modify this function.
	 */
	public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
		public Trip createFromParcel(Parcel p) {
			return new Trip(p);
		}

		public Trip[] newArray(int size) {
			return new Trip[size];
		}
	};
	
	/**
	 * Create a Trip model object from a Parcel. This
	 * function is called via the Parcelable creator.
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Trip(Parcel p) {
		
		// TODO - fill in here
		setTripId(p.readString());
		setTripName(p.readString());
		setDate(p.readString());
		setDestination(p.readString());
		setFriends(p.readString());
		Log.i(TAG, "Trips read from parcel");
	}
	
	/**
	 * Create a Trip model object from arguments
	 * 
	 * @param name  Add arbitrary number of arguments to
	 * instantiate Trip class based on member variables.
	 */
	public Trip(String gettripid,String tripname, String date, String destination, String friends) {
		
		// TODO - fill in here, please note you must have more arguments here
		setTripId(gettripid);
		setTripName(tripname);
		setDate(date);
		setDestination(destination);
		setFriends(friends);
		
	}

	/**
	 * Serialize Trip object by using writeToParcel. 
	 * This function is automatically called by the
	 * system when the object is serialized.
	 * 
	 * @param dest Parcel object that gets written on 
	 * serialization. Use functions to write out the
	 * object stored via your member variables. 
	 * 
	 * @param flags Additional flags about how the object 
	 * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
	 * In our case, you should be just passing 0.
	 */
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		
		// TODO - fill in here 
		arg0.writeString(tripId);
		arg0.writeString(tripName);
		arg0.writeString(date);
		arg0.writeString(destination);
		arg0.writeString(friends);
	}
	
	/**
	 * Feel free to add additional functions as necessary below.
	 */
	// This method is used for create JSON object from trip
	public JSONObject toJSON() throws JSONException {
		Log.i(TAG, "Trips write to json");
		JSONObject json = new JSONObject();
		json.put(JSON_ID, String.valueOf(tripId));
		json.put(JSON_DESTINATION, destination);
		json.put(JSON_PEOPLE, new JSONArray(people));
		json.put(JSON_DATE, date);

		return json;
	}
	/**
	 * Do not implement
	 */
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getTripName() {
		return tripName;
	}

	public void setTripName(String tripName) {
		this.tripName = tripName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public ArrayList<String> getPeople() {
		return people;
	}

	public void setPeople(ArrayList<String> people) {
		this.people = people;
	}

}
