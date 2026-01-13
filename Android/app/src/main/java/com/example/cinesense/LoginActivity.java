package com.example.cinesense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private TextView errorLabel;
    private Button loginBtn, backBtn;

    private static final String VALID_EMAIL = "sanzidajerin28@gmail.com";
    private static final String VALID_PASSWORD = "1122";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        errorLabel = findViewById(R.id.errorLabel);
        loginBtn = findViewById(R.id.loginBtn);
        backBtn = findViewById(R.id.backBtn);

        loginBtn.setOnClickListener(v -> onLogin());
        backBtn.setOnClickListener(v -> finish());
    }

    private void onLogin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("❌ Please fill in all fields");
            errorLabel.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            errorLabel.setVisibility(android.view.View.VISIBLE);
            return;
        }

        if (email.equals(VALID_EMAIL) && password.equals(VALID_PASSWORD)) {
            Intent intent = new Intent(this, ModeratorActivity.class);
            startActivity(intent);
            finish();
        } else {
            errorLabel.setText("❌ Invalid email or password");
            errorLabel.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            errorLabel.setVisibility(android.view.View.VISIBLE);
        }
    }
}
