package com.trm.a2023178056;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private List<QuizResult> quizResults;

    public LeaderboardAdapter(List<QuizResult> quizResults) {
        this.quizResults = quizResults;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        QuizResult quizResult = quizResults.get(position);
        holder.textViewName.setText("Name: " + quizResult.getName());
        holder.textViewEmail.setText(quizResult.getEmail());
        holder.textViewCategory.setText(quizResult.getCategory());
        holder.textViewCorrectAnswers.setText("Correct Answers: " + quizResult.getCorrect_answers()+"s");
        holder.textViewTimeTaken.setText("Time Taken: " + quizResult.getTime_taken());
        holder.textViewDate.setText(quizResult.getDate());

        // Set ranking number
        int rank = position + 1;
        holder.textViewRank.setText("#" + rank);
        holder.textViewRank.setVisibility(View.VISIBLE); // Make the ranking number visible
    }

    @Override
    public int getItemCount() {
        return quizResults.size();
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewEmail;
        TextView textViewCategory;
        TextView textViewCorrectAnswers;
        TextView textViewTimeTaken;
        TextView textViewDate;
        TextView textViewRank;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewCorrectAnswers = itemView.findViewById(R.id.textViewCorrectAnswers);
            textViewTimeTaken = itemView.findViewById(R.id.textViewTimeTaken);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewRank = itemView.findViewById(R.id.textViewRank);
        }
    }
}
