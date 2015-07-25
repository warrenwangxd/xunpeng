package com.warren.contact.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Path.FillType;
import android.location.Address;

import com.warren.contact.domain.FriendLocation;
import com.warren.contact.domain.Location;
import com.warren.contact.domain.User;
import com.warren.contact.utils.Constants;
import com.warren.contact.utils.JsonUtil;
import com.warren.contact.utils.StreamTool;
import com.warren.contact.utils.StringUtils;

public class LocationService {

	public static String updateLocation(User user, Address address)
			throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(
				Constants.UPDATE_LOCATION).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");
		StringBuffer addressLine = new StringBuffer();
		for (int i = 0; i < address.getMaxAddressLineIndex() + 1; i++) {
			addressLine.append(address.getAddressLine(i)).append("-");
		}
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (!StringUtils.isEmpty(user.getDeviceId())) {
			json.append(JsonUtil.DEVICE_NODE).append(":\"")
					.append(user.getDeviceId()).append("\",");
		} else {
			json.append(JsonUtil.DEVICE_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getPhone())) {
			json.append(JsonUtil.PHONE_NODE).append(":\"")
					.append(user.getPhone()).append("\",");
		} else {
			json.append(JsonUtil.PHONE_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getUid())) {
			json.append(JsonUtil.UID_NODE).append(":\"").append(user.getUid())
					.append("\",");
		} else {
			json.append(JsonUtil.UID_NODE).append(":\"").append("")
					.append("\",");
		}
		json.append(JsonUtil.LOCATION_LONGITUDE).append(":\"")
				.append(address.getLongitude()).append("\",");
		json.append(JsonUtil.LOCATION_LATITUDE).append(":\"")
				.append(address.getLatitude()).append("\",");
		json.append(JsonUtil.LOCATION_ADDRESS_NODE).append(":\"")
				.append(addressLine).append("\",");
		json.append(JsonUtil.LOCATION_COUNTRY_NODE).append(":\"")
				.append(address.getCountryName()).append("\",");
		json.append(JsonUtil.LOCATION_PROVINCE_NODE).append(":\"")
				.append(address.getAdminArea()).append("\",");
		json.append(JsonUtil.LOCATION_CITY_NODE).append(":\"")
				.append(address.getLocality()).append("\",");
		json.append(JsonUtil.LOCATION_COMMUNITY_NODE).append(":\"")
				.append(address.getFeatureName()).append("\"}");

		conn.getOutputStream().write(json.toString().getBytes());
		if (conn.getResponseCode() == 200) {
			try {
				InputStream jsonStream = conn.getInputStream();
				byte[] data;

				data = StreamTool.read(jsonStream);

				String repJson = new String(data, "UTF-8");
				JSONObject jsonObj = new JSONObject(repJson);
				String result = jsonObj.getString(JsonUtil.RESULT_NODE);

				return result;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		else {
			return null;
		}
	}

	/**
	 * 返回的json格式形如:
	 * {18081967335:[{phone:"18081971036",name:"小菊",latitude:"30.1234897344"
	 * ,longitude
	 * :"140.12445677544",locFeature:"蜀郡",locTime:"2015-06-12 HH:mm:ss"},
	 * {phone:"18081971036",name:"小菊",latitude:"30.1234897344",longitude:
	 * "140.12445677544",locFeature:"蜀郡",locTime:"2015-06-12 HH:mm:ss}]}
	 * 
	 * @param phone
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<FriendLocation> getFriendsLocation(String phone,
			String uid) throws MalformedURLException, IOException {
		List<FriendLocation> friendLocationList = new ArrayList<FriendLocation>();
		HttpURLConnection conn = (HttpURLConnection) new URL(
				Constants.GET_FRIENDS_LOCATION).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");

		StringBuilder json = new StringBuilder();
		json.append("{");
		if (!StringUtils.isEmpty(phone)) {
			json.append(JsonUtil.PHONE_NODE).append(":\"").append(phone)
					.append("\",");
		} else {
			json.append(JsonUtil.PHONE_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(uid)) {
			json.append(JsonUtil.UID_NODE).append(":\"").append(uid)
					.append("\",");
		} else {
			json.append(JsonUtil.UID_NODE).append(":\"").append("")
					.append("\",");
		}
		json.deleteCharAt(json.length() - 1);
		json.append("}");
		conn.getOutputStream().write(json.toString().getBytes());
		if (conn.getResponseCode() == 200) {
			try {
				InputStream jsonStream = conn.getInputStream();
				byte[] data;
				data = StreamTool.read(jsonStream);
				String repJson = new String(data, "UTF-8");
				JSONObject jsonObj = new JSONObject(repJson);
				if (!StringUtils.isEmpty(phone)) {
					JSONArray locationArray = jsonObj.getJSONArray(phone);
					fillFriendLocationList(friendLocationList, locationArray);
				} else if (!StringUtils.isEmpty(uid)) {

					JSONArray locationArray = jsonObj.getJSONArray(uid);
					fillFriendLocationList(friendLocationList, locationArray);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

		return friendLocationList;
	}

	private static void fillFriendLocationList(
			List<FriendLocation> friendLocationList, JSONArray locationArray)
			throws JSONException {
		for (int i = 0; i < locationArray.length(); i++) {
			JSONObject jsonObject = locationArray.getJSONObject(i);
			String friendName = jsonObject.getString(JsonUtil.USER_NAME_NODE);
			String friendPhone = jsonObject.getString(JsonUtil.PHONE_NODE);
			String friendSex = jsonObject.getString(JsonUtil.USER_SEX_NODE);
			String latitude = jsonObject.getString(JsonUtil.LOCATION_LATITUDE);
			String longitude = jsonObject
					.getString(JsonUtil.LOCATION_LONGITUDE);
			String locTime = jsonObject.getString(JsonUtil.LOCATION_TIME);
			String locFeature = jsonObject.getString(JsonUtil.LOCATION_FEATURE);
			String locationPublic = jsonObject
					.getString(JsonUtil.LOCATION_PUBLIC_NODE);

			FriendLocation friendLocation = new FriendLocation();
			Location location = new Location(Double.valueOf(latitude),
					Double.valueOf(longitude));
			location.setLocationFeature(locFeature);
			location.setLocTime(new Date(Long.valueOf(locTime)));
			friendLocation.setLocation(location);
			User user = new User();
			user.setPhone(friendPhone);
			user.setUserName(friendName);
			user.setUserSex(friendSex);
			user.setLocationPublic(locationPublic);
			friendLocation.setUser(user);
			friendLocationList.add(friendLocation);
		}
	}

}
