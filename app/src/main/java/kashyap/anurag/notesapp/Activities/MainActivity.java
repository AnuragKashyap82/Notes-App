package kashyap.anurag.notesapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import kashyap.anurag.notesapp.Adapters.NotesAdapter;
import kashyap.anurag.notesapp.Model.Notes;
import kashyap.anurag.notesapp.R;
import kashyap.anurag.notesapp.ViewModel.NotesViewModel;
import kashyap.anurag.notesapp.databinding.ActivityMainBinding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    NotesViewModel notesViewModel;
    NotesAdapter adapter;

    List<Notes> filterNotesAllList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        binding.noFilter.setBackgroundResource(R.drawable.filte_selected_shape);

        loadData(0);

        binding.newNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InsertNotesActivity.class));
            }
        });
        binding.noFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(0);
                binding.noFilter.setBackgroundResource(R.drawable.filte_selected_shape);
                binding.filterHighToLow.setBackgroundResource(R.drawable.filte_shape);
                binding.filteLowToHigh.setBackgroundResource(R.drawable.filte_shape);
            }
        });
        binding.filteLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(1);
                binding.filteLowToHigh.setBackgroundResource(R.drawable.filte_selected_shape);
                binding.noFilter.setBackgroundResource(R.drawable.filte_shape);
                binding.filterHighToLow.setBackgroundResource(R.drawable.filte_shape);
            }
        });
        binding.filterHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(2);
                binding.filterHighToLow.setBackgroundResource(R.drawable.filte_selected_shape);
                binding.noFilter.setBackgroundResource(R.drawable.filte_shape);
                binding.filteLowToHigh.setBackgroundResource(R.drawable.filte_shape);
            }
        });

        notesViewModel.getallNotes.observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                setAdapter(notes);
            }
        });
    }

    private void loadData(int i) {
        if (i==0){
            notesViewModel.getallNotes.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterNotesAllList = notes;
                }
            });
        }else if (i==1){
            notesViewModel.lowToHigh.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterNotesAllList = notes;
                }
            });
        }else if (i == 2){
            notesViewModel.highToLow.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterNotesAllList = notes;
                }
            });
        }
    }

    public void setAdapter(List<Notes> notes) {
        binding.notesRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NotesAdapter(MainActivity.this, notes);
        binding.notesRv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_notes, menu);

        MenuItem menuItem = menu.findItem(R.id.appBarSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Notes here!!!");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        return super.onCreateOptionsMenu(menu);
    }

    private void NotesFilter(String newText) {
        ArrayList<Notes> filterNames = new ArrayList<>();
        for (Notes notes : this.filterNotesAllList){
            if (notes.notesTitle.toLowerCase().contains(newText.toLowerCase()) || notes.notesSubtitle.toLowerCase().contains(newText.toLowerCase())){
                filterNames.add(notes);
            }
        }
        this.adapter.searchNotes(filterNames);
    }
}