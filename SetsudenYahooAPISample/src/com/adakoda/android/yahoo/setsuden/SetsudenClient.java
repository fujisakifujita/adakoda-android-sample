package com.adakoda.android.yahoo.setsuden;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public class SetsudenClient {

	// http://developer.yahoo.co.jp/webapi/shinsai/setsuden/v1/latestpowerusage.html
	private static final String REQUEST_URL = "http://setsuden.yahooapis.jp/v1/Setsuden/latestPowerUsage";
	private static final String PARAM_KEY_APPID = "appid";
	private static final String PARAM_KEY_OUTPUT = "output";
	private static final String PARAM_VALUE_OUTPUT_JSON = "json";

	private static final String ELECTRIC_POWER_USAGE = "ElectricPowerUsage"; // String
	private static final String AREA = "Area"; // String
	private static final String USAGE = "Usage"; // Object
	private static final String CAPACITY = "Capacity"; // Object
	private static final String DATE = "Date"; // String
	private static final String HOUR = "Hour"; // String

	// for Usage and Capacity
	private static final String UNIT = "@unit"; // String
	private static final String VALUE = "$"; // int

	private String mAppID;

	public SetsudenClient(String appID) {
		mAppID = appID;
	}

	public LatestPowerUsageResult getLatestPowerUsage() {
		LatestPowerUsageResult result = new LatestPowerUsageResult();
		// Build uri
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.path(REQUEST_URL);
		uriBuilder.appendQueryParameter(PARAM_KEY_APPID, mAppID);
		uriBuilder.appendQueryParameter(PARAM_KEY_OUTPUT, PARAM_VALUE_OUTPUT_JSON);
		String uri = Uri.decode(uriBuilder.build().toString());
		// Request HTTP GET
		HttpUriRequest httpGet = new HttpGet(uri);
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
			String stringEntity = EntityUtils.toString(httpResponse.getEntity());
			// Parse JSON result
			JSONObject jsonEntity = new JSONObject(stringEntity);
			if (jsonEntity != null) {
				JSONObject jsonElectricPowerUsage =
					jsonEntity.optJSONObject(ELECTRIC_POWER_USAGE);
				if (jsonElectricPowerUsage != null) {
					result.setArea(jsonElectricPowerUsage.optString(AREA));
					JSONObject jsonUsage =
						jsonElectricPowerUsage.optJSONObject(USAGE);
					if (jsonUsage != null) {
						result.setUsageUnit(jsonUsage.optString(UNIT));
						result.setUsageValue(jsonUsage.optInt(VALUE));
					}
					JSONObject jsonCapacity =
						jsonElectricPowerUsage.optJSONObject(CAPACITY);
					if (jsonCapacity != null) {
						result.setCapacityUnit(jsonCapacity.optString(UNIT));
						result.setCapacityValue(jsonCapacity.optInt(VALUE));
					}
					result.setDate(jsonElectricPowerUsage.optString(DATE));
					result.setHour(jsonElectricPowerUsage.optString(HOUR));
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
