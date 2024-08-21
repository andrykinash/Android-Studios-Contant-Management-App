package com.example.hw2;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    EditText usernameEditText, passwordEditText;
    Button loginButton, registerButton;
    CheckBox rememberPasswordCheckBox;
    SharedPreferences sharedPreferences;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        rememberPasswordCheckBox = findViewById(R.id.rememberPasswordCheckBox);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        db = new DBAdapter(this);
        db.open();
        checkSavedPreferences();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (rememberPasswordCheckBox.isChecked()) {
                    saveLoginDetails(username, password);
                } else {
                    clearLoginDetails();
                }
                authenticateUser(username, password);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkSavedPreferences() {
        boolean isRemembered = sharedPreferences.getBoolean("RememberMe", false);
        if (isRemembered) {
            usernameEditText.setText(sharedPreferences.getString("Username", ""));
            passwordEditText.setText(sharedPreferences.getString("Password", ""));
            rememberPasswordCheckBox.setChecked(true);
        }
    }

    private void saveLoginDetails(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("RememberMe", true);
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.apply();
    }

    private void clearLoginDetails() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("RememberMe");
        editor.remove("Username");
        editor.remove("Password");
        editor.apply();
        passwordEditText.setText("");
    }

    private void authenticateUser(String username, String password) {
        int result = db.checkUser(username, password);
        switch (result) { //handling authentication errors
            case 0: // success
                long userId = db.getUserId(username);
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case 1: // username not found
                Toast.makeText(LoginActivity.this, "User not found. Please register.", Toast.LENGTH_SHORT).show();
                break;
            case 2: // incorrect password
                Toast.makeText(LoginActivity.this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
