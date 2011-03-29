package com.adakoda.android.setsudenyahooapisample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.adakoda.android.yahoo.setsuden.LatestPowerUsageResult;
import com.adakoda.android.yahoo.setsuden.SetsudenClient;

public class SetsudenYahooAPISampleActivity extends Activity {

	private static final String TAG = "SetsudenYahooAPISampleActivity";
	private static final String MY_APP_ID = "XXXXXXXXXX"; // TODO:Pleas put your app id
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SetsudenClientTask setsudenClientTask = new SetsudenClientTask();
		setsudenClientTask.execute();
	}

	class SetsudenClientTask extends AsyncTask<Void, Void, LatestPowerUsageResult> {
		
		protected LatestPowerUsageResult doInBackground(Void... params) {
			SetsudenClient setsudenClient = new SetsudenClient(MY_APP_ID);
			return setsudenClient.getLatestPowerUsage();
		}

		protected void onPostExecute(LatestPowerUsageResult result) {
			// Output result to LogCat
			Log.v(TAG, "Area = " + result.getArea());
			Log.v(TAG, "Usage = " + result.getUsageValue() + result.getUsageUnit());
			Log.v(TAG, "Capacity = " + result.getCapacityValue() + result.getCapacityUnit());
			Log.v(TAG, "Date = " + result.getDate());
			Log.v(TAG, "Hour = " + result.getHour());
		}
	}
}
