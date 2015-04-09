package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	Trip trip = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO - fill in here
		Button btViewTrip = (Button) findViewById(R.id.btViewTrip);
		Button btCreateTrip = (Button) findViewById(R.id.btCreateTrip);

		btViewTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				startViewTripActivity();
			}
		});

		btCreateTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				startCreateTripActivity();
			}
		});

	}

	/**
	 * This method should start the Activity responsible for creating a Trip.
	 */
	public void startCreateTripActivity() {

		// TODO - fill in here
		Intent i = new Intent(this, CreateTripActivity.class);
		startActivityForResult(i, 1);
	}

	/**
	 * This method should start the Activity responsible for viewing a Trip.
	 */
	public void startViewTripActivity() {

		// TODO - fill in here
		Intent i = new Intent(this, ViewTripActivity.class);

		i.putExtra("created trip", trip);			
		startActivity(i);
		
	}

	/**
	 * Receive result from CreateTripActivity here. Can be used to save instance
	 * of Trip object which can be viewed in the ViewTripActivity.
	 * 
	 * Note: This method will be called when a Trip object is returned to the
	 * main activity. Remember that the Trip will not be returned as a Trip
	 * object; it will be in the persisted Parcelable form. The actual Trip
	 * object should be created and saved in a variable for future use, i.e. to
	 * view the trip.
	 * 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO - fill in here
		if(resultCode == 1)trip = data.getParcelableExtra("create trip");
	}
}
