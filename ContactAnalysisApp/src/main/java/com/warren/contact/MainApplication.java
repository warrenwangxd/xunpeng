package com.warren.contact;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.warren.contact.utils.Constants;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化配置数据
		try {
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(this.getPackageName(),PackageManager.GET_META_DATA);
			Constants.CONTACT_SERVER = appInfo.metaData
					.getString("CONTACT_SERVER");
			Constants.CONTACNT_APP_URL = appInfo.metaData
					.getString("CONTACNT_APP_URL");
			Constants.CONTACT_APP_DECLARATION = appInfo.metaData
					.getString("CONTACT_APP_DECLARAITION");
			Constants.CONTACT_APP_LOGO = Constants.CONTACT_SERVER
					+ "/"
					+ appInfo.metaData
							.getString("CONTACT_APP_LOGO");
			Constants.CONTACT_APP_NAME = appInfo.metaData
					.getString("CONTACT_APP_NAME");
			Constants.LOCATION_MIN_DISTANCE_CHANGE = appInfo.metaData
					.getInt("LOCATION_MIN_DISTANCE_CHANGE");
			Constants.LOCATION_MIN_UPDATE_TIME = appInfo.metaData
					.getInt("LOCATION_MIN_UPDATE_TIME");
			Constants.SINA_APP_ID = String.valueOf(appInfo.metaData
					.getString("SINA_APP_ID"));
			Constants.SINA_APP_KEY = appInfo.metaData
					.getString("SINA_APP_KEY");
			Constants.TENCENT_APP_ID = String.valueOf(appInfo.metaData
					.getString("TENCENT_APP_ID"));
			Constants.TENCENT_APP_KEY = appInfo.metaData
					.getString("TENCENT_APP_KEY");
			Constants.WEIXIN_APP_ID = appInfo.metaData
					.getString("WEIXIN_APP_ID");
			Constants.WEIXIN_APP_KEY = appInfo.metaData
					.getString("WEIXIN_APP_KEY");
			Constants.VERSION_FILE = Constants.CONTACT_SERVER + "/"
					+ appInfo.metaData.getString("VERSION_FILE");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
