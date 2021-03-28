package com.example.testtaskone.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.testtaskone.entities.User;
import com.example.testtaskone.helpers.SavedUsersDatabaseHelper;
import com.example.testtaskone.utils.DbBitmapUtility;
import com.example.testtaskone.validators.StringValidator;

import java.util.ArrayList;
import java.util.List;

public class SavedUsersDatabaseAdapter {
    private SavedUsersDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public SavedUsersDatabaseAdapter(Context context){
        dbHelper = new SavedUsersDatabaseHelper(context.getApplicationContext());
    }

    public SavedUsersDatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {
                SavedUsersDatabaseHelper.COLUMN_ID,
                SavedUsersDatabaseHelper.COLUMN_FIRST_NAME,
                SavedUsersDatabaseHelper.COLUMN_LAST_NAME,
                SavedUsersDatabaseHelper.COLUMN_EMAIL,
                SavedUsersDatabaseHelper.COLUMN_PHONE,
                SavedUsersDatabaseHelper.COLUMN_PHOTO
        };
        return database.query(SavedUsersDatabaseHelper.TABLE, columns, null, null, null, null, null);
    }

    public List<User> getUsers(){
        ArrayList<User> savedUsers = new ArrayList<>();
        Cursor cursor = getAllEntries();
        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_ID));
            String firstName = cursor.getString(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_LAST_NAME));
            String email = cursor.getString(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_PHONE));
            Bitmap photo = DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_PHOTO)));
            savedUsers.add(new User(id, photo, firstName, lastName, email, phone));
        }
        cursor.close();
        return savedUsers;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, SavedUsersDatabaseHelper.TABLE);
    }

    public User getUser(long id){
        User savedUser = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", SavedUsersDatabaseHelper.TABLE, SavedUsersDatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String firstName = cursor.getString(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_LAST_NAME));
            String email = cursor.getString(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_PHONE));
            Bitmap photo = DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(SavedUsersDatabaseHelper.COLUMN_PHOTO)));
            savedUser = new User(id, photo, firstName, lastName, email, phone);
        }
        cursor.close();
        return savedUser;
    }

    public long insert(User user){

        ContentValues cv = new ContentValues();
        cv.put(SavedUsersDatabaseHelper.COLUMN_FIRST_NAME, user.getFirstName());
        cv.put(SavedUsersDatabaseHelper.COLUMN_LAST_NAME, user.getLastName());
        cv.put(SavedUsersDatabaseHelper.COLUMN_EMAIL, user.getEmail());
        cv.put(SavedUsersDatabaseHelper.COLUMN_PHONE, user.getPhone());
        cv.put(SavedUsersDatabaseHelper.COLUMN_PHOTO, DbBitmapUtility.getBytes(user.getPhoto()));

        return database.insert(SavedUsersDatabaseHelper.TABLE, null, cv);
    }

    public long delete(long userId){
        String whereClause = SavedUsersDatabaseHelper.COLUMN_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        return database.delete(SavedUsersDatabaseHelper.TABLE, whereClause, whereArgs);
    }

    public long update(User user){
        String whereClause = SavedUsersDatabaseHelper.COLUMN_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(user.getId())};
        ContentValues cv = new ContentValues();
        cv.put(SavedUsersDatabaseHelper.COLUMN_FIRST_NAME, user.getFirstName());
        cv.put(SavedUsersDatabaseHelper.COLUMN_LAST_NAME, user.getLastName());
        cv.put(SavedUsersDatabaseHelper.COLUMN_EMAIL, user.getEmail());
        cv.put(SavedUsersDatabaseHelper.COLUMN_PHONE, user.getPhone());
        cv.put(SavedUsersDatabaseHelper.COLUMN_PHOTO, DbBitmapUtility.getBytes(user.getPhoto()));
        return database.update(SavedUsersDatabaseHelper.TABLE, cv, whereClause, whereArgs);
    }
}
