package com.example.mobile_labs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class NotesInput extends AppCompatActivity {

    private EditText editTitle, editSubTitle, editDescription; // Edit Text variables
    private Button deleteButton; // Delete button variable
    private RadioButton yellowBtn, redBtn, blueBtn; // RadioButton variables
    private Notes selectedNote; // Variable for picked note

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_input);
        initWidgets();
        checkForEditNote();

        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    selectedNote.setChangeBackground("yellow");
                } else {
                    // Handle creating a new note and setting its color
                    setEditTextBackground(Color.YELLOW);
                }
            }
        });

        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    selectedNote.setChangeBackground("red");
                } else {
                    // Handle creating a new note and setting its color
                    setEditTextBackground(Color.RED);
                }
            }
        });

        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    selectedNote.setChangeBackground("blue");
                } else {
                    // Handle creating a new note and setting its color
                    setEditTextBackground(Color.BLUE);
                }
            }
        });
    }

    private void initWidgets(){
        editTitle = findViewById(R.id.editTitleText);
        editSubTitle = findViewById(R.id.editSubTitleText);
        editDescription = findViewById(R.id.editDescriptionText);
        deleteButton = findViewById(R.id.buttonDeleteNotes);
        yellowBtn = findViewById(R.id.radioYellow);
        redBtn = findViewById(R.id.radioRed);
        blueBtn = findViewById(R.id.radioBlue);
    }

    private void setEditTextBackground(int color) {
        editTitle.setBackgroundColor(color);
        editSubTitle.setBackgroundColor(color);
        editDescription.setBackgroundColor(color);
    }

    private void checkForEditNote()
    {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Notes.NOTE_EDIT_EXTRA, -1);
        selectedNote = Notes.getNoteForID(passedNoteID);

        if (selectedNote != null)
        {
            editTitle.setText(selectedNote.getTitle());
            editSubTitle.setText(selectedNote.getSubTitle());
            editDescription.setText(selectedNote.getDescription());

            // Update radio buttons based on the note's color
            String noteColor = selectedNote.getChangeBackground();
            if (noteColor != null) {
                switch (noteColor) {
                    case "yellow":
                        yellowBtn.setChecked(true);
                        break;
                    case "red":
                        redBtn.setChecked(true);
                        break;
                    case "blue":
                        blueBtn.setChecked(true);
                        break;
                    default:
                        // Handle the default case or leave it unchanged
                        break;
                }
            }
        }

        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveNotes( View view){
        SQLDatabase sqlDatabase = SQLDatabase.instanceofDatabase(this);
        String title = String.valueOf(editTitle.getText());
        String subtitle = String.valueOf(editSubTitle.getText());
        String description = String.valueOf(editDescription.getText());

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a Title", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedColor = "white"; // Default color
        if (yellowBtn.isChecked()) {
            selectedColor = "yellow";
        } else if (redBtn.isChecked()) {
            selectedColor = "red";
        } else if (blueBtn.isChecked()) {
            selectedColor = "blue";
        }

        if ("white".equals(selectedColor)) {
            Toast.makeText(this, "Please select either yellow, red or blue.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(selectedNote == null)
        {
            int id = Notes.notesArrayList.size();
            Notes newNotes = new Notes(id, title, subtitle, description, selectedColor);
            Notes.notesArrayList.add(newNotes);
            newNotes.setChangeBackground(selectedColor);
            sqlDatabase.NoteAddition(newNotes);
            Log.d("NotesSave", "New note added: " + newNotes.getTitle());
        }
        else
        {
            selectedNote.setTitle(title);
            selectedNote.setSubTitle(subtitle);
            selectedNote.setDescription(description);
            selectedNote.setChangeBackground(selectedColor);
            sqlDatabase.NoteUpdate(selectedNote);
            Log.d("NotesSave", "New note added: " + selectedNote.getTitle());
        }


        finish();
    }

    public void deleteNotes(View view)
    {
        selectedNote.setDeleted(new Date());
        SQLDatabase sqlDatabase = SQLDatabase.instanceofDatabase(this);
        sqlDatabase.NoteUpdate(selectedNote);
        finish();
    }

    public void cancelNotes(View view){
        finish();
    }
}
