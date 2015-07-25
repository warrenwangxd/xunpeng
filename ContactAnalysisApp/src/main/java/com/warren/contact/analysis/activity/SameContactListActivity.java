package com.warren.contact.analysis.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.umeng.analytics.MobclickAgent;
import com.warren.contact.domain.Contact;
import com.warren.contact.domain.User;
import com.warren.contact.service.ContactService;
import com.warren.contact.service.UserService;

public class SameContactListActivity extends Activity {
	private Context mContext;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.same_contact_list);
		ListView listView = (ListView) this.findViewById(R.id.listView);

		try {
			User user = UserService
					.getUserInfoFromSession(SameContactListActivity.this);
			List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
			Map<String, List<Contact>> sameContactMap = ContactService
					.findSameContacts(user.getPhone(), user.getUid());
			Set<String> phoneSet = sameContactMap.keySet();
			Iterator phoneIter = phoneSet.iterator();
			while (phoneIter.hasNext()) {
				String samePhoneKey = (String) phoneIter.next();
				List<Contact> contacts = sameContactMap.get(samePhoneKey);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("phone", samePhoneKey);
				data.put("name", contacts.toString());
				dataList.add(data);

			}
			SimpleAdapter adapter = new SimpleAdapter(this, dataList,
					R.layout.same_contact_item,
					new String[] { "phone", "name" }, new int[] { R.id.phone,
							R.id.name });
			listView.setAdapter(adapter);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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