package com.trm.a2023178056;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        editTextName = view.findViewById(R.id.Name);
        editTextEmail = view.findViewById(R.id.EmailAddress);
        editTextPassword = view.findViewById(R.id.Password);
        editTextConfirmPassword = view.findViewById(R.id.ConfirmPassword);
        Button buttonSignup = view.findViewById(R.id.signup);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Get the currently signed-in user
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Store user details in Firestore
                                    storeUserDetails(user, name, email);

                                    // Send a verification email
                                    sendVerificationEmail();

                                    // Additional logic if needed
                                    Toast.makeText(requireContext(), "Sign up successful! Verification email sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Email sent
                                // You can add additional logic here if needed
                            } else {
                                // Failed to send email
                                Toast.makeText(requireContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void storeUserDetails(FirebaseUser user, String name, String email) {
        if (user != null) {
            String userId = user.getUid();

            // Create a new user with name and email
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("name", name);
            userDetails.put("email", email);

            // Add a new document with a generated ID
            db.collection("users").document(userId)
                    .set(userDetails)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // User details stored successfully
                                Toast.makeText(requireContext(), "User details stored successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to store user details
                                Toast.makeText(requireContext(), "Failed to store user details.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
