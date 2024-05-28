package com.trm.a2023178056;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = view.findViewById(R.id.EmailAddress);
        editTextPassword = view.findViewById(R.id.Password);
        Button buttonLogin = view.findViewById(R.id.login);
        TextView buttonSignup = view.findViewById(R.id.signup);
        TextView forgotPassword = view.findViewById(R.id.forgot_password); // Make sure the ID matches your TextView

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticatorActivity) requireActivity()).switchToSignup();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() { // Set OnClickListener for Forgot Password
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        return view;
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(requireActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(requireActivity(), MainActivity.class));
                            requireActivity().finish();
                        } else {
                            Toast.makeText(requireActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetPassword() {
        String email = editTextEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireActivity(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "Failed to send reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
