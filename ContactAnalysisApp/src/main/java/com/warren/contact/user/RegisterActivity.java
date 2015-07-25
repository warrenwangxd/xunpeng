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
import com.warren.contact.analysis.activity.R;
import com.warren.contact.domain.User;
import com.warren.contact.service.UserService;
import com.warren.contact.utils.JsonUtil;
import com.warren.contact.utils.MD5Util;
import com.warren.contact.utils.StringUtils;

public class RegisterActivity extends Activity {

	private Button cancelBtn, registerBtn, verifyCodeSendBtn;
	private EditText phoneEditText, pwdEditText, repwdEditText, verifyCodeText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		cancelBtn = (Button) findViewById(R.id.cancelButton);
		registerBtn = (Button) findViewById(R.id.registerButton);
		verifyCodeSendBtn = (Button) findViewById(R.id.verifyCodeSendBtn);

		phoneEditText = (EditText) findViewById(R.id.userEditText);
		pwdEditText = (EditText) findViewById(R.id.pwdEditText);
		repwdEditText = (EditText) findViewById(R.id.repwdEditText);
		verifyCodeText = (EditText) findViewById(R.id.verifyCode);

		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		registerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validate()) {
					String registerResult= register();
					if (JsonUtil.RESULT_TRUE_VALUE.equals(registerResult)) {
						Intent intent = new Intent(RegisterActivity.this,
								RegisterSuccess.class);
						startActivity(intent);
					} else {
						showDialog("注册失败:"+registerResult);
					}
				}
			}
		});

		verifyCodeSendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = phoneEditText.getText().toString();
				if (phone.equals("")) {
					showDialog("手机号是必填项！");
				} else {
					String sentResult = requestSendVerifyCode(phone);
					if (JsonUtil.RESULT_TRUE_VALUE.equals(sentResult)) {

						showDialog("验证码发送成功，请注意查收短信");
					} else {
						showDialog("验证码发送失败:" + sentResult);
					}
				}
			}
		});
	}

	public String requestSendVerifyCode(String phone) {
		return UserService.requestSendVerifyCode(phone);

	}

	private String register() {
		String phone = phoneEditText.getText().toString();
		String pwd = pwdEditText.getText().toString();
		String verifyCode=verifyCodeText.getText().toString();
		TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = telephonyMgr.getDeviceId(); // Requires
														// READ_PHONE_STATE
		String md5Pwd = MD5Util.getMD5Code(pwd);
		User user = new User();
		user.setDeviceId(deviceId);
		user.setPhone(phone);
		user.setPwd(md5Pwd);
		return UserService.userRegister(user,verifyCode);

	}

	private boolean validate() {
		String username = phoneEditText.getText().toString();
		if (username.equals("")) {
			showDialog("手机号是必填项！");
			return false;
		}
		String pwd = pwdEditText.getText().toString();
		if (pwd.equals("")) {
			showDialog("密码是必填项！");
			return false;
		}
		if (!pwd.equals(repwdEditText.getText().toString())) {
			showDialog("两次密码输入不一致");
			return false;
		}
		String verifyCode = verifyCodeText.getText().toString();
		if (StringUtils.isEmpty(verifyCode)) {
			showDialog("验证码不能为空");
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
