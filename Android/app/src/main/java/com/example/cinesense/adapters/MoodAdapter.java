package com.example.cinesense.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinesense.R;

import java.util.List;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {
    
    private List<String> moods;
    private OnMoodClickListener listener;
    
    public interface OnMoodClickListener {
        void onMoodClick(String mood);
    }
    
    public MoodAdapter(List<String> moods, OnMoodClickListener listener) {
        this.moods = moods;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood, parent, false);
        return new MoodViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        String mood = moods.get(position);
        holder.moodText.setText(mood);
        holder.itemView.setOnClickListener(v -> listener.onMoodClick(mood));
    }
    
    @Override
    public int getItemCount() {
        return moods.size();
    }
    
    static class MoodViewHolder extends RecyclerView.ViewHolder {
        TextView moodText;
        
        MoodViewHolder(View itemView) {
            super(itemView);
            moodText = itemView.findViewById(R.id.moodText);
        }
    }
}
