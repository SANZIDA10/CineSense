package com.example.cinesense.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinesense.R;
import com.example.cinesense.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class ModeratorMovieAdapter extends RecyclerView.Adapter<ModeratorMovieAdapter.MovieViewHolder> {
    
    private List<Movie> movies = new ArrayList<>();
    private OnMovieDeleteListener listener;
    private Context context;
    
    public interface OnMovieDeleteListener {
        void onMovieDelete(Movie movie);
    }
    
    public ModeratorMovieAdapter(Context context, OnMovieDeleteListener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_moderator_movie, parent, false);
        return new MovieViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleText.setText(movie.getTitle());
        holder.genreText.setText(movie.getGenre());
        holder.moodText.setText(movie.getMood());
        holder.descriptionText.setText(movie.getDescription() != null && !movie.getDescription().isEmpty() 
            ? movie.getDescription() : "No description");
        
        holder.deleteBtn.setOnClickListener(v -> listener.onMovieDelete(movie));
    }
    
    @Override
    public int getItemCount() {
        return movies.size();
    }
    
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, genreText, moodText, descriptionText;
        Button deleteBtn;
        
        MovieViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            genreText = itemView.findViewById(R.id.genreText);
            moodText = itemView.findViewById(R.id.moodText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
