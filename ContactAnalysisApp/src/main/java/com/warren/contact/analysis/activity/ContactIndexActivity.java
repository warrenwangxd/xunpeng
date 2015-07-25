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
		// �첽�������Ƿ���Ҫ����.
		User user = UserService
				.getUserInfoFromSession(ContactIndexActivity.this);
		AsyncUpdateChecker asyncUpdaChecker = new AsyncUpdateChecker(user);
		asyncUpdaChecker.execute(this);
		//ע��������Ϣ����
		PushAgent mPushAgent = PushAgent.getInstance(ContactIndexActivity.this);
		mPushAgent.enable();
		PushAgent.getInstance(ContactIndexActivity.this).onAppStart();
		
		// �Ƿ���Ǽ��.
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

		// ΢�ŵ�¼
		weixinLoginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				UMSnsService.qqLogin(ContactIndexActivity.this);
			}
		});

		// ΢����¼
		weiboLoginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				UMSnsService.weiboLogin(mController, ContactIndexActivity.this);
			}
		});

		// ���·ֱ�Ϊÿ����ť�����¼����� setOnClickListener
		registerButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent(ContactIndexActivity.this,
						RegisterActivity.class);
				startActivity(it);
			}
		});

		// ��¼���
		loginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent(ContactIndexActivity.this,
						UserLoginActivity.class);
				startActivity(it);
			}
		});

		// ��������ĵ���¼�
		searchButton.setClickable(true);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// �Ի���
				Dialog dialog = new AlertDialog.Builder(
						ContactIndexActivity.this)
				// ���öԻ���ı���
						.setTitle("Ѱ����ʾ")
						// ���öԻ���Ҫ��ʾ����Ϣ
						.setMessage("�����ȵ�¼")
						// ���Ի���������ť �С�ȷ������ ���������ü�����
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// ��� "ȷ����" ��ť֮��Ҫִ�еĲ�����д������
									}
								}).create();// ������ť
				dialog.show();// ��ʾһ��

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

		// ���ѹ�ϵ��������ĵ���¼�
		networkButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// �Ի���
				Dialog dialog = new AlertDialog.Builder(
						ContactIndexActivity.this)
				// ���öԻ���ı���
						.setTitle("Ѱ����ʾ")
						// ���öԻ���Ҫ��ʾ����Ϣ
						.setMessage("�����ȵ�¼")
						// ���Ի���������ť �С�ȷ������ ���������ü�����
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// ��� "ȷ����" ��ť֮��Ҫִ�еĲ�����д������
									}
								}).create();// ������ť
				dialog.show();// ��ʾһ��
			}
		});

		friendsButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// �Ի���
				Dialog dialog = new AlertDialog.Builder(
						ContactIndexActivity.this)
				// ���öԻ���ı���
						.setTitle("Ѱ����ʾ")
						// ���öԻ���Ҫ��ʾ����Ϣ
						.setMessage("�����ȵ�¼")
						// ���Ի���������ť �С�ȷ������ ���������ü�����
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).create();// ������ť
				dialog.show();// ��ʾһ��
			}
		});

		locationButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// �Ի���
				Dialog dialog = new AlertDialog.Builder(
						ContactIndexActivity.this)
				// ���öԻ���ı���
						.setTitle("Ѱ����ʾ")
						// ���öԻ���Ҫ��ʾ����Ϣ
						.setMessage("�����ȵ�¼")
						// ���Ի���������ť �С�ȷ������ ���������ü�����
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// ��� "ȷ����" ��ť֮��Ҫִ�еĲ�����д������
									}
								}).create();// ������ť
				dialog.show();// ��ʾһ��
			}
		});

		// ��ά�����
		qrCodeButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent(ContactIndexActivity.this,
						QRCodeActivity.class);
				startActivity(it);
			}
		});

	}

	/**
	 * ����΢���ص�
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** ʹ��SSO��Ȩ����������´��� */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * ����ͳ�ƴ���
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
