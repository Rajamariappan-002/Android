package com.trm.a2023178056;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class CategoryFragment extends Fragment {

    private TextView userTextView;
    private FirebaseFirestore firestore;
    private CardView mathCardView;
    private CardView gkCardView;
    private CardView polityCardView;
    private CardView geographyCardView;
    private CardView englishCardView;
    private float dX, dY; // Variables to store initial touch position

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userTextView = view.findViewById(R.id.user);
        mathCardView = view.findViewById(R.id.math_category);
        gkCardView = view.findViewById(R.id.gk_category);
        polityCardView = view.findViewById(R.id.polity_category);
        geographyCardView = view.findViewById(R.id.geography_category);
        englishCardView = view.findViewById(R.id.english_category);

        firestore = FirebaseFirestore.getInstance();
        displayUserInfo();

        mathCardView.setOnClickListener(v -> navigateToMathFragment());
        gkCardView.setOnClickListener(v -> navigateToGKFragment());
        polityCardView.setOnClickListener(v -> navigateToPolityFragment());
        geographyCardView.setOnClickListener(v -> navigateToGeographyFragment());
        englishCardView.setOnClickListener(v -> navigateToEnglishFragment());
    }

    private void displayUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            firestore.collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String username = document.getString("name");
                                if (username != null && !username.isEmpty()) {
                                    userTextView.setText(username);
                                } else {
                                    String email = currentUser.getEmail();
                                    if (email != null && !email.isEmpty()) {
                                        userTextView.setText(email);
                                    } else {
                                        userTextView.setText("Logged in");
                                    }
                                }
                            } else {
                                userTextView.setText("Not_logged in");
                            }
                        } else {
                            userTextView.setText("Not logged in");
                        }
                    });
        } else {
            userTextView.setText("Not logged in");
        }
    }

    private void navigateToMathFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new MathematicsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToGKFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new GeneralKnowledgeFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToPolityFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new PolityFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToGeographyFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new GeographyFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToEnglishFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new EnglishFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setTouchListener(CardView cardView) {
        cardView.setOnTouchListener((view, event) -> {
            final int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    return true;
                default:
                    return false;
            }
        });
    }
}
