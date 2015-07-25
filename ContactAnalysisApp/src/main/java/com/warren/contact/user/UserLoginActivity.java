package com.warren.contact.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.warren.contact.analysis.activity.ContactMainActivity;
import com.warren.contact.analysis.activity.R;
import com.warren.contact.domain.User;
import com.warren.contact.service.UserService;
import com.warren.contact.update.AsyncUpdateChecker;
import com.warren.contact.utils.MD5Util;

public class UserLoginActivity extends Activity {

	private Button cancelBtn, loginBtn;
	private EditText userEditText, pwdEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		cancelBtn = (Button) findViewById(R.id.cancelButton);
		loginBtn = (Button) findViewById(R.id.registerButton);

		userEditText = (EditText) findViewById(R.id.userEditText);
		pwdEditText = (EditText) findViewById(R.id.pwdEditText);

		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validate()) {
					if (login()) {
						Intent intent = new Intent(UserLoginActivity.this,
								ContactMainActivity.class);
						startActivity(intent);
					} else {
						showDialog("手机号或者密码错误，请重新输入！");
					}
				}
			}
		});
	}

	private boolean login() {
		String phone = userEditText.getText().toString();
		String pwd = pwdEditText.getText().toString();
		TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = telephonyMgr.getDeviceId(); // Requires
														// READ_PHONE_STATE
		String md5Pwd = MD5Util.getMD5Code(pwd);
		User user = new User();
		user.setDeviceId(deviceId);
		user.setPhone(phone);
		user.setPwd(md5Pwd);
		if(UserService.userLogin(user, this)) {
			// 异步检查软件是否需要更新.
			AsyncUpdateChecker asyncUpdaChecker = new AsyncUpdateChecker(user);
			asyncUpdaChecker.execute(this);
			return true;
		} else {
			return false;
		}
	}

	private boolean validate() {
		String username = userEditText.getText().toString();
		if (username.equals("")) {
			showDialog("手机号是必填项！");
			return false;
		}
		String pwd = pwdEditText.getText().toString();
		if (pwd.equals("")) {
			showDialog("密码是必填项！");
			return false;
		}
		return true;
	}

	private void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
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
