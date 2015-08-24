package com.nng.onedollar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nng.onedollar.R;
import com.nng.onedollar.adapters.NotificationAdapter;

public class NotificationFragment extends BaseFragment {

	private View mRootView;
	private ListView mLvNotification;
	private NotificationAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_notification,
					container, false);
			mLvNotification = (ListView) mRootView
					.findViewById(R.id.lv_notification);
			mAdapter = new NotificationAdapter(getActivity());
			mLvNotification.setAdapter(mAdapter);
		}

		return mRootView;
	}

}
