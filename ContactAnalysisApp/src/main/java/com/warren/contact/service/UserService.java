package com.warren.contact.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.warren.contact.domain.User;
import com.warren.contact.utils.Constants;
import com.warren.contact.utils.JsonUtil;
import com.warren.contact.utils.StreamTool;
import com.warren.contact.utils.StringUtils;

public class UserService {
	private static User currentUser;

	public static User getCurrentUser(Activity activty) {
		if(currentUser==null||currentUser.isEmpty()) {
			currentUser = getUserInfoFromSession(activty);
		}
		return currentUser;
	}

	public static void setCurrentUser(Activity activity,User currentUser) {
		UserService.currentUser = currentUser;
		putSession(activity, currentUser);
	}

	public static String userRegister(User user, String verifyCode) {
		String registerResult = "";
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(Constants.USER_REGISTER_URL)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			StringBuilder json = new StringBuilder();
			json.append("{").append(JsonUtil.PHONE_NODE).append(":\"")
					.append(user.getPhone()).append("\",");
			json.append(JsonUtil.USER_PWD_NODE).append(":\"")
					.append(user.getPwd()).append("\",");
			json.append(JsonUtil.VERIFY_CODE).append(":\"").append(verifyCode)
					.append("\",");
			json.append(JsonUtil.DEVICE_NODE).append(":\"")
					.append(user.getDeviceId()).append("\"}");
			conn.getOutputStream().write(json.toString().getBytes());
			if (conn.getResponseCode() == 200) {

				try {
					InputStream jsonStream = conn.getInputStream();
					byte[] data;

					data = StreamTool.read(jsonStream);

					String repJson = new String(data, "UTF-8");
					JSONObject jsonObj = new JSONObject(repJson);
					registerResult = jsonObj.getString(JsonUtil.RESULT_NODE);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return registerResult;
	}

	public static String userInfoModifyWithVerifyCode(User user , String verifyCode) {
		String modifyResult = "";
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(Constants.USER_INFO_MODIFY_URL)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append(JsonUtil.VERIFY_CODE).append(":\"").append(verifyCode).append("\",");
			json.append(fillUserModifyJson(user, json));
			conn.getOutputStream().write(json.toString().getBytes());
			if (conn.getResponseCode() == 200) {

				try {
					InputStream jsonStream = conn.getInputStream();
					byte[] data;

					data = StreamTool.read(jsonStream);

					String repJson = new String(data, "UTF-8");
					JSONObject jsonObj = new JSONObject(repJson);
					modifyResult = jsonObj.getString(JsonUtil.RESULT_NODE);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return modifyResult;
	}
	
	private static StringBuilder fillUserModifyJson(User user, StringBuilder json) {
		if (!StringUtils.isEmpty(user.getPhone())) {
			json.append(JsonUtil.PHONE_NODE).append(":\"")
					.append(user.getPhone()).append("\",");
		} else {
			json.append(JsonUtil.PHONE_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getScreenName())) {
			json.append(JsonUtil.USER_SCREEN_NAME).append(":\"")
					.append(user.getScreenName()).append("\",");
		} else {
			json.append(JsonUtil.USER_SCREEN_NAME).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getUid())) {
			json.append(JsonUtil.UID_NODE).append(":\"")
					.append(user.getUid()).append("\",");
		} else {
			json.append(JsonUtil.UID_NODE).append(":\"").append("")
					.append("\",");
		}

		if (!StringUtils.isEmpty(user.getPwd())) {
			json.append(JsonUtil.USER_PWD_NODE).append(":\"")
					.append(user.getPwd()).append("\",");
		} else {
			json.append(JsonUtil.USER_PWD_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getUserName())) {
			json.append(JsonUtil.USER_NAME_NODE).append(":\"")
					.append(user.getUserName()).append("\",");
		} else {
			json.append(JsonUtil.USER_NAME_NODE).append(":\"")
					.append(user.getUserName()).append("\",");
		}
		if (!StringUtils.isEmpty(user.getUserSex())) {
			json.append(JsonUtil.USER_SEX_NODE).append(":\"")
					.append(user.getUserSex()).append("\",");
		} else {
			json.append(JsonUtil.USER_SEX_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getUserImg())) {
			json.append(JsonUtil.USER_IMG_NODE).append(":\"")
					.append(user.getUserImg()).append("\",");
		} else {
			json.append(JsonUtil.USER_IMG_NODE).append(":\"").append("")
					.append("\",");
		}
		if (!StringUtils.isEmpty(user.getLocationPublic())) {
			json.append(JsonUtil.LOCATION_PUBLIC_NODE).append(":\"")
					.append(user.getLocationPublic()).append("\",");
		} else {
			json.append(JsonUtil.LOCATION_PUBLIC_NODE).append(":\"")
					.append("").append("\",");
		}
		if (!StringUtils.isEmpty(user.getDeviceId())) {
			json.append(JsonUtil.DEVICE_NODE).append(":\"")
					.append(user.getDeviceId()).append("\"}");
		} else {
			json.append(JsonUtil.DEVICE_NODE).append(":\"").append("")
					.append("\"}");
		}
		return json;
	}
	public static String userInfoModify(User user) {
		String modifyResult = "";
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(Constants.USER_INFO_MODIFY_URL)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			StringBuilder json = new StringBuilder();
			json.append("{");
		    json.append(fillUserModifyJson(user, json));

			conn.getOutputStream().write(json.toString().getBytes());
			if (conn.getResponseCode() == 200) {

				try {
					InputStream jsonStream = conn.getInputStream();
					byte[] data;

					data = StreamTool.read(jsonStream);

					String repJson = new String(data, "UTF-8");
					JSONObject jsonObj = new JSONObject(repJson);
					modifyResult = jsonObj.getString(JsonUtil.RESULT_NODE);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return modifyResult;
	}

	public static boolean userLogin(User user, Activity actity) {
		boolean loginResult = false;
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(Constants.USER_LOGIN_URL)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			StringBuilder json = new StringBuilder();
			json.append("{").append(JsonUtil.PHONE_NODE).append(":\"")
					.append(user.getPhone()).append("\",");
			json.append(JsonUtil.USER_PWD_NODE).append(":\"")
					.append(user.getPwd()).append("\",");
			json.append(JsonUtil.DEVICE_NODE).append(":\"")
					.append(user.getDeviceId()).append("\"}");
			conn.getOutputStream().write(json.toString().getBytes());
			int repCode = conn.getResponseCode();
			if (repCode == 200) {
				try {

					InputStream jsonStream = conn.getInputStream();
					byte[] data;

					data = StreamTool.read(jsonStream);

					String repJson = new String(data, "UTF-8");
					JSONObject jsonObj = new JSONObject(repJson);
					String result = jsonObj.getString(JsonUtil.RESULT_NODE);
					if (result.equals(JsonUtil.RESULT_TRUE_VALUE)) {
						loginResult = true;
						String session_value = conn
								.getHeaderField("Set-Cookie");
						String[] sessionId = session_value.split(";");

						// save session info
						SharedPreferences mySharedPreferences = actity
								.getSharedPreferences(Constants.PACKAGE_PATH,
										Activity.MODE_PRIVATE);
						// 实例化SharedPreferences.Editor对象（第二步）
						SharedPreferences.Editor editor = mySharedPreferences
								.edit();
						// 用putString的方法保存数据
						editor.putString(Constants.SESSION_ID, sessionId[0]);
						editor.putString(Constants.SESSION_PHONE_NO,
								user.getPhone());
						editor.putString(Constants.SESSION_DEVICE_ID,
								user.getDeviceId());
						// 提交当前数据
						editor.commit();
					} else {
						Log.e("Login", result);
						loginResult = false;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			} else {
				Log.e("Login", String.valueOf(conn.getResponseCode()));
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return loginResult;
	}

	public static void logout(Activity activity) {
		// save session info
		SharedPreferences mySharedPreferences = activity.getSharedPreferences(
				Constants.PACKAGE_PATH, Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.remove(Constants.SESSION_PHONE_NO);
		editor.remove(Constants.SESSION_DEVICE_ID);
		editor.remove(Constants.SESSION_UID);
		// 提交当前数据
		editor.commit();
	}

	/**
	 * 返回"true"表示发送成功.
	 * 
	 * @param phone
	 * @return
	 */
	public static String requestSendVerifyCode(String phone) {
		String result = "";
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(Constants.SEND_VERIFYCODE_URL)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			StringBuilder json = new StringBuilder();
			json.append("{").append(JsonUtil.PHONE_NODE).append(":\"")
					.append(phone).append("\"}");
			conn.getOutputStream().write(json.toString().getBytes());
			int repCode = conn.getResponseCode();
			if (repCode == 200) {
				try {
					InputStream jsonStream = conn.getInputStream();
					byte[] data;

					data = StreamTool.read(jsonStream);

					String repJson = new String(data, "UTF-8");
					JSONObject jsonObj = new JSONObject(repJson);
					result = jsonObj.getString(JsonUtil.RESULT_NODE);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			} else {
				Log.e("Login", String.valueOf(conn.getResponseCode()));
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return result;
	}

	public static User getUserInfo(String phone) {
		User user = new User();
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(Constants.USER_INFO_GET_URL)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			StringBuilder json = new StringBuilder();
			json.append("{").append(JsonUtil.PHONE_NODE).append(":\"")
					.append(phone).append("\"}");
			conn.getOutputStream().write(json.toString().getBytes());
			if (conn.getResponseCode() == 200) {

				try {
					InputStream jsonStream = conn.getInputStream();
					fillUserInfo(user, jsonStream);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return user;
	}

	private static void fillUserInfo(User user, InputStream jsonStream)
			throws Exception {

		byte[] data = StreamTool.read(jsonStream);

		String repJson = new String(data, "UTF-8");
		JSONObject jsonObj = new JSONObject(repJson);
		if (!StringUtils.isEmpty(jsonObj.getString(JsonUtil.DEVICE_NODE))) {
			user.setDeviceId(jsonObj.getString(JsonUtil.DEVICE_NODE));
		}
		if (!StringUtils.isEmpty(jsonObj.getString(JsonUtil.PHONE_NODE))) {
			user.setPhone(jsonObj.getString(JsonUtil.PHONE_NODE));
		}
		if (!StringUtils.isEmpty(jsonObj.getString(JsonUtil.UID_NODE))) {
			user.setUid(jsonObj.getString(JsonUtil.UID_NODE));
		}
		if (!StringUtils.isEmpty(jsonObj.getString(JsonUtil.USER_SCREEN_NAME))) {
			user.setScreenName(jsonObj.getString(JsonUtil.USER_SCREEN_NAME));
		}
		if (!StringUtils.isEmpty(jsonObj.getString(JsonUtil.USER_NAME_NODE))) {
			user.setUserName(jsonObj.getString(JsonUtil.USER_NAME_NODE));
		}

		if (!StringUtils.isEmpty(jsonObj.getString(JsonUtil.USER_SEX_NODE))) {
			user.setUserSex(jsonObj.getString(JsonUtil.USER_SEX_NODE));
		}
		if (!StringUtils.isEmpty(jsonObj.getString(JsonUtil.USER_IMG_NODE))) {
			user.setUserImg(jsonObj.getString(JsonUtil.USER_IMG_NODE));
		}
		if (!StringUtils.isEmpty(jsonObj
				.getString(JsonUtil.LOCATION_PUBLIC_NODE))) {
			user.setLocationPublic(jsonObj
					.getString(JsonUtil.LOCATION_PUBLIC_NODE));
		}

	}

	public static User getUserInfoByUid(String uid) {
		User user = new User();
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(Constants.USER_INFO_GET_URL)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			StringBuilder json = new StringBuilder();
			json.append("{").append(JsonUtil.UID_NODE).append(":\"")
					.append(uid).append("\"}");
			conn.getOutputStream().write(json.toString().getBytes());
			if (conn.getResponseCode() == 200) {

				try {
					InputStream jsonStream = conn.getInputStream();
					fillUserInfo(user, jsonStream);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return user;

	}

	/**
	 * 获取用户头像图片.
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getImage(User user) throws Exception {
		if (StringUtils.isEmpty(user.getUserImg())) {
			return null;
		}
		URL url = new URL(user.getUserImg());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inStream);
			return bitmap;
		}
		return null;
	}

	public static void putSession(Activity mContext, User user) {
		// 信任登录.
		// 获取本地存储的登录用户信息.
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Constants.PACKAGE_PATH, Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = sharedPreferences.edit();
		TelephonyManager telephonyMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String szImei = telephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
		user.setDeviceId(szImei);

		// 用putString的方法保存数据
		editor.putString(Constants.SESSION_PHONE_NO, user.getPhone());
		editor.putString(Constants.SESSION_DEVICE_ID, user.getDeviceId());
		editor.putString(Constants.SESSION_UID, user.getUid());
		editor.putString(Constants.SESSION_SCREEN_NAME, user.getScreenName());
		editor.putString(Constants.SESSION_USER_IMG, user.getUserImg());
		// 提交当前数据
		editor.commit();
	}

	public static boolean checkLogin(Activity mContext) {
		// 获取本地存储的登录用户信息.
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Constants.PACKAGE_PATH, Activity.MODE_PRIVATE);

		String phone = sharedPreferences.getString(Constants.SESSION_PHONE_NO,
				"");
		String deviceId = sharedPreferences.getString(
				Constants.SESSION_DEVICE_ID, "");
		String uid = sharedPreferences.getString(Constants.SESSION_UID, "");
		// 是否免登检查.
		if (!StringUtils.isEmpty(phone) || !StringUtils.isEmpty(deviceId)
				|| !StringUtils.isEmpty(uid)) {
			return true;

		} else {
			return false;
		}

	}
	
	public static User getUserInfoFromSession(Activity mContext) {
		// 获取本地存储的登录用户信息.
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				Constants.PACKAGE_PATH, Activity.MODE_PRIVATE);

		String phone = sharedPreferences.getString(Constants.SESSION_PHONE_NO,
				"");
		String deviceId = sharedPreferences.getString(
				Constants.SESSION_DEVICE_ID, "");
		String uid = sharedPreferences.getString(Constants.SESSION_UID, "");
		String screenName = sharedPreferences.getString(
				Constants.SESSION_SCREEN_NAME, "");
		String userImg = sharedPreferences.getString(
				Constants.SESSION_USER_IMG, "");
		User user = new User();
		user.setDeviceId(deviceId);
		user.setPhone(phone);
		user.setScreenName(screenName);
		user.setUserImg(userImg);
		user.setUid(uid);
		return user;
	}

}
