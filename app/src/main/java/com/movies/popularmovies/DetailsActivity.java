package com.movies.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.popularmovies.adapter.TrailerAdapter;
import com.movies.popularmovies.database.AppDatabase;
import com.movies.popularmovies.model.Movie;
import com.movies.popularmovies.model.Trailer;
import com.movies.popularmovies.utils.JsonUtils;
import com.movies.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trailer>>, TrailerAdapter.TrailerClickListener, View.OnClickListener {

    public static final String MOVIE_ID_EXTRA = "movie_id";
    public static final int MOVIE_LOADER_ID = 101;

    TrailerAdapter trailerAdapter;
    int idMovie;
    Movie data;
    AppDatabase mDb;

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
    @BindView(R.id.trailer_list)
    RecyclerView trailer_list;
    @BindView(R.id.fav_btn)
    Button fav_btn;
    @BindView(R.id.review_btn)
    Button review_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        mDb = AppDatabase.getInstance(getApplicationContext());

        trailerAdapter = new TrailerAdapter(new ArrayList<Trailer>(), this);
        trailer_list.setHasFixedSize(true);
        trailer_list.setNestedScrollingEnabled(false);
        trailer_list.setLayoutManager(new LinearLayoutManager(this));
        trailer_list.setAdapter(trailerAdapter);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
       data = intent.getParcelableExtra(MOVIE_ID_EXTRA);

        if(data == null){
            Toast.makeText(this, "No movie to load...", Toast.LENGTH_SHORT).show();
            finish();

        }else{
            idMovie = data.getId();
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

            LiveData<Movie> liveData = mDb.movieDao().select(idMovie);
            liveData.observe(DetailsActivity.this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    if(movie != null){
                        fav_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AsyncTask<Void, Void,Void>(){
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        mDb.movieDao().delete(data);
                                        return null;
                                    }
                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        Toast.makeText(DetailsActivity.this, getString(R.string.unfav_success), Toast.LENGTH_LONG).show();
                                    }
                                }.execute();
                            }
                        });
                        fav_btn.setText(R.string.un_favorit_btn);
                    }else{
                        fav_btn.setOnClickListener(DetailsActivity.this);
                        fav_btn.setText(R.string.favorit_btn);
                    }
                }
            });


            review_btn.setOnClickListener(this);

            Bundle queryBundle = new Bundle();
            queryBundle.putString(MOVIE_ID_EXTRA, String.valueOf(data.getId()));

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
        trailer_list.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.INVISIBLE);

    }

    public void endLoadSuccess(){
        progressBar.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.INVISIBLE);
        trailer_list.setVisibility(View.VISIBLE);
    }
    public void endLoadError(){
        progressBar.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.VISIBLE);
        trailer_list.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<List<Trailer>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<List<Trailer>>(this) {

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
            public List<Trailer> loadInBackground() {
                String movieId = args.getString(MOVIE_ID_EXTRA);
                if(movieId == null || movieId.isEmpty()){
                    return null;
                }
                try{
                    String result = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(movieId+NetworkUtils.TRAILER));
                    return JsonUtils.buildTrailerListFromJson(JsonUtils.getMoviesListJson(new JSONObject(result)));

                }catch(Exception e){
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Trailer>> loader, List<Trailer> data) {
        if(data != null){
            trailerAdapter.setTrailerList(data);
            endLoadSuccess();
        }else{
            endLoadError();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Trailer>> loader) {

    }

    @Override
    public void onTrailerClick(Trailer trailer) {
        Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkUtils.YOUTUBE_WATCH+trailer.getKey()));
        startActivity(link);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.review_btn){
            Intent review = new Intent(DetailsActivity.this, ReviewsActivity.class);
            review.putExtra(MOVIE_ID_EXTRA, idMovie);
            startActivity(review);
        }else if(id == R.id.fav_btn){
            new AsyncTask<Void, Void,Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    mDb.movieDao().insert(data);
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(DetailsActivity.this, getString(R.string.success), Toast.LENGTH_LONG).show();
                }
            }.execute();
        }
    }
}
