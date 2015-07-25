package com.warren.contact.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.warren.contact.domain.PhoneInfo;
import com.warren.contact.utils.Constants;
import com.warren.contact.utils.JsonUtil;
import com.warren.contact.utils.StreamTool;

public class PhoneService {
	public static void updatePhoneInfo(PhoneInfo phone) {
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(Constants.UPDATE_PHONE_INFO)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			StringBuilder json = new StringBuilder();
			json.append("{").append(JsonUtil.DEVICE_NODE).append(":\"")
					.append(phone.getDeviceId()).append("\",");
			json.append(JsonUtil.PHONE_TYPE_NODE).append(":\"")
					.append(phone.getPhoneType()).append("\",");
			json.append(JsonUtil.PHONE_NODE).append(":\"")
					.append(phone.getPhoneNo()).append("\"}");

			conn.getOutputStream().write(json.toString().getBytes());
			if (conn.getResponseCode() == 200) {
				try {
					InputStream jsonStream = conn.getInputStream();
					byte[] data;

					data = StreamTool.read(jsonStream);

					String repJson = new String(data, "UTF-8");
					JSONObject jsonObj = new JSONObject(repJson);
					String result = jsonObj.getString(JsonUtil.RESULT_NODE);

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

	}

}
