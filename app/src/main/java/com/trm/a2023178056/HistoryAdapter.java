package com.trm.a2023178056;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<QuizResult> quizResultsList;

    public HistoryAdapter(List<QuizResult> quizResultsList) {
        this.quizResultsList = quizResultsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizResult quizResult = quizResultsList.get(position);
        holder.nameTextView.setText("Name: " + quizResult.getName());
        holder.emailTextView.setText("Email: " + quizResult.getEmail());
        holder.correctAnswersTextView.setText("Correct Answers: " + quizResult.getCorrect_answers());
        holder.timeTakenTextView.setText("Time Taken: " + quizResult.getTime_taken()+"s");
        holder.dateTextView.setText("Date: " + quizResult.getDate());
        holder.categoryTextView.setText("Category: " + quizResult.getCategory());
    }

    @Override
    public int getItemCount() {
        return quizResultsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, correctAnswersTextView, timeTakenTextView, dateTextView, categoryTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            correctAnswersTextView = itemView.findViewById(R.id.correctAnswersTextView);
            timeTakenTextView = itemView.findViewById(R.id.timeTakenTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
        }
    }
}
