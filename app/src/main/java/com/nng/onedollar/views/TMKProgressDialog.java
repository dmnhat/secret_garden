package com.nng.onedollar.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.nng.onedollar.R;

public class TMKProgressDialog extends ProgressDialog {

	private TextView mTvMessage;
	private Context mContext;

	public TMKProgressDialog(Context context) {
		super(context);
		mContext = context;
		setIndeterminate(true);
		setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_progress_dialog);

		mTvMessage = (TextView) findViewById(R.id.tv_message);
	}

	public void setMessage(String message) {
		if (!TextUtils.isEmpty(message)) {
			mTvMessage.setVisibility(View.VISIBLE);
			mTvMessage.setText(message);
		} else {
			mTvMessage.setVisibility(View.GONE);
		}
	}

	public void setMessage(int message) {
		String msg = null;
		if (message != -1) {
			msg = mContext.getString(message);
		}
		setMessage(msg);
	}

}
