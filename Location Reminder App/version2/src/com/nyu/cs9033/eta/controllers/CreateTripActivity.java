package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.models.TripDatabaseHelper;

public class CreateTripActivity extends Activity {

	private static final String TAG = "CreateTripActivity";

	private Button mCancelButton;
	private Button mCreateButton;
	private EditText mDate;
	private EditText mTime;
	private EditText mDestination;
	private EditText sLocation;
	private EditText mFriend;

	private EditText mTheme;
	private TextView vName;
	private TextView vAddress;
	private TextView vLat;
	private TextView vLgt;

	private Trip mTrip;
	private Calendar c;
	private TripDatabaseHelper dbHelper;
	static final int REQUEST_SELECT_CONTACT = 1;
	static final int REQUEST_LOCATION = 5;
	private String Destination = null;
	public ArrayList<String> mContactsName = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
		setContentView(R.layout.activity_createtrip);

		dbHelper = new TripDatabaseHelper(this);

		sLocation = (EditText) findViewById(R.id.search_loc);
		mTheme = (EditText) findViewById(R.id.search_theme);
		vName = (TextView) findViewById(R.id.locname);
		vAddress = (TextView) findViewById(R.id.locaddress);
		vLat = (TextView) findViewById(R.id.loclat);
		vLgt = (TextView) findViewById(R.id.loclgt);

		/*
		 * if (Intent.ACTION_SEND.equals(action) && type != null) { // Figure
		 * out what to do based on the intent type if
		 * (type.equals("text/plain")) { // Handle intents with text ... if
		 * (back != null && back.size() > 0) { String vNameGet = back.get(0);
		 * vName.setText(vNameGet); String vAddressGet = back.get(1);
		 * vAddress.setText(vAddressGet); String vLatGet = back.get(2);
		 * vLat.setText(vLatGet); String vLgtGet = back.get(3);
		 * vLgt.setText(vLgtGet); Destination =
		 * vNameGet+" "+vAddressGet+" "+vLatGet+ " "+vLgtGet;
		 * Toast.makeText(getApplicationContext(), Destination,
		 * Toast.LENGTH_SHORT).show(); } } }
		 */

		mCancelButton = (Button) findViewById(R.id.create_trip_cancel_button);
		mCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelTrip();
			}
		});
		c = Calendar.getInstance();
		mDate = (EditText) findViewById(R.id.create_trip_date);
		mDate.setFocusable(false);
		mDate.setClickable(true);
		mDate.setInputType(InputType.TYPE_NULL);

		mDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(CreateTripActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker dp, int year,
									int month, int dayOfMonth) {
								// TODO Auto-generated method stub
								mDate.setText(year + "-" + (month + 1) + "-"
										+ dayOfMonth);

							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();

			}

		});

		mTime = (EditText) findViewById(R.id.create_trip_time);
		mTime.setFocusable(false);
		mTime.setClickable(true);
		mTime.setInputType(InputType.TYPE_NULL);

		mTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(CreateTripActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								mTime.setText(hourOfDay + ":" + minute);
							}
						}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
						true).show();
			}
		});

		Button mSearch = (Button) findViewById(R.id.btsearch);
		mSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri location = Uri.parse("location://com.example.nyu.hw3api");

				Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
				String loc = sLocation.getText().toString();
				String theme = mTheme.getText().toString();
				String searchText = loc + "::" + theme;
				mapIntent.putExtra("searchVal", searchText);
				PackageManager packageManager = getPackageManager();
				List<ResolveInfo> activities = packageManager
						.queryIntentActivities(mapIntent, 0);
				boolean isIntentSafe = activities.size() > 0;

				if (isIntentSafe) {
					startActivityForResult(mapIntent, REQUEST_LOCATION);
				}
			}
		});

		mFriend = (EditText) findViewById(R.id.create_trip_friends);

		mFriend.setClickable(true);

		mFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mFriend.setText(mContactsName.toString());
				selectContact();

			}

		});

		mCreateButton = (Button) findViewById(R.id.create_trip_submit_button);
		mCreateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mTrip = new Trip(1, mDate.getText().toString() + " "
						+ mTime.getText().toString(), Destination, mContactsName.toString());
				saveTrip(mTrip);

			}
		});
	}

	/**
	 * This method should be used to instantiate a Trip model object.
	 * 
	 * @return The Trip as represented by the View.
	 */
	/*
	 * public Trip createTrip() {
	 * 
	 * // TODO - fill in here EditText etName = (EditText)
	 * findViewById(R.id.etName); String name = etName.getText().toString();
	 * EditText etLocation = (EditText) findViewById(R.id.etLocation); String
	 * location = etLocation.getText().toString(); EditText etTime = (EditText)
	 * findViewById(R.id.etTime); String time = etTime.getText().toString();
	 * EditText etFriends = (EditText) findViewById(R.id.etFriends); String
	 * friends = etFriends.getText().toString(); Trip trip = new Trip(name,
	 * location, time, friends); return trip; }
	 */

	/**
	 * For HW2 you should treat this method as a way of sending the Trip data
	 * back to the main Activity.
	 * 
	 * Note: If you call finish() here the Activity will end and pass an Intent
	 * back to the previous Activity using setResult().
	 * 
	 * @return whether the Trip was successfully saved.
	 */
	public void saveTrip(Trip trip) {

		// TODO - fill in here
		/*
		 * createTrip(); Intent i = new Intent(this, MainActivity.class);
		 * i.putExtra("create trip", trip); setResult(1, i); finish(); return
		 * false;
		 */
		insertData(dbHelper.getReadableDatabase(), trip.getDate(),
				trip.getDestination(), trip.getFriends());
		Toast.makeText(CreateTripActivity.this, "Create a trip successfully!",
				1000).show();
		finish();
	}

	private void insertData(SQLiteDatabase db, String date_time, String desc,
			String friends) {
		// ÷¥––≤Â»Î”Ôæ‰
		db.execSQL("insert into trip values(null , ? , ?, ?)", new String[] {
				date_time, desc, friends });
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

	public void selectContact() {

		Intent pickContactIntent = new Intent(Intent.ACTION_PICK);
		pickContactIntent.setType(CommonDataKinds.Phone.CONTENT_TYPE); 

		if (pickContactIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(pickContactIntent, REQUEST_SELECT_CONTACT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
			Uri contactUri = data.getData();
			String[] projection = new String[] { CommonDataKinds.Phone.DISPLAY_NAME };
			Cursor cursor = getContentResolver().query(contactUri, projection,
					null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					int nameIndex = cursor
							.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME);
					String name = cursor.getString(nameIndex);
					mContactsName.add(name);
				}
			}
		} else {
			List<String> back = null;
			back = (ArrayList<String>) data.getStringArrayListExtra("retVal");
			if (back != null) {
				String vNameGet = back.get(0);
				vName.setText(vNameGet);
				String vAddressGet = back.get(1);
				vAddress.setText(vAddressGet);
				String vLatGet = back.get(2);
				vLat.setText(vLatGet);
				String vLgtGet = back.get(3);
				vLgt.setText(vLgtGet);
				Destination = vNameGet + " " + vAddressGet + " " + vLatGet
						+ " " + vLgtGet;
				Toast.makeText(getApplicationContext(), Destination,
						Toast.LENGTH_SHORT).show();
			} 
		}
	}

	public void cancelTrip() {

		// TODO - fill in here
		/*
		 * Intent i = new Intent(this, MainActivity.class); finish();
		 */

		Intent i = new Intent(CreateTripActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

}
