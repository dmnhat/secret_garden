package com.nng.onedollar.activities;

import com.nng.onedollar.R;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.utils.UiUtil;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends BaseActivity {

	private EditText mEtEmail, mEtPassword;
	private String mEmail, mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		overridePendingTransition(R.anim.in_from_right, R.anim.no_change);

		mEtEmail = (EditText) findViewById(R.id.et_email);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		mEtPassword.setTypeface(mEtEmail.getTypeface());

		TextView tvSignIn = (TextView) findViewById(R.id.tv_sign_in);
		tvSignIn.setPaintFlags(tvSignIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		tvSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
				finish();
				overridePendingTransition(R.anim.no_change, R.anim.out_to_bottom);
			}
		});

		findViewById(R.id.btn_sign_up).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
				if (validate()) {
					Intent intent = new Intent(getApplicationContext(), SignUpDetailActivity.class);
					intent.putExtra("email", mEmail);
					intent.putExtra("password", mPassword);
					startActivity(intent);
				}
			}
		});
	}

	private boolean validate() {
		mEmail = mEtEmail.getText().toString().trim();
		mPassword = mEtPassword.getText().toString();

		if (TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPassword)) {
			ErrorUtil.showError(this, getString(R.string.msg_err_not_enough_info));
			return false;
		} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
			ErrorUtil.showError(this, getString(R.string.msg_invalid_email));
			return false;
		}

		return true;
	}
}
