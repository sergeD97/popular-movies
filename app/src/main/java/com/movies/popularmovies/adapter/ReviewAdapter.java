package com.movies.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movies.popularmovies.R;
import com.movies.popularmovies.model.Review;

import java.util.List;

/**
 * Created by Serge Pessokho on 11/08/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView author, content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author_tv);
            content = itemView.findViewById(R.id.content_tv);
        }

        public void bind(Review review){
            author.setText(review.getAuthor());
            content.setText(review.getContent());
        }
    }
}
