package kashyap.anurag.notesapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import kashyap.anurag.notesapp.Adapters.NotesAdapter;
import kashyap.anurag.notesapp.Database.NotesDatabase;
import kashyap.anurag.notesapp.Model.Notes;
import kashyap.anurag.notesapp.R;
import kashyap.anurag.notesapp.databinding.ActivityMainBinding;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    NotesAdapter notesAdapter;
    List<Notes> noteList;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteList = new ArrayList<>();
        binding.notesRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        notesAdapter = new NotesAdapter(MainActivity.this, noteList);
        binding.notesRv.setAdapter(notesAdapter);

        loadData(0);

        binding.newNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this, InsertNotesActivity.class);
                startActivity(intent);
            }
        });

        SearchView inputSearch = findViewById(R.id.inputSearch);
        EditText searchEditText = (EditText) inputSearch.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(Color.GRAY);
        inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                NotesFilter(newText);
                return false;
            }
        });

        binding.filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
        binding.filterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
    }

    private void loadData(int i) {
        if (i==0){
            @SuppressLint("StaticFieldLeak")
            class GetNotesTask extends AsyncTask<Void, Void, List<Notes>> {

                @Override
                protected List<Notes> doInBackground(Void... voids) {
                    return NotesDatabase.getDatabaseInstance(getApplicationContext()).notesDao().getallNotes();
                }

                @Override
                protected void onPostExecute(List<Notes> notes) {
                    super.onPostExecute(notes);
                    if (noteList.size() == 0){
                        noteList.addAll(notes);
                        notesAdapter.notifyDataSetChanged();
                    }else {
                        noteList.add(0, notes.get(0));
                        notesAdapter.notifyItemInserted(0);


                    }
                    binding.notesRv.smoothScrollToPosition(0);
                }
            }
            new GetNotesTask().execute();

        }else if (i==1){

            @SuppressLint("StaticFieldLeak")
            class GetNotesTask extends AsyncTask<Void, Void, List<Notes>> {

                @Override
                protected List<Notes> doInBackground(Void... voids) {
                    return NotesDatabase.getDatabaseInstance(getApplicationContext()).notesDao().lowToHigh();
                }

                @Override
                protected void onPostExecute(List<Notes> notes) {
                    super.onPostExecute(notes);
                    if (noteList.size() == 0){
                        noteList.addAll(notes);
                        notesAdapter.notifyDataSetChanged();

                    }else {
                        noteList.clear();
                        noteList.addAll(notes);
                        notesAdapter.notifyDataSetChanged();

                    }

                }
            }
            new GetNotesTask().execute();

        }else if (i == 2){
            @SuppressLint("StaticFieldLeak")
            class GetNotesTask extends AsyncTask<Void, Void, List<Notes>> {

                @Override
                protected List<Notes> doInBackground(Void... voids) {
                    return NotesDatabase.getDatabaseInstance(getApplicationContext()).notesDao().highToLow();
                }

                @Override
                protected void onPostExecute(List<Notes> notes) {
                    super.onPostExecute(notes);
                    if (noteList.size() == 0){
                        noteList.addAll(notes);
                        notesAdapter.notifyDataSetChanged();

                    }else {
                        noteList.clear();
                        noteList.addAll(notes);
                        notesAdapter.notifyDataSetChanged();

                    }

                }
            }
            new GetNotesTask().execute();
        }
    }

    private void NotesFilter(String newText) {
        ArrayList<Notes> filterNames = new ArrayList<>();
        for (Notes notes : this.noteList){
            if (notes.notesTitle.toLowerCase().contains(newText.toLowerCase()) || notes.notes.toLowerCase().contains(newText.toLowerCase())
                    || notes.notesDate.toLowerCase().contains(newText.toLowerCase()) || notes.notesSubtitle.toLowerCase().contains(newText.toLowerCase())){
                filterNames.add(notes);
            }
        }
        this.notesAdapter.searchNotes(filterNames);
    }
    private void showFilterDialog() {
        Dialog filterDialog = new Dialog(MainActivity.this, R.style.BottomSheetStyle);
        filterDialog.setContentView(R.layout.filter_dialog);
        filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RelativeLayout noFilterRl = filterDialog.findViewById(R.id.noFilterRl);
        RelativeLayout filterHighToLow = filterDialog.findViewById(R.id.filterHighToLow);
        RelativeLayout filterLowToHigh = filterDialog.findViewById(R.id.filterLowToHigh);


        filterDialog.setCancelable(true);

        noFilterRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteList.clear();
                loadData(0);
                filterDialog.dismiss();
            }
        });
        filterLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.dismiss();
                loadData(1);

            }
        });
        filterHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
                loadData(2);
            }
        });
        filterDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}