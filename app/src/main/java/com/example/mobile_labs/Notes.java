package com.example.mobile_labs;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Date;

public class Notes {

    // Array for storing information about notes
    public static ArrayList<Notes> notesArrayList = new ArrayList<>();
    // Variable for editing notes
    public static String NOTE_EDIT_EXTRA =  "noteEdit";
    private int id; // Variable for note ID
    private String title, subTitle, description, changeBackground; // Variable for note information
    private Date deleted; // Variable for deleting notes

    // Constructor for Notes
    public Notes(int id, String title, String subTitle, String description, String changeBackground) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.changeBackground = changeBackground;
        deleted = null;
    }

    public Notes(int id, String title, String subTitle, String description, String changeBackground, Date deleted) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.changeBackground = changeBackground;
        this.deleted = deleted;
    }

    // function for getting note ID
    public static Notes getNoteForID(int passedNoteID)
    {
        for (Notes notes : notesArrayList)
        {
            if(notes.getId() == passedNoteID)
                return notes;
        }

        return null;
    }


    // Array list for deleted notes
    public static ArrayList<Notes> nonDeletedNotes()
    {
        ArrayList<Notes> nonDeleted = new ArrayList<>();
        for(Notes notes : notesArrayList)
        {
            if(notes.getDeleted() == null)
                nonDeleted.add(notes);
        }

        return nonDeleted;
    }

    public int getId() {
        return id;
    } // Getter for ID

    public String getTitle() {
        return title;
    } // Getter for Title

    public String getSubTitle() {
        return subTitle;
    } // Getter for subtitle

    public String getDescription() {
        return description;
    } // Getter for description

    public Date getDeleted() {
        return deleted;
    } // Getter for deleted

    public void setId(int id) {
        this.id = id;
    } // Setter for ID

    public void setTitle(String title) {
        this.title = title;
    } // Setter for ID

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    } // Setter for subtitle

    public void setDescription(String description) {
        this.description = description;
    } // Setter for description

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    } // Setter for deleted note

    public String getChangeBackground() {
        return changeBackground;
    } // Getter for background colour

    public void setChangeBackground(String changeBackground) {
        this.changeBackground = changeBackground; // Setter for background colour
    }



}
