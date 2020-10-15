package com.example.appmovies;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appmovies.model.Movie;
import com.example.appmovies.network.GetDataService;
import com.example.appmovies.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.appmovies.util.StringHandler.formatDate;


public class MovieDetailsActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ImageView imageView;
    private TextView title;
    private TextView originalTitle;
    private TextView date;
    private TextView overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get reference to UI elements
        imageView = findViewById(R.id.image);
        title = findViewById(R.id.title);
        originalTitle = findViewById(R.id.original_title);
        date = findViewById(R.id.date);
        overview = findViewById(R.id.overview);

        // Set loading message
        progressDialog = new ProgressDialog(MovieDetailsActivity.this);
        progressDialog.setMessage("Loading....");

        // Get movie and set UI with result data
        getMovie(getIntent().getIntExtra("movieId", 0));
    }

    private void getMovie(int movieId) {
        // Show loading message
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Movie> call = service.getMovieById(movieId, RetrofitClientInstance.getApiKey(), "pt-BR");
        call.enqueue(new Callback<Movie>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                progressDialog.dismiss();

                attachDataToView(response.body());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Movie> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(
                        MovieDetailsActivity.this,
                        "Algo deu errado... Tente novamente mais tarde!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void attachDataToView(Movie movie) {

        // Get poster image and set image view
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        builder.build().load(RetrofitClientInstance.getImgUrl() + movie.getPosterPath())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(imageView);

        // Text views
        title.setText(movie.getTitle());
        originalTitle.setText("Título original: " + movie.getOriginalTitle());
        date.setText("Data de lançamento: " + formatDate(movie.getReleaseDate()));
        overview.setText(movie.getOverview());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            overview.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}