package com.movies.popularmovies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.popularmovies.model.Movie;
import com.movies.popularmovies.utils.JsonUtils;
import com.movies.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    public static final String MOVIE_ID_EXTRA = "movie_id";
    public static final int MOVIE_LOADER_ID = 101;


    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.rated_tv)
    TextView rated_tv;
    @BindView(R.id.synopsis_tv)
    TextView synopsis_tv;
    @BindView(R.id.date_tv)
    TextView date_tv;
    @BindView(R.id.error_tv)
    TextView error_tv;
    @BindView(R.id.poster_iv)
    ImageView poster_iv;
    @BindView(R.id.content_cl)
    ConstraintLayout content_cl;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        int id = intent.getIntExtra(MOVIE_ID_EXTRA, -1);

        if(id == -1){
            endLoadError();
            Toast.makeText(this, "No movie to load...", Toast.LENGTH_SHORT).show();

        }else{
            Bundle queryBundle = new Bundle();
            queryBundle.putString(MOVIE_ID_EXTRA, String.valueOf(id));

            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,queryBundle,this);
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

    public void startLoad(){
        progressBar.setVisibility(View.VISIBLE);
        content_cl.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.INVISIBLE);

    }

    public void endLoadSuccess(){
        progressBar.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.INVISIBLE);
        content_cl.setVisibility(View.VISIBLE);
    }
    public void endLoadError(){
        progressBar.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.VISIBLE);
        content_cl.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<Movie> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    //no id specified nothibg to do
                    return;
                }
                startLoad();
                forceLoad();
            }

            @Nullable
            @Override
            public Movie loadInBackground() {
                String movieId = args.getString(MOVIE_ID_EXTRA);
                if(movieId == null || movieId.isEmpty()){
                    return null;
                }
                try{
                    String result = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(movieId));
                    return JsonUtils.buildMovieFromJson(new JSONObject(result));

                }catch(Exception e){
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movie> loader, Movie data) {
        if(data != null){
            title_tv.setText(data.getTitle());
            rated_tv.setText(String.valueOf(data.getRate()+"/10"));
            synopsis_tv.setText(data.getSynopsis());
            date_tv.setText(data.getDate());
            Picasso.with(this)
                    .load(NetworkUtils.BASE_POSTER_URL+NetworkUtils.posterType+"//"+data.getPosterUrl())
                    .centerCrop()
                    .fit()
                    .placeholder(R.drawable.load)
                    .error(R.drawable.error)
                    .into(poster_iv);
            setTitle(data.getTitle());
            endLoadSuccess();
        }else{
            endLoadError();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie> loader) {

    }
}
