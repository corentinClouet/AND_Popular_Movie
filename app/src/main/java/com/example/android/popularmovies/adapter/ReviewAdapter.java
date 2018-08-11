/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.entities.Movie;
import com.example.android.popularmovies.entities.Review;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * {@link ReviewAdapter} exposes a list of reviews to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Review> mLstReview;
    private Context mContext;

    /**
     * Creates a ReviewAdapter.
     */
    public ReviewAdapter(Context context) {
        mLstReview = new ArrayList<>(); //create empty list by default
        mContext = context;
    }

    /**
     * Cache of the children views for a review list item.
     */
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView mAuthorTextView;
        private TextView mContentTextView;

        ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = view.findViewById(R.id.author_textView);
            mContentTextView = view.findViewById(R.id.content_textView);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ReviewAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     *
     * @param reviewAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        Review review = mLstReview.get(position);
        reviewAdapterViewHolder.mAuthorTextView.setText(review.getAuthor());
        reviewAdapterViewHolder.mContentTextView.setText(review.getContent());
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mLstReview) return 0;
        return mLstReview.size();
    }

    /**
     * This method is used to set the review list on a ReviewAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param lstReview The new review list to be displayed.
     */
    public void refreshData(List<Review> lstReview) {
        Log.d(TAG, "Refresh data");
        this.mLstReview = lstReview;
        notifyDataSetChanged();
    }
}