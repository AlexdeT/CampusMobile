package fr.alexdet.android.model.webservices;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Abstract class - Contain a function to parse the InputStream in order to
 * return a JSON Object
 * 
 * @author alexisdetalhouet
 * 
 */
public abstract class JSONParser {

	static JSONObject jObj = null;
	static String jsonLine = "";

	/**
	 * Get the JSON Object from the InputStream returned by the server
	 * 
	 * @param json
	 *            InputStream
	 * @return a JSONObject
	 */
	public static JSONObject getJSONObject(InputStream json) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					json, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			json.close();
			jsonLine = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.i("JSONParser", jsonLine);

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(jsonLine);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
	}
}
