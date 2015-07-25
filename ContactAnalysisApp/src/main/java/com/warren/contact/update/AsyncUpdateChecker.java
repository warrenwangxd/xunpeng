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
 * �첽����Ƿ���Ҫ���µ�����.
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
		// ����Ƿ���Ҫ�����ϴ��ֻ���ϵ��
		checkUpdateContact();
		// �����ֻ��ͻ�����Ϣ
		updatePhoneOtherInfo();
		// ���µ�ǰλ����Ϣ
		updateLocation();
		// ����������
		return manager.checkUpdate();
	}

	// ��doInBackground�������У�ÿ�ε���publishProgrogress()����֮�󣬶��ᴥ���÷���
	@Override
	protected void onProgressUpdate(Integer... values) {

	}

	// ��doInBackground����ִ�н����������У�����������UI�̵߳���
	// ��Ҫ���ڽ��첽��������ִ�еĽ��չʾ���û�
	@Override
	protected void onPostExecute(Boolean result) {
		//�첽����λ����Ϣ.
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
		// ȡ��Ч����õ�criteria
		String provider = locationManager.getBestProvider(criteria, false);
		return provider;
		
	}

	private void updateLocation() {
		LocationManager locationManager;
		String contextString = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) mContext
				.getSystemService(contextString);		
		// ȡ��Ч����õ�criteria
		String provider = getLocationProvider(locationManager);
		if (provider == null) {
			return;
		}
		
		//ͬ��ȡһ�ε�ǰλ��.
		Location location= locationManager.getLastKnownLocation(provider);
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			// ���ߵ�������ȷ������
			Geocoder gc = new Geocoder(mContext, Locale.getDefault());
			try {
				// ȡ�õ�ַ��ص�һЩ��Ϣ\���ȡ�γ��
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
	 * �ϴ��ֻ�������Ϣ�� �����ֻ��ͺ�,���������.
	 */
	private void updatePhoneOtherInfo() {
		TelephonyManager telephonyMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String szImei = telephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
		String mtype = android.os.Build.MODEL; // �ֻ��ͺ�
		String numer = telephonyMgr.getLine1Number(); // �ֻ����룬�еĿɵã��еĲ��ɵ�
		PhoneInfo phone = new PhoneInfo();
		phone.setDeviceId(szImei);
		phone.setPhoneNo(numer);
		phone.setPhoneType(mtype);
		PhoneService.updatePhoneInfo(phone);
	}

	private void checkUpdateContact() {

		// ȡ��������
		FullContact fullContacts = getPhoneContacts();
		try {
			String serverContactsSize = ContactService
					.CheckUpateContact(fullContacts.getContactOwner());
			user.setContacts(fullContacts.getContact());
			
			//�������-1,���ʾ����Ҫ����;����0���ʾÿ�ζ�����.
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

		// ��������Ĳ������û���ǰ��λ��
		@Override
		public void onLocationChanged(Location location) {
		  Address address=null;
			if (location != null) {
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				// ���ߵ�������ȷ������
				Geocoder gc = new Geocoder(mContext, Locale.getDefault());
				try {
					// ȡ�õ�ַ��ص�һЩ��Ϣ\���ȡ�γ��
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

	/** �õ��ֻ�ͨѶ¼��ϵ����Ϣ **/
	private FullContact getPhoneContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		ContentResolver resolver = mContext.getContentResolver();
		/** ��ȡ��Phon���ֶ� **/
		String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME,
				Phone.NUMBER };

		/** ��ϵ����ʾ���� **/
		int PHONES_DISPLAY_NAME_INDEX = 0;

		/** �绰���� **/
		int PHONES_NUMBER_INDEX = 1;

		TelephonyManager telephonyMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String szImei = telephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE

		// ��ȡ�ֻ���ϵ��
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// �õ��ֻ�����
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phoneNumber)) {
					continue;
				}

				// �õ���ϵ������
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
