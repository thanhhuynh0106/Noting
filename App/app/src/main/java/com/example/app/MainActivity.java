package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton menuButton;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (navHostFragment != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                NavController navController = navHostFragment.getNavController();

                int destinationId = navController.getCurrentDestination().getId();
                int selectedItemId = item.getItemId();

                int direction = getNavDirection(destinationId, selectedItemId);

                NavOptions navOptions;
                if (direction > 0) {
                    // Slide to left
                    navOptions = new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build();
                } else if (direction < 0) {
                    // Slide to right
                    navOptions = new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_left)
                            .setExitAnim(R.anim.slide_out_right)
                            .setPopEnterAnim(R.anim.slide_in_right)
                            .setPopExitAnim(R.anim.slide_out_left)
                            .build();
                } else {
                    // No direction change, use default animations
                    navOptions = new NavOptions.Builder().build();
                }

                navController.navigate(selectedItemId, null, navOptions);
                return true;
            });
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        menuButton = findViewById(R.id.menu_btn_sidebar);
        menuButton.setOnClickListener(v -> {
            if (drawerLayout != null) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Log.d("MainActivity", "Drawer closed");
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                    Log.d("MainActivity", "Drawer opened");
                }
            }
        });

    }

    private int getNavDirection(int currentId, int targetId) {
        List<Integer> order = Arrays.asList(
                R.id.home_fragment,
                R.id.task_fragment,
                R.id.calendar_fragment,
                R.id.navigation_settings
        );

        int currentIndex = order.indexOf(currentId);
        int targetIndex = order.indexOf(targetId);
        return Integer.compare(targetIndex, currentIndex);  // >0 means right, <0 means left, 0 means no change
    }
}