package com.warren.contact.utils;
/**
 * 常量类.
 * @author dong.wangxd
 *
 */
public class Constants {
	public static String SESSION_ID="JSESSIONID";
	public static String SESSION_PHONE_NO="phone";
	public static String SESSION_DEVICE_ID="deviceId";
	public static String SESSION_UID="uid";
	public static String SESSION_SCREEN_NAME="screenName";
	public static String SESSION_USER_IMG="userImg";
	public static String PACKAGE_PATH = "com.warren.contact.analysis.activity";
	public static int LOCATION_MIN_UPDATE_TIME= 1*60*60*1000;//半天更新一次
	public static int LOCATION_MIN_DISTANCE_CHANGE= 1000;//1公里更新一次
	public static String WEIXIN_APP_ID="wx4781a939b804a2b2";
	public static String WEIXIN_APP_KEY="72b68429fa9aad796af1c4c371c827bb";
	public static String TENCENT_APP_ID="1104691196";
	public static String TENCENT_APP_KEY="a0VXyodT8rrSrjXX";
	public static String SINA_APP_ID="339526677";
	public static String SINA_APP_KEY="5ce01fc7e9052857bf2419879d91741e";
	public static String CONTACT_SERVER = "http://xunpeng.pub";
	public static String CONTACT_APP_LOGO=CONTACT_SERVER+"/logo.png";
	public static String CONTACT_APP_NAME="寻朋吧";
	public static String CONTACT_APP_DECLARATION="寻朋吧-发现朋友圈千丝万缕的关系!";
	public static String CONTACNT_APP_URL="http://a.app.qq.com/o/simple.jsp?pkgname=com.warren.contact.analysis.activity";
	public static String VERSION_FILE = CONTACT_SERVER+"/version.xml";
	public static String CHECK_UPDATE_CONTACT_URL = CONTACT_SERVER+"/checkUpdateContact";
	public static String UPDATE_CONTACT_PATH = CONTACT_SERVER+"/UploadContactServlet";
	public static String GET_ALL_CONTACTS_BY_OWNER_URL = CONTACT_SERVER+"/getAllContactsServlet";
	public static String USER_REGISTER_URL = CONTACT_SERVER+"/userRegisterServlet";
	public static String USER_LOGIN_URL = CONTACT_SERVER+"/userLoginServlet";
	public static String SEND_VERIFYCODE_URL=CONTACT_SERVER+"/sendVerifyCodeServlet";
	public static String FIND_SAME_CONTACT_URL=CONTACT_SERVER+"/findSameContactServlet";
	public static String UPDATE_PHONE_INFO=CONTACT_SERVER+"/updatePhoneInfoServlet";
	public static String UPDATE_LOCATION=CONTACT_SERVER+"/updateLocationServlet";
	public static String UPLOAD_USER_IMAGE_URL=CONTACT_SERVER+"/uploadUserImageServlet";
	public static String USER_INFO_MODIFY_URL=CONTACT_SERVER+"/userInfoModifyServlet";
	public static String USER_INFO_GET_URL=CONTACT_SERVER+"/userInfoGetServlet";
	public static String GET_FRIENDS_LOCATION=CONTACT_SERVER+"/getFriendsLocationServlet";

	

}
