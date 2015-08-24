package com.nng.onedollar.fragments;

import java.util.ArrayList;

import com.nng.onedollar.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class CartFragment extends BaseFragment implements OnClickListener {

	private View mRootView;
	private TextView mTvBet, mTvWon, mTvMissed;
	private ArrayList<TextView> mTabItems;

	private CartBetFragment mFragmentBet;
	private CartWonFragment mFragmentWon;
	private CartMissedFragment mFragmentMissed;

	private int mSelectedTabId = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_cart, container, false);

			mTvBet = (TextView) mRootView.findViewById(R.id.tv_bet);
			mTvBet.setOnClickListener(this);
			mTvWon = (TextView) mRootView.findViewById(R.id.tv_won);
			mTvWon.setOnClickListener(this);
			mTvMissed = (TextView) mRootView.findViewById(R.id.tv_missed);
			mTvMissed.setOnClickListener(this);

			mTabItems = new ArrayList<TextView>();
			mTabItems.add(mTvBet);
			mTabItems.add(mTvWon);
			mTabItems.add(mTvMissed);

			mFragmentBet = new CartBetFragment();
			mFragmentWon = new CartWonFragment();
			mFragmentMissed = new CartMissedFragment();

			getChildFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentBet).commit();
			getChildFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentWon).commit();
			getChildFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentMissed).commit();

			// init screen
			mSelectedTabId = R.id.tv_bet;
			showOrHideFragment("bet");
		}

		return mRootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		switch (mSelectedTabId) {
		case R.id.tv_bet:
			showOrHideFragment("bet");
			break;

		case R.id.tv_won:
			showOrHideFragment("won");
			break;

		case R.id.tv_missed:
			showOrHideFragment("missed");
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_bet:
			mSelectedTabId = v.getId();
			showOrHideFragment("bet");
			break;

		case R.id.tv_won:
			mSelectedTabId = v.getId();
			showOrHideFragment("won");
			break;

		case R.id.tv_missed:
			mSelectedTabId = v.getId();
			showOrHideFragment("missed");
			break;
		}
	}

	private void setTabItemSelected(TextView item) {
		for (TextView view : mTabItems) {
			view.setSelected(false);
		}
		item.setSelected(true);
	}

	private void showOrHideFragment(String fragment) {
		getChildFragmentManager().beginTransaction().hide(mFragmentBet).commit();
		getChildFragmentManager().beginTransaction().hide(mFragmentWon).commit();
		getChildFragmentManager().beginTransaction().hide(mFragmentMissed).commit();

		if ("bet".equals(fragment)) {
			getChildFragmentManager().beginTransaction().show(mFragmentBet).commit();
			setTabItemSelected(mTvBet);
		} else if ("won".equals(fragment)) {
			getChildFragmentManager().beginTransaction().show(mFragmentWon).commit();
			setTabItemSelected(mTvWon);
		} else if ("missed".equals(fragment)) {
			getChildFragmentManager().beginTransaction().show(mFragmentMissed).commit();
			setTabItemSelected(mTvMissed);
		}
	}

}
