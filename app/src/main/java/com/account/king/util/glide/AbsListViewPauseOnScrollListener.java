package com.account.king.util.glide;

import android.app.Activity;
import android.content.Context;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public class AbsListViewPauseOnScrollListener implements OnScrollListener {

	private final boolean pauseOnScroll;
	private final boolean pauseOnFling;
	private final OnScrollListener externalListener;
	private RequestManager requestManager;
	private Activity activity;


	public AbsListViewPauseOnScrollListener(Activity activity, boolean pauseOnScroll, boolean pauseOnFling) {
		this(Glide.with(activity), pauseOnScroll, pauseOnFling, null);
	}

	public AbsListViewPauseOnScrollListener(Context context, boolean pauseOnScroll, boolean pauseOnFling) {
		this(Glide.with(context), pauseOnScroll, pauseOnFling, null);
	}

	public AbsListViewPauseOnScrollListener(Context context, boolean pauseOnScroll, boolean pauseOnFling, OnScrollListener customListener) {
		this(Glide.with(context), pauseOnScroll, pauseOnFling, customListener);
	}

	public AbsListViewPauseOnScrollListener(RequestManager requestManager, boolean pauseOnScroll, boolean pauseOnFling,
											OnScrollListener customListener) {
		this.requestManager = requestManager;
		this.pauseOnScroll = pauseOnScroll;
		this.pauseOnFling = pauseOnFling;
		externalListener = customListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				requestManager.resumeRequests();
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				if (pauseOnScroll) {
					requestManager.pauseRequests();
				}
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				if (pauseOnFling) {
					requestManager.pauseRequests();
				}
				break;
		}
		if (externalListener != null) {
			externalListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (externalListener != null) {
			externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
}
