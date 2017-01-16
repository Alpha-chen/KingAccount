package com.account.king.util.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public class RecyclerViewPauseOnScrollListener extends RecyclerView.OnScrollListener {

	private final boolean pauseOnScroll;
	private final boolean pauseOnFling;
	private final RecyclerView.OnScrollListener externalListener;
	private RequestManager mRequestManager;


	public RecyclerViewPauseOnScrollListener(Activity activity, boolean pauseOnScroll, boolean pauseOnFling) {
		this(activity, pauseOnScroll, pauseOnFling, null);
	}

	public RecyclerViewPauseOnScrollListener(Context context, boolean pauseOnScroll, boolean pauseOnFling) {
		this(context, pauseOnScroll, pauseOnFling, null);
	}

	public RecyclerViewPauseOnScrollListener(Context context, boolean pauseOnScroll, boolean pauseOnFling,
											 RecyclerView.OnScrollListener customListener) {
		this.mRequestManager = Glide.with(context);
		this.pauseOnScroll = pauseOnScroll;
		this.pauseOnFling = pauseOnFling;
		externalListener = customListener;
	}

	public RecyclerViewPauseOnScrollListener(Activity activity, boolean pauseOnScroll, boolean pauseOnFling,
											 RecyclerView.OnScrollListener customListener) {
		this.mRequestManager = Glide.with(activity);
		this.pauseOnScroll = pauseOnScroll;
		this.pauseOnFling = pauseOnFling;
		externalListener = customListener;
	}

	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
		switch (newState) {
			case 0:
				mRequestManager.resumeRequests();
				break;
			case 1:
				if (pauseOnScroll) {
					mRequestManager.pauseRequests();
				}
				break;
			case 2:
				if (pauseOnFling) {
					mRequestManager.pauseRequests();
				}
		}
		if (this.externalListener != null) {
			this.externalListener.onScrollStateChanged(recyclerView, newState);
		}
	}

	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		if(this.externalListener != null) {
			this.externalListener.onScrolled(recyclerView, dx, dy);
		}
	}
}
