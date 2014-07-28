/*
 * Copyright (C) 2014 Open Access Button
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package org.openaccessbutton.openaccessbutton.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import org.openaccessbutton.openaccessbutton.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows a list of blog entries, automatically loading more as user scrolls
 *
 */
public class BlogListFragment extends android.app.ListFragment implements AbsListView.OnScrollListener {
    public BlogListFragment() {}

    // The posts shown
    List<Post> mItems = new ArrayList<Post>();
    ArrayAdapter<Post> mAdapter;

    // Loading spinner
    View footerView;

    // For pagination
    // TODO: Stop when we get to the end of all posts
    private int mCurrentPage = 0;

    // Number of posts above the bottom at which the threshold to load more posts is reached
    private static int sThreshold = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Setup ArrayAdapter
        mAdapter = new ArrayAdapter<Post>(inflater.getContext(),
                R.layout.blog_list_item, mItems);
        setListAdapter(mAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);

        // Call this class on scroll movements
        getListView().setOnScrollListener(this);

        // Setup footer view and hide it
        footerView = getActivity().getLayoutInflater().inflate(R.layout.blog_list_footer, null);
        getListView().addFooterView(footerView);
        footerView.setVisibility(View.GONE);

        // TODO: Automatically determine how many posts to load
        loadMore();
    }

    /**
     * Load more posts
     */
    private void loadMore() {
        // Show loading spinner
        footerView.setVisibility(View.VISIBLE);
        // Update now not after posts downloaded in case two requests fired
        mCurrentPage++;
        new DownloadTask().execute(new DownloadTask.OnDownloadCompleteListener() {
            @Override
            public void onDownloadComplete(List<Post> items) {
                // Add posts to List and show them
                for (Post item : items) {
                    mItems.add(item);
                }
                mAdapter.notifyDataSetChanged();

                // Hide loading spinner
                footerView.setVisibility(View.GONE);
            }
        }, mCurrentPage);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {}

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        // Once scrolling has stopped
        if (scrollState == SCROLL_STATE_IDLE) {
            if (needsMorePosts()) { loadMore(); }
        }
    }

    /**
     * Whether more posts should be loaded (based on how far user has scrolled)
     */
    private boolean needsMorePosts() {
        AbsListView listView = getListView();
        return listView.getLastVisiblePosition() >= listView.getCount() - 1 - sThreshold;
    }
}
