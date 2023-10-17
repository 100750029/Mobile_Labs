package com.example.mobile_labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLDatabase extends SQLiteOpenHelper {

    private static SQLDatabase sqlDatabase;

    private static final String DATABASE_NAME = "NotesDatabases"; // Database name
    private static final int DATABASE_VERSION = 1; // Database version
    private static final String TABLE_NAME = "NotesTable"; // Table name
    private static final String Counter = "Counter"; // Counter
    private static final String Unique_ID = "id"; // Note ID
    private static final String Title = "title"; // Note title
    private static final String SubTitle = "subtitle"; // Note subtitle
    private static final String Description = "description"; // Note description
    private static final String Note_Background = "noteBackground"; // Note colour
    private static final String Deleted_Note = "deleted"; // Deleted note

    // Converting date format
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    // Creating Database and setting version
    public SQLDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Most updated version of database
    public static SQLDatabase instanceofDatabase(Context context){
        if(sqlDatabase == null){
            sqlDatabase = new SQLDatabase(context);
        }
        return sqlDatabase;
    }

    // Function for creating table and column in tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(Counter)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(Unique_ID)
                .append(" INT, ")
                .append(Title)
                .append(" TEXT, ")
                .append(SubTitle)
                .append(" TEXT, ")
                .append(Description)
                .append(" TEXT, ")
                .append(Note_Background)
                .append(" TEXT, ")
                .append(Deleted_Note)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());

    }

    // Function for upgrading database version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

//        if(oldVersion < 2){
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + Note_Background + " TEXT ");
//        }

    }

    // Function for adding note
    public void NoteAddition(Notes notes){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase(); // Writable to database

        // adding notes to database
        ContentValues cv = new ContentValues();
        cv.put(Unique_ID,notes.getId());
        cv.put(Title,notes.getTitle());
        cv.put(SubTitle,notes.getSubTitle());
        cv.put(Description,notes.getDescription());
        cv.put(Note_Background,notes.getChangeBackground());
        cv.put(Deleted_Note, getStringFromDate(notes.getDeleted()));

        // SQl statement for adding notes
        sqLiteDatabase.insert(TABLE_NAME, null, cv);

        sqLiteDatabase.close();
    }

    // Adding notes from database to array
    public void keepNotesArray(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try(Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)){

            if(result.getCount()!=0){

                // Assigning column values
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String subtitle = result.getString(3);
                    String description = result.getString(4);
                    String changebackground = result.getString(5);
                    String stringDelete = result.getString(6);
                    Date delete = getDateFromString(stringDelete);
                    Notes notes = new Notes(id,title,subtitle,description, changebackground, delete);
                    Notes.notesArrayList.add(notes);
                }

            }

        }

        sqLiteDatabase.close();
    }

    // Function for updating notes * currently not active in app
    public void NoteUpdate(Notes notes){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Unique_ID,notes.getId());
        cv.put(Title,notes.getTitle());
        cv.put(SubTitle,notes.getSubTitle());
        cv.put(Description,notes.getDescription());
        cv.put(Note_Background,notes.getChangeBackground());
        cv.put(Deleted_Note, getStringFromDate(notes.getDeleted()));

        // SQl statement for updating notes
        sqLiteDatabase.update(TABLE_NAME, cv, Unique_ID + " =? ", new String[]{String.valueOf(notes.getId())});
    }

    // Get string from date
    private String getStringFromDate(Date date){
        if(date == null){
            return null;
        }
        return dateFormat.format(date);
    }

    // Get date from String
    private Date getDateFromString(String string){
        try{
            return dateFormat.parse(string);
        }
        catch (ParseException | NullPointerException e){
            return null;
        }
    }
}
