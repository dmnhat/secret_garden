package com.nng.onedollar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nng.onedollar.R;
import com.nng.onedollar.adapters.ChatAdapter;

public class ChatFragment extends BaseFragment {

    private View mRootView;
    private ListView mLvChat;
    private ChatAdapter mAdapter;
    private TextView mTvMessage;
    private ImageView mIvClose;
    private RelativeLayout mRlMessage;
    private String mMessage;

    public ChatFragment() {

    }

    public ChatFragment(String message) {
        mMessage = message;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_chat, container,
                    false);
            mLvChat = (ListView) mRootView.findViewById(R.id.lv_chat);
            mAdapter = new ChatAdapter(getActivity());
            mLvChat.setAdapter(mAdapter);

            mTvMessage = (TextView) mRootView.findViewById(R.id.tv_message);
            mTvMessage.setText(mMessage);
            mRlMessage = (RelativeLayout) mRootView
                    .findViewById(R.id.rl_message);
            mIvClose = (ImageView) mRootView.findViewById(R.id.iv_close);
            mIvClose.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mRlMessage.setVisibility(View.GONE);
                }
            });
        }

        return mRootView;
    }

}
