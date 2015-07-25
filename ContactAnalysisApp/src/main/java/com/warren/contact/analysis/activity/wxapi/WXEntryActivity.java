package com.warren.contact.analysis.activity.wxapi;

import android.content.Intent;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.warren.contact.service.UMShareHelper;

public class WXEntryActivity extends WXCallbackActivity {
	
	@Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
    }

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = UMShareHelper.getInstance().getController().getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
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