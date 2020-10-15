package com.example.appmovies;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmovies.adapter.MyAdapter;
import com.example.appmovies.model.Movie;
import com.example.appmovies.model.ResultMovieData;
import com.example.appmovies.network.GetDataService;
import com.example.appmovies.network.RetrofitClientInstance;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.appmovies.util.StringHandler.formatDate;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextInputEditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.search_field);

        // Set loading message
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");

        // Get default data and generate list
        getPopularMovies();
    }

    // default data
    private void getPopularMovies() {

        // Show loading message
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResultMovieData> call = service.getPopularMovies(RetrofitClientInstance.getApiKey(), "pt-BR", 1);
        call.enqueue(new Callback<ResultMovieData>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResultMovieData> call, Response<ResultMovieData> response) {

                progressDialog.dismiss();

                // Generate movie list with API data
                generateMovieList(response
                        .body()
                        .getResults()
                        .stream()
                        .peek(movie -> movie.setReleaseDate(formatDate(movie.getReleaseDate())))
                        .collect(Collectors.toList()));
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResultMovieData> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(
                        MainActivity.this,
                        "Algo deu errado... Tente novamente mais tarde!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // Personalized data based on search field
    public void searchMovies(View view) {

        // Show loading message
        progressDialog.show();

        String query = searchField.getText().toString();

        if (query == null || query.isEmpty()) {
            getPopularMovies();
            return;
        }

        GetDataService service = RetrofitClientInstance
                .getRetrofitInstance()
                .create(GetDataService.class);

        Call<ResultMovieData> call = service.searchMovies(
                RetrofitClientInstance.getApiKey(),
                "pt-BR",
                query,
                1,
                false
        );
        call.enqueue(new Callback<ResultMovieData>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResultMovieData> call, Response<ResultMovieData> response) {

                progressDialog.dismiss();

                // Generate movie list with API data
                generateMovieList(response
                        .body()
                        .getResults()
                        .stream()
                        .peek(movie -> movie.setReleaseDate(formatDate(movie.getReleaseDate())))
                        .collect(Collectors.toList()));
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResultMovieData> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(
                        MainActivity.this,
                        "Algo deu errado... Tente novamente mais tarde!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // Set movie list
    private void generateMovieList(List<Movie> movies) {

        // Get reference to recycler view
        RecyclerView recyclerView = findViewById(R.id.customRecyclerView);

        // Create instance of custom adapter
        MyAdapter adapter = new MyAdapter(this, movies);

        // Define a layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        // Set recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}