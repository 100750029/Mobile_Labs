package com.example.mobile_labs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends ArrayAdapter<Notes> {

    private Context context;

    // Note adapter
    public NotesAdapter(Context context, List<Notes> notes ){
        super(context, 0, notes);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Notes notes = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_layout, parent, false);
        }

        // Getting ID for Note inputs
        TextView title = convertView.findViewById(R.id.cellTitle);
        TextView subtitle = convertView.findViewById(R.id.cellSubTitle);
        TextView description = convertView.findViewById(R.id.cellDescription);

        Button editButton = convertView.findViewById(R.id.buttonEditNote);
        Button deleteButton = convertView.findViewById(R.id.buttonDeleteNote);

        // Setting note information to text output
        title.setText(notes.getTitle());
        subtitle.setText(notes.getSubTitle());
        description.setText(notes.getDescription());

        // Setting background colour of note
        String color = notes.getChangeBackground();
        if ("yellow".equals(color)) {
            convertView.setBackgroundColor(Color.YELLOW);
        } else if ("red".equals(color)) {
            convertView.setBackgroundColor(Color.RED);
        } else if ("blue".equals(color)) {
            convertView.setBackgroundColor(Color.BLUE);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        // OnClick listener for edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notes note = getItem(position); // Gets the note position
                if (note != null) {
                    Intent editNoteIntent = new Intent(context, NotesInput.class);
                    editNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    editNoteIntent.putExtra(Notes.NOTE_EDIT_EXTRA, note.getId()); // Passes the notes ID
                    context.startActivity(editNoteIntent); // Starts the note_input page
                }
            }
        });

        // OnClick listener for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notes notes = getItem(position); // Gets the note position
                if (notes != null) {
                    notes.setDeleted(new Date()); // Sets the deleted date
                    SQLDatabase sqlDatabase = SQLDatabase.instanceofDatabase(context); // Passes it to the database
                    sqlDatabase.NoteUpdate(notes); // Updates the list of notes
                    remove(notes); // Removes the note
                    notifyDataSetChanged();
                }
            }
        });

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_layout, parent, false);
        }

        return convertView;

    }

}
