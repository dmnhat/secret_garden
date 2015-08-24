package com.nng.onedollar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.nng.onedollar.R;

public class TMKLoadMoreListView extends ListView implements OnScrollListener {

	private boolean mLoadMore = false;
	private View mFooterView;
	private ProgressBar mProgressBar;
	private boolean mShowFooter = true;

	public TMKLoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnScrollListener(this);

		LayoutInflater inflater = LayoutInflater.from(context);
		mFooterView = inflater.inflate(R.layout.item_listview_footer, null);
		mProgressBar = (ProgressBar) mFooterView
				.findViewById(R.id.progress_bar);

		mProgressBar.setVisibility(View.GONE);
		addFooterView(mFooterView);

		setOverScrollMode(OVER_SCROLL_NEVER);
	}

	public void allowShowFooter(boolean value) {
		mShowFooter = value;

		if (!mShowFooter && getFooterViewsCount() == 1) {
			removeFooterView(mFooterView);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// don't need to implement this method
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mLoadMore) {
			int threshold = 1;

			if (!mShowFooter) {
				threshold = 10;
			}

			int count = view.getCount();

			if (scrollState == SCROLL_STATE_IDLE) {
				if (view.getLastVisiblePosition() >= count - threshold) {
					mLoadMore = false;

					if (mOnLoadMoreListener != null) {
						mOnLoadMoreListener.loadMore();
					}
				}
			}
		}
	}

	public void setOnLoadMoreListener(OnLoadMoreListener listener) {
		mOnLoadMoreListener = listener;
	}

	public void setLoadMore(boolean value) {
		mLoadMore = value;

		if (mShowFooter) {
			if (mLoadMore) {
				mProgressBar.setVisibility(View.VISIBLE);
				if (getFooterViewsCount() == 0) {
					addFooterView(mFooterView);
				}
			} else {
				mProgressBar.setVisibility(View.GONE);
				if (getFooterViewsCount() == 1) {
					removeFooterView(mFooterView);
				}
			}
		}
	}

	private OnLoadMoreListener mOnLoadMoreListener;

	public interface OnLoadMoreListener {

		public void loadMore();
	}

}
