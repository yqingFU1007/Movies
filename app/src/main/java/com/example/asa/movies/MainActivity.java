package com.example.asa.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    String URL_BASE = "https://api.themoviedb.org/3/movie/popular?api_key=cee990f8787ceedaa4db6a9496a53c45";

    HomepageAdapter mAdapter;
    GridView gridView;
    ArrayList<MovieInfo> mMovieInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new NetworkTask().execute(URL_BASE);



        gridView = (GridView)findViewById(R.id.grid_view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                MovieInfo movieCurrent = mAdapter.getItem(position);

                String posterSource = movieCurrent.getmPosterUrlString();
                String overView = movieCurrent.getmOverView();
                String title = movieCurrent.getmMovieName();
                String score = movieCurrent.getmVoteAverage();
                String date = movieCurrent.getmReleaseDate();
                Log.e("MainActivity",movieCurrent.getmReleaseDate() + "");
                Log.e("MainActivity",movieCurrent.getmOverView() + "");

                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("posterSource",posterSource);
                bundle.putString("overView",overView);
                bundle.putString("title",title);
                bundle.putString("score",score);
                bundle.putString("date",date);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    private class NetworkTask extends AsyncTask<String,Void,ArrayList<MovieInfo>>{

        @Override
        protected ArrayList<MovieInfo> doInBackground(String... strings) {

            mMovieInfos = Utils.parseJSONResponse(strings[0]);
            Log.e("MainActivity",mMovieInfos.get(0).getmMovieName() + "");
            return  mMovieInfos;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieInfos) {
            mAdapter = new HomepageAdapter(MainActivity.this,movieInfos);
            Log.e("MainActivity","" + mAdapter);
            gridView.setAdapter(mAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        Comparator<MovieInfo> c;
        if (menuItemThatWasSelected == R.id.action_sort_by_score){
            c = new Comparator<MovieInfo>() {
                @Override
                public int compare(MovieInfo movieInfo, MovieInfo t1) {
                    if (Double.parseDouble(movieInfo.getmVoteAverage()) > Double.parseDouble(t1.getmVoteAverage())){
                        return -1;
                    }
                    else return 1;
                }
            };
            Collections.sort(mMovieInfos,c);
            for (int i = 0;i < mMovieInfos.size();i++){
                Log.e("MainActivity",mMovieInfos.get(i).getmVoteAverage());
                Log.e("MainActivity",mMovieInfos.get(i).getmPosterUrlString());
            }

            mAdapter = new HomepageAdapter(MainActivity.this,mMovieInfos);
            gridView.setAdapter(mAdapter);
        }

        if (menuItemThatWasSelected == R.id.action_sort_by_popularity){
            c = new Comparator<MovieInfo>() {
                @Override
                public int compare(MovieInfo movieInfo, MovieInfo t1) {
                    if (Double.parseDouble(movieInfo.getmPopularity()) > Double.parseDouble(t1.getmPopularity())){
                        return -1;
                    }
                    else return 1;
                }
            };
            Collections.sort(mMovieInfos,c);
            for (int i = 0;i < mMovieInfos.size();i++){
                Log.e("MainActivity",mMovieInfos.get(i).getmPopularity());
                Log.e("MainActivity",mMovieInfos.get(i).getmPopularity());
            }

            mAdapter = new HomepageAdapter(MainActivity.this,mMovieInfos);
            gridView.setAdapter(mAdapter);
        }

        return super.onOptionsItemSelected(item);
    }
}
