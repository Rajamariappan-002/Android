package com.trm.a2023178056;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class QuizResult {
    private String name;
    private String email;
    private String correct_answers; // Store correct_answers as a string
    private String time_taken; // Store time_taken as a string
    private String date;
    private String category;

    // Default no-argument constructor required for Firebase deserialization
    public QuizResult() {

    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCorrect_answers() {
        return correct_answers;
    }

    public void setCorrect_answers(String correct_answers) {
        this.correct_answers = correct_answers;
    }

    public String getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(String time_taken) {
        this.time_taken = time_taken;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
