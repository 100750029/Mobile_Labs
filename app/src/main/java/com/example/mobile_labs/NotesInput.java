package com.example.mobile_labs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotesInput extends AppCompatActivity {

    private EditText editTitle, editSubTitle, editDescription; // Edit Text variables
    private Button deleteButton, captureButton, uploadButton; // Delete button variable
    private RadioButton yellowBtn, redBtn, blueBtn; // RadioButton variables
    private Notes selectedNote; // Variable for picked note
    private ImageView imagePicture; // ImageView variable
    Uri imageUri;
    private Bitmap bitmap; // Bitmap variable
    private static final int CAMERA_PERM_CODE = 101; // Code for camera permission
    private static final int CAMERA_GALLERY_CODE = 010; // Code for gallery permission
    private static final int CAMERA_REQ_CODE = 102; // Code for request

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_input);
        initWidgets(); // Loads all the ID's
        checkForEditNote(); // Check if the note is being edited

        // onClick function for taking a picture from the camera
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermisson();
            }
        });

        // onClick function for uploading a picture from gallery
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), CAMERA_GALLERY_CODE);

            }
        });

        // onChecked function for setting note colour to yellow
        yellowBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEditTextBackground(Color.YELLOW);
                }
            }
        });

        // onChecked function for setting note colour to red
        redBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEditTextBackground(Color.RED);
                }
            }
        });

        // onChecked function for setting note colour to blue
        blueBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEditTextBackground(Color.BLUE);
                }
            }
        });
    }

    // Function for accessing the camera
    private void askCameraPermisson(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            openCamera();
        }
    }

    // Function check is permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Function opens the camera
    public void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQ_CODE);
    }

    // Function for displaying the image on the imageView based on image type
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQ_CODE){ // Capturing image
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imagePicture.setImageBitmap(image);
        }
        if(requestCode == CAMERA_GALLERY_CODE){ // Selecting from gallery
            imageUri = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imagePicture.setImageBitmap(bitmap);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Loading all the ID's
    private void initWidgets(){
        editTitle = findViewById(R.id.editTitleText);
        editSubTitle = findViewById(R.id.editSubTitleText);
        editDescription = findViewById(R.id.editDescriptionText);
        deleteButton = findViewById(R.id.buttonDeleteNotes);
        yellowBtn = findViewById(R.id.radioYellow);
        redBtn = findViewById(R.id.radioRed);
        blueBtn = findViewById(R.id.radioBlue);
        imagePicture = findViewById(R.id.imageviewPicture);
        captureButton = findViewById(R.id.buttonCapture);
        uploadButton = findViewById(R.id.buttonUpload);
    }

    // Function for setting color of note
    private void setEditTextBackground(int color) {
        editTitle.setBackgroundColor(color);
        editSubTitle.setBackgroundColor(color);
        editDescription.setBackgroundColor(color);
    }

    // Function for editing a note
    private void checkForEditNote()
    {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Notes.NOTE_EDIT_EXTRA, -1);
        selectedNote = Notes.getNoteForID(passedNoteID);

        if (selectedNote != null)
        {
            // Sets the values from the database
            editTitle.setText(selectedNote.getTitle());
            editSubTitle.setText(selectedNote.getSubTitle());
            editDescription.setText(selectedNote.getDescription());

            // Adds the image from the database if one was taken
            if(selectedNote.getPicture() != null){
                imagePicture.setImageBitmap(BitmapFactory.decodeByteArray(selectedNote.getPicture(), 0, selectedNote.getPicture().length));
            }

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
            deleteButton.setVisibility(View.INVISIBLE); // Delete button is visible when note is being updated
        }
    }

    // Function for saving a note
    public void saveNotes( View view){
        SQLDatabase sqlDatabase = SQLDatabase.instanceofDatabase(this); // Gets instance of database

        // Values of note
        String title = String.valueOf(editTitle.getText());
        String subtitle = String.valueOf(editSubTitle.getText());
        String description = String.valueOf(editDescription.getText());
        byte[] picture = imageViewToByteArray(imagePicture);

        // Check is title is entered, if not a warning is given to save a note
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a Title", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedColor = "white"; // Default color
        if (yellowBtn.isChecked()) {
            selectedColor = "yellow";
            setEditTextBackground(Color.YELLOW);
        } else if (redBtn.isChecked()) {
            selectedColor = "red";
            setEditTextBackground(Color.RED);
        } else if (blueBtn.isChecked()) {
            selectedColor = "blue";
            setEditTextBackground(Color.BLUE);
        }

        // Check is a colour is selected, if not a warning is given and a colour must be selected to save a note
        if ("white".equals(selectedColor)) {
            Toast.makeText(this, "Please select either yellow, red or blue.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(selectedNote == null)
        {
            int id = Notes.notesArrayList.size();
            Notes newNotes = new Notes(id, title, subtitle, description, selectedColor, picture);
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
            selectedNote.setPicture(picture);
            sqlDatabase.NoteUpdate(selectedNote);
            Log.d("NotesSave", "New note added: " + selectedNote.getTitle());

            setEditTextBackground(Color.parseColor(selectedColor));
        }


        finish();
    }

    // Function for deleting a note
    public void deleteNotes(View view)
    {
        selectedNote.setDeleted(new Date());
        SQLDatabase sqlDatabase = SQLDatabase.instanceofDatabase(this);
        sqlDatabase.NoteUpdate(selectedNote);
        finish();
    }

    // Function for converting image byte to bitmap
    public byte[] imageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }else{
            return new byte[0];
        }
    }

    // Function for canceling a note
    public void cancelNotes(View view){
        finish();
    }
}
