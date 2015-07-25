package com.warren.contact.user;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.warren.contact.analysis.activity.R;
import com.warren.contact.domain.User;
import com.warren.contact.service.UserService;
/**
 * 异步获取用户头像数据.
 * @author dong.wangxd
 *
 */
public class AsyncFetchUserImgTask extends AsyncTask<Activity, Integer, Boolean> {
	private Activity mContext;
	private Bitmap imageBitMap;
	private User user;
	
	public AsyncFetchUserImgTask(User user) {
		this.user=user;
	}


	@Override
	protected Boolean doInBackground(Activity... arg0) {
		Boolean result = false;
		mContext = arg0[0];
		try {
			imageBitMap=UserService.getImage(user);
			if(imageBitMap!=null) {
				result= true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	protected void onPostExecute(Boolean result) {
		
		
		if (result) {
			ImageView imageView = (ImageView) mContext.findViewById(R.id.imageView);
			imageView.setImageBitmap(imageBitMap);
		} else {
			Toast.makeText(mContext, "读取用户头像数据失败", Toast.LENGTH_LONG).show();
		}
	}

}
