package com.movies.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.movies.popularmovies.R;
import com.movies.popularmovies.model.PosterMovie;
import com.movies.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by root on 05/08/18.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    List<PosterMovie> listPoster;
    PosterItemClickListener listener;

    public HomeAdapter(List<PosterMovie> listPoster, PosterItemClickListener listener) {
        this.listPoster = listPoster;
        this.listener = listener;

    }

    public HomeAdapter(PosterItemClickListener listener) {
        this.listener = listener;
    }

    public PosterItemClickListener getListener() {
        return listener;
    }

    public void setListener(PosterItemClickListener listener) {
        this.listener = listener;
    }

    public List<PosterMovie> getListPoster() {
        return listPoster;
    }

    public void setListPoster(List<PosterMovie> listPoster) {
        this.listPoster = listPoster;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.poster_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new HomeViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.getListPoster().size();
    }



    class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView posterIV;
        TextView titleTV;
        ConstraintLayout itemCL;
        Context context;

        public HomeViewHolder(View itemView, Context context) {
            super(itemView);
            posterIV = (ImageView) itemView.findViewById(R.id.poster_iv);
            titleTV = (TextView) itemView.findViewById(R.id.title_tv);
            itemCL = (ConstraintLayout) itemView.findViewById(R.id.item_cl);
            this.context = context;

            itemCL.setOnClickListener(this);

        }

        public void bind(int position){
            PosterMovie posterMovie = listPoster.get(position);
            titleTV.setText(posterMovie.getTitle());

            //TODO : load image with picasso
            Picasso.with(context)
                    .load(NetworkUtils.BASE_POSTER_URL+NetworkUtils.posterType+"//"+posterMovie.getPosterUrl())
                    .centerCrop()
                    .fit()
                    .placeholder(R.drawable.load)
                    .error(R.drawable.error)
                    .into(posterIV);
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onPosterClick(listPoster.get(getAdapterPosition()).getId());
            }

        }
    }

    public interface PosterItemClickListener{
        public void onPosterClick(int idMovie);
    }
}
