package com.nng.onedollar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nng.onedollar.R;
import com.nng.onedollar.adapters.CartMissedAdapter;

public class CartMissedFragment extends BaseFragment {

	private View mRootView;
	private ListView mLvChat;
	private CartMissedAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_cart_bet, container,
					false);
			mLvChat = (ListView) mRootView.findViewById(R.id.lv_cart);
			mAdapter = new CartMissedAdapter(getActivity());
			mLvChat.setAdapter(mAdapter);
		}

		return mRootView;
	}

}
