package com.warren.contact.analysis.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.warren.contact.analysis.activity.R;
import com.warren.contact.domain.User;
import com.warren.contact.service.UMSnsService;
import com.warren.contact.service.UserService;
import com.warren.contact.update.AsyncUpdateChecker;
import com.warren.contact.update.QRCodeActivity;
import com.warren.contact.user.RegisterActivity;
import com.warren.contact.user.UserLoginActivity;

public class ContactIndexActivity extends Activity {

	Button registerButton, loginButton;
	ImageButton weixinLoginButton, weiboLoginButton;
	LinearLayout searchButton, networkButton, friendsButton, locationButton,
			qrCodeButton;

	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.login");

	/** Called when the activity is first created. */
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 异步检查软件是否需要更新.
		User user = UserService
				.getUserInfoFromSession(ContactIndexActivity.this);
		AsyncUpdateChecker asyncUpdaChecker = new AsyncUpdateChecker(user);
		asyncUpdaChecker.execute(this);
		//注册友盟消息推送
		PushAgent mPushAgent = PushAgent.getInstance(ContactIndexActivity.this);
		mPushAgent.enable();
		PushAgent.getInstance(ContactIndexActivity.this).onAppStart();
		
		// 是否免登检查.
		if (UserService.checkLogin(ContactIndexActivity.this)) {
			Intent intent = new Intent(ContactIndexActivity.this,
					ContactMainActivity.class);
			startActivity(intent);
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_index);
		registerButton = (Button) findViewById(R.id.register);
		loginButton = (Button) findViewById(R.id.login);
		weixinLoginButton = (ImageButton) findViewById(R.id.weixinLogin);
		weiboLoginButton = (ImageButton) findViewById(R.id.weiboLogin);
		searchButton = (LinearLayout) findViewById(R.id.search);
		networkButton = (LinearLayout) findViewById(R.id.network);
		friendsButton = (LinearLayout) findViewById(R.id.friends);
		locationButton = (LinearLayout) findViewById(R.id.location);
		qrCodeButton = (LinearLayout) findViewById(R.id.qrcode);

		// 微信登录
		weixinLoginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				UMSnsService.qqLogin(ContactIndexActivity.this);
			}
		});

		// 微博登录
		weiboLoginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				UMSnsService.weiboLogin(mController, ContactIndexActivity.this);
			}
		});

		// 以下分别为每个按钮设置事件监听 setOnClickListener
		registerButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent(ContactIndexActivity.this,
						RegisterActivity.class);
				startActivity(it);
			}
		});

		// 登录组件
		loginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent(ContactIndexActivity.this,
						UserLoginActivity.class);
				startActivity(it);
			}
		});

		// 搜索组件的点击事件
		searchButton.setClickable(true);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 对话框
				Dialog dialog = new AlertDialog.Builder(
						ContactIndexActivity.this)
				// 设置对话框的标题
						.setTitle("寻朋提示")
						// 设置对话框要显示的消息
						.setMessage("请您先登录")
						// 给对话框来个按钮 叫“确定定” ，并且设置监听器
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// 点击 "确定定" 按钮之后要执行的操作就写在这里
									}
								}).create();// 创建按钮
				dialog.show();// 显示一把

			}
		});

		searchButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					searchButton.setBackgroundColor(Color.rgb(127, 127, 127));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					searchButton.setBackgroundColor(Color.TRANSPARENT);
				}
				return false;
			}
		});

		// 朋友关系网络组件的点击事件
		networkButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 对话框
				Dialog dialog = new AlertDialog.Builder(
						ContactIndexActivity.this)
				// 设置对话框的标题
						.setTitle("寻朋提示")
						// 设置对话框要显示的消息
						.setMessage("请您先登录")
						// 给对话框来个按钮 叫“确定定” ，并且设置监听器
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// 点击 "确定定" 按钮之后要执行的操作就写在这里
									}
								}).create();// 创建按钮
				dialog.show();// 显示一把
			}
		});

		friendsButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 对话框
				Dialog dialog = new AlertDialog.Builder(
						ContactIndexActivity.this)
				// 设置对话框的标题
						.setTitle("寻朋提示")
						// 设置对话框要显示的消息
						.setMessage("请您先登录")
						// 给对话框来个按钮 叫“确定定” ，并且设置监听器
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).create();// 创建按钮
				dialog.show();// 显示一把
			}
		});

		locationButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 对话框
				Dialog dialog = new AlertDialog.Builder(
						ContactIndexActivity.this)
				// 设置对话框的标题
						.setTitle("寻朋提示")
						// 设置对话框要显示的消息
						.setMessage("请您先登录")
						// 给对话框来个按钮 叫“确定定” ，并且设置监听器
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// 点击 "确定定" 按钮之后要执行的操作就写在这里
									}
								}).create();// 创建按钮
				dialog.show();// 显示一把
			}
		});

		// 二维码组件
		qrCodeButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent(ContactIndexActivity.this,
						QRCodeActivity.class);
				startActivity(it);
			}
		});

	}

	/**
	 * 新浪微博回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 友盟统计代码
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
