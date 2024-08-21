package com.example.hw2;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
    EditText usernameEditText, passwordEditText;
    Button registerButton;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        db = new DBAdapter(this);
        db.open();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                //some more handling stuff
                if(username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (db.isUsernameExists(username)) {
                    Toast.makeText(RegistrationActivity.this, "Username already taken. Please try another.", Toast.LENGTH_LONG).show();
                } else {
                    long userId = db.insertUser(username, password);
                    if(userId == -1) {
                        Toast.makeText(RegistrationActivity.this, "Registration failed due to an unexpected error.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
