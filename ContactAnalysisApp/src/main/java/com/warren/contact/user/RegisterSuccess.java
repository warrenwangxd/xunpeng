package com.warren.contact.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;
import com.warren.contact.analysis.activity.R;

public class RegisterSuccess extends Activity{
	
	private Button loginButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_success);
		loginButton= (Button) this.findViewById(R.id.login);
		loginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent it = new Intent(RegisterSuccess.this,
						UserLoginActivity.class);
				startActivity(it);
			}
		});
	}
	
	/**
	 * ÓÑÃËÍ³¼Æ´úÂë
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
