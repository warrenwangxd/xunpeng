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
 * 在地图上展示通讯录中朋友的位置,
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
		setContentView(R.layout.location_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();

		User user = UserService.getUserInfoFromSession(LocationMapActivity.this);
		try {
			List<FriendLocation> friendLocationList = LocationService
					.getFriendsLocation(user.getPhone(),user.getUid());
			if(friendLocationList.size()==0) {
				Toast.makeText(this, " 没有发现朋友足迹，分享到sns邀请好友加入", Toast.LENGTH_LONG).show();
			}

			for (int i = 0; i < friendLocationList.size(); i++) {
				MarkerOptions option = new MarkerOptions(); // 设置坐标点
				option.position(new LatLng(friendLocationList.get(i)
						.getLocation().getLatitude(), friendLocationList.get(i)
						.getLocation().getLongitude()));
				option.title(friendLocationList.get(i).getUser().getUserName()+"("+friendLocationList.get(i).getUser().getPhone()+")");
				SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分",Locale.CHINA); 
				option.snippet("最近出现的时间："+dateformat.format(friendLocationList.get(i).getLocation().getLocTime()));
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
		 * MarkerOptions option1 = new MarkerOptions(); // 设置坐标点
		 * option1.position(new LatLng(30.5328628192244, 104.06435421309348));
		 * option1.title("汪小东"); option1.visible(true);
		 * 
		 * option1.setFlat(true);
		 * 
		 * markers.add(option1); MarkerOptions option2 = new MarkerOptions();
		 * option2.position(new LatLng(30.632829756644713, 104.06434485305516));
		 * option2.title("张云菊 "); option2.visible(true); option2.setFlat(true);
		 * 
		 * markers.add(option2); MarkerOptions option3 = new MarkerOptions();
		 * option3.position(new LatLng(29.632829756644713, 105.06434485305516));
		 * option3.title("老丈人 "); option3.visible(true);
		 * 
		 * option3.setFlat(true); markers.add(option3);
		 * 
		 * for (int i = 0; i < markers.size(); i++) { MarkerOptions marker =
		 * markers.get(i); aMap.addMarker(marker); }
		 */

	}

	/**
	 * 设置地图缩放范围
	 * 
	 * @param list
	 */
	/*
	 * private void zoomToSpan(List<Location> list) {
	 * 
	 * double southWestLatitue = 0; double southWestLongitude = 0;
	 * 
	 * double northEastLatitude = 0; double northEastLogitude = 0; for (Location
	 * data : list) { // 计算southWest最远点. if (southWestLatitue == 0) {
	 * southWestLatitue = data.getLatitude(); } else { if (data.getLatitude() <
	 * southWestLatitue) { southWestLatitue = data.getLatitude(); } } if
	 * (southWestLongitude == 0) { southWestLongitude = data.getLogitude(); }
	 * else { if (data.getLogitude() < southWestLongitude) { southWestLongitude
	 * = data.getLogitude(); } }
	 * 
	 * // 计算northEast最远点. if (northEastLatitude == 0) { northEastLatitude =
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
	 * 初始化
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
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.gps_locate_button:
			// 设置定位的类型为定位模式
			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
		case R.id.gps_follow_button:
			// 设置定位的类型为 跟随模式
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
			break;
		case R.id.gps_rotate_button:
			// 设置定位的类型为根据地图面向方向旋转
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
			break;
		}

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		MobclickAgent.onResume(this);//友盟统计
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		MobclickAgent.onPause(this);//友盟统计
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
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
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				//定位后，把当前位置也加入marker,重绘地图,确保所有marker都在地图中显示.
				// 设置所有maker显示在View中
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (int i = 0; i < markers.size(); i++) {
					builder.include(markers.get(i).getPosition());
				}
				MarkerOptions myOption = new MarkerOptions(); // 设置当前坐标点marker
				myOption.position(new LatLng(amapLocation.getLatitude(),
						amapLocation.getLongitude()));
				myOption.title("我");
				aMap.addMarker(myOption);
				
				builder.include(myOption.getPosition());
				
				LatLngBounds bounds = builder.build();
				// 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
				aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
				
			} else {
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorCode());
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用destroy()方法
			// 其中如果间隔时间为-1，则定位只定一次
			// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 5*60 * 1000, 1000, this);
		}
	}

	/**
	 * 停止定位
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
		// 设置所有maker显示在View中
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (int i = 0; i < markers.size(); i++) {
			builder.include(markers.get(i).getPosition());
		}
		LatLngBounds bounds = builder.build();
		// 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));

	}
	

}
