package com.warren.contact.analysis.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.umeng.analytics.MobclickAgent;
import com.warren.contact.domain.FriendLocation;
import com.warren.contact.domain.User;
import com.warren.contact.service.LocationService;
import com.warren.contact.service.UserService;
import com.warren.contact.utils.JsonUtil;

/**
 * �ڵ�ͼ��չʾͨѶ¼�����ѵ�λ��,
 * */
public class LocationMapActivity extends Activity implements LocationSource,
		AMapLocationListener, OnCheckedChangeListener, OnMapLoadedListener {
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private RadioGroup mGPSModeGroup;
	private ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ����ʾ����ı�����
		setContentView(R.layout.location_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init();

		User user = UserService.getUserInfoFromSession(LocationMapActivity.this);
		try {
			List<FriendLocation> friendLocationList = LocationService
					.getFriendsLocation(user.getPhone(),user.getUid());
			if(friendLocationList.size()==0) {
				Toast.makeText(this, " û�з��������㼣������sns������Ѽ���", Toast.LENGTH_LONG).show();
			}

			for (int i = 0; i < friendLocationList.size(); i++) {
				MarkerOptions option = new MarkerOptions(); // ���������
				option.position(new LatLng(friendLocationList.get(i)
						.getLocation().getLatitude(), friendLocationList.get(i)
						.getLocation().getLongitude()));
				option.title(friendLocationList.get(i).getUser().getUserName()+"("+friendLocationList.get(i).getUser().getPhone()+")");
				SimpleDateFormat dateformat=new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��",Locale.CHINA); 
				option.snippet("������ֵ�ʱ�䣺"+dateformat.format(friendLocationList.get(i).getLocation().getLocTime()));
				option.visible(true);
				if(JsonUtil.RESULT_USER_SEX_MALE.equals(friendLocationList.get(i).getUser().getUserSex())) {
					option.icon(BitmapDescriptorFactory.fromResource(R.drawable.male_user_128));
				} else {
					option.icon(BitmapDescriptorFactory.fromResource(R.drawable.female_user_128));
				}
				
				markers.add(option);
			}
			aMap.addMarkers(markers, true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
		 * 
		 * MarkerOptions option1 = new MarkerOptions(); // ���������
		 * option1.position(new LatLng(30.5328628192244, 104.06435421309348));
		 * option1.title("��С��"); option1.visible(true);
		 * 
		 * option1.setFlat(true);
		 * 
		 * markers.add(option1); MarkerOptions option2 = new MarkerOptions();
		 * option2.position(new LatLng(30.632829756644713, 104.06434485305516));
		 * option2.title("���ƾ� "); option2.visible(true); option2.setFlat(true);
		 * 
		 * markers.add(option2); MarkerOptions option3 = new MarkerOptions();
		 * option3.position(new LatLng(29.632829756644713, 105.06434485305516));
		 * option3.title("������ "); option3.visible(true);
		 * 
		 * option3.setFlat(true); markers.add(option3);
		 * 
		 * for (int i = 0; i < markers.size(); i++) { MarkerOptions marker =
		 * markers.get(i); aMap.addMarker(marker); }
		 */

	}

	/**
	 * ���õ�ͼ���ŷ�Χ
	 * 
	 * @param list
	 */
	/*
	 * private void zoomToSpan(List<Location> list) {
	 * 
	 * double southWestLatitue = 0; double southWestLongitude = 0;
	 * 
	 * double northEastLatitude = 0; double northEastLogitude = 0; for (Location
	 * data : list) { // ����southWest��Զ��. if (southWestLatitue == 0) {
	 * southWestLatitue = data.getLatitude(); } else { if (data.getLatitude() <
	 * southWestLatitue) { southWestLatitue = data.getLatitude(); } } if
	 * (southWestLongitude == 0) { southWestLongitude = data.getLogitude(); }
	 * else { if (data.getLogitude() < southWestLongitude) { southWestLongitude
	 * = data.getLogitude(); } }
	 * 
	 * // ����northEast��Զ��. if (northEastLatitude == 0) { northEastLatitude =
	 * data.getLatitude(); } else { if (data.getLatitude() > northEastLatitude)
	 * { northEastLatitude = data.getLatitude(); } } if (northEastLogitude == 0)
	 * { northEastLogitude = data.getLogitude(); } else { if (data.getLogitude()
	 * > northEastLogitude) { northEastLogitude = data.getLogitude(); } }
	 * 
	 * } LatLng southWest = new LatLng(southWestLatitue, southWestLongitude);
	 * LatLng northEast = new LatLng(northEastLatitude, northEastLogitude);
	 * aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(
	 * southWest, northEast), 1)); }
	 */

	/**
	 * ��ʼ��
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
		mGPSModeGroup.setOnCheckedChangeListener(this);
	}

	/**
	 * ����һЩamap������
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// ���ö�λ������Ϊ��λģʽ �������ɶ�λ��������ͼ������������ת����
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.gps_locate_button:
			// ���ö�λ������Ϊ��λģʽ
			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
		case R.id.gps_follow_button:
			// ���ö�λ������Ϊ ����ģʽ
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
			break;
		case R.id.gps_rotate_button:
			// ���ö�λ������Ϊ���ݵ�ͼ��������ת
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
			break;
		}

	}

	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		MobclickAgent.onResume(this);//����ͳ��
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		MobclickAgent.onPause(this);//����ͳ��
		deactivate();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
				//��λ�󣬰ѵ�ǰλ��Ҳ����marker,�ػ��ͼ,ȷ������marker���ڵ�ͼ����ʾ.
				// ��������maker��ʾ��View��
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (int i = 0; i < markers.size(); i++) {
					builder.include(markers.get(i).getPosition());
				}
				MarkerOptions myOption = new MarkerOptions(); // ���õ�ǰ�����marker
				myOption.position(new LatLng(amapLocation.getLatitude(),
						amapLocation.getLongitude()));
				myOption.title("��");
				aMap.addMarker(myOption);
				
				builder.include(myOption.getPosition());
				
				LatLngBounds bounds = builder.build();
				// �ƶ���ͼ������marker����Ӧ��ʾ��LatLngBounds���ͼ��Ե10���ص��������
				aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
				
			} else {
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorCode());
			}
		}
	}

	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
			// ����������ʱ��Ϊ-1����λֻ��һ��
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 5*60 * 1000, 1000, this);
		}
	}

	/**
	 * ֹͣ��λ
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(android.location.Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapLoaded() {
		// ��������maker��ʾ��View��
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (int i = 0; i < markers.size(); i++) {
			builder.include(markers.get(i).getPosition());
		}
		LatLngBounds bounds = builder.build();
		// �ƶ���ͼ������marker����Ӧ��ʾ��LatLngBounds���ͼ��Ե10���ص��������
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));

	}
	

}
