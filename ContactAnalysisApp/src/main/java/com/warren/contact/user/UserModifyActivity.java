package com.warren.contact.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.sax.TextElementListener;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.warren.contact.analysis.activity.ContactMainActivity;
import com.warren.contact.analysis.activity.R;
import com.warren.contact.domain.User;
import com.warren.contact.service.UserService;
import com.warren.contact.utils.Constants;
import com.warren.contact.utils.JsonUtil;
import com.warren.contact.utils.MD5Util;
import com.warren.contact.utils.StringUtils;

public class UserModifyActivity extends Activity implements OnClickListener {

	private static String requestURL = Constants.UPLOAD_USER_IMAGE_URL;
	private Button selectImage, uploadImage, userModifyButton, cancelButton;
	private ImageView imageView;
	private TextView phoneText, userNameText, pwdText,verifyCodeEditText;
	private RadioButton maleRadio, femaleRadio, locationPublicYesRadio,
			locationPublicNoRadio;
	private String sex, phone, locationPublic;
	private User user;
	private String picPath;


	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_modify);
		mContext = this;

		/*
		 * selectImage = (Button) this.findViewById(R.id.selectImage);
		 * uploadImage = (Button) this.findViewById(R.id.uploadImage);
		 */
		// selectImage.setOnClickListener(this);
		// uploadImage.setOnClickListener(this);

		imageView = (ImageView) this.findViewById(R.id.imageView);
		userNameText = (TextView) this.findViewById(R.id.userNameEditText);
		pwdText = (TextView) this.findViewById(R.id.pwdEditText);
		maleRadio = (RadioButton) this.findViewById(R.id.radioMale);
		femaleRadio = (RadioButton) this.findViewById(R.id.radioFemale);
		locationPublicYesRadio = (RadioButton) this
				.findViewById(R.id.radioLocationPublicYes);
		locationPublicNoRadio = (RadioButton) this
				.findViewById(R.id.radioLocationPublicNo);
		userModifyButton = (Button) this.findViewById(R.id.userModifyButton);
		userModifyButton.setOnClickListener(this);
		cancelButton = (Button) this.findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);

		phoneText = (TextView) this.findViewById(R.id.phoneText);

		// ��ȡ���ش洢�ĵ�¼�û���Ϣ.
		user = UserService.getUserInfoFromSession(UserModifyActivity.this);
		phone = user.getPhone();
		if (!StringUtils.isEmpty(phone)) {
			user = UserService.getUserInfo(phone);
			phoneText.setEnabled(false);
			phoneText.setText(phone);
		} else if (!StringUtils.isEmpty(user.getUid())) {
			user = UserService.getUserInfoByUid(user.getUid());
			if (StringUtils.isEmpty(user.getPhone())) {
				phoneText.setEnabled(true);
				// ������֤��ؼ�
				TableLayout tableLayout = (TableLayout) this
						.findViewById(R.id.userModifyLayout);
				TableRow phoneTableRow = (TableRow)tableLayout.getChildAt(1);
			//	TableRow verifyCodeTableRow = new TableRow(this);

				Button verifyCodeSendButton = new Button(this);
				verifyCodeSendButton.setText("������֤��");
				verifyCodeSendButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						String phone = phoneText.getText().toString();
						if (phone.equals("")) {
							showDialog("�ֻ����Ǳ����");
						} else {
							String sentResult = requestSendVerifyCode(phone);
							if (JsonUtil.RESULT_TRUE_VALUE.equals(sentResult)) {

								showDialog("��֤�뷢�ͳɹ�����ע����ն���");
							} else {
								showDialog("��֤�뷢��ʧ��:" + sentResult);
							}
						}
					}
				});
				phoneTableRow.addView(verifyCodeSendButton);
				//tableLayout.addView(verifyCodeTableRow, 2);
				
				//�����֤������ؼ�
				TableRow verifyCodeTextTableRow = new TableRow(this);
				TextView verifyCodeTextView = new TextView(this);
				verifyCodeTextView.setText("������֤��:");
			    verifyCodeTextTableRow.addView(verifyCodeTextView);
                 verifyCodeEditText = new EditText(this);
                 verifyCodeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                verifyCodeTextTableRow.addView(verifyCodeEditText);
            /*    Button verifyConfirmButton = new Button(this);
                verifyConfirmButton.setText("��֤");
                verifyConfirmButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						String phone = phoneText.getText().toString();
						if (phone.equals("")) {
							showDialog("�ֻ����Ǳ����");
						} else {
							String sentResult = requestSendVerifyCode(phone);
							if (JsonUtil.RESULT_TRUE_VALUE.equals(sentResult)) {

								showDialog("��֤�뷢�ͳɹ�����ע����ն���");
							} else {
								showDialog("��֤�뷢��ʧ��:" + sentResult);
							}
						}
					}
				});*/
                tableLayout.addView(verifyCodeTextTableRow, 2);
			}
		}

		// �첽��ȡͷ������
		AsyncFetchUserImgTask fetchImgTask = new AsyncFetchUserImgTask(user);
		fetchImgTask.execute(this);
		RadioGroup sexRadioGroup = (RadioGroup) this
				.findViewById(R.id.sexRadioGroup);
		sexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// ��ȡ������ѡ�����ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// ����ID��ȡRadioButton��ʵ��
				RadioButton rb = (RadioButton) UserModifyActivity.this
						.findViewById(radioButtonId);
				sex = rb.getText().toString();
			}
		});

		RadioGroup locationRadioGroup = (RadioGroup) this
				.findViewById(R.id.locationRadioGroup);
		locationRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						// ��ȡ������ѡ�����ID
						int radioButtonId = arg0.getCheckedRadioButtonId();
						// ����ID��ȡRadioButton��ʵ��
						RadioButton rb = (RadioButton) UserModifyActivity.this
								.findViewById(radioButtonId);
						locationPublic = rb.getText().toString();
					}
				});
		if (!StringUtils.isEmpty(user.getUserName())) {
			userNameText.setText(user.getUserName());
		} else if (!StringUtils.isEmpty(user.getScreenName())) {
			userNameText.setText(user.getScreenName());
		}

		if (this.getString(R.string.sex_female).equals(user.getUserSex())) {
			femaleRadio.setChecked(true);
		} else if (this.getString(R.string.sex_male).equals(user.getUserSex())) {
			maleRadio.setChecked(true);
		}
		if (this.getString(R.string.no).equals(user.getLocationPublic())) {
			locationPublicNoRadio.setChecked(true);
		} else {
			locationPublicYesRadio.setChecked(true);
		}
		// ����locationPublicĬ��ֵ.
		RadioButton checkedLocationButton = (RadioButton) this
				.findViewById(locationRadioGroup.getCheckedRadioButtonId());
		locationPublic = checkedLocationButton.getText().toString();

	}

	public String requestSendVerifyCode(String phone) {
		return UserService.requestSendVerifyCode(phone);

	}

	private void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.selectImage:
		 *//***
		 * ����ǵ���android���õ�intent��������ͼƬ�ļ� ��ͬʱҲ���Թ���������
		 */
		/*
		 * Intent intent = new Intent(); intent.setType("image/*");
		 * intent.setAction(Intent.ACTION_GET_CONTENT);
		 * startActivityForResult(intent, 1); break; case R.id.uploadImage: if
		 * (picPath == null) {
		 * 
		 * Toast.makeText(UserModifyActivity.this, "��ѡ��ͼƬ��", 1000).show(); }
		 * else { final File file = new File(picPath);
		 * 
		 * if (file != null) { int responseCode = UploadUtil.uploadFile(file,
		 * requestURL,phone); if(responseCode==200) { Toast.makeText(mContext,
		 * "�ϴ��ɹ�", Toast.LENGTH_LONG).show();; }
		 * 
		 * } } break;
		 */
		case R.id.userModifyButton:
			user.setPhone(phoneText.getText().toString());
			if (!StringUtils.isEmpty(userNameText.getText().toString())) {
				user.setUserName(userNameText.getText().toString());
			}
			if (!StringUtils.isEmpty(sex)) {
				user.setUserSex(sex);
			}
			if (!StringUtils.isEmpty(pwdText.getText().toString())) {
				String md5Pwd = MD5Util
						.getMD5Code(pwdText.getText().toString());
				user.setPwd(md5Pwd);
			}
			if (!StringUtils.isEmpty(locationPublic)) {
				user.setLocationPublic(locationPublic);
			}
			if(verifyCodeEditText!=null) {
				String verifyCode= verifyCodeEditText.getText().toString();
				if(StringUtils.isEmpty(verifyCode)) {
					showDialog("��֤�벻��Ϊ��");
					return;
				} else {
					UserService.userInfoModifyWithVerifyCode(user, MD5Util.getMD5Code(verifyCode));
				}
			} else {
				UserService.userInfoModify(user);
			}
			
			UserService.setCurrentUser(UserModifyActivity.this, user);
			Intent it = new Intent(UserModifyActivity.this,
					ContactMainActivity.class);
			startActivity(it);

			break;
		case R.id.cancelButton:
			Intent it1 = new Intent(UserModifyActivity.this,
					ContactMainActivity.class);
			startActivity(it1);
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * ��ѡ���ͼƬ��Ϊ�յĻ����ڻ�ȡ��ͼƬ��;��
			 */
			Uri uri = data.getData();

			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					/***
					 * ���������һ���ж���Ҫ��Ϊ�˵����������ѡ�񣬱��磺ʹ�õ��������ļ��������Ļ�����ѡ����ļ��Ͳ�һ����ͼƬ�ˣ�
					 * �����Ļ��������ж��ļ��ĺ�׺�� �����ͼƬ��ʽ�Ļ�����ô�ſ���
					 */
					if (path.endsWith("jpg") || path.endsWith("png")) {
						picPath = path;
						Bitmap bitmap = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
						imageView.setImageBitmap(bitmap);
					} else {
						alert();
					}
				} else {
					alert();
				}

			} catch (Exception e) {
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("��ʾ")
				.setMessage("��ѡ��Ĳ�����Ч��ͼƬ")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
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
