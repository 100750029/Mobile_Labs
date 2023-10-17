package com.example.mobile_labs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Notes> {

    // Note adapter
    public NotesAdapter(Context context, List<Notes> notes ){
        super(context, 0, notes);
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


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_layout, parent, false);
        }

        return convertView;

    }

}
