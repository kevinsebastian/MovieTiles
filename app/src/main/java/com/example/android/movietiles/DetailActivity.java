package com.example.android.movietiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movietiles.model.MovieInfo;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    public static class DetailFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            MovieInfo movieInfo = intent.getParcelableExtra("MOVIE_INFO");

            TextView titleTextView = (TextView) rootView.findViewById(R.id.movie_title);
            titleTextView.setText(movieInfo.getTitle());
            TextView ratingTextView = (TextView) rootView.findViewById(R.id.movie_rating);
            ratingTextView.setText(movieInfo.getRating());
            TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.movie_release_date);
            releaseDateTextView.setText(movieInfo.getReleaseDate());
            TextView synopsisTextView = (TextView) rootView.findViewById(R.id.movie_synopsis);
            synopsisTextView.setText(movieInfo.getSynopsis());

            return rootView;
        }
    }
}
