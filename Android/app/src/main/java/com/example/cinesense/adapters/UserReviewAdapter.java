package com.example.cinesense.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinesense.R;
import com.example.cinesense.models.UserReview;

import java.util.List;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ReviewViewHolder> {
    
    private List<UserReview> reviews;
    
    public UserReviewAdapter(List<UserReview> reviews) {
        this.reviews = reviews;
    }
    
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_review, parent, false);
        return new ReviewViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        UserReview review = reviews.get(position);
        holder.reviewerNameText.setText(review.getReviewerName());
        holder.reviewDateText.setText(review.getReviewDate());
        holder.reviewTextView.setText(review.getReviewText());
    }
    
    @Override
    public int getItemCount() {
        return reviews.size();
    }
    
    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerNameText, reviewDateText, reviewTextView;
        
        ReviewViewHolder(View itemView) {
            super(itemView);
            reviewerNameText = itemView.findViewById(R.id.reviewerNameText);
            reviewDateText = itemView.findViewById(R.id.reviewDateText);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
        }
    }
}
