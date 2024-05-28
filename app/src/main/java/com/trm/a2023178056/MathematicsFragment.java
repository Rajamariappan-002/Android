package com.trm.a2023178056;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MathematicsFragment extends Fragment {

    private TextView questionTextView;
    private Button optionAButton, optionBButton, optionCButton, optionDButton;
    private TextView resultTextView, timerTextView;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private List<String> questionIds;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;
    private boolean[] answeredQuestions;
    private long startTimeInMillis = 0;
    private CountDownTimer timer;
    private String correctAnswer; // To store the correct answer of the current question
    private Button okButton;
    private boolean quizCompleted = false; // Flag to track if quiz is completed

    public MathematicsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mathematics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        questionTextView = view.findViewById(R.id.questionTextView);
        optionAButton = view.findViewById(R.id.optionAButton);
        optionBButton = view.findViewById(R.id.optionBButton);
        optionCButton = view.findViewById(R.id.optionCButton);
        optionDButton = view.findViewById(R.id.optionDButton);
        resultTextView = view.findViewById(R.id.resultTextView);
        timerTextView = view.findViewById(R.id.timerTextView);
        okButton = view.findViewById(R.id.okButton);

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Load random set of 10 questions from Firestore
        loadRandomQuestionsFromFirestore();

        // Set click listeners for option buttons
        optionAButton.setOnClickListener(v -> checkAnswer(optionAButton));
        optionBButton.setOnClickListener(v -> checkAnswer(optionBButton));
        optionCButton.setOnClickListener(v -> checkAnswer(optionCButton));
        optionDButton.setOnClickListener(v -> checkAnswer(optionDButton));

        // Initialize navigation buttons
        view.findViewById(R.id.nextButton).setOnClickListener(v -> moveToNextQuestion());
        view.findViewById(R.id.previousButton).setOnClickListener(v -> moveToPreviousQuestion());

        // Set click listener for OK button
        okButton.setOnClickListener(v -> navigateToCategoryFragment());

        // Start the timer
        startTimeInMillis = System.currentTimeMillis();
        startTimer();
    }

    private void loadRandomQuestionsFromFirestore() {
        questionIds = new ArrayList<>();
        firestore.collection("maths_questions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            questionIds.add(document.getId());
                        }
                        // Shuffle the list of question IDs
                        Collections.shuffle(questionIds);
                        // Initialize the answered questions array
                        answeredQuestions = new boolean[questionIds.size()];
                        // Load the first question
                        loadQuestion();
                    } else {
                        Toast.makeText(getContext(), "Failed to load questions.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questionIds.size()) {
            String questionId = questionIds.get(currentQuestionIndex);
            firestore.collection("maths_questions").document(questionId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String question = document.getString("question");
                                String optionA = document.getString("option_a");
                                String optionB = document.getString("option_b");
                                String optionC = document.getString("option_c");
                                String optionD = document.getString("option_d");
                                correctAnswer = document.getString("correct_answer"); // Get the correct answer

                                // Update UI with the question and options
                                questionTextView.setText(question);
                                optionAButton.setText(optionA);
                                optionBButton.setText(optionB);
                                optionCButton.setText(optionC);
                                optionDButton.setText(optionD);

                                // Clear the previous result
                                resultTextView.setText("");

                                // Enable buttons if not already answered
                                enableOptionButtons(!answeredQuestions[currentQuestionIndex]);

                                // Animate options appearance
                                animateOptions();
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to load question.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // All questions are answered
            finishQuiz();
        }
    }

    private void checkAnswer(Button selectedOptionButton) {
        if (!answeredQuestions[currentQuestionIndex]) {
            String selectedAnswer = selectedOptionButton.getText().toString();

            // Compare the selected answer with the correct answer and update UI accordingly
            if (selectedAnswer.equals(correctAnswer)) {
                resultTextView.setText("Correct!");
                correctAnswersCount++;
            } else {
                resultTextView.setText("Incorrect. Correct answer is: " + correctAnswer);
            }

            // Mark the question as answered

            answeredQuestions[currentQuestionIndex] = true;
            // Disable buttons to prevent re-answering
            enableOptionButtons(false);

            // Automatically navigate to the next question after 2 seconds
            new Handler().postDelayed(this::moveToNextQuestion, 1000);
        }
    }

    private void moveToNextQuestion() {
        if (!quizCompleted && currentQuestionIndex < questionIds.size() - 1) {
            currentQuestionIndex++;
            loadQuestion();
        } else {
            // Last question reached, finish quiz
            finishQuiz();
        }
    }

    private void moveToPreviousQuestion() {
        if (!quizCompleted && currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion();
        }
    }

    private void enableOptionButtons(boolean enable) {
        optionAButton.setEnabled(enable);
        optionBButton.setEnabled(enable);
        optionCButton.setEnabled(enable);
        optionDButton.setEnabled(enable);
    }

    private void startTimer() {
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsedTime = System.currentTimeMillis() - startTimeInMillis;
                int seconds = (int) (elapsedTime / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                // This should never be called since we're counting up
            }
        }.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void finishQuiz() {
        if (!quizCompleted) {
            quizCompleted = true;
            stopTimer();
            // Show the result
            resultTextView.setText("Quiz finished. Correct answers: " + correctAnswersCount);
            // Show the OK button
            okButton.setVisibility(View.VISIBLE);
            // Enable "Go to Home Page" button
            Button goToHomePageButton = getView().findViewById(R.id.okButton);
            goToHomePageButton.setEnabled(true);
            // Save the result to Firestore
            saveQuizResultToFirestore();
        }
    }

    private void saveQuizResultToFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String name = document.getString("name");
                                String email = currentUser.getEmail();
                                long elapsedTime = (System.currentTimeMillis() - startTimeInMillis) / 1000; // Convert milliseconds to seconds
                                String currentDateAndTime = getCurrentDateTime();

                                Map<String, Object> quizResult = new HashMap<>();
                                quizResult.put("name", name != null ? name : "");
                                quizResult.put("email", email != null ? email : "");
                                quizResult.put("correct_answers", String.valueOf(correctAnswersCount)); // Store correct_answers as string
                                quizResult.put("time_taken", String.valueOf(elapsedTime)); // Store time_taken as string
                                quizResult.put("date", currentDateAndTime);
                                quizResult.put("category", "Mathematics");

                                firestore.collection("quiz_results")
                                        .add(quizResult)
                                        .addOnSuccessListener(documentReference -> {
                                            // Successfully saved
                                            Toast.makeText(getContext(), "Quiz result saved successfully.", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Failed to save
                                            Toast.makeText(getContext(), "Failed to save quiz result.", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private String getCurrentDateTime() {
        // Get the current date and time in a suitable format (e.g., "yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void navigateToCategoryFragment() {
        Fragment categoryFragment = new CategoryFragment(); // Assuming you have a CategoryFragment class
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, categoryFragment); // Replace 'fragment_container' with the ID of your container
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void animateOptions() {
        Button[] buttons = {optionAButton, optionBButton, optionCButton, optionDButton};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.INVISIBLE);
        }
        Handler handler = new Handler();
        for (int i = 0; i < buttons.length; i++) {
            final Button button = buttons[i];
            handler.postDelayed(() -> {
                button.setVisibility(View.VISIBLE);
                ObjectAnimator animator = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(100);
                animator.start();
            }, i * 100);
        }
    }
}
