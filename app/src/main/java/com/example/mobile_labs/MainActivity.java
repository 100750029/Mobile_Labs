package com.example.mobile_labs;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView notesListView;
    private SearchView notesSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        setNotesAdapter();
        DatabaseLoad();
        setOnClickListener();
        setupSearchView();

    }

    private void initWidgets(){

        notesListView = findViewById(R.id.notesListView);
        notesSearch = findViewById(R.id.searchView);
    }

    private void setupSearchView() {
        notesSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the search as the user types in the search box.
                filterNotes(newText);
                return false;
            }
        });
    }

    private void filterNotes(String query) {
        NotesAdapter notesAdapter = (NotesAdapter) notesListView.getAdapter();
        List<Notes> allNotes = Notes.nonDeletedNotes();
        List<Notes> filteredNotes = new ArrayList<>();

        for (Notes note : allNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())){
                filteredNotes.add(note);
            }
        }

        notesAdapter = new NotesAdapter(getApplicationContext(), filteredNotes);
        notesListView.setAdapter(notesAdapter);
    }

    private void setNotesAdapter(){
        NotesAdapter notesAdapter = new NotesAdapter(getApplicationContext(), Notes.nonDeletedNotes());
        notesListView.setAdapter(notesAdapter);
    }

    private void DatabaseLoad(){
         SQLDatabase sqlDatabase = SQLDatabase.instanceofDatabase(this);
         sqlDatabase.keepNotesArray();
    }


    private void setOnClickListener()
    {
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Notes selectedNote = (Notes) notesListView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), NotesInput.class);
                editNoteIntent.putExtra(Notes.NOTE_EDIT_EXTRA, selectedNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }

    public void newNotes(View view){
        Intent newNotesIntent = new Intent(this,NotesInput.class);
        startActivity(newNotesIntent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setNotesAdapter();
    }
}

