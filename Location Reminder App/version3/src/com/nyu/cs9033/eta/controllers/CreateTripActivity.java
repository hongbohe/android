package com.nyu.cs9033.eta.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class CreateTripActivity extends Activity {

	private static final String TAG = "Create Trip Acitivity";

	public static final int PICK_CONTACT = 1;
	private EditText et1;
	private String tripname;
	private String triptime;
	private String partnername;
	private String tripid;
	private Intent intent;
	private TextView txtContacts;
	
	private EditText mDate;
	private EditText mTime;
	
	private Calendar c;
	private EditText sLocation;
	private String fullDestination = null;
	static final int REQUEST_SELECT_CONTACT = 1;
	static final int REQUEST_LOCATION = 5;
	
	private EditText mTheme;
	private TextView vName;
	private TextView vAddress;
	private TextView vLat;
	private TextView vLgt;

	private static ArrayList<String> namelist;
	private static ArrayList<String> locationlist;
	private TripDatabaseHelper dbHelper;
	private int cnt = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
		setContentView(R.layout.activity_createtrip);
		dbHelper = new TripDatabaseHelper(this);

		sLocation = (EditText) findViewById(R.id.locationText);
		mTheme = (EditText) findViewById(R.id.search_theme);
		vName = (TextView) findViewById(R.id.locname);
		vAddress = (TextView) findViewById(R.id.locaddress);
		vLat = (TextView) findViewById(R.id.loclat);
		vLgt = (TextView) findViewById(R.id.loclgt);		
		
		
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
		
		mTime = (EditText) findViewById(R.id.TPtimeinput);
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
		
		txtContacts = (TextView) findViewById(R.id.txt_contacts);
		intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			}
		}

		namelist = new ArrayList<String>();
		locationlist = new ArrayList<String>();
		onContact();
		ondone();
		cancelTrip();

	}

	// handle text/plain from google map
	public void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {
			// Update UI to reflect text being shared
			EditText tv = (EditText) findViewById(R.id.locationText);
			String[] res = sharedText.split("http");
			tv.setText(res[0]);
		}
	}
	
	/**
	 * This method should be used to instantiate a Trip model object.
	 * 
	 * @return The Trip as represented by the View.
	 */
	
	public Trip createTrip() {

		// TODO - fill in here
		return new Trip(tripid, tripname, triptime, fullDestination, partnername);
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
	public void saveTrip(Trip trip) {

		// TODO - fill in here

		dbHelper.insertTrip(trip);
		Toast.makeText(CreateTripActivity.this, "Create a trip successfully!",
				1000).show();
		finish();
	}

	public void ondone() {
		final Button toMain = (Button) findViewById(R.id.buttondone);
		toMain.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				et1 = (EditText) findViewById(R.id.Tripnameinput);
				tripname = et1.getText().toString();


				triptime = mDate.getText().toString() + " " + mTime.getText().toString();


				et1 = (EditText) findViewById(R.id.TPstarternameinput);
				String startname = et1.getText().toString();

				startname += " ";
				startname += partnername;

				partnername = startname;
				// add post function here.
				//New part for geting trip_id from web server.
				if (isConnected()) {
					new HttpAsyncTask()
							.execute("http://cs9033-homework.appspot.com");
				} else {
					txtContacts.setText("You are NOT conncted");
				}
			}
		});
	}

	// If you choose the button to add friends from contact ,this method will be
	// called, once return one friend's name
	public void onContact() {
		final Button toContact = (Button) findViewById(R.id.viewContact);
		toContact.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
			}
		});
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


	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);


		if (reqCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
				ContentResolver contentResolver = getContentResolver();

				Uri contactData = data.getData();
				Cursor c = contentResolver.query(contactData, null, null, null,
						null);
				if (c.moveToFirst()) {
					String name = c.getString(c
							.getColumnIndexOrThrow(Contacts.DISPLAY_NAME));
					if (namelist.contains(name)) {
						String message = "You have added " + name
								+ ", Please pick another person\n"
								+ "Now You have add " + partnername;
						txtContacts.setText(message);
						return;
					} else {
						namelist.add(name);
						if (cnt == 0) {
							partnername = name;
							cnt++;
						} else
							partnername += name;
						partnername += " ";
						String message = "Now You have add " + partnername;
						txtContacts.setText(message);
					}
				}
			}else {
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
					fullDestination = vNameGet + " " + vAddressGet + " " + vLatGet
							+ " " + vLgtGet;
					locationlist.add(vNameGet);
					locationlist.add(vAddressGet);
					locationlist.add(vLatGet);
					locationlist.add(vLgtGet);
					Toast.makeText(getApplicationContext(), fullDestination,
							Toast.LENGTH_SHORT).show();
				} 
			}
		

	}

	public void cancelTrip() {

		// TODO - fill in here

		final Button cancel = (Button) findViewById(R.id.buttoncancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				finish();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	/*********** new functions are here: ****************/

	// This method is used to check the status of the network info.
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	// This method is used to change the received json object to json string.
	public static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

	// This method is used to post command and data to server, and receive
	public String POST(String url) {
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("command", "CREATE_TRIP");
			JSONArray jarraylocation = new JSONArray(locationlist);
			jsonObject.accumulate("location",jarraylocation);
			Date date = new Date();
			jsonObject.accumulate("datetime", date.getTime());
			JSONArray jarraypeople = new JSONArray(namelist);
			jsonObject.accumulate("people", jarraypeople);

			// 4. convert JSONObject to JSON to String
			json = jsonObject.toString();
			Log.i(TAG, json);

			// ** Alternative way to convert Person object to JSON string usin
			// Jackson Lib
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(person);

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d(TAG, e.getLocalizedMessage());
		}

		// 11. return result
		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return POST(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
	
			try {
				JSONObject json = new JSONObject(result);
				int status = json.getInt("response_code");
				if (status == 0) {
					Toast.makeText(getBaseContext(),
							"Data received correctly!", Toast.LENGTH_LONG)
							.show();
					tripid = String.valueOf(json.getLong("trip_id"));
					Trip tmptrip = createTrip();
					saveTrip(tmptrip);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, "JSON parse failed");
			}
		}
	}	

}
