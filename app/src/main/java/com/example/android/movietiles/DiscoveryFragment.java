package com.example.android.movietiles;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.movietiles.model.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryFragment extends Fragment {

    private ImageAdapter imageAdapter;

    public DiscoveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);

        imageAdapter = new ImageAdapter(rootView.getContext());

        // Create the GridView and retrieve the movies info
        GridView gridview = (GridView) rootView.findViewById(R.id.poster_grid);
        gridview.setAdapter(imageAdapter);
        updateMovieInfo();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(rootView.getContext(), DetailActivity.class);
                intent.putExtra("MOVIE_INFO", (MovieInfo) imageAdapter.getItem(position));
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void updateMovieInfo() {
        FetchMovieTask task = new FetchMovieTask();
        task.execute();
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, List<MovieInfo>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected List<MovieInfo> doInBackground(Void... params) {
            List<MovieInfo> movieInfoList = new ArrayList<>();

            String jsonResponse = sendGetMoviesRequest();
            if (jsonResponse == null) {
                Log.i(LOG_TAG, "No JSON response received from the API call");
                return null;
            }

            try {
                movieInfoList = getMoviesInfoFromJson(jsonResponse);
            } catch (JSONException jsonE) {
                Log.e(LOG_TAG, jsonE.getMessage(), jsonE);
            }
            return movieInfoList;
        }

        @Override
        protected void onPostExecute(List<MovieInfo> result) {
            if (result != null) {
                // Create an array of the movie image URLs
                List<String> movieImageURLList = new ArrayList<>();
                for (MovieInfo movieInfo : result) {
                    movieImageURLList.add(movieInfo.getImageLink());
                }
                String[] movieURLs = movieImageURLList.toArray(new String[movieImageURLList.size()]);
                MovieInfo[] movieInfos = result.toArray(new MovieInfo[result.size()]);

                // Set the new adapter data
                imageAdapter.setMoviesInfo(movieInfos);
            }
        }

        private String sendGetMoviesRequest() {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr = null;

            // URL parameters
            String API_KEY = getString(R.string.api_key);
            String SORT_BY = "popularity.desc";

            try {
                // Build the URL
                Uri uri = Uri.parse("http://api.themoviedb.org/3/discover/movie")
                        .buildUpon()
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter("sort_by", SORT_BY)
                        .build();
                URL url = new URL(uri.toString());

                // Create the request, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("DiscoveryFragment", "Error closing stream", e);
                    }
                }
            }
            return jsonStr;
        }

        public List<MovieInfo> getMoviesInfoFromJson(String jsonString) throws JSONException {
            List<MovieInfo> movieInfoList = new ArrayList<>();

            // Get the list of movies from the JSON string
            JSONArray moviesInfoJSON = new JSONObject(jsonString).getJSONArray("results");
            for (int i = 0; i < moviesInfoJSON.length(); i++) {
                JSONObject movieInfoJSON = moviesInfoJSON.getJSONObject(i);

                // Build a MovieInfo object for each movie info
                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setTitle(movieInfoJSON.getString("original_title"));
                movieInfo.setImageLink(movieInfoJSON.getString("poster_path"));
                movieInfo.setSynopsis(movieInfoJSON.getString("overview"));
                movieInfo.setRating(movieInfoJSON.getString("vote_average"));
                movieInfo.setReleaseDate(movieInfoJSON.getString("release_date"));
                movieInfoList.add(movieInfo);
            }
            return movieInfoList;
        }
    }
}
