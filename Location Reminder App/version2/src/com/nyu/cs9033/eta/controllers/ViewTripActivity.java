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
	private Trip mTrip;
	private TextView et;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
	/*	Trip trip = null;
		trip = getTrip(getIntent());
		if(trip != null){
			setContentView(R.layout.activity_viewtrip);
			viewTrip(trip);
		}
		else{
			setContentView(R.layout.error_msg);
		}*/
		
		Intent intent = getIntent();
		mTrip=getTrip(intent);
		viewTrip(mTrip);
		

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
		Trip trip = i.getParcelableExtra("tripCreated");
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
		setContentView(R.layout.activity_viewtrip);
		et = (TextView) findViewById(R.id.tripDate);
		et.setText(trip.getDate());

		et = (TextView) findViewById(R.id.tripDestination);
		et.setText(trip.getDestination());

		et = (TextView) findViewById(R.id.friendName);
		et.setText(trip.getFriends());
	}
}
