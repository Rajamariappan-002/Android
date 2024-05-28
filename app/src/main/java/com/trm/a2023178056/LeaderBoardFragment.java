package com.trm.a2023178056;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardFragment extends Fragment {

    private static final String TAG = "LeaderBoardFragment";

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private List<QuizResult> quizResultsList;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private Button sendEmailButton;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leader_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Initialize list and adapter
        quizResultsList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(quizResultsList);
        historyRecyclerView.setAdapter(historyAdapter);

        // Load quiz results from Firestore
        loadQuizResultsFromFirestore();
    }

    private void loadQuizResultsFromFirestore() {
        if (currentUser != null) {
            firestore.collection("quiz_results")
                    .whereEqualTo("email", currentUser.getEmail())
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            Log.e(TAG, "Error fetching quiz results: " + e.getMessage());
                            return;
                        }

                        // Clear previous quiz results
                        quizResultsList.clear();

                        if (queryDocumentSnapshots != null) {
                            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    QuizResult quizResult = documentChange.getDocument().toObject(QuizResult.class);
                                    quizResultsList.add(quizResult);
                                }
                            }
                        }

                        // Notify adapter of data changes
                        historyAdapter.notifyDataSetChanged();
                    });
        }
    }
}
