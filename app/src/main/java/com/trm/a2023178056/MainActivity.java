package com.trm.a2023178056;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new CategoryFragment();
            } else if (itemId == R.id.nav_leaderboard) {
                selectedFragment = new LeaderBoardFragment();
            } else if (itemId == R.id.nav_account) {
                selectedFragment = new AccountFragment();
            } else if (itemId == R.id.nav_calculator) {
                selectedFragment = new CalculatorFragment();
            } else if (itemId == R.id.nav_account){
                selectedFragment = new AccountFragment();
            }else if(itemId == R.id.nav_history){
                selectedFragment = new HistoryFragment();
            }

            if (selectedFragment != null) {
                // Replace fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Displaying the default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).commit();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Log a test event
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Test Event");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
