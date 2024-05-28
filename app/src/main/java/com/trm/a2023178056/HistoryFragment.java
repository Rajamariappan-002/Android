package com.trm.a2023178056;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<QuizResult> quizResults;

    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        quizResults = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        leaderboardAdapter = new LeaderboardAdapter(quizResults);
        recyclerView.setAdapter(leaderboardAdapter);

        loadLeaderboardData();

        return view;
    }

    private void loadLeaderboardData() {
        CollectionReference leaderboardRef = db.collection("quiz_results");
        leaderboardRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, QuizResult> topPerformances = new HashMap<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    QuizResult quizResult = document.toObject(QuizResult.class);
                    String email = quizResult.getEmail();
                    if (!topPerformances.containsKey(email) ||
                            isBetterPerformance(quizResult, topPerformances.get(email))) {
                        topPerformances.put(email, quizResult);
                    }
                }
                quizResults.clear();
                quizResults.addAll(topPerformances.values());
                sortLeaderboard();
                leaderboardAdapter.notifyDataSetChanged();
            } else {
                // Handle the error
            }
        });
    }

    private boolean isBetterPerformance(QuizResult newResult, QuizResult currentBest) {
        int newCorrectAnswers = Integer.parseInt(newResult.getCorrect_answers());
        int currentCorrectAnswers = Integer.parseInt(currentBest.getCorrect_answers());
        int newTimeTaken = Integer.parseInt(newResult.getTime_taken());
        int currentTimeTaken = Integer.parseInt(currentBest.getTime_taken());

        if (newCorrectAnswers > currentCorrectAnswers) {
            return true;
        } else if (newCorrectAnswers == currentCorrectAnswers) {
            return newTimeTaken < currentTimeTaken;
        } else {
            return false;
        }
    }

    private void sortLeaderboard() {
        Collections.sort(quizResults, new Comparator<QuizResult>() {
            @Override
            public int compare(QuizResult o1, QuizResult o2) {
                int correctAnswers1 = Integer.parseInt(o1.getCorrect_answers());
                int correctAnswers2 = Integer.parseInt(o2.getCorrect_answers());
                int timeTaken1 = Integer.parseInt(o1.getTime_taken());
                int timeTaken2 = Integer.parseInt(o2.getTime_taken());

                if (correctAnswers1 != correctAnswers2) {
                    return correctAnswers2 - correctAnswers1; // Descending order
                } else {
                    return timeTaken1 - timeTaken2; // Ascending order
                }
            }
        });
    }
}
