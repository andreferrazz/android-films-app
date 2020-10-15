package com.example.appmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmovies.MovieDetailsActivity;
import com.example.appmovies.R;
import com.example.appmovies.model.Movie;
import com.example.appmovies.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {

    private final List<Movie> movies;
    private final Context context;

    public MyAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        private final TextView title;
        private final TextView date;
        private final ImageView image;
        private final CardView card;

        CustomViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            image = view.findViewById(R.id.image);
            card = view.findViewById(R.id.card);
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_tile, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.title.setText(movies.get(position).getTitle());
        holder.date.setText(movies.get(position).getReleaseDate());

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(RetrofitClientInstance.getImgUrl() + movies.get(position).getPosterPath())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.image);

        holder.card.setOnClickListener(v -> {
            System.out.println(movies.get(position).getId());

            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("movieId", movies.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}
