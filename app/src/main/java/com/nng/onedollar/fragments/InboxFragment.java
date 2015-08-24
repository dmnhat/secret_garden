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

public class InboxFragment extends BaseFragment implements OnClickListener {

	private View mRootView;
	private TextView mTvSold, mTvWon;
	private ArrayList<TextView> mTabItems;

	private ChatFragment mFragmentSold, mFragmentWon;

	private int mSelectedTabId = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_inbox, container, false);

			mTvSold = (TextView) mRootView.findViewById(R.id.tv_sold);
			mTvSold.setOnClickListener(this);
			mTvWon = (TextView) mRootView.findViewById(R.id.tv_won);
			mTvWon.setOnClickListener(this);

			mTabItems = new ArrayList<TextView>();
			mTabItems.add(mTvSold);
			mTabItems.add(mTvWon);

			mFragmentSold = new ChatFragment(getString(R.string.fragment_chat_text1));
			mFragmentWon = new ChatFragment(getString(R.string.fragment_chat_text2));

			getChildFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentSold).commit();
			getChildFragmentManager().beginTransaction().add(R.id.content_frame, mFragmentWon).commit();

			// init screen
			mSelectedTabId = R.id.tv_sold;
			showOrHideFragment("sold");
		}

		return mRootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		switch (mSelectedTabId) {
		case R.id.tv_sold:
			showOrHideFragment("sold");
			break;

		case R.id.tv_won:
			showOrHideFragment("won");
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sold:
			mSelectedTabId = v.getId();
			showOrHideFragment("sold");
			break;

		case R.id.tv_won:
			mSelectedTabId = v.getId();
			showOrHideFragment("won");
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
		getChildFragmentManager().beginTransaction().hide(mFragmentSold).commit();
		getChildFragmentManager().beginTransaction().hide(mFragmentWon).commit();

		if ("sold".equals(fragment)) {
			getChildFragmentManager().beginTransaction().show(mFragmentSold).commit();
			setTabItemSelected(mTvSold);
		} else if ("won".equals(fragment)) {
			getChildFragmentManager().beginTransaction().show(mFragmentWon).commit();
			setTabItemSelected(mTvWon);
		}
	}

}
