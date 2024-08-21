package com.example.hw2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.security.MessageDigest; //password hashing
import java.security.NoSuchAlgorithmException;

public class DBAdapter {
    private static final String DATABASE_NAME = "ContactsDB";
    private static final int DATABASE_VERSION = 13;  // increment to clear database ****
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_USERS = "users";

    // public constants for column names
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + TABLE_CONTACTS + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT NOT NULL, " +
                    KEY_PHONE + " TEXT NOT NULL, " +
                    KEY_EMAIL + " TEXT NOT NULL, " +
                    "user_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(" + KEY_ROWID + "));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    // user methods second -------------------------------------------------------------------------
    public long insertUser(String username, String password) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("username", username);
        initialValues.put("password", hashPassword(password));
        try {
            long result = db.insert(TABLE_USERS, null, initialValues);
            if (result == -1) {
                Log.e("DBError", "Failed to insert user with username: " + username);
            }
            return result;
        } catch (Exception e) {
            Log.e("DBError", "Exception while inserting user: " + username, e);
            return -1;
        }
    }

        public int checkUser(String username, String password) {
        Cursor cursor = db.query(TABLE_USERS, new String[]{"password"}, "username=?",
                new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
            cursor.close();
            if (storedPassword.equals(hashPassword(password))) {
                return 0; // success
            } else {
                return 2; // incorrect password
            }
        } else {
            return 1; // username not found
        }
    }

    public boolean isUsernameExists(String username) {
        Cursor cursor = db.query(TABLE_USERS, new String[]{"username"}, "username=?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Contact methods
    public long insertContact(String name, String phone, String email, long userId) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put("user_id", userId);
        return db.insert(TABLE_CONTACTS, null, initialValues);
    }

    public Cursor getAllContacts(long userId) {
        return db.query(TABLE_CONTACTS, new String[] {KEY_ROWID, KEY_NAME, KEY_PHONE, KEY_EMAIL},
                "user_id = ?", new String[] {String.valueOf(userId)}, null, null, null);
    }
    public long getUserId(String username) {
        long userId = -1;
        Cursor cursor = db.query(TABLE_USERS, new String[] {KEY_ROWID}, "username=?", new String[] {username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
            cursor.close();
        }
        return userId;
    }
    public String getUsername(long userId) {
        String username = null;
        Cursor cursor = db.query(TABLE_USERS, new String[]{"username"}, KEY_ROWID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex("username"));
            cursor.close();
        }
        return username;
    }

    public boolean deleteContact(long rowId) {
        return db.delete(TABLE_CONTACTS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getContact(long rowId) {
        Cursor mCursor = db.query(true, TABLE_CONTACTS, new String[] {KEY_ROWID, KEY_NAME, KEY_PHONE, KEY_EMAIL},
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private String hashPassword(String password) { //encryption of the password
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
}
