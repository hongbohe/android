package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class ViewTripActivity extends Activity {

	private Trip trip;
	private TextView et;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here

		
		trip = getTrip(getIntent());
		viewTrip(trip);
		

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
		return i.getParcelableExtra("receive");
	}

	/**
	 * Populate the View using a Trip model.
	 * 
	 * @param trip
	 *            The Trip model used to populate the View.
	 */
	public void viewTrip(Trip trip) {

		// TODO - fill in here
		if (trip == null) {
			setContentView(R.layout.activity_viewtripnull);
		} else {
			setContentView(R.layout.activity_viewtrip);
			startMainCurrentTrip();
			et = (TextView) findViewById(R.id.VTPTripidValue);
			et.setText(trip.getTripId());
			
			et = (TextView) findViewById(R.id.VTPname_Value);
			et.setText(trip.getTripName());

			et = (TextView) findViewById(R.id.VTPlocation_Value);
			et.setText(trip.getDestination());

			et = (TextView) findViewById(R.id.VTPtime_Value);
			et.setText(trip.getDate());
			// ////
			et = (TextView) findViewById(R.id.VTPstartername_Value);
			et.setText(trip.getFriends());
			
	
		}
	}
	
	//This method is used to set current_trip to show real time status in main activity
	public void startMainCurrentTrip() {
		final Button setCurrentTrip = (Button) findViewById(R.id.setcurrent);
		setCurrentTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intentSetCurTrip = new Intent(ViewTripActivity.this,
						MainActivity.class);
				intentSetCurTrip.putExtra("curtrip", trip);
				startActivity(intentSetCurTrip);
			}
		});
	}
}
