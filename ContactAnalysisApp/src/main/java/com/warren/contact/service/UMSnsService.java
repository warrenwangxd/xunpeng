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
 * 友盟社交组件服务. 与UMShareHelper雷同.
 * 
 * @author dong.wangxd
 * 
 */
public class UMSnsService {

	public static void shareContent(Activity mContext) {
		// 首先在您的Activity中添加如下成员变量
		UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.share");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mContext,
				Constants.WEIXIN_APP_ID, Constants.WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext,
				Constants.WEIXIN_APP_ID, Constants.WEIXIN_APP_KEY);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(Constants.CONTACT_APP_DECLARATION);
		// 设置title
		weixinContent.setTitle(Constants.CONTACT_APP_NAME);
		weixinContent.setShareMedia((new UMImage(mContext,
				Constants.CONTACT_APP_LOGO)));
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(Constants.CONTACNT_APP_URL);
		mController.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(Constants.CONTACT_APP_DECLARATION);
		// 设置朋友圈title
		circleMedia.setTitle(Constants.CONTACT_APP_NAME);
		circleMedia.setShareImage((new UMImage(mContext,
				Constants.CONTACT_APP_LOGO)));
		circleMedia.setTargetUrl(Constants.CONTACNT_APP_URL);
		mController.setShareMedia(circleMedia);

		// 设置新浪微博分享内容
		SinaShareContent sinaShare = new SinaShareContent();
		sinaShare.setTargetUrl(Constants.CONTACNT_APP_URL);
		sinaShare.setTitle(Constants.CONTACT_APP_NAME);
		sinaShare.setAppWebSite(Constants.CONTACNT_APP_URL);
		sinaShare.setShareContent(Constants.CONTACT_APP_DECLARATION);
		sinaShare.setShareImage(new UMImage(mContext,
				Constants.CONTACT_APP_LOGO));
		mController.setShareMedia(sinaShare);

		// 去除多余的社交渠道.
		mController.getConfig().removePlatform(SHARE_MEDIA.QQ,
				SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT);
		mController.openShare(mContext, false);

	}

	public static void weixinLogin(final Activity mContext) {
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.login");

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mContext,
				Constants.WEIXIN_APP_ID, Constants.WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();

		mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT)
								.show();
						// 获取相关授权信息
						mController.getPlatformInfo(mContext,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(mContext, "获取平台数据开始...",
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
											Log.d("TestData", "发生错误：" + status);
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT)
								.show();
					}
				});

	}

	public static void qqLogin(final Activity mContext) {
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.login");
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mContext,
				Constants.TENCENT_APP_ID, Constants.TENCENT_APP_KEY);
		qqSsoHandler.addToSocialSDK();
		mController.doOauthVerify(mContext, SHARE_MEDIA.QQ,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						if (value != null
								&& !StringUtils.isEmpty(value.getString("uid"))) {
							final String uid = value.getString("uid");
							Toast.makeText(mContext, "登录成功，即将跳转到主功能页面...", Toast.LENGTH_SHORT)
									.show();
							// 获取相关授权信息
							mController.getPlatformInfo(mContext,
									SHARE_MEDIA.QQ, new UMDataListener() {
										@Override
										public void onStart() {
											Toast.makeText(mContext,
													"获取平台数据开始...",
													Toast.LENGTH_SHORT).show();
										}

										@Override
										public void onComplete(int status,
												Map<String, Object> info) {
											if (status == 200 && info != null) {
												User user = UserService
														.getUserInfoByUid(UserLoginTypeEnum.QQ.getValue()+uid);
												if (user.isEmpty()) {
													// 根据qq获取的信息初始化用户
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
											 	// 异步检查软件是否需要更新.
													AsyncUpdateChecker asyncUpdaChecker = new AsyncUpdateChecker(user);
													asyncUpdaChecker.execute(mContext);
												// 跳转到主功能页面 .
												Intent intent = new Intent(
														mContext,
														ContactMainActivity.class);
												mContext.startActivity(intent);
											}

											else {
												Log.d("TestData", "发生错误："
														+ status);
											}
										}
									});
						}

					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT)
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
							Toast.makeText(activity, "登录成功，即将跳转到主功能页面...",
									Toast.LENGTH_SHORT).show();
							// 获取授权相关信息
							mController.getPlatformInfo(activity,
									SHARE_MEDIA.SINA, new UMDataListener() {
										@Override
										public void onStart() {
											Toast.makeText(activity,
													"获取平台数据开始...",
													Toast.LENGTH_SHORT).show();
										}

										@Override
										public void onComplete(int status,
												Map<String, Object> info) {
											if (status == 200 && info != null) {
												User user = UserService
														.getUserInfoByUid(UserLoginTypeEnum.WEIBO.getValue()+uid);
												if (user.isEmpty()) {
													// 根据qq获取的信息初始化用户
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
											 	// 异步检查软件是否需要更新.
													AsyncUpdateChecker asyncUpdaChecker = new AsyncUpdateChecker(user);
													asyncUpdaChecker.execute(activity);
												// 跳转到主功能页面 .
												Intent intent = new Intent(
														activity,
														ContactMainActivity.class);
												activity.startActivity(intent);
											} else {
												Log.d("TestData", "发生错误："
														+ status);
											}
										}
									});

						} else {
							Toast.makeText(activity, "授权失败", Toast.LENGTH_SHORT)
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
