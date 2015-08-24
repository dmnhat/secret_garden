package com.nng.onedollar.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nng.onedollar.R;
import com.nng.onedollar.adapters.ClaimCashAdapter;

public class ClaimCashActivity extends BaseActivity {

	private ListView mLvClaimCash;
	private ClaimCashAdapter mAdapter;
	private ImageView mIvClose, mIvBack;
	private RelativeLayout mRlMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_cash);
		overridePendingTransition(R.anim.in_from_right, R.anim.no_change);

		mLvClaimCash = (ListView) findViewById(R.id.lv_claim_cash);
		mAdapter = new ClaimCashAdapter(this);
		mLvClaimCash.setAdapter(mAdapter);

		mRlMessage = (RelativeLayout) findViewById(R.id.rl_message);
		mIvClose = (ImageView) findViewById(R.id.iv_close);
		mIvClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRlMessage.setVisibility(View.GONE);
			}
		});

		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mIvBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.no_change, R.anim.out_to_bottom);
			}
		});
	}

}
