package com.example.cinesense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button userBtn, moderatorBtn, getStartedBtn;
    private TextView selectedRoleLabel;
    private String selectedRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userBtn = findViewById(R.id.userBtn);
        moderatorBtn = findViewById(R.id.moderatorBtn);
        getStartedBtn = findViewById(R.id.getStartedBtn);
        selectedRoleLabel = findViewById(R.id.selectedRoleLabel);

        userBtn.setOnClickListener(v -> onSelectUser());
        moderatorBtn.setOnClickListener(v -> onSelectModerator());
        getStartedBtn.setOnClickListener(v -> onGetStarted());
    }

    private void onSelectUser() {
        selectedRole = "user";
        selectedRoleLabel.setText("✓ User role selected");
        selectedRoleLabel.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        updateButtonStyles();
    }

    private void onSelectModerator() {
        selectedRole = "moderator";
        selectedRoleLabel.setText("✓ Moderator role selected");
        selectedRoleLabel.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        updateButtonStyles();
    }

    private void onGetStarted() {
        if (selectedRole == null) {
            selectedRoleLabel.setText("Please select a role first!");
            selectedRoleLabel.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            return;
        }

        Intent intent;
        if (selectedRole.equals("moderator")) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, UserActivity.class);
        }
        startActivity(intent);
    }

    private void updateButtonStyles() {
        if ("user".equals(selectedRole)) {
            userBtn.setSelected(true);
            moderatorBtn.setSelected(false);
        } else if ("moderator".equals(selectedRole)) {
            moderatorBtn.setSelected(true);
            userBtn.setSelected(false);
        }
    }
}
