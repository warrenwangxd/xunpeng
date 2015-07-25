package com.warren.contact.service;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.warren.contact.analysis.activity.ContactMainActivity;
import com.warren.contact.domain.User;
import com.warren.contact.domain.UserLoginTypeEnum;
import com.warren.contact.update.AsyncUpdateChecker;
import com.warren.contact.utils.Constants;
import com.warren.contact.utils.StringUtils;

/**
 * �����罻�������. ��UMShareHelper��ͬ.
 * 
 * @author dong.wangxd
 * 
 */
public class UMSnsService {

	public static void shareContent(Activity mContext) {
		// ����������Activity��������³�Ա����
		UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.share");
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(mContext,
				Constants.WEIXIN_APP_ID, Constants.WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();
		// ���΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext,
				Constants.WEIXIN_APP_ID, Constants.WEIXIN_APP_KEY);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// ����΢�ź��ѷ�������
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// ���÷�������
		weixinContent.setShareContent(Constants.CONTACT_APP_DECLARATION);
		// ����title
		weixinContent.setTitle(Constants.CONTACT_APP_NAME);
		weixinContent.setShareMedia((new UMImage(mContext,
				Constants.CONTACT_APP_LOGO)));
		// ���÷���������תURL
		weixinContent.setTargetUrl(Constants.CONTACNT_APP_URL);
		mController.setShareMedia(weixinContent);

		// ����΢������Ȧ��������
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(Constants.CONTACT_APP_DECLARATION);
		// ��������Ȧtitle
		circleMedia.setTitle(Constants.CONTACT_APP_NAME);
		circleMedia.setShareImage((new UMImage(mContext,
				Constants.CONTACT_APP_LOGO)));
		circleMedia.setTargetUrl(Constants.CONTACNT_APP_URL);
		mController.setShareMedia(circleMedia);

		// ��������΢����������
		SinaShareContent sinaShare = new SinaShareContent();
		sinaShare.setTargetUrl(Constants.CONTACNT_APP_URL);
		sinaShare.setTitle(Constants.CONTACT_APP_NAME);
		sinaShare.setAppWebSite(Constants.CONTACNT_APP_URL);
		sinaShare.setShareContent(Constants.CONTACT_APP_DECLARATION);
		sinaShare.setShareImage(new UMImage(mContext,
				Constants.CONTACT_APP_LOGO));
		mController.setShareMedia(sinaShare);

		// ȥ��������罻����.
		mController.getConfig().removePlatform(SHARE_MEDIA.QQ,
				SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT);
		mController.openShare(mContext, false);

	}

	public static void weixinLogin(final Activity mContext) {
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.login");

		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(mContext,
				Constants.WEIXIN_APP_ID, Constants.WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();

		mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "��Ȩ��ʼ", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(mContext, "��Ȩ����", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						Toast.makeText(mContext, "��Ȩ���", Toast.LENGTH_SHORT)
								.show();
						// ��ȡ�����Ȩ��Ϣ
						mController.getPlatformInfo(mContext,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(mContext, "��ȡƽ̨���ݿ�ʼ...",
												Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (status == 200 && info != null) {
											StringBuilder sb = new StringBuilder();
											Set<String> keys = info.keySet();
											for (String key : keys) {
												sb.append(key
														+ "="
														+ info.get(key)
																.toString()
														+ "\r\n");
											}
											Log.d("TestData", sb.toString());
										} else {
											Log.d("TestData", "��������" + status);
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "��Ȩȡ��", Toast.LENGTH_SHORT)
								.show();
					}
				});

	}

	public static void qqLogin(final Activity mContext) {
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.login");
		// ����1Ϊ��ǰActivity�� ����2Ϊ��������QQ���������APP ID������3Ϊ��������QQ���������APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mContext,
				Constants.TENCENT_APP_ID, Constants.TENCENT_APP_KEY);
		qqSsoHandler.addToSocialSDK();
		mController.doOauthVerify(mContext, SHARE_MEDIA.QQ,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "��Ȩ��ʼ", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(mContext, "��Ȩ����", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						if (value != null
								&& !StringUtils.isEmpty(value.getString("uid"))) {
							final String uid = value.getString("uid");
							Toast.makeText(mContext, "��¼�ɹ���������ת��������ҳ��...", Toast.LENGTH_SHORT)
									.show();
							// ��ȡ�����Ȩ��Ϣ
							mController.getPlatformInfo(mContext,
									SHARE_MEDIA.QQ, new UMDataListener() {
										@Override
										public void onStart() {
											Toast.makeText(mContext,
													"��ȡƽ̨���ݿ�ʼ...",
													Toast.LENGTH_SHORT).show();
										}

										@Override
										public void onComplete(int status,
												Map<String, Object> info) {
											if (status == 200 && info != null) {
												User user = UserService
														.getUserInfoByUid(UserLoginTypeEnum.QQ.getValue()+uid);
												if (user.isEmpty()) {
													// ����qq��ȡ����Ϣ��ʼ���û�
													user.setUid(UserLoginTypeEnum.QQ.getValue()+uid);
													user.setScreenName((String) info
															.get("screen_name"));
													user.setUserImg((String) info
															.get("profile_image_url"));
													user.setCity((String) info
															.get("city"));
													TelephonyManager telephonyMgr = (TelephonyManager) mContext
															.getSystemService(Context.TELEPHONY_SERVICE);
													String szImei = telephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
													user.setDeviceId(szImei);
													UserService
															.userInfoModify(user);
												}
											     UserService.putSession(mContext, user);
											 	// �첽�������Ƿ���Ҫ����.
													AsyncUpdateChecker asyncUpdaChecker = new AsyncUpdateChecker(user);
													asyncUpdaChecker.execute(mContext);
												// ��ת��������ҳ�� .
												Intent intent = new Intent(
														mContext,
														ContactMainActivity.class);
												mContext.startActivity(intent);
											}

											else {
												Log.d("TestData", "��������"
														+ status);
											}
										}
									});
						}

					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "��Ȩȡ��", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	public static void weiboLogin(final UMSocialService mController,
			final Activity activity) {

		mController.doOauthVerify(activity, SHARE_MEDIA.SINA,
				new UMAuthListener() {
					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						if (value != null
								&& !StringUtils.isEmpty(value.getString("uid"))) {
							final String uid = value.getString("uid");
							Toast.makeText(activity, "��¼�ɹ���������ת��������ҳ��...",
									Toast.LENGTH_SHORT).show();
							// ��ȡ��Ȩ�����Ϣ
							mController.getPlatformInfo(activity,
									SHARE_MEDIA.SINA, new UMDataListener() {
										@Override
										public void onStart() {
											Toast.makeText(activity,
													"��ȡƽ̨���ݿ�ʼ...",
													Toast.LENGTH_SHORT).show();
										}

										@Override
										public void onComplete(int status,
												Map<String, Object> info) {
											if (status == 200 && info != null) {
												User user = UserService
														.getUserInfoByUid(UserLoginTypeEnum.WEIBO.getValue()+uid);
												if (user.isEmpty()) {
													// ����qq��ȡ����Ϣ��ʼ���û�
													user.setUid(UserLoginTypeEnum.WEIBO.getValue()+uid);
													user.setScreenName((String) info
															.get("screen_name"));
													user.setUserImg((String) info
															.get("profile_image_url"));
													user.setCity((String) info
															.get("location"));
													TelephonyManager telephonyMgr = (TelephonyManager) activity
															.getSystemService(Context.TELEPHONY_SERVICE);
													String szImei = telephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
													user.setDeviceId(szImei);
													UserService
															.userInfoModify(user);
												}
											     UserService.putSession(activity, user);
											 	// �첽�������Ƿ���Ҫ����.
													AsyncUpdateChecker asyncUpdaChecker = new AsyncUpdateChecker(user);
													asyncUpdaChecker.execute(activity);
												// ��ת��������ҳ�� .
												Intent intent = new Intent(
														activity,
														ContactMainActivity.class);
												activity.startActivity(intent);
											} else {
												Log.d("TestData", "��������"
														+ status);
											}
										}
									});

						} else {
							Toast.makeText(activity, "��Ȩʧ��", Toast.LENGTH_SHORT)
									.show();
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
					}

					@Override
					public void onStart(SHARE_MEDIA platform) {
					}
				});

	}

}
