package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class ViewTripActivity extends Activity {

	private static final String TAG = "ViewTripActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
		Trip trip = null;
		trip = getTrip(getIntent());
		if(trip != null){
			setContentView(R.layout.activity_viewtrip);
			viewTrip(trip);
		}
		else{
			setContentView(R.layout.error_msg);
		}
		
		

	}

	/**
	 * Create a Trip object via the recent trip that was passed to TripViewer
	 * via an Intent.
	 * 
	 * @param i
	 *            The Intent that contains the most recent trip data.
	 * 
	 * @return The Trip that was most recently passed to TripViewer, or null if
	 *         there is none.
	 */
	public Trip getTrip(Intent i) {

		// TODO - fill in here
		Trip trip = i.getParcelableExtra("created trip");
		return trip;
	}

	/**
	 * Populate the View using a Trip model.
	 * 
	 * @param trip
	 *            The Trip model used to populate the View.
	 */
	public void viewTrip(Trip trip) {

		// TODO - fill in here
		TextView etName = (TextView) findViewById(R.id.etVName);
		etName.setText(trip.getName());
		TextView etLocation = (TextView) findViewById(R.id.etVLocation);
		etLocation.setText(trip.getLocation());
		TextView etTime = (TextView) findViewById(R.id.etVTime);
		etTime.setText(trip.getTime());
		TextView etFriends = (TextView) findViewById(R.id.etVFriends);
		etFriends.setText(trip.getFriends());
	}
}
