package com.warren.contact.update;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.warren.contact.domain.Contact;
import com.warren.contact.domain.FullContact;
import com.warren.contact.domain.PhoneInfo;
import com.warren.contact.domain.User;
import com.warren.contact.service.ContactService;
import com.warren.contact.service.LocationService;
import com.warren.contact.service.PhoneService;
import com.warren.contact.utils.Constants;
import com.warren.contact.utils.StringUtils;

/**
 * 异步检查是否需要更新的任务.
 * 
 * @author dong.wangxd
 * 
 */
public class AsyncUpdateChecker extends AsyncTask<Activity, Integer, Boolean> {

	private Context mContext;
	private UpdateManager manager;
	private User user;
	
	public AsyncUpdateChecker(User user) {
		this.user=user;
	
	}

	@Override
	protected Boolean doInBackground(Activity... arg0) {
		mContext = arg0[0];
		manager = new UpdateManager(arg0[0]);
		// 检查是否需要更新上传手机联系人
		checkUpdateContact();
		// 更新手机客户端信息
		updatePhoneOtherInfo();
		// 更新当前位置信息
		updateLocation();
		// 检查软件更新
		return manager.checkUpdate();
	}

	// 在doInBackground方法当中，每次调用publishProgrogress()方法之后，都会触发该方法
	@Override
	protected void onProgressUpdate(Integer... values) {

	}

	// 在doInBackground方法执行结束后再运行，并且运行在UI线程当中
	// 主要用于将异步操作任务执行的结果展示给用户
	@Override
	protected void onPostExecute(Boolean result) {
		//异步监听位置信息.
		LocationManager	locationManager = (LocationManager) mContext
				.getSystemService( Context.LOCATION_SERVICE);		
		locationManager.requestLocationUpdates(getLocationProvider(locationManager),
				Constants.LOCATION_MIN_UPDATE_TIME,
				Constants.LOCATION_MIN_DISTANCE_CHANGE,
				new LocationChangedListener());
		
		if (result) {
			manager.showNoticeDialog();
		} else {
			//Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
		}
	}
	
	private String getLocationProvider(LocationManager locationManager) {
	
		String contextString = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) mContext
				.getSystemService(contextString);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 取得效果最好的criteria
		String provider = locationManager.getBestProvider(criteria, false);
		return provider;
		
	}

	private void updateLocation() {
		LocationManager locationManager;
		String contextString = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) mContext
				.getSystemService(contextString);		
		// 取得效果最好的criteria
		String provider = getLocationProvider(locationManager);
		if (provider == null) {
			return;
		}
		
		//同步取一次当前位置.
		Location location= locationManager.getLastKnownLocation(provider);
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			// 更具地理环境来确定编码
			Geocoder gc = new Geocoder(mContext, Locale.getDefault());
			try {
				// 取得地址相关的一些信息\经度、纬度
				List<Address> addresses = gc.getFromLocation(latitude,
						longitude, 1);
				if (addresses.size() > 0) {
					Address address = addresses.get(0);
					LocationService.updateLocation(user, address);
				} 
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
	}

	/**
	 * 上传手机其他信息。 包括手机型号,本机号码等.
	 */
	private void updatePhoneOtherInfo() {
		TelephonyManager telephonyMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String szImei = telephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
		String mtype = android.os.Build.MODEL; // 手机型号
		String numer = telephonyMgr.getLine1Number(); // 手机号码，有的可得，有的不可得
		PhoneInfo phone = new PhoneInfo();
		phone.setDeviceId(szImei);
		phone.setPhoneNo(numer);
		phone.setPhoneType(mtype);
		PhoneService.updatePhoneInfo(phone);
	}

	private void checkUpdateContact() {

		// 取本地数据
		FullContact fullContacts = getPhoneContacts();
		try {
			String serverContactsSize = ContactService
					.CheckUpateContact(fullContacts.getContactOwner());
			user.setContacts(fullContacts.getContact());
			
			//如果返回-1,则表示不需要更新;返回0则表示每次都更新.
			if(null!=serverContactsSize&&Integer.valueOf(serverContactsSize)==-1) {
				return;
			} else if(null!=serverContactsSize&&Integer.valueOf(serverContactsSize)==0) {
				ContactService.uploadContacts(user);
			} else if(null != serverContactsSize
					&& Integer.valueOf(serverContactsSize)<fullContacts
					.getContact().size()){
				ContactService.uploadContacts(user);
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private class LocationChangedListener implements LocationListener {

		// 这个函数的参数是用户当前的位置
		@Override
		public void onLocationChanged(Location location) {
		  Address address=null;
			if (location != null) {
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				// 更具地理环境来确定编码
				Geocoder gc = new Geocoder(mContext, Locale.getDefault());
				try {
					// 取得地址相关的一些信息\经度、纬度
					List<Address> addresses = gc.getFromLocation(latitude,
							longitude, 1);
					if (addresses.size() > 0) {
						address = addresses.get(0);
					} else {
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			} else{
				return;
			}
			try {
				LocationService.updateLocation(user,address);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

	/** 得到手机通讯录联系人信息 **/
	private FullContact getPhoneContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		ContentResolver resolver = mContext.getContentResolver();
		/** 获取库Phon表字段 **/
		String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME,
				Phone.NUMBER };

		/** 联系人显示名称 **/
		int PHONES_DISPLAY_NAME_INDEX = 0;

		/** 电话号码 **/
		int PHONES_NUMBER_INDEX = 1;

		TelephonyManager telephonyMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String szImei = telephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber)) {
					continue;
				}

				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);
				Contact contact = new Contact();
				contact.setName(contactName);
				contact.setPhoneNumber(phoneNumber);
				contacts.add(contact);
			}

			phoneCursor.close();

		}
		FullContact fullContact = new FullContact();
		fullContact.setContactOwner(szImei);
		fullContact.setContact(contacts);
		return fullContact;
	}

}
