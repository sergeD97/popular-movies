package com.movies.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.movies.popularmovies.adapter.HomeAdapter;
import com.movies.popularmovies.database.AppDatabase;
import com.movies.popularmovies.model.Movie;
import com.movies.popularmovies.utils.JsonUtils;
import com.movies.popularmovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeAdapter.PosterItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<List<Movie>> {
    public static final String MOVIE_URL_EXTRA = "movies_url";
    public static final int MOVIE_LOADER_TASK_ID = 111;


    private HomeAdapter homeAdapter;
    AppDatabase mDb;
    LiveData<List<Movie>> liveData;

    @BindView(R.id.list_rv)
    RecyclerView movieListRv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.error_tv)
    TextView error_tv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mDb = AppDatabase.getInstance(getApplicationContext());

        setSupportActionBar(toolbar);

        homeAdapter = new HomeAdapter(new ArrayList<Movie>(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        movieListRv.setLayoutManager(gridLayoutManager);
        movieListRv.setHasFixedSize(true);
        movieListRv.setAdapter(homeAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(MOVIE_URL_EXTRA, sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_most_pop)));

        getSupportLoaderManager().initLoader(MOVIE_LOADER_TASK_ID,queryBundle,this);
        setTitle(sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_most_pop)));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent setting = new Intent (MainActivity.this, SettingActivity.class);
            startActivity(setting);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPosterClick(Movie movie) {
        Intent detail = new Intent(MainActivity.this, DetailsActivity.class);
        detail.putExtra(DetailsActivity.MOVIE_ID_EXTRA, movie);
        startActivity(detail);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        reloadMovie(sharedPreferences.getString(key, getString(R.string.pref_most_pop)));
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable final Bundle args) {



        return new AsyncTaskLoader<List<Movie>>(this){

            List<Movie> list;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    //nothing to do
                    return;
                }
                startLoad();
                if(args.getString(MOVIE_URL_EXTRA).equals(getString(R.string.pref_favorite))){
                    if(liveData == null){
                        liveData = mDb.movieDao().selectAll();
                    }
                    liveData.observe(MainActivity.this, new Observer<List<Movie>>() {
                        @Override
                        public void onChanged(@Nullable List<Movie> movies) {
                            deliverResult(movies);
                        }
                    });
                    return;
                }else{
                    if(liveData != null){
                        if(liveData.hasObservers()){
                            liveData.removeObservers(MainActivity.this);
                        }
                    }
                }



                forceLoad();
            }

            @Override
            public List<Movie> loadInBackground() {
                try{
                    String sort = args.getString(MOVIE_URL_EXTRA);
                    String req = "";

                    if(sort.equals(getString(R.string.pref_most_pop))){
                        req = getString(R.string.popular);
                    }else if(sort.equals(getString(R.string.pref_more_rated))){
                        req = getString(R.string.rated);;
                    }else if(sort.equals(getString(R.string.pref_favorite))){


                    }else{
                        return null;
                    }
                    String result = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(req));

                    return JsonUtils.buildMovieListFromJson(JsonUtils.getMoviesListJson(new JSONObject(result)));

                }catch (Exception e){
                    //We get error, we return null;
                    /**********
                    List<Movie> l = new ArrayList<>();
                    Movie m = new Movie();
                    m.setTitle(e.getMessage());
                    l.add(m);
                     *******/
                    Log.e("", e.getMessage());
                    return  null;
                }

            }

        };


    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        if(data!=null){
           /* if(data.size() == 1){
                Toast.makeText(this, data.get(0).getTitle(), Toast.LENGTH_LONG).show();
            }*/
            homeAdapter.setListPoster(data);
             endLoadSuccess();
        }else{
            endLoadError();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }

    ////////////////

    public void startLoad(){
        progressBar.setVisibility(View.VISIBLE);
        movieListRv.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.INVISIBLE);

    }

    public void endLoadSuccess(){
        progressBar.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.INVISIBLE);
        movieListRv.setVisibility(View.VISIBLE);
    }
    public void endLoadError(){
        progressBar.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.VISIBLE);
        movieListRv.setVisibility(View.INVISIBLE);
    }

    public void reloadMovie(String sortBy){

        Bundle queryBundle = new Bundle();
        queryBundle.putString(MOVIE_URL_EXTRA, sortBy);
        setTitle(sortBy);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieLoader = loaderManager.getLoader(MOVIE_LOADER_TASK_ID);

        if(movieLoader != null){
            loaderManager.restartLoader(MOVIE_LOADER_TASK_ID,queryBundle, this);
        }else{
            loaderManager.initLoader(MOVIE_LOADER_TASK_ID,queryBundle,this);

        }
    }


}
