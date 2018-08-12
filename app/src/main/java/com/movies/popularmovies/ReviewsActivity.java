package com.movies.popularmovies;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movies.popularmovies.adapter.ReviewAdapter;
import com.movies.popularmovies.model.Review;
import com.movies.popularmovies.utils.JsonUtils;
import com.movies.popularmovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Review>> {
    public static final int REVIEW_LOADER_ID = 121;
    ReviewAdapter reviewAdapter;

    @BindView(R.id.error_tv)
    TextView error_tv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.list_rv)
    RecyclerView list_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        reviewAdapter = new ReviewAdapter(new ArrayList<Review>());
        list_rv.setLayoutManager(new LinearLayoutManager(this));
        list_rv.setAdapter(reviewAdapter);

        int idMovie = getIntent().getIntExtra(DetailsActivity.MOVIE_ID_EXTRA, -1);

        if(idMovie != -1){
            Bundle queryBundle = new Bundle();
            queryBundle.putString(DetailsActivity.MOVIE_ID_EXTRA, String.valueOf(idMovie));
            getSupportLoaderManager().initLoader(REVIEW_LOADER_ID,queryBundle,this);
        }else{
           endLoadError();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Review>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<List<Review>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    return;
                }
                if(args.getString(DetailsActivity.MOVIE_ID_EXTRA) == null){
                    return;
                }
                startLoad();
                forceLoad();
            }

            @Nullable
            @Override
            public List<Review> loadInBackground() {
                try{
                    String id = args.getString(DetailsActivity.MOVIE_ID_EXTRA);
                    if(id == null){
                        return null;
                    }
                    String result = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(id+NetworkUtils.REVIEW));
                    return JsonUtils.buildReviewListFromJson(JsonUtils.getMoviesListJson(new JSONObject(result)));

                }catch (Exception e){
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> data) {
        if(data != null){
            reviewAdapter.setReviewList(data);
            endLoadSuccess();
        }else{
            endLoadError();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Review>> loader) {

    }

    public void startLoad(){
        progressBar.setVisibility(View.VISIBLE);
        list_rv.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.INVISIBLE);
    }

    public void endLoadSuccess(){
        progressBar.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.INVISIBLE);
        list_rv.setVisibility(View.VISIBLE);
    }
    public void endLoadError(){
        progressBar.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.VISIBLE);
        list_rv.setVisibility(View.INVISIBLE);
    }

}
