package com.example.hw2;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ListView listViewContacts;
    ArrayList<String> contactsList;
    ArrayAdapter<String> adapter;
    Button addButton;
    DBAdapter db;
    private long userId;  // storing the user ID
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewContacts = findViewById(R.id.listViewContacts);
        addButton = findViewById(R.id.addButton);
        textViewTitle = findViewById(R.id.textViewTitle);
        contactsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        listViewContacts.setAdapter(adapter);
        db = new DBAdapter(this);
        db.open();

        userId = getIntent().getLongExtra("userId", -1);
        loadContacts(userId);
        displayUserTitle(userId);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 1);
            }
        });

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contactDetails = contactsList.get(position);
                long contactId = Long.parseLong(contactDetails.split(",")[0].split(":")[1].trim());
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                intent.putExtra("contactId", contactId);
                startActivity(intent);
            }
        });

        listViewContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String contactDetails = contactsList.get(position);
                long contactId = Long.parseLong(contactDetails.split(",")[0].split(":")[1].trim());
                db.deleteContact(contactId);
                loadContacts(userId);
                return true;
            }
        });
    }

    private void loadContacts(long userId) {
        contactsList.clear();
        Cursor c = db.getAllContacts(userId);
        if (c.moveToFirst()) {
            do {
                String contact = "ID: " + c.getLong(c.getColumnIndex(DBAdapter.KEY_ROWID)) +
                        ", Name: " + c.getString(c.getColumnIndex(DBAdapter.KEY_NAME)) +
                        ", Phone: " + c.getString(c.getColumnIndex(DBAdapter.KEY_PHONE)) +
                        ", Email: " + c.getString(c.getColumnIndex(DBAdapter.KEY_EMAIL));
                contactsList.add(contact);
            } while (c.moveToNext());
        }
        adapter.notifyDataSetChanged();
    }

    private void displayUserTitle(long userId) {
        String username = db.getUsername(userId); // fetch the username from the database
        if (username != null && !username.isEmpty()) {
            textViewTitle.setText(username + "'s Contacts"); // dynamically set the title based on the username
        } else {
            textViewTitle.setText("Contacts");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String contact = data.getStringExtra("contactData");
            String[] parts = contact.split(", ");
            db.insertContact(parts[0], parts[1], parts[2], userId);
            loadContacts(userId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
