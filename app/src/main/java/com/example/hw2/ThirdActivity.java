package com.example.hw2;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class ThirdActivity extends Activity {
    TextView detailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        detailTextView = findViewById(R.id.detailTextView);
        Intent intent = getIntent();
        long contactId = intent.getLongExtra("contactId", -1);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getContact(contactId);
        if (c != null && c.moveToFirst()) {
            int nameIdx = c.getColumnIndex(DBAdapter.KEY_NAME);
            int phoneIdx = c.getColumnIndex(DBAdapter.KEY_PHONE);
            int emailIdx = c.getColumnIndex(DBAdapter.KEY_EMAIL);

            String details = "";
            if (nameIdx != -1) {
                details += "Name: " + c.getString(nameIdx) + "\n";
            }
            if (phoneIdx != -1) {
                details += "Phone: " + c.getString(phoneIdx) + "\n";
            }
            if (emailIdx != -1) {
                details += "Email: " + c.getString(emailIdx);
            }

            detailTextView.setText(details);
        } else {
            detailTextView.setText("Contact not found.");
        }
        if (c != null) {
            c.close();
        }
        db.close();
    }
}