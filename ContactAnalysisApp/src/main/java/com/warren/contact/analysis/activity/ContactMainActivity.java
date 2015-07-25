package com.warren.contact.analysis.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.warren.contact.domain.User;
import com.warren.contact.service.UMSnsService;
import com.warren.contact.service.UserService;
import com.warren.contact.update.AsyncUpdateChecker;
import com.warren.contact.user.UserModifyActivity;
import com.warren.contact.utils.StringUtils;

public class ContactMainActivity extends Activity {
	LinearLayout friendsButton, locationButton, qrCodeButton;
	Button shareButton, logoutButton, userModifyButton;
	private Activity mContext;

	private User user;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// ע��������Ϣ����
		PushAgent mPushAgent = PushAgent.getInstance(ContactMainActivity.this);
		mPushAgent.enable();
		PushAgent.getInstance(ContactMainActivity.this).onAppStart();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_main);
		mContext = this;

		friendsButton = (LinearLayout) findViewById(R.id.friends);
		locationButton = (LinearLayout) findViewById(R.id.location);
		shareButton = (Button) findViewById(R.id.share);
		logoutButton = (Button) findViewById(R.id.logout);
		userModifyButton = (Button) findViewById(R.id.userModify);
		/*
		 * qrCodeButton = (LinearLayout) findViewById(R.id.qrcode);
		 * 
		 * // ��ά����� qrCodeButton.setOnClickListener(new Button.OnClickListener()
		 * { public void onClick(View v) { Intent it = new
		 * Intent(ContactMainActivity.this, QRCodeActivity.class);
		 * startActivity(it); } });
		 */

		// ��������ĵ���¼�
		shareButton.setClickable(true);
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UMSnsService.shareContent(mContext);
			}
		});
		// �˳��ĵ���¼�
		logoutButton.setClickable(true);
		logoutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UserService.logout(mContext);
				Intent it = new Intent(ContactMainActivity.this,
						ContactIndexActivity.class);

				startActivity(it);

			}
		});
		// �޸ĸ�����Ϣ�ĵ���¼�
		userModifyButton.setClickable(true);
		userModifyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(ContactMainActivity.this,
						UserModifyActivity.class);

				startActivity(it);

			}
		});

		friendsButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				user = UserService.getCurrentUser(ContactMainActivity.this);
				if (StringUtils.isEmpty(user.getPhone())) {
					// �Ի���
					Dialog dialog = new AlertDialog.Builder(
							ContactMainActivity.this)
							// ���öԻ���ı���
							.setTitle("Ѱ����ʾ")
							// ���öԻ���Ҫ��ʾ����Ϣ
							.setMessage("���µ����ѹ�ϵʱͨ���ֻ�����֪ͨ��")
							// ���Ի���������ť �С�ȷ������ ���������ü�����
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent it = new Intent(
													ContactMainActivity.this,
													UserModifyActivity.class);
											startActivity(it);
										}
									})
							.setNegativeButton("�ݲ���Ҫ",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent it = new Intent(
													ContactMainActivity.this,
													SameContactListActivity.class);
											startActivity(it);
										}
									}).create();// ������ť
					dialog.show();// ��ʾһ��
				} else {
					Intent it = new Intent(ContactMainActivity.this,
							SameContactListActivity.class);
					startActivity(it);
				}
			}
		});

		locationButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				user = UserService.getCurrentUser(ContactMainActivity.this);
				if (StringUtils.isEmpty(user.getPhone())) {
					// �Ի���
					Dialog dialog = new AlertDialog.Builder(
							ContactMainActivity.this)
							// ���öԻ���ı���
							.setTitle("Ѱ����ʾ")
							// ���öԻ���Ҫ��ʾ����Ϣ
							.setMessage("���µ����ѹ�ϵʱͨ���ֻ�����֪ͨ��")
							// ���Ի���������ť �С�ȷ������ ���������ü�����
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent it = new Intent(
													ContactMainActivity.this,
													UserModifyActivity.class);
											startActivity(it);
										}
									})
							.setNegativeButton("�ݲ���Ҫ",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent multyIntent = new Intent(
													ContactMainActivity.this,
													LocationMapActivity.class);
											startActivity(multyIntent);
										}
									}).create();// ������ť
					dialog.show();// ��ʾһ��
				} else {
					Intent multyIntent = new Intent(ContactMainActivity.this,
							LocationMapActivity.class);
					startActivity(multyIntent);
				}
			}
		});

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
