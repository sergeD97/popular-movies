package com.movies.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movies.popularmovies.R;
import com.movies.popularmovies.model.Trailer;

import java.util.List;

/**
 * Created by Serge Pessokho on 11/08/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailerList;
    private TrailerClickListener trailerClickListener;

    public TrailerAdapter(List<Trailer> trailerList, TrailerClickListener trailerClickListener) {
        this.trailerList = trailerList;
        this.trailerClickListener = trailerClickListener;
    }

    public List<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    public TrailerClickListener getTrailerClickListener() {
        return trailerClickListener;
    }

    public void setTrailerClickListener(TrailerClickListener trailerClickListener) {
        this.trailerClickListener = trailerClickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(trailerList.get(position));
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name_tv;
        View trailer_view;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            name_tv = (TextView) itemView.findViewById(R.id.author_tv);
            trailer_view = itemView.findViewById(R.id.trailer);
            trailer_view.setOnClickListener(this);
        }

        public void bind(Trailer trailer){
            name_tv.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            if(trailerClickListener != null){
                trailerClickListener.onTrailerClick(trailerList.get(getAdapterPosition()));
            }
        }
    }

    public interface TrailerClickListener{
        public void onTrailerClick(Trailer trailer);
    }
}
