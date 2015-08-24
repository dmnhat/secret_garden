package com.nng.onedollar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nng.onedollar.R;
import com.nng.onedollar.activities.ViewProductUserProfileActivity;
import com.nng.onedollar.adapters.CartBetAdapter;

public class CartSoldFragment extends BaseFragment {

	private View mRootView;
	private ListView mLvChat;
	private CartBetAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_cart_bet, container,
					false);
			mLvChat = (ListView) mRootView.findViewById(R.id.lv_cart);
			mAdapter = new CartBetAdapter(getActivity());
			mLvChat.setAdapter(mAdapter);
		}
		mLvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent userProfileViewProduct = new Intent(getActivity(), ViewProductUserProfileActivity.class);
				getActivity().startActivity(userProfileViewProduct);
			}
		});
		return mRootView;
	}

}
