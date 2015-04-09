package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class CreateTripActivity extends Activity {

	private static final String TAG = "CreateTripActivity";
	Trip trip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
		setContentView(R.layout.activity_createtrip);

		Button btCreateTrip = (Button) findViewById(R.id.btNCreateTrip);
		Button btCancelTrip = (Button) findViewById(R.id.btCancelTrip);

		btCreateTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Trip trip = createTrip();
				saveTrip(trip);
			}

		});
		
		btCancelTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				cancelTrip();

			}

		});

	}

	/**
	 * This method should be used to instantiate a Trip model object.
	 * 
	 * @return The Trip as represented by the View.
	 */
	public Trip createTrip() {

		// TODO - fill in here
		EditText etName = (EditText) findViewById(R.id.etName);
		String name = etName.getText().toString();
		EditText etLocation = (EditText) findViewById(R.id.etLocation);
		String location = etLocation.getText().toString();
		EditText etTime = (EditText) findViewById(R.id.etTime);
		String time = etTime.getText().toString();
		EditText etFriends = (EditText) findViewById(R.id.etFriends);
		String friends = etFriends.getText().toString();
		Trip trip = new Trip(name, location, time, friends);
		return trip;
	}

	/**
	 * For HW2 you should treat this method as a way of sending the Trip data
	 * back to the main Activity.
	 * 
	 * Note: If you call finish() here the Activity will end and pass an Intent
	 * back to the previous Activity using setResult().
	 * 
	 * @return whether the Trip was successfully saved.
	 */
	public boolean saveTrip(Trip trip) {

		// TODO - fill in here
		createTrip();
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra("create trip", trip);
		setResult(1, i);
		finish();
		return false;
	}

	/**
	 * This method should be used when a user wants to cancel the creation of a
	 * Trip.
	 * 
	 * Note: You most likely want to call this if your activity dies during the
	 * process of a trip creation or if a cancel/back button event occurs.
	 * Should return to the previous activity without a result using finish()
	 * and setResult().
	 */
	public void cancelTrip() {

		// TODO - fill in here
		Intent i = new Intent(this, MainActivity.class);
		finish();
	}
}
