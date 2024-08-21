package com.example.hw2;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecondActivity extends Activity {
    EditText nameEditText, phoneEditText, emailEditText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate inputs
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SecondActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                } else {
                    Intent returnIntent = new Intent();
                    String contactDetails = name + ", " + phone + ", " + email;
                    returnIntent.putExtra("contactData", contactDetails);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}