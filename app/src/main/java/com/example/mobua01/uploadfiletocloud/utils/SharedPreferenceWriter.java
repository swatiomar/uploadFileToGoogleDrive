package com.example.mobua01.uploadfiletocloud.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SharedPreferenceWriter {
	private static Context context = null;
	private static SharedPreferenceWriter sharePref = null;
	private static SharedPreferences mPrefs;
	private static SharedPreferences.Editor prefEditor = null;

	public static SharedPreferenceWriter getInstance(Context c) {
		context = c;
		if (null == sharePref) {
			sharePref = new SharedPreferenceWriter();
			mPrefs = context.getSharedPreferences("SUPLIER_RESPONSE", Context.MODE_PRIVATE);
			prefEditor = mPrefs.edit();
		}
		return sharePref;
	}

	private SharedPreferenceWriter() {
		// TODO Auto-generated constructor stub
	}
	public void writeStringValue(String key, String value) {

		prefEditor.putString(key, value);
		prefEditor.commit();
	}

	public void writeIntValue(String key, int value) {

		prefEditor.putInt(key, value);
		prefEditor.commit();
	}

	
	public void writeBooleanValue(String key, boolean value) {

		prefEditor.putBoolean(key, value);
		prefEditor.commit();
	}

	public void writeLongValue(String key, long value) {

		prefEditor.putLong(key, value);
		prefEditor.commit();
	}
	public void writeFloatValue(String key, float value) {

		prefEditor.putFloat(key, value);
		prefEditor.commit();
	}

	public void clearPreferenceValue(String key, String value) {

		prefEditor.putString(key, "");
		prefEditor.commit();

	}

	public String getString(String key) {
			return mPrefs.getString(key, null);

	}

	public int getInt(String key) {

		return mPrefs.getInt(key, 0);
	}
	public float getFloat(String key) {

		return mPrefs.getFloat(key, 0);
	}
	/*public int getInt(String key,int i) {

		return mPrefs.getInt(key, i);
	}*/
	public boolean getBoolean(String key) {

		return mPrefs.getBoolean(key, false);
	}

	public long getLong(String key) {

		return mPrefs.getLong(key, (long) 0.0);
	}

	public void clearPreferenceValues(String key) {
		prefEditor.clear();
		prefEditor.commit();

	}

	/**stores dataObject to SharedPreferences and commit the user default. This must be called in main thread*/
	public synchronized static void saveDataIntoPreferences(Object dataObject, String key)   {
		Gson gson = new Gson();
		String json = gson.toJson(dataObject);
		int size = json.length();
//		Log.v(TAG,"size of json "+size);

		SharedPreferences.Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString(key, json);
		prefsEditor.commit();
	}

	/**Return the dataObject of type typeOfT class if successfull. if fail to get data from SharedPreferences then returns null*/
	public synchronized static <T> T getDataFromPreferences(String key, Type typeOfT)   {
		T dataObject = null;
		try {
			Gson gson = new Gson();
			String jsonString = mPrefs.getString(key, "");
			dataObject = gson.fromJson(jsonString, typeOfT);
		}catch (Exception e)    {
			e.printStackTrace();
		}
		return dataObject;
	}
}
