package com.nng.onedollar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class TMKLoadingView extends RelativeLayout implements OnClickListener {

	public TMKLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// setBackgroundColor(context.getResources().getColor(android.R.color.black));
		// setAlpha(0.7f);
		setVisibility(View.GONE);

		ProgressBar pBar = new ProgressBar(context);
		LayoutParams pBarLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pBarLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		addView(pBar, pBarLayoutParams);

		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// leave this method blank to avoid click on this view
	}

	public void show() {
		setVisibility(View.VISIBLE);
	}

	public void dismiss() {
		setVisibility(View.GONE);
	}

	public boolean isShowing() {
		return getVisibility() == View.VISIBLE ? true : false;
	}

}
