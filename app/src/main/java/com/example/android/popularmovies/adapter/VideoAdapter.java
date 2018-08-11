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
import com.example.android.popularmovies.entities.Video;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * {@link VideoAdapter} exposes a list of videos to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Video> mLstVideo;
    private final VideoAdapterOnClickHandler mClickHandler;
    private Context mContext;

    /**
     * The interface that receives onClick messages.
     */
    public interface VideoAdapterOnClickHandler {
        void onClick(Video video);
    }

    /**
     * Creates a VideoAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public VideoAdapter(VideoAdapterOnClickHandler clickHandler, Context context) {
        mLstVideo = new ArrayList<>(); //create empty list by default
        mClickHandler = clickHandler;
        mContext = context;
    }

    /**
     * Cache of the children views for a video list item.
     */
    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private ImageView mIconImageView;
        private TextView mTitleTextView;

        VideoAdapterViewHolder(View view) {
            super(view);
            mIconImageView = view.findViewById(R.id.icon_imageView);
            mTitleTextView = view.findViewById(R.id.title_textView);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Video video = mLstVideo.get(adapterPosition);
            mClickHandler.onClick(video);
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
     * @return A new VideoAdapterViewHolder that holds the View for each list item
     */
    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new VideoAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     *
     * @param videoAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(VideoAdapterViewHolder videoAdapterViewHolder, int position) {
        Video video = mLstVideo.get(position);
        videoAdapterViewHolder.mTitleTextView.setText(video.getName());
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mLstVideo) return 0;
        return mLstVideo.size();
    }

    /**
     * This method is used to set the video list on a VideoAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new VideoAdapter to display it.
     *
     * @param lstVideo The new video list to be displayed.
     */
    public void refreshData(List<Video> lstVideo) {
        Log.d(TAG, "Refresh data");
        this.mLstVideo = lstVideo;
        notifyDataSetChanged();
    }
}