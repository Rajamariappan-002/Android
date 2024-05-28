package com.trm.a2023178056;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AuthenticatorActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            // Load the LoginFragment initially
            loadFragment(new LoginFragment(), false);
        }
    }

    public void switchToSignup() {
        loadFragment(new SignupFragment(), true);
    }

    public void switchToLogin() {
        loadFragment(new LoginFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Load animations
        Animation enterAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation exitAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        // Set custom animations
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
